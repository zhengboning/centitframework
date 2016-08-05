package com.centit.framework.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.model.adapter.SocketEventHandler;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;


/**
 * Created by sx on 2014/12/17.
 *
 * 封装SocketIO服务端启动监听及事件推送方法
 *
 * 需要和前端Js socket.io client配合使用，push方法参数中的listenerName就是监听事件，前端Js中也需要监听同名事件就可捕获服务端推送消息。
 * 前端Js在连接服务端地址时将userCode作为请求参数，将userCode和Socket连接进行绑定方便推送消息。
 */
public class DataPushSocketServer {

    private static SocketIOServer socketServer;
    private static final Log logger = LogFactory.getLog(DataPushSocketServer.class);
    private static boolean started = false;
    
    private static String scoketHost;
    private static int scoketPort;
    private static List<SocketEventHandler> socketEventHandlers = new ArrayList<>();
    /**
     * 用户代码 usercode 和 socket 映射关系
     */
    private static Map<String, UUID> connectClients = new HashMap<>();

    private DataPushSocketServer() {
        
    }
    
    public static void registerEventHandler(SocketEventHandler eventHandler){
        socketEventHandlers.add(eventHandler);
    }

    public static void initSockect(String host, int port) {
        if(started)
            stopServer();
        
        scoketHost = host;
        scoketPort = port;
        
        Configuration configuration = new Configuration();
        configuration.setHostname(scoketHost);//设置主机名
        configuration.setPort(scoketPort);//设置监听的端口号

        //根据配置创建服务器对象
        socketServer = new SocketIOServer(configuration);
        //添加客户端连接监听器
        socketServer.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                
                String userCode = client.getHandshakeData().getSingleUrlParam("userCode");
                if (StringUtils.isBlank(userCode)) {
                    return;
                }
                logger.debug("用户"+userCode+"连接服务器成功！");
                connectClients.put(userCode, client.getSessionId());//保存客户端
                
                for(SocketEventHandler eventHandler : socketEventHandlers){
                    eventHandler.postConnectEventHandler(userCode);
                }
            }
        });      

    }

    public static void startServer() {
        if(socketServer==null)
            return;
        socketServer.start();
        started = true;
    }
 
    public static void stopServer() {
        if(socketServer==null)
            return;
        socketServer.stop();
        started = false;
    }
    
    /**
     * 向客户端推送数据，数据格式为json
     * @param userCode
     * @param eventType
     * @param dataJSON
     * @return
     */
    public static boolean pushDataToClient(String userCode,String eventType,String dataJSON){
        if(!started)
            return false;
        UUID sessionUuid = connectClients.get(userCode);
        if(sessionUuid==null)
            return false;
        SocketIOClient socketIOClient = socketServer.getClient(sessionUuid);
        if(socketIOClient==null/* || !socketIOClient.isChannelOpen()*/ ){
            
            connectClients.remove(userCode);
            return false;
        }
        socketIOClient.sendEvent(eventType, dataJSON);
        return true;
    }

    public static int pushDataToAll(String eventType,String dataJSON) {
        Collection<SocketIOClient> sockects = socketServer.getAllClients();
        for(SocketIOClient socketIOClient : sockects)
            socketIOClient.sendEvent(eventType, dataJSON);
        return sockects.size();
    }
    
    /**
     * 服务端推送消息，默认监听push方法，不解析contentText。
     * 
     * 这个方法 如果响应比较慢可以参考log（日志）管理模块修改为后台线程处理，用一个数组缓存。
     * 
     * @param socketIOClients SocketIOClient集合
     * @param contentText 消息内容，可以是Json数据，前端再解析
     */
    public static boolean pushMessage(String userCode, String contentText, String viewUrl) {
        logger.debug("给"+ userCode + "发送消息"+contentText);
        Map<String, String> param = new HashMap<>();
        param.put("message", contentText);
        if(viewUrl!=null)
            param.put("url", viewUrl);
        String text = JSONObject.toJSONString(param);
        
        return pushDataToClient(userCode,"msg",text);
    }
    
    public static boolean pushMessage(String userCode, String contentText) {
        return pushMessage(userCode,contentText,null);
    }

    public static int pushMessageToAll(String contentText, String viewUrl) {        
        Map<String, String> param = new HashMap<>();
        param.put("message", contentText);
        if(viewUrl!=null)
            param.put("url", viewUrl);
        String text = JSONObject.toJSONString(param);
        return pushDataToAll("msg", text);
    }
    
    public static int pushMessageToAll(String contentText) {
        return  pushMessageToAll(contentText,null);        
    }
}

package com.centit.framework.components.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.components.NotificationCenter;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.adapter.PlatformEnvironment;

/**
 * 通知中心实现，所有的消息通过此类进行发送，消息中心会通过接收用户设置的消息接收方式自行决定使用哪种消息发送方式
 */
public class NotificationCenterImpl implements NotificationCenter {

    private static final Log logger = LogFactory.getLog(NotificationCenterImpl.class);
    private static Map<String, MessageSender> msgSenders;

    /**
     * 用户设置
     */

    private PlatformEnvironment platformEnvironment;
    //注入接口MessageSender实现类，通过setMsgSenders方法进行配置
    
    public void setPlatformEnvironment(PlatformEnvironment platformEnvironment) {
		this.platformEnvironment = platformEnvironment;
	}

	private MessageSender defautlMsgSender;
    /**
     * 这个通过spring注入
     */
    public void initMsgSenders() {
    	if (msgSenders == null) {
            msgSenders = new HashMap<String, MessageSender>();
        }
        msgSenders.put("email", EmailMessageSenderImpl.instance);
        defautlMsgSender = EmailMessageSenderImpl.instance;
        //目前支持内部消息、短信
        //msgSenders中的键与UserSetting表中receiveways中值一一对应
        /*msgSenders.put("I", innerMessageSender);
        */
    }

    /**
     * 如果 MessageSender 是spring托管类请 
     * MessageSender msgManager = 
                ContextLoaderListener.getCurrentWebApplicationContext().
                getBean("optLogManager",  OperationLogWriter.class);
        // 这个地方不能直接用 this， this不是spring管理的bean，必须从容器中获取托管的 bean
        notificationCenter.registerMessageSender("type",msgManager); 
     */
    public NotificationCenter registerMessageSender(String sendType,MessageSender sender){
        if (msgSenders == null) {
            msgSenders = new HashMap<String, MessageSender>();
        }
        msgSenders.put(sendType, sender);
        return this;
    }
    
    public MessageSender setDefaultSendType(String sendType){
        MessageSender ms = msgSenders.get(sendType);
        if(ms!=null)
            defautlMsgSender = ms;
        return defautlMsgSender;
    }
    
    @Override
    public String sendMessage(String sender, String receiver, String msgSubject, String msgContent,
            String optId, String optMethod, String optTag) {

        /**
         *  从用户设置中获得用户希望的接收消息的方式，可能是多个，比如用户希望同时接收到Email和短信，这样就要发送两天
         *  并在数据库中记录发送信息，在发送方式中用逗号把多个方式拼接在一起保存在对应的字段中
         */
        String returnText = "OK";
        String receiveways = platformEnvironment.getUserSetting(receiver, "receiveways");

        StringBuilder errorObjects = new StringBuilder();

        String noticeType ="" ;//default;
        int sendTypeCount = 0;
        int sendErrorCount = 0;
        if (receiveways!= null && StringUtils.isNotBlank(receiveways)) {
            String[] vals = receiveways.split(",");
            
            if (ArrayUtils.isNotEmpty(vals)) {
                
                noticeType = receiveways; 
                for (String val : vals) {
                    if (StringUtils.isNotBlank(val)) {
                        sendTypeCount++;
                        String errorText = sendMessage(msgSenders.get(val.trim()), sender, receiver, msgSubject, msgContent,
                                optId,  optMethod,  optTag);
                        if (StringUtils.isNotBlank(errorText)) {
                            sendErrorCount ++;
                            errorObjects.append(errorText).append("\r\n");
                        }
                    }
                }
            }
        }
        if(sendTypeCount==0 || sendErrorCount==sendTypeCount){
            String infoText = "用户 " + CodeRepositoryUtil.getUserInfoByCode(receiver).getLoginName() + " " +
                    "未选择任何通知接收方式，默认通过内部消息发送通知";
            logger.info(infoText);
            noticeType = StringUtils.isBlank(noticeType)?"D":noticeType+",D";
            sendTypeCount++;
            String errorText = sendMessage(defautlMsgSender, sender, receiver, msgSubject, msgContent,
                    optId,  optMethod,  optTag);
            if (StringUtils.isNotBlank(errorText)) {
                sendErrorCount ++;
                errorObjects.append(errorText).append("\r\n");
            }
        }        

        String notifyState =sendErrorCount==0?"0":(sendErrorCount==sendTypeCount?"1":"2");
        
        if (sendErrorCount>0) {//返回异常信息
            returnText = errorObjects.toString();
         }
        wirteNotifyLog(sender, receiver, msgSubject, msgContent, noticeType,
                optId,  optMethod,  optTag,
                returnText, notifyState);

        return returnText;
    }
       

    @Override
    public String sendMessage(String sender, String receiver, String msgSubject, String msgContent) {
        
        return sendMessage( sender,  receiver,  msgSubject,  msgContent,
                 "",  "",  "");
    }
    
    @Override
    public String sendMessage(String sender, String receiver, String msgSubject, String msgContent,
            String optId, String optMethod, String optTag, String noticeType) {
        String returnText = "OK";
        String errorText = sendMessage(msgSenders.get(noticeType), sender, receiver, msgSubject, msgContent,
                optId,  optMethod,  optTag);

        //发送成功
        String notifyState = "0";
        if (StringUtils.isNotBlank(errorText)) {
            notifyState = "1";
            returnText = errorText;
        }
        wirteNotifyLog(sender, receiver, msgSubject, msgContent, noticeType, 
                optId,  optMethod,  optTag,
                errorText, notifyState);
        return returnText;
    }
    
    @Override
    public String sendMessage(String sender, String receiver, String msgSubject, String msgContent, String noticeType) {
        
        return sendMessage( sender,  receiver,  msgSubject,  msgContent,
                 "",  "",  "",  noticeType);
    }

     /**
     * 保存系统通知中心数据
     *
     * @param sender
     * @param receiver
     * @param msgSubject
     * @param msgContent
     * @param noticeType
     * @param errorText
     * @param notifyState
     */
    private void wirteNotifyLog(String sender, String receiver,
                               String msgSubject, String msgContent, String noticeType, 
                               String optId, String optMethod, String optTag,
                               String errorText, String notifyState ) {
        Map<String,String> sysNotify = new HashMap<String,String>();
        sysNotify.put("sender", sender);
        sysNotify.put("receiver", receiver);
        sysNotify.put("msgSubject", msgSubject);
        sysNotify.put("msgContent", msgContent);
        sysNotify.put("noticeType", noticeType);
        sysNotify.put("optId", optId);
        sysNotify.put("optMethod", optMethod);
        sysNotify.put("optTag", optTag);
        sysNotify.put("notifyState", notifyState);
        sysNotify.put("errorText", errorText);
        
    	OperationLogCenter.log(sender, "Notify","notify", JSON.toJSONString(sysNotify));
  
    }

    /**
     * 发送通知中心消息
     * @param messageSender
     * @param sender
     * @param receiver
     * @param msgSubject
     * @param msgContent
     * @return
     */
    private static String sendMessage(MessageSender messageSender, String sender, String receiver, String msgSubject, 
            String msgContent , String optId, String optMethod, String optTag) {
        if (null == messageSender) {
            String errorText = "找不到消息发送器，请检查Spring中的配置和数据字典 WFNotice中的配置是否一致";
            logger.error(errorText);
            return errorText;
        }
        try {
            messageSender.sendMessage(sender, receiver, msgSubject, msgContent,optId,  optMethod,  optTag);

        } catch (Exception e) {
            String errorText = messageSender.getClass().getName() + "发送通知失败，异常信息 " + e.getMessage();
            logger.error(errorText);
            return errorText;
        }

        return null;
    }
}

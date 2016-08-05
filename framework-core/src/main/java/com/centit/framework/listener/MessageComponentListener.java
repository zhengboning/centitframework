package com.centit.framework.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.components.DataPushSocketServer;

/**
 * Created by sx on 2014/12/17.
 *
 * 加载SocketIO服务组件监听器
 *
 */
public class MessageComponentListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DataPushSocketServer.initSockect(
                SysParametersUtils.getStringValue("socketio.host"), 
                SysParametersUtils.getIntValue("socketio.port"));
        DataPushSocketServer.startServer();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DataPushSocketServer.stopServer();
    }
}

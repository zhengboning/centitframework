package com.centit.framework.model.adapter;

/**
 * 实现这个接口并在 DataPushSocketServer 注册，
 * 推送数据服务会调用这个接口向用户推送数据
 * @author codefan
 *
 */
public interface SocketEventHandler {
    /**
     * 用户中 新连接时推送（登录时）
     * @param userCode
     */
    public void postConnectEventHandler(String userCode);
    
    /**
     * 定时推送 这个接口对应的功能暂时还没有实现
     * @param userCode
     */
    @Deprecated
    public void pustDataTimingEventHandler(String userCode);
}

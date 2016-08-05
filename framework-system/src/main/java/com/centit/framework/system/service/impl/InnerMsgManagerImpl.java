package com.centit.framework.system.service.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoaderListener;

import com.centit.framework.components.NotificationCenter;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.system.dao.InnerMsgDao;
import com.centit.framework.system.dao.InnerMsgRecipientDao;
import com.centit.framework.system.po.InnerMsg;
import com.centit.framework.system.po.InnerMsgRecipient;
import com.centit.framework.system.service.InnerMsgManager;
@Service("InnerMessageManager")
@Transactional
public class InnerMsgManagerImpl extends BaseEntityManagerImpl<InnerMsg,String,InnerMsgDao> 
    implements InnerMsgManager, MessageSender{

    @Override
    @Resource(name="innerMsgDao")
    @NotNull
    protected void setBaseDao(InnerMsgDao baseDao) {
      super.baseDao=baseDao;
      logger = LogFactory.getLog(InnerMsgManagerImpl.class);
    }

    @Resource(name="innerMsgRecipientDao")
    @NotNull
    private InnerMsgRecipientDao recipientDao;
    
    
    @Resource
    //@NotNull
    public void setNotificationCenter(NotificationCenter notificationCenter){
    	if(notificationCenter!=null){
	    	MessageSender sender = 
	                ContextLoaderListener.getCurrentWebApplicationContext().
	                getBean("InnerMessageManager",  MessageSender.class);
	        // 这个地方不能直接用 this， this不是spring管理的bean，必须从容器中获取托管的 bean
	    	notificationCenter.registerMessageSender("innerMsg", sender);
    	}
    }
    
    /*
     * 更新消息
     * 
     */
    @Override
    public void mergeMInnerMsg(InnerMsg msgCopy, InnerMsg msg) {
       baseDao.deleteObject(msgCopy);
       baseDao.mergeObject(msg);
    }
    /**
     * 发送消息
     * 
     */
    @Override
    public String sendMessage(String sender, String receiver,
            String msgSubject, String msgContent, String optId,
            String optMethod, String optTag) {
        InnerMsg msg = new InnerMsg(sender,msgSubject,msgContent);
        msg.setSendDate(new Date());
        msg.setMsgType("P");
        msg.setMailType("O");
        msg.setMsgState("U");
        msg.setOptId(optId);
        msg.setOptMethod(optMethod);
        msg.setOptTag(optTag);
        InnerMsgRecipient recipient=new InnerMsgRecipient();
        recipient.setMInnerMsg(msg);
        recipient.setReplyMsgCode(0);
        recipient.setReceiveType("P");
        recipient.setMailType("T");
        recipient.setMsgState("U");
        recipientDao.saveNewObject(recipient);
            return "OK";
    }
    
    @Override
    public String sendMessage(String sender, String receiver,
        String msgSubject, String msgContent) {
       
        return sendMessage( sender,  receiver,
                 msgSubject,  msgContent,  "msg",
                 "sender", "");
    }

    @Override
    public void deleteMsgById(String msgCode) {
       InnerMsg msg= baseDao.getObjectById(msgCode);
       msg.setMsgState("D");
       baseDao.saveObject(msg);
    } 
}

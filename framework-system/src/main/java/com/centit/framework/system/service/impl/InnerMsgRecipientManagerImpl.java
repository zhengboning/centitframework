package com.centit.framework.system.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoaderListener;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.components.DataPushSocketServer;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.adapter.SocketEventHandler;
import com.centit.framework.model.basedata.IUnitInfo;
import com.centit.framework.system.dao.InnerMsgDao;
import com.centit.framework.system.dao.InnerMsgRecipientDao;
import com.centit.framework.system.dao.UnitInfoDao;
import com.centit.framework.system.po.InnerMsg;
import com.centit.framework.system.po.InnerMsgRecipient;
import com.centit.framework.system.service.InnerMsgRecipientManager;
@Service("innerMsgRecipientManager")
public class InnerMsgRecipientManagerImpl
     extends BaseEntityManagerImpl<InnerMsgRecipient,String,InnerMsgRecipientDao>
    implements InnerMsgRecipientManager, SocketEventHandler, MessageSender{

    @Override
    @Resource(name="innerMsgRecipientDao")
    @NotNull
    protected void setBaseDao(InnerMsgRecipientDao baseDao) {
        super.baseDao=baseDao;
        logger = LogFactory.getLog(InnerMsgRecipientManagerImpl.class);
    }

    @Resource
    private InnerMsgDao innerMsgDao;
    
    @Resource
    private UnitInfoDao unitDao;
    
    
    @PostConstruct
    public void init() {        
        SocketEventHandler eventHandler = 
                ContextLoaderListener.getCurrentWebApplicationContext().
                getBean("innerMsgRecipientManager",  SocketEventHandler.class);
        // 这个地方不能直接用 this， this不是spring管理的bean，必须从容器中获取托管的 bean
        DataPushSocketServer.registerEventHandler(eventHandler); 
    }
    
    /*
     * 更新接受者信息
     * 
     */
    @Override
    @Transactional
    public void mergeRecipient(InnerMsgRecipient recipientCopy,
            InnerMsgRecipient recipient) {
       baseDao.deleteObject(recipientCopy);
       baseDao.mergeObject(recipient);
        
    }
    
    /*
     * msg为消息主题，recipient为接收人
     * 添加消息接受者,群发(receipient.receive为数组，但是保存到数据库是挨个保存)
     * 
     */
    @Override
    @Transactional
    public void sendMsg(InnerMsgRecipient recipient,String sysUserCode){
        String receive=recipient.getReceive();
        String receives[] = StringUtils.split(receive, ",");
        InnerMsg msg=recipient.getMInnerMsg();
        //拆分recieve   
        if(!StringUtils.isNotBlank(msg.getSender())) {
            msg.setSender(sysUserCode);
            //msg.setSenderName(CodeRepositoryUtil.getUserInfoByCode(sysUserCode).getUserName());
            if (null == msg.getSendDate()) {
                msg.setSendDate(new Date());
            }
        }
        sendToMany(receives,msg,recipient);        
    }
    public void sendToMany(String []receives,InnerMsg msg,InnerMsgRecipient recipient){
    	if(null!=receives&&receives.length>0){
            String receiveName="";
            int bo=0;
            for(String userCode:receives){
                if(bo>0){
                	receiveName+=",";
                }
                receiveName+=CodeRepositoryUtil.getUserInfoByCode(userCode).getUserName();
                bo++;
            }
            msg.setReceiveName(receiveName);
            innerMsgDao.saveNewObject(msg);
            recipient.setMInnerMsg(msg);
            //DataPushSocketServer.pushMessage(msg.getSender(), "你发送邮件："+ msg.getMsgTitle());
            for(String userCode:receives){
                recipient.setReceive(userCode);
                baseDao.saveNewObject(recipient);
                DataPushSocketServer.pushMessage(userCode, "你有新邮件："+ recipient.getMsgTitle());
            }            
        }
    }
    
    
    /*
     * 获取两者间来往消息列表
     * 
     */
    @Override
    @Transactional
    public  List<InnerMsgRecipient> getExchangeMsgs(String sender, String receiver) {
        return baseDao.getExchangeMsgs(sender,receiver);
    }

    /*
     * 给部门成员，所有直属或间接下级部门成员发消息
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void noticeByUnitCode(String unitCode, InnerMsg msg) throws Exception {
        List<IUnitInfo> unitList= CodeRepositoryUtil.getUnitList(unitCode);
        		//(ArrayList<UnitInfo>) unitDao.listAllSubUnits(unitCode);
        String units="";
        int i=0;
        if(null!=unitList && unitList.size()>0)
            {for(IUnitInfo ui:unitList)
                {
                if(i>0)
                { units+=",";}
                    units+="'"+ui.getUnitCode()+"'";
                    i++;
                }
            }
        String sValue="("+units+")";
        String shql="Select userCode from F_USERINFO Where userCode in (Select userCode From F_USERUNIT where unitCode in "+sValue+")";
        List<String> userList=(List<String>) DatabaseOptUtils.findObjectsBySql(baseDao, shql);
        String[] receives=new String[1000];
        if(null!=userList && userList.size()>0){
            String receiveName=CodeRepositoryUtil.getUnitName(unitCode);
            msg.setReceiveName(receiveName);
            msg.setMsgType("A");
            innerMsgDao.saveNewObject(msg);
            int j=0;
            for(String rec:userList){   
                receives[j]=rec;
                j++;
            }
            int l=0;
            InnerMsgRecipient recipient=new InnerMsgRecipient();
            recipient.setMsgState(msg.getMsgState());
            recipient.setMailType(msg.getMailType());
            recipient.setMInnerMsg(msg);
            while(null!=receives[l]){
                recipient.setReceive(receives[l]);
                baseDao.saveNewObject(recipient);
                DataPushSocketServer.pushMessage(receives[l], "你有新邮件："+ recipient.getMsgTitle());
                l++;
            }
        }else{
            throw new Exception("该机构中暂无用户");
        }
    }

    @Override
    //返回消息持有者数量
    @Transactional
    public void deleteOneRecipientById(String id) {
        InnerMsgRecipient re=baseDao.getObjectById(id);
        re.setMsgState("D");
        baseDao.saveObject(re);
    }
   
    
    @Override
    @Transactional
    public long getUnreadMessageCount(String userCode){
        return baseDao.getUnreadMessageCount(userCode);
    }
    
    @Override
    @Transactional
    public List<InnerMsgRecipient> listUnreadMessage(String userCode){
        return baseDao.listUnreadMessage(userCode);
    }

 

    /**
     * 发送消息
     * 
     */
    @Override
    @Transactional
    public String sendMessage(String sender, String receiver,
            String msgSubject, String msgContent, String optId,
            String optMethod, String optTag) {
        InnerMsg msg = new InnerMsg(sender,msgSubject,msgContent);
        msg.setSendDate(new Date());
        msg.setMsgType("P");
        msg.setMailType("O");
        msg.setMsgState("U");
        msg.setOptId(optId);
        msg.setReceiveName(CodeRepositoryUtil.getUserInfoByCode(receiver).getUserName());
        msg.setOptMethod(optMethod);
        msg.setOptTag(optTag);
        InnerMsgRecipient recipient=new InnerMsgRecipient();
        recipient.setMInnerMsg(msg);
        recipient.setReplyMsgCode(0);
        recipient.setReceiveType("P");
        recipient.setMailType("T");
        recipient.setMsgState("U");
        String []receives=new String[]{receiver};
        sendToMany(receives,msg,recipient);
        return "OK";
    }
    
    @Override
    @Transactional
    public String sendMessage(String sender, String receiver,
        String msgSubject, String msgContent) {
        return sendMessage( sender,  receiver,
                 msgSubject,  msgContent,  "msg",
                 "sender", "");
    }
    
    @Override
    @Transactional
    public void postConnectEventHandler(String userCode) {
        long unreadMsg = baseDao.getUnreadMessageCount(userCode);
        if(unreadMsg>0){
            DataPushSocketServer.pushMessage(userCode, "您有 "+unreadMsg+" 未读消息，请进入消息中心->收件箱 查看！");
        }        
    }

	@Override
	public void pustDataTimingEventHandler(String userCode) {
		// TODO Auto-generated method stub
		
	}
}

package com.centit.framework.components;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.centit.framework.core.common.WebOptUtils;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.framework.model.basedata.OperationLog;

public class OperationLogCenter {
    
    private OperationLogCenter()
    {
        
    }

    private static final Log logger = LogFactory.getLog(OperationLogCenter.class);
    
    private static OperationLogWriter logWriter = null;
    
    /**
     * 设置默认的写入器，如果用户调用  registerOperationLogWriter 则不执行
     */
    public static void initOperationLogWriter(OperationLogWriter optLogWriter){ 
        if(logWriter == null)
            logWriter = optLogWriter;
    }
    
    /**
     * 个用户设置自己的日志写入器
     * @param optLogWriter
     */
    public static void registerOperationLogWriter(OperationLogWriter optLogWriter){        
        logWriter = optLogWriter;
    }

  
   private static BlockingQueue<OperationLog> blockingQueue = new LinkedBlockingQueue<OperationLog>();

   private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);

   /**
    * 异步写入日志
    */
   static {
       executor.scheduleWithFixedDelay(
               new Runnable() {
                   @Override
                   public void run() {
                       if(logWriter == null)
                           return;
                     while (!blockingQueue.isEmpty()) {
                           try {
                               OperationLog optLog = blockingQueue.take();
                               //logger.error("logWriter +++++ " + optLog.getOptContent());
                               logWriter.save(optLog);
                           } catch (Exception e) {
                               logger.error(e.getMessage());
                           }
                       }
                     
                   }
               }, 30, 10, TimeUnit.SECONDS);
       //默认执行时间间隔为10秒
   }


   public static void log(OperationLog optLog) {
       try {
           blockingQueue.put(optLog);
       } catch (InterruptedException e) {
           logger.error(e.getMessage());
       }
   }

   /**
    * 记录日志内容 
    * @param loglevel 操作日志还是错误提示
    * @param userCode 操作人员
    * @param optId    业务代码（类别）
    * @param optTag   对象主键，如果是联合主键请用url的方式编写，比如a=1&b=2
    * @param optMethod 操作方法
    * @param optContent 操作内容描述
    * @param newValue 新增json(或者操作参数明细，比如查询操作可以记录查询参数)
    * @param oldValue 旧值json
    */
   public static void log(String loglevel, String userCode, String optId, String optTag, String optMethod,
           String optContent,String newValue,String oldValue) {
       OperationLog optLog = new OperationLog(loglevel, userCode, optId, optTag, optMethod,  optContent ,newValue, oldValue);
       optLog.setOptTime(new Date());
       log(optLog);
   }
   
   /**
    * 记录日志内容 
    * @param userCode 操作人员
    * @param optId    业务代码（类别）
    * 对象主键 可以没有
    * @param optMethod 操作方法
    * @param optContent 者操作说明
    */
   public static void log(String userCode, String optId, String optMethod, String optContent) {       
       log(OperationLog.LEVEL_INFO,userCode, optId, null, optMethod,  optContent,null,null);
   }

   /**
    * 记录日志内容 
    * @param userCode 操作人员
    * @param optId    业务代码（类别）
    * 对象主键  可以没有
    * @param optMethod 操作方法  
    * 操作前原始值 可以没有 
    * @param optContent 者操作说明
    * @param newValue 详细参数json
    */
   public static void log(String userCode, String optId, String optMethod,
           String optContent,String newValue) {       
       log(OperationLog.LEVEL_INFO,userCode, optId, null, optMethod,  optContent,newValue, null);
   }
   
   /**
    * 记录日志内容 
    * @param userCode 操作人员
    * @param optId    业务代码（类别）
    * @param optTag对象主键  可以没有
    * @param optMethod 操作方法  
    * @param optContent 者操作说明
    * @param newValue 新增json
    * @param oldValue 旧值json
    */
   public static void log(String userCode, String optId,String optTag, String optMethod,
           String optContent,String newValue,String oldValue) {       
       log(OperationLog.LEVEL_INFO,userCode, optId, optTag, optMethod,  optContent,newValue, oldValue);
   }

   
   /**
    * 记录日志内容 
    * @param userCode 操作人员
    * @param optId    业务代码（类别）
    * @param optMethod 操作方法
    * @param optContent 操作说明
    */  
   public static void logError(String userCode, String optId, String optMethod, String optContent) {
		   log(OperationLog.LEVEL_ERROR,userCode, optId, null, optMethod,  optContent,null, null);
   }	

   /**
    * 记录日志内容 
    * @param userCode 操作人员
    * @param optId    业务代码（类别）
    * @param optTag   对象主键
    * @param optMethod 操作方法
    * @param optContent 操作说明
    * @param newValue 详细参数JSON
    */
   public static void logError(String userCode, String optId, String optMethod,
		   String optContent,String newValue) {
	   log(OperationLog.LEVEL_ERROR,userCode, optId, null, optMethod,  optContent,newValue, null);
   }
   
   /**
    * 记录日志内容 
    * @param request 获取当前用户
    * @param optId    业务代码（类别）
    * @param optTag   对象主键
    * @param optMethod 操作方法
    * @param optContent 操作说明
    * @param newValue 新增json
    * @param oldValue 旧值json
    */
   public static void log(HttpServletRequest request, String optId, String optTag, String optMethod,
		   String optContent, String newValue, String oldValue ) {
       log(OperationLog.LEVEL_INFO, WebOptUtils.getLoginUser(request).getUserCode(),
                optId, optTag, optMethod, optContent,newValue ,oldValue );
      
   }
   
   /**
    * 记录日志内容 
    * @param request 获取当前用户
    * @param optId    业务代码（类别）
    * @param optMethod 操作方法
    * @param optContent 操作说明
    * @param newValue 详细参数json
    */
   public static void log(HttpServletRequest request, String optId,
		  String optMethod,String optContent, String newValue) {       
       log(request, optId, null, optMethod, optContent, newValue,null);
   }
  
   /**
    * 记录日志内容 
    * @param request 获取当前用户
    * @param optId    业务代码（类别）
    * @param optMethod 操作方法
    * @param optContent 操作说明
    */
   public static void log(HttpServletRequest request, String optId, String optMethod,String optContent ) {       
       log(request, optId, null, optMethod, optContent, null,null);
   }
  

   /**
    * 新建对象日志
    * @param userCode 操作人员
    * @param optId    业务代码（类别）
    * @param optTag   对象主键
    * @param optMethod 操作方法
    * @param optContent 操作说明
    * @param newObject 新增对象
    */
   public static void logNewObject(String userCode, String optId, String optTag, String optMethod,
		   String optContent, Object newObject ) {
       log(OperationLog.LEVEL_INFO, userCode,
                optId, optTag, optMethod, optContent,JSON.toJSONString(newObject) ,null );
      
   }
   
   /**
    * 删除对象日志
    * @param userCode 操作人员
    * @param optId    业务代码（类别）
    * @param optTag   对象主键
    * @param optMethod 操作方法
    * @param optContent 操作说明
    * @param oldObject 删除的对象
    */
   public static void logDeleteObject(String userCode, String optId, String optTag, String optMethod,
		   String optContent, Object oldObject ) {
       log(OperationLog.LEVEL_INFO, userCode,
                optId, optTag, optMethod, optContent,null,JSON.toJSONString(oldObject)  );
      
   }
   
   /**
    * 更新对对象日志
    * @param userCode 操作人员
    * @param optId    业务代码（类别）
    * @param optTag   对象主键
    * @param optMethod 操作方法
    * @param optContent 操作说明
    * @param newObject 新对象
    * @param oldObject 旧对象
    */
   public static void logUpdateObject(String userCode, String optId, String optTag, String optMethod,
		   String optContent,Object newObject , Object oldObject ) {
	   
	   OperationLog optLog = new OperationLog(userCode,
			   optId, optTag, optMethod,  optContent );
	   optLog.setOptDifference(newObject, oldObject);
	   optLog.setOptTime(new Date());
       log(optLog);
   }


   /**
    * 新建对象日志
    * @param request 获取操作人员
    * @param optId    业务代码（类别）
    * @param optTag   对象主键
    * @param optMethod 操作方法
    * @param optContent 操作说明
    * @param newObject 新增对象
    */
   public static void logNewObject(HttpServletRequest request, String optId, String optTag, String optMethod,
		   String optContent, Object newObject ) {
	   logNewObject( WebOptUtils.getLoginUser(request).getUserCode(),
                optId, optTag, optMethod, optContent,newObject );
      
   }
   
   /**
    * 查询数据日志
    * @param request 获取操作人员
    * @param optId    业务代码（类别）
    * @param optTag   对象主键
    * @param queryDesc 查询说明
    * @param optContent 操作说明
    * @param queryMap 查询条件对象
    */
   public static void logQuery(String userCode, String optId, String optMethod,
		   String queryDesc, Map<String,Object> queryMap ) {
	   log(OperationLog.LEVEL_INFO, userCode,
               optId, null, optMethod, queryDesc,JSON.toJSONString(queryMap),null);
   }
   
   public static void logQuery(HttpServletRequest request, String optId, String optMethod,
			   String queryDesc, Map<String,Object> queryMap ) {
		   logQuery( WebOptUtils.getLoginUser(request).getUserCode(),
                optId,  optMethod, queryDesc,queryMap);
   }
   
   /**
    * 删除对象日志
    * @param request 获取操作人员
    * @param optId    业务代码（类别）
    * @param optTag   对象主键
    * @param optMethod 操作方法
    * @param optContent 操作说明
    * @param oldObject 删除的对象
    */
   public static void logDeleteObject(HttpServletRequest request, String optId, String optTag, String optMethod,
		   String optContent, Object oldObject ) {
	   logDeleteObject( WebOptUtils.getLoginUser(request).getUserCode(),
                optId, optTag, optMethod, optContent,oldObject);
      
   }
   
   /**
    * 更新对对象日志
    * @param request 获取操作人员
    * @param optId    业务代码（类别）
    * @param optTag   对象主键
    * @param optMethod 操作方法
    * @param optContent 操作说明
    * @param newObject 新对象
    * @param oldObject 旧对象
    */
   public static void logUpdateObject(HttpServletRequest request, String optId, String optTag, String optMethod,
		   String optContent,Object newObject , Object oldObject ) {
	   
	   logUpdateObject( WebOptUtils.getLoginUser(request).getUserCode(),
               optId, optTag, optMethod, optContent,newObject,oldObject);
   }
 }

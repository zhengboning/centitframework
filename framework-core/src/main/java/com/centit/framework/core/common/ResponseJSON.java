package com.centit.framework.core.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;

public class ResponseJSON {
    
    public static final String RES_CODE_FILED="code";
    public static final String RES_MSG_FILED="message";
    public static final String RES_DATA_FILED="data";    
  
    /**
     * 返回的详细数据， 可能是需要回显的参数，也可能是验证的错误提示
     */
    private JSONObject resJSON;    

    public ResponseJSON() {  
    }

    public boolean isNull() {
        return resJSON==null;
    }
    
    public Integer getCode() {
    	if(resJSON==null)
    		return null;
    	return resJSON.getInteger(RES_CODE_FILED);
    }
    
    public String getMessage() {
    	if(resJSON==null)
    		return null;
    	return resJSON.getString(RES_MSG_FILED);
    }
    
    public Object getData() {
    	if(resJSON==null)
    		return null;
        return resJSON.get(RES_DATA_FILED);
    }
    
    public String getDataAsString() {
        return StringBaseOpt.objectToString(
        		resJSON.get(RES_DATA_FILED));
    }

    public String getDataAsString(String skey) {
    	Object data = getData();
        if(data==null || !(data instanceof JSONObject))
            return null; 
        Object dataObj = ((JSONObject)data).get(skey);         
        return  StringBaseOpt.objectToString(dataObj);
    }
    /**
     * ScalarObject 标量对象，只系统的内置的 Integer、long、String、float、double、Date等标量
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
	public <T> T getDataAsScalarObject( Class<T> clazz) {
    	try{
	        String sdata = String.valueOf(getData());
	        if(clazz == java.lang.Integer.class){
	        	return (T)java.lang.Integer.valueOf(sdata);
	        }else if(clazz == java.lang.Long.class){
	        	return (T)java.lang.Long.valueOf(sdata);
	        }else if(clazz == java.lang.Double.class){
	        	return (T)java.lang.Double.valueOf(sdata);
	        }else if(clazz == java.lang.Boolean.class){
	        	return (T)java.lang.Boolean.valueOf(sdata);
	        }else if(clazz == java.lang.Float.class){
	        	return (T)java.lang.Float.valueOf(sdata);
	        }else if(clazz == java.lang.String.class){
	        	return (T)sdata;
	        }else if(clazz == java.util.Date.class){
	        	return (T)DatetimeOpt.smartPraseDate(sdata);
	        }else if(clazz == java.util.UUID.class){
	        	return (T)java.util.UUID.fromString(sdata);
	        }else        
	        	return null;
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }    
     
    public <T> T getDataAsObject( Class<T> clazz) {
        Object data = getData();
        if(data==null)
            return null;
        //这个地方重复解释字符串效率较低，应该可以优化
        return JSON.parseObject(data.toString(), clazz);        
    }

    @SuppressWarnings("unchecked")
	public <T> T getDataAsScalarObject(String skey, Class<T> clazz) {
    	Object data = getData();
        if(data==null || !(data instanceof JSONObject))
            return null;
        Object dataObj = ((JSONObject)data).get(skey);
        
    	try{
	        String sdata = StringBaseOpt.objectToString(dataObj);
	        if(clazz == java.lang.Integer.class){
	        	return (T)java.lang.Integer.valueOf(sdata);
	        }else if(clazz == java.lang.Long.class){
	        	return (T)java.lang.Long.valueOf(sdata);
	        }else if(clazz == java.lang.Double.class){
	        	return (T)java.lang.Double.valueOf(sdata);
	        }else if(clazz == java.lang.Boolean.class){
	        	return (T)java.lang.Boolean.valueOf(sdata);
	        }else if(clazz == java.lang.Float.class){
	        	return (T)java.lang.Float.valueOf(sdata);
	        }else if(clazz == java.lang.String.class){
	        	return (T)sdata;
	        }else if(clazz == java.util.Date.class){
	        	return (T)DatetimeOpt.smartPraseDate(sdata);
	        }else if(clazz == java.util.UUID.class){
	        	return (T)java.util.UUID.fromString(sdata);
	        }else        
	        	return null;
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }    
    
    public <T> List<T> getDataAsArray(Class<T> clazz) {
        Object data = getData();
        if(data==null)
            return null;
        //这个地方重复解释字符串效率较低，应该可以优化
        return JSON.parseArray(data.toString(), clazz);        
    }
    
    public <T> T getDataAsObject(String sKey, Class<T> clazz) {
        Object data = getData();
        if(data==null || !(data instanceof JSONObject))
            return null;
        // TypeUtils.castToJavaBean 
        //return ((JSONObject)data).getObject(sKey,clazz);
        
        String str = JSON.toJSONString(((JSONObject)data).get(sKey));
        return JSON.parseObject(str, clazz);   
        /*
        Object dataObj = ((JSONObject)data).get(sKey);
        if(dataObj==null)
            return null;
        //这个地方重复解释字符串效率较低，应该可以优化
        return JSON.parseObject(dataObj.toString(), clazz);   
        */    
    }    

    public <T> List<T> getDataAsArray(String sKey, Class<T> clazz) {
        Object data = getData();
        if(data==null || !(data instanceof JSONObject))
            return null;
        Object dataObj = ((JSONObject)data).get(sKey);
        if(dataObj==null)
            return null;
        //这个地方重复解释字符串效率较低，应该可以优化
        return JSON.parseArray(dataObj.toString(), clazz);        
    }
    
    public <T> Map<String,T> getDataAsMap( Class<T> clazz) {
        Object data = getData();
        if(data==null)
            return null;
        //这个地方重复解释字符串效率较低，应该可以优化
        if(data instanceof JSONObject){
        	JSONObject jsonMap = (JSONObject)data;        	
        	Map<String,T> ret = new HashMap<>();
        	for(Map.Entry<String, Object> ent : jsonMap.entrySet()){
        		String str = JSON.toJSONString(ent.getValue());
        		ret.put( ent.getKey(), JSON.parseObject(str, clazz));
        		//ret.put( skey, jsonMap.getObject(skey, clazz));
        	}        	
        	return ret;
        }
        return null;
    }
    
    public <T> Map<String,T> getDataAsMap(String key, Class<T> clazz) {
    	Object data = getData();
        if(data==null || !(data instanceof JSONObject))
            return null; 
        Object dataObj = ((JSONObject)data).get(key);        
        if(dataObj instanceof JSONObject){
        	JSONObject jsonMap = (JSONObject)dataObj;        	
        	Map<String,T> ret = new HashMap<>();
        	for(Map.Entry<String, Object> ent : jsonMap.entrySet()){
        		String str = JSON.toJSONString(ent.getValue());
        		ret.put( ent.getKey(), JSON.parseObject(str, clazz));
        		//ret.put(skey, jsonMap.getObject(skey, clazz));
        	}        	
        	return ret;
        }
        return null;
    }
    
    public static ResponseJSON valueOfJson(String jsonStr){
        if(jsonStr==null)
            return null;
        //System.out.print(jsonStr);
        ResponseJSON retJson = new ResponseJSON();
        retJson.resJSON =JSON.parseObject(jsonStr);
        return retJson;
        //JSON.parseObject(jsonStr, ResponseJSON.class); 
    }
    
}

package com.centit.framework.core.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyPreFilter;

public class ResponseData {
    
    public static final String RES_CODE_FILED="code";
    public static final String RES_MSG_FILED="message";
    public static final String RES_DATA_FILED="data";
    
    /**
     * 返回代码，0 表示正确，其他的为错误代码
     */
    private int resCode;

    /**
     * 返回消息提示 ，code为0时是提示，其他为 错误提示
     */
    private String resMessage;

    /**
     * 返回的详细数据， 可能是需要回显的参数，也可能是验证的错误提示
     */
    private Map<String, Object> resMapData;

    

    public ResponseData() {
        resCode = 0;
        resMessage = "OK";
        resMapData = new LinkedHashMap<String, Object>();
    }

    public ResponseData(int nCode, String errorMessage) {
        resCode = nCode;
        resMessage = errorMessage;
        resMapData = new LinkedHashMap<String, Object>();
    }

    public int getCode() {
        return resCode;
    }

    public void setCode(int resCode) {
        this.resCode = resCode;
    }

    public String getResponseMessage() {
        return resMessage;
    }

    public void setResponseMessage(String resMessage) {
        this.resMessage = resMessage;
    }

    public Map<String, Object> getResponseData() {
        return resMapData;
    }

    public void addResponseData(String sKey, Object objValue) {
        resMapData.put(sKey, objValue);
    }
    
    public Object getResponseData(String sKey) {
        return resMapData.get(sKey);
    }
    
    public String toJSONString(){
        Map<String, Object> param = new HashMap<>();
        param.put(RES_CODE_FILED, resCode );
        param.put(RES_MSG_FILED,  resMessage );
        if(resMapData!=null)
            param.put(RES_DATA_FILED, resMapData);
        return JSONObject.toJSONString(param);
    }
    
    public String toJSONString(PropertyPreFilter simplePropertyPreFilter){
        Map<String, Object> param = new HashMap<>();
        param.put(RES_CODE_FILED, resCode );
        param.put(RES_MSG_FILED,  resMessage );
        if(resMapData!=null)
            param.put(RES_DATA_FILED, resMapData);
        return JSONObject.toJSONString(param,simplePropertyPreFilter);
    }
}

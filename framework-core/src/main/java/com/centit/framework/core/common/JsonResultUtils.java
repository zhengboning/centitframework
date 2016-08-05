package com.centit.framework.core.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

/**
 * 使用阿里提供的Json API 格式化Json数据
 *
 * @author sx 
 * @author codefan@sina.com 代码全部重写
 * @version fastjson文档地址：https://github.com/alibaba/fastjson/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98
 *          fastJson,jackJson,Gson性能比较 http://chenyanxi.blog.51cto.com/4599355/1543445
 * @create 2014-9-16
 */
public class JsonResultUtils {

    private static Log logger = LogFactory.getLog(JsonResultUtils.class);
    
    static {
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }

    private JsonResultUtils() {

    }

    /**
     * 格式化Json数据输出
     *
     * @param response
     */
    public static void writeOriginalJson(String jsonValue, HttpServletResponse response) {
        
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
 
        try {
            response.getWriter().print(jsonValue);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ObjectException(e.getMessage());
        }
    }
    
    /**
     * 直接文本数据输出
     *
     * @param response
     */
    public static <T> void writeOriginalObject(T objValue, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=utf-8"); 
        try {
            response.getWriter().print(objValue);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ObjectException(e.getMessage());
        }
    }
    /**
     * javascript脚本输出
     *
     * @param response
     */
    public static void writeJavaScript(String scriptValue, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/javascript;charset=UTF-8"); 
        try {
            response.getWriter().print(scriptValue);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ObjectException(e.getMessage());
        }
    }
    /**
     * 格式化Json数据输出
     *
     * @param response
     */
    public static void writeSingleDataJson(int code,String message, Object objValue, HttpServletResponse response,
                                           PropertyPreFilter simplePropertyPreFilter) {      

        Map<String, Object> param = new HashMap<>();
        param.put(ResponseData.RES_CODE_FILED, code );
        param.put(ResponseData.RES_MSG_FILED,  message );
        if(objValue!=null)
            param.put(ResponseData.RES_DATA_FILED, objValue);

        String text = JSONObject.toJSONString(param, simplePropertyPreFilter);

        writeOriginalJson(text,response);
    }
    
   
    /**
     * 格式化Json数据输出
     *
     * @param response
     * @param propertyPreFilter {@link SimplePropertyPreFilter} 格式化时过滤指定的属性
     */
    public static void writeResponseDataAsJson(ResponseData resData, HttpServletResponse response,
                                        PropertyPreFilter propertyPreFilter) {
        writeSingleDataJson(resData.getCode(),resData.getResponseMessage(), 
                resData.getResponseData(), response, propertyPreFilter);
    }
    
    /**
     * 格式化Json数据输出
     *
     * @param response
     */
    public static void writeResponseDataAsJson(ResponseData resData, HttpServletResponse response) {
        writeSingleDataJson(resData.getCode(),resData.getResponseMessage(), 
                resData.getResponseData(), response, null);
    }
    
    
    /**
     * 格式化Json数据输出
     * 请使用 writeResponseDataAsJson 
     * @param response
     * @param propertyPreFilter {@link SimplePropertyPreFilter} 格式化时过滤指定的属性
     */
    @Deprecated
    public static void writeMapDataJson(ResponseData resData, HttpServletResponse response,
                                        PropertyPreFilter propertyPreFilter) {
        writeSingleDataJson(resData.getCode(),resData.getResponseMessage(), 
                resData.getResponseData(), response, propertyPreFilter);
    }
    
    /**
     * 格式化Json数据输出
     * 请使用 writeResponseDataAsJson 
     * @param response
     */
    @Deprecated
    public static void writeMapDataJson(ResponseData resData, HttpServletResponse response) {
        writeSingleDataJson(resData.getCode(),resData.getResponseMessage(), 
                resData.getResponseData(), response, null);
   }

    
    /**
     * Ajax 请求失败，http的状态码设置为 code
     *
     * @param response
     */
    public static void writeAjaxErrorMessage(int errorCode, String errorMessage, HttpServletResponse response) {
        try {
            response.sendError(errorCode, errorMessage);
        } catch (IOException e) {
        }
        
        writeSingleDataJson(errorCode,errorMessage, 
                null, response, null);
    }
    /**
     * 格式化Json数据输出 , 输出 业务提示的错误信息，http的状态仍然是 200 OK
     *
     * @param response
     */
    public static void writeErrorMessageJson(int errorCode, String errorMessage, HttpServletResponse response) {
        writeSingleDataJson(errorCode,errorMessage, 
                null, response, null);
    }

    /**
     * 格式化Json数据输出 , 输出 业务提示的错误信息，http的状态仍然是 200 OK
     *
     * @param response
     */
    public static void writeCodeAndMessageJson(int errorCode, String errorMessage, HttpServletResponse response) {
        writeSingleDataJson(errorCode,errorMessage, 
                null, response, null);
    }
 
    /**
     * 格式化Json数据输出
     *
     * @param response
     */
    public static void writeMessageJson(String message, HttpServletResponse response) {
        writeSingleDataJson(0,message, 
                null, response, null);
    }
    /**
     * 格式化Json数据输出
     *
     * @param response
     */
    public static void writeErrorMessageJson(String errorMessage, HttpServletResponse response) {
        writeSingleDataJson(500,errorMessage, 
                null, response, null);
    }

    /**
     * 格式化Json数据输出
     *
     * @param response
     */
    public static void writeBlankJson(HttpServletResponse response) {
        writeSingleDataJson(0,"OK", 
                null, response, null);
    }

    

    public static void writeSingleDataJson(Object objValue, HttpServletResponse response,
            PropertyPreFilter simplePropertyPreFilter) {
        writeSingleDataJson(0,"OK",objValue,response,simplePropertyPreFilter);
    }
    /**
     * 格式化Json数据输出
     *
     * @param response
     */
    public static void writeSingleDataJson(Object objValue, HttpServletResponse response) {
        writeSingleDataJson(0,"OK",objValue, response, null);
    }

    /**
     * 格式化Json数据输出
     *
     * @param response
     */
    public static void writeSingleErrorDataJson(int errorCode, String errorMessage,  Object objValue,
                                                HttpServletResponse response) {
        writeSingleDataJson(errorCode,errorMessage,objValue, response,null);
    }
    
    /**
     * 格式化Json数据输出
     *
     * @param response
     */
    public static void writeSuccessJson(HttpServletResponse response) {
        writeBlankJson(response);
    }
    
    public static <T> T getDataAsObject(String jsonResult,String key, Class<T> clazz) {
        JSONObject jobj = JSON.parseObject(jsonResult);
        JSONObject dataObj = (JSONObject)jobj.get("data");
        if(dataObj==null)
            return null;
        Object rsObj = dataObj.get(key);
        if(rsObj==null)
            return null;
        //这个地方重复解释字符串效率较低，应该可以优化
        return JSON.parseObject(rsObj.toString(), clazz);        
    }
    
    public static <T> T getDataAsObject(String jsonResult, Class<T> clazz) {
        JSONObject jobj = JSON.parseObject(jsonResult);
        //这个地方重复解释字符串效率较低，应该可以优化
        return JSON.parseObject(jobj.get("data").toString(), clazz);        
    }
    
    public static <T> List<T> getDataAsArray(String jsonResult,String key, Class<T> clazz) {
        JSONObject jobj = JSON.parseObject(jsonResult);
        JSONObject dataObj = (JSONObject)jobj.get("data");
        if(dataObj==null)
            return null;
        Object rsObj = dataObj.get(key);
        if(rsObj==null)
            return null;
        //这个地方重复解释字符串效率较低，应该可以优化
        return JSON.parseArray(rsObj.toString(), clazz);        
    }
    
    public static <T> List<T> getDataAsArray(String jsonResult, Class<T> clazz) {
        JSONObject jobj = JSON.parseObject(jsonResult);
        //这个地方重复解释字符串效率较低，应该可以优化
        return JSON.parseArray(jobj.get("data").toString(), clazz);        
    }
}

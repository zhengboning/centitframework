package com.centit.framework.core.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ObjectException;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.common.WebOptUtils;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.algorithm.ListOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.network.HtmlFormUtils;

/**
 * @author sx
 * @create 2013年12月30日
 */

@Controller
public class BaseController {
    
    /**
     * 转换查询参数为字符串，用于 = 或者 like 查询
     */
    public static final String SEARCH_STRING_PREFIX = "s_";

    public static final int SEARCH_STRING_PREFIX_LEN = 2;
    
    /**
     * 转换查询参数为字符串数组，用于 in 查询
     */
    public static final String SEARCH_ARRAY_PREFIX = "a_";

    public static final int SEARCH_ARRAY_PREFIX_LEN = 2;
    
    /**
     * 转换查询参数为数字，用于 Long 类型的 = 或者 like 查询
     */
    public static final String SEARCH_NUMBER_PREFIX = "n_";
    
    public static final int SEARCH_NUMBER_PREFIX_LEN = 2;
    
    /**
     * 转换查询参数为数字数组，用于 Long 类型的 in 查询
     */
    public static final String SEARCH_NUMBER_ARRAY_PREFIX = "na_";
    
    public static final int SEARCH_NUMBER_ARRAY_PREFIX_LEN = 3;
    

    protected Log logger = LogFactory.getLog(BaseController.class);
    /**
     * 当前log4j日志是否打开Debug模式
     */
    protected boolean logDebug = logger.isDebugEnabled();

    protected static final String PAGE_DESC = "pageDesc";

    protected static final String OBJECT = "object";

    protected static final String OBJLIST = "objList";

    /**
     * 绑定所有字符串参数绑定，将Html字符转义 和日期参数转义
     *
     * @param binder {@link WebDataBinder}
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringPropertiesEditor(true));
        binder.registerCustomEditor(Date.class, new DatePropertiesEditor());
        binder.registerCustomEditor(java.sql.Date.class, new SqlDatePropertiesEditor());
        binder.registerCustomEditor(java.sql.Timestamp.class, new SqlTimestampPropertiesEditor());
    }

    /**
     * 前端统一异常处理，
     *
     * @param ex {@link BindException}
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = {BindException.class})
    public void validatorExceptionHandler(BindException ex, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        BindingResult bindingResult = ex.getBindingResult();

        ResponseData responseData = new ResponseData(400, "后台对表单数据验证未通过！");
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                responseData.addResponseData(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }

        logger.error(ex);
        if (WebOptUtils.isAjax(request)) {
            JsonResultUtils.writeResponseDataAsJson(responseData, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/system/exception/error/500");
        }
    }

     
    /**
     * 业务抛出统一异常处理，
     *
     * @param ex {@link ObjectException}
     * @return
     * @throws IOException
     */
    @ExceptionHandler
    public void exceptionHandler(Exception ex, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        logger.error(ex);
        if (WebOptUtils.isAjax(request)) {
            JsonResultUtils.writeErrorMessageJson(400, ex.getMessage(), response);
            ex.printStackTrace();
        } else {
            response.sendRedirect(request.getContextPath() + "/system/exception/error/500");
            ex.printStackTrace();
        }
    }

    /**
     * 获取当前用户信息
     * @param request
     * @return
     */
    protected CentitUserDetails getLoginUser(HttpServletRequest request) {
        return WebOptUtils.getLoginUser(request);
    }

    protected String getLoginUserName(HttpServletRequest request) {
        CentitUserDetails ud = getLoginUser(request);
        if(ud==null)
            return null;
        return ud.getUsername();
    }
    
    protected String getLoginUserCode(HttpServletRequest request) {
        CentitUserDetails ud = getLoginUser(request);
        if(ud==null)
            return null;
        return ud.getUserCode();
    }

    /**
     * 将查询条件转换为Dao中hql语句的参数变量
     * @param request
     * @return
     */
    public static Map<String, Object> convertSearchColumn(HttpServletRequest request) {
        // log.error("规则化前参数表：" + paramMap.toString());
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = new HashMap<>();
        //map.put("isValid", "T");
        for (Map.Entry<String, String[]> ent : parameterMap.entrySet()) {
        	String key = ent.getKey();
        	
        	// 查询字符串 s_
        	if (key.startsWith(SEARCH_STRING_PREFIX)) {
                String sKey = key.substring(SEARCH_STRING_PREFIX_LEN);
                String sValue = HtmlFormUtils.getParameterString(ent.getValue());
                if (sValue != null) {
                    if ("isAll".equals(sKey)) {
                        if ("true".equalsIgnoreCase(sValue) || "1".equals(sValue))
                            map.remove("isvalid");
                    } else
                        map.put(sKey, sValue);
                }
            }
        	// 查询数组 a_
        	else if (key.startsWith(SEARCH_ARRAY_PREFIX)) {
        	    String sKey = key.substring(SEARCH_ARRAY_PREFIX_LEN);
                Object sValue = HtmlFormUtils.getParameterObject(ent.getValue());                
                map.put(sKey, sValue);
        	}
        	// 查询数字 n_
        	else if (key.startsWith(SEARCH_NUMBER_PREFIX)) {
                String sKey = key.substring(SEARCH_NUMBER_PREFIX_LEN);
                String sValue = HtmlFormUtils.getParameterString(ent.getValue());                
                map.put(sKey, NumberBaseOpt.parseLong(sValue));
            }
        	// 查询数字数组 na_
            else if (key.startsWith(SEARCH_NUMBER_ARRAY_PREFIX)) {
                String sKey = key.substring(SEARCH_NUMBER_ARRAY_PREFIX_LEN);
                
                String[] sValue = HtmlFormUtils.getParameterStringArray(ent.getValue());  
                if(sValue==null){
                    map.put(sKey,null);
                }else{
                    Long[] ll = new Long[sValue.length];                
                    for (int i=0;i<sValue.length;i++) {
                        ll[i]=NumberBaseOpt.parseLong(sValue[i]);
                    }                    
                    map.put(sKey, ll);
                }
            }
            else if (StringUtils.equals(key, CodeBook.SELF_ORDER_BY)) {
                map.put(key, HtmlFormUtils.getParameterString(ent.getValue()));
            }
            else if (StringUtils.equals(key, CodeBook.TABLE_SORT_FIELD)) {
                map.put(key, HtmlFormUtils.getParameterString(ent.getValue()));
            }
            else if (StringUtils.equals(key, CodeBook.TABLE_SORT_ORDER)) {
                map.put(key, HtmlFormUtils.getParameterString(ent.getValue()));
            }
        }
        // log.error("规则化后参数表：" + map.toString());
        // 回写查询变量
        setbackSearchColumn(map, request);

        return map;
    }
    /**
     * 将参数变量转换为查询条件
     * @param filterMap
     * @return
     */
    public static Map<String, Object> collectRequestParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = new HashMap<>();
        //map.put("isValid", "T");
        for (Map.Entry<String, String[]> ent : parameterMap.entrySet()) {
        	String[] values = ListOpt.removeBlankString(ent.getValue());
        	if(values==null)
        		continue;
        	if(values.length==1){
        		map.put(ent.getKey(), values[0]);
        	}else{
        		map.put(ent.getKey(), values);
        	}
        }
        return map;
    }

    public static void setbackSearchColumn(Map<String, Object> filterMap, HttpServletRequest request) {
        if (filterMap == null || filterMap.size() < 1) {
            return;
        }
        for (Map.Entry<String, Object> ent : filterMap.entrySet()) {
            Object objValue = ent.getValue();
            String skey;
            if(objValue instanceof String ){
                //和default一样，为了效率优先判断
                skey = SEARCH_STRING_PREFIX + ent.getKey();
            }else if(objValue instanceof String[]){
                skey = SEARCH_ARRAY_PREFIX + ent.getKey();
            }else if(objValue instanceof Long){
                skey = SEARCH_NUMBER_PREFIX + ent.getKey();
            }else if(objValue instanceof Long[]){
                skey = SEARCH_NUMBER_ARRAY_PREFIX + ent.getKey();
            }else{
                skey = SEARCH_STRING_PREFIX + ent.getKey();
            }
            request.setAttribute(skey, objValue);
        }
    }
}

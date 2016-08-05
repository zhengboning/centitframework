package com.centit.framework.security;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.security.model.AccessTokenMetadata;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.security.model.CentitUserDetailsService;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;

public class AjaxAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	 
	private boolean writeLog = false;
    
    public void setWriteLog(boolean writeLog) {
        this.writeLog = writeLog;
    } 
	
    private boolean registToken = false;
    
    public void setRegistToken(boolean registToken) {
        this.registToken = registToken;
    } 
	
    
    @NotNull
    @Resource(name="userDetailsService")
    private CentitUserDetailsService userDetailsService;
	
    public AjaxAuthenticationSuccessHandler() {
    }
 
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
    	CentitUserDetails ud = (CentitUserDetails) authentication.getPrincipal();

    	String lang = request.getParameter("LOCAL_LANG");
		if(!StringBaseOpt.isNvl(lang)){
			request.getSession().setAttribute("LOCAL_LANG", lang);			
            String userLang = ud.getUserSettingValue("LOCAL_LANG");                
            if(! lang.equals(userLang)){
            	userDetailsService.saveUserSetting(ud.getUserCode(),
            			"LOCAL_LANG", lang, "SYS", "用户默认区域语言");
            }
		}else{
            lang = ud.getUserSettingValue("LOCAL_LANG");
            request.getSession().setAttribute("LOCAL_LANG", lang);
		}
		//ud.setAuthenticated(true);
		String tokenKey =null;
		
		if(registToken){
			tokenKey = UuidOpt.getUuidAsString();		
			AccessTokenMetadata.addToken(tokenKey,ud);    
		}
		
		if(writeLog){
            OperationLogCenter.log(ud.getUserCode(),"login", "login",
                    "用户 ："+ud.getUserCode()+"于"+DatetimeOpt.convertDatetimeToString(DatetimeOpt.currentUtilDate())
                    + "从主机"+request.getRemoteHost()+":"+request.getRemotePort()+"登录。");
        }
		
    	String ajax = request.getParameter("ajax");
    	if(ajax==null || "".equals(ajax) || "null".equals(ajax)  || "false".equals(ajax)){
    		super.onAuthenticationSuccess(request,response,authentication);
    	}else{
    		ResponseData resData = new ResponseData();
    		if(registToken)
    			resData.addResponseData("accessToken", tokenKey);
    		resData.addResponseData("userInfo", ud);
    		JsonResultUtils.writeResponseDataAsJson(resData, response);
    		//request.getSession().setAttribute("SPRING_SECURITY_AUTHENTICATION", authentication);
    		//JsonResultUtils.writeSingleErrorDataJson(0,authentication.getName() + " login ok！",request.getSession().getId(), response);
    	}
    }
}
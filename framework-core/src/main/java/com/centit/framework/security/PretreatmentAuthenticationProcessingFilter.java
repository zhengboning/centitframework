package com.centit.framework.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.centit.framework.security.model.CheckFailLogs;
import com.centit.support.image.CaptchaImageUtil;

public class PretreatmentAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {   
    //是否有验证码
    private boolean checkCaptcha=false;     
        
    public boolean isCheckCaptcha() {
        return checkCaptcha;
    }
    
    public void setCheckCaptcha(boolean checkCaptcha) {
        this.checkCaptcha = checkCaptcha;
    }

    public void setMaxTryTimes(int maxTryTimes) {
        CheckFailLogs.setMaxTryTimes(maxTryTimes);
    }

    public void setCheckType(String checkType) {
        CheckFailLogs.setCheckType(checkType);
    }

    public void setLockMinites(int lockMinites) {
        CheckFailLogs.setLockMinites(lockMinites);
    }

    public void setCheckTimeTnterval(int checkTimeTnterval) {
        CheckFailLogs.setCheckTimeTnterval(checkTimeTnterval);
    }
    
    @Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {   
		
        if(checkCaptcha){        
            String request_checkcode = request.getParameter(CaptchaImageUtil.REQUESTCHECKCODE);
            
            String session_checkcode = null;
            Object obj = request.getSession().getAttribute(CaptchaImageUtil.SESSIONCHECKCODE);  
            if (obj!=null)
                session_checkcode = obj.toString();
            request.getSession().removeAttribute(CaptchaImageUtil.SESSIONCHECKCODE);  
            
            if(! "nocheckcode".equals(request_checkcode)){          
                if(!CaptchaImageUtil.checkcodeMatch(session_checkcode, request_checkcode))
                //if(request_checkcode==null || ! request_checkcode.equalsIgnoreCase(session_checkcode)  )
                    throw new AuthenticationServiceException("bad checkcode");   
            }
        }
        
        if(CheckFailLogs.getMaxTryTimes() > 0){
            String checkKey;
            if(CheckFailLogs.getCheckType()=='L')
                checkKey = request.getParameter("username");
            else
                checkKey = request.getRemoteHost()+":"+request.getRemotePort();
            if( CheckFailLogs.isLocked(checkKey)){
                throw new AuthenticationServiceException("User " + checkKey + " is locked, please try late!");
            } 
        }
        
        Authentication auth = null;
                
		//if(!onlyPretreat || writeLog || CheckFailLogs.getMaxTryTimes() > 0){
            try{
                
                auth = super.attemptAuthentication(request, response);
                
                if(CheckFailLogs.getMaxTryTimes() > 0){
                    String checkKey;
                    if(CheckFailLogs.getCheckType()=='L')
                        checkKey = request.getParameter("username");
                    else
                        checkKey = request.getRemoteHost()/*+":"+request.getRemotePort()*/;
                    CheckFailLogs.removeCheckFail(checkKey);
                }
                
            }catch (AuthenticationException failed) {
                //System.err.println(failed.getMessage());
                if(CheckFailLogs.getMaxTryTimes() > 0){
                    String checkKey;
                    if(CheckFailLogs.getCheckType()=='L')
                        checkKey = request.getParameter("username");
                    else
                        checkKey = request.getRemoteHost()/*+":"+request.getRemotePort()*/;
                    CheckFailLogs.plusCheckFail(checkKey); 
                }
                throw failed;
            }
		//}
        
        return auth;
	}
  
}

package com.centit.framework.core.common;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.centit.framework.security.SecurityContextUtils;
import com.centit.framework.security.model.AccessTokenMetadata;
import com.centit.framework.security.model.CentitUserDetails;

/**
 * 系统Web常用工具类可以和spring WebUtils配合使用
 *
 * @author codefan
 * @create 2014年10月24日
 */
public class WebOptUtils {
    public static boolean isAjax(HttpServletRequest request) {
        
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    public static CentitUserDetails getLoginUser() {
    	SecurityContext sch = SecurityContextHolder.getContext();
    	if (sch == null)
            return null;
        Authentication auth = sch.getAuthentication();
        if (auth == null)
            return null;

        CentitUserDetails ud = null;
        Object o = auth.getPrincipal();
        if (o instanceof CentitUserDetails) {
            ud = (CentitUserDetails) o;// auth.getPrincipal();
        }
        return ud;
    }
       
    private static CentitUserDetails innerGetLoginUser(HttpSession session) {
    	Object attr = session.getAttribute(
    			SecurityContextUtils.SecurityContextUserdetail);
    	if(attr!=null && attr instanceof CentitUserDetails){
    		return (CentitUserDetails)attr;
    	}

    	SecurityContext scontext = (SecurityContext) session.getAttribute(
    			HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
    	
    	if(scontext==null)
    		return null;
    	Authentication auth = scontext.getAuthentication();        
        if (auth == null)
            return null;

        CentitUserDetails ud = null;
        Object o = auth.getPrincipal();
        if (o instanceof CentitUserDetails) {
            ud = (CentitUserDetails) o;// auth.getPrincipal();
        }
        return ud;
    }
    
    public static CentitUserDetails getLoginUser(HttpSession session) {
    	CentitUserDetails ud = getLoginUser();
    	if(ud==null){
    		 ud = innerGetLoginUser(session);
    	}
        return ud;
    }
    
    
    public static CentitUserDetails getLoginUser(HttpServletRequest request) {
    	 CentitUserDetails ud = getLoginUser();
    	 //根据token获取用户信息
         if(ud==null){
         	String accessToken = request.getParameter("accessToken");
     		if(StringUtils.isBlank(accessToken))
     			accessToken = String.valueOf(request.getAttribute("accessToken") );
     		ud = AccessTokenMetadata.getTokenUserDetails(accessToken);
         }
         //在session中手动获得用户信息
         if(ud==null){
    		 ud = innerGetLoginUser(request.getSession());
    	 }
         
         return ud;
    }

    public static String getCurrentLang(HttpServletRequest request) {
    	Object obj = request.getSession().getAttribute("LOCAL_LANG");
    	if(obj==null)
    		return null;
    	return String.valueOf(obj);
    }
    
    
    public static String getLoginUserName(HttpServletRequest request) {
        UserDetails ud = getLoginUser(request);
        if (ud == null)
            return "";
        return ud.getUsername();
    }
   
    /**
     * 文件下载
     * @param downloadFile 下载文件流
     * @param downloadName 下载文件名
     * @param response HttpServletResponse
     * @throws IOException
     */
    public static void download(InputStream downloadFile, String downloadName, HttpServletResponse response) throws IOException {
        downloadName = new String(downloadName.getBytes("GBK"), "ISO8859-1");
        response.setContentType("application/x-msdownload;");
        response.setHeader("Content-disposition", "attachment; filename=" + downloadName);
        response.setHeader("Content-Length", String.valueOf(downloadFile.available()));
        IOUtils.copy(downloadFile, response.getOutputStream());
    }
}

package com.centit.framework.security;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;

import com.centit.framework.security.model.AccessTokenMetadata;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.algorithm.UuidOpt;

public class SecurityContextUtils {
	
	public final static String SecurityContextUserdetail = "SECURITY_CONTEXT_USERDETAIL";
	
	public static String registerUserToken(CentitUserDetails ud,String channel ){
		String tokenKey = UuidOpt.getUuidAsString();		
		AccessTokenMetadata.addToken(tokenKey, channel, ud);   
		return tokenKey;
	}
	
	public static String registerUserToken(CentitUserDetails ud){
		String tokenKey = UuidOpt.getUuidAsString();		
		AccessTokenMetadata.addToken(tokenKey, ud);   
		return tokenKey;
	}
	
	public static void setSecurityContext(CentitUserDetails ud){
		SecurityContextHolder.getContext().setAuthentication(ud);
	}
	
	public static void setSecurityContext(CentitUserDetails ud,HttpSession session){
		SecurityContextHolder.getContext().setAuthentication(ud);
		session.setAttribute(SecurityContextUserdetail, ud);
	}

}

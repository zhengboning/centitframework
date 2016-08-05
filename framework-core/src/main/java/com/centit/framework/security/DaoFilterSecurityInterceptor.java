package com.centit.framework.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.centit.framework.security.model.AccessTokenMetadata;
import com.centit.framework.security.model.CentitUserDetails;

public class DaoFilterSecurityInterceptor extends AbstractSecurityInterceptor
        implements Filter {

    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    // ~ Methods
    // ========================================================================================================

    /**
     * Method that is actually called by the filter chain. Simply delegates to
     * the {@link #invoke(FilterInvocation)} method.
     *
     * @param request  the servlet request
     * @param response the servlet response
     * @param chain    the filter chain
     * @throws IOException      if the filter chain fails
     * @throws ServletException if the filter chain fails
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);

        // throw new AccessDeniedException("");
    }

    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    public Class<? extends Object> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    public void invoke(FilterInvocation fi) throws IOException,
            ServletException {
    	
    	Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
    	
    	boolean alwaysReauthenticate = false;
    	  	
    	//从token中获取用户信息
    	if(authentication==null || "anonymousUser".equals(authentication.getName())){
    		String accessToken = fi.getHttpRequest().getParameter("accessToken");
    		if(StringUtils.isBlank(accessToken))
    			accessToken = String.valueOf(fi.getHttpRequest().getAttribute("accessToken") );
    		CentitUserDetails ud = AccessTokenMetadata.getTokenUserDetails(accessToken);
    		if(ud!=null){
    			alwaysReauthenticate = this.isAlwaysReauthenticate();
    			if(alwaysReauthenticate)
    				this.setAlwaysReauthenticate(false);
    			SecurityContextHolder.getContext().setAuthentication(ud);
    		}
    	}
    	//从session中获取用户信息
    	if(authentication==null || "anonymousUser".equals(authentication.getName())){
	    	Object attr = fi.getHttpRequest().getSession().getAttribute(
	    			SecurityContextUtils.SecurityContextUserdetail);
	    	if(attr!=null && attr instanceof CentitUserDetails){
	    		authentication = (CentitUserDetails)attr;
	    		alwaysReauthenticate = this.isAlwaysReauthenticate();
    			if(alwaysReauthenticate)
    				this.setAlwaysReauthenticate(false);
    			SecurityContextHolder.getContext().setAuthentication(authentication);
	    	}
    	}
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }

		if(alwaysReauthenticate)
			this.setAlwaysReauthenticate(true);
    }

    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    public void setSecurityMetadataSource(
            FilterInvocationSecurityMetadataSource newSource) {
        this.securityMetadataSource = newSource;
    }

    public void destroy() {
    }

    public void init(FilterConfig arg0) throws ServletException {
    }

}


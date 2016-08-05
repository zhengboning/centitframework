package com.centit.framework.security;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.centit.framework.security.model.CentitSecurityMetadata;

//@Component("centitSecurityMetadataSource")
public class DaoInvocationSecurityMetadataSource
            implements FilterInvocationSecurityMetadataSource
         {
    //private static final Log logger = LogFactory.getLog(DaoInvocationSecurityMetadataSource.class);
    //private static boolean logDebug = logger.isDebugEnabled();
    @Override
    public boolean supports(Class<?> clazz) {
        if (FilterInvocation.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    // According to a URL, Find out permission configuration of this URL.
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // guess object is a URL.
        if ((object == null) || !this.supports(object.getClass())) {
            throw new IllegalArgumentException("对不起,目标对象不是类型");
        }
        FilterInvocation fi = (FilterInvocation) object;
        HttpServletRequest request = fi.getHttpRequest();
        String requestUrl = fi.getRequestUrl();
       /* if (logDebug) {
            logger.debug("通过权限过滤器 请求url = " + requestUrl + " 请求类型 = " + request.getMethod());
        }*/
        return CentitSecurityMetadata.matchUrlToRole(requestUrl,request);
    }
}
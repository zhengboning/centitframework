package com.centit.framework.interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.WebOptUtils;

/**
 * 在Ajax环境下判断当前用户会话是否已超时，默认拦截/system|service/**下所有请求，可以通过excludes属性添加例外。
 */
public class SessionInvalidHandleInterceptor extends HandlerInterceptorAdapter {

    protected AntPathMatcher matcher = new AntPathMatcher();
    /**
     * 排除拦截的uri表达式
     */
    protected String[] excludes;

    public void setExcludes(String[] excludes) {
        if(excludes==null || excludes.length<1){
            excludes = null;
            return;
        }
        
        this.excludes = 
                Arrays.copyOf(excludes, excludes.length); 
    }


    protected boolean isExcludes(HttpServletRequest request) {
        if (ArrayUtils.isEmpty(excludes)) {
            return false;
        }

        for (String exclude : excludes) {
            if (matcher.match(exclude, request.getRequestURI())) {
                return true;
            }
        }

        return false;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isExcludes(request)) {
            return super.preHandle(request, response, handler);
        }

        if (WebOptUtils.isAjax(request)) {
            HttpSession session = request.getSession(false);
            if (null == session || null == session.getAttribute("SPRING_SECURITY_CONTEXT")) {
                JsonResultUtils.writeErrorMessageJson(401, "用户未登录或者用户会话已超时！", response);
                return false;
            }
        }
        
        return super.preHandle(request, response, handler);
    }


}

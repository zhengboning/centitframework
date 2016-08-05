package com.centit.framework.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResponseCorsFilter implements Filter {
	@Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse alteredResponse = (HttpServletResponse) servletResponse;
		
		if (servletResponse instanceof HttpServletResponse) {
            if (request.getMethod().equals("OPTION") || request.getMethod().equals("OPTIONS")) {
                System.out.println("In Options");
                if (request.getServletPath().equals("/login")) {
                    System.out.println("request path match");
                    alteredResponse.setStatus(HttpServletResponse.SC_OK);
                    addHeadersFor200Response(alteredResponse);
                    return;
                }
            }

            addHeadersFor200Response(alteredResponse);
        }
		
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void addHeadersFor200Response(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL");
        response.addHeader("Access-Control-Allow-Headers", "useXDomain, withCredentials, Overwrite, Destination, Content-Type, Depth, User-Agent, Translate, Range, Content-Range, Timeout, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control, Location, Lock-Token, If");
        response.addHeader("Access-Control-Expose-Headers", "DAV, content-length, Allow");
        response.addHeader("Access-Control-Max-Age", "86400");
    }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
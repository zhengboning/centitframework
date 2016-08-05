package com.centit.product.ip.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.centit.framework.security.AjaxAuthenticationSuccessHandler;
import com.centit.framework.system.controller.MainFrameController;

public class IpAuthenticationSuccessHandler extends AjaxAuthenticationSuccessHandler{
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
		request.getSession().setAttribute(
				MainFrameController.ENTRANCE_TYPE, MainFrameController.DEPLOY_LOGIN);
		super.onAuthenticationSuccess(request,response,authentication);
	}
}

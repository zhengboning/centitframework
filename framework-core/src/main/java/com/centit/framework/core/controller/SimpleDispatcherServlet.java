package com.centit.framework.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.UrlPathHelper;

import com.centit.framework.core.common.JsonResultUtils;

/**
 * 重写404
 *
 * @author sx
 * @create 2014-10-22
 */
public class SimpleDispatcherServlet extends DispatcherServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Log logger = LogFactory.getLog(SimpleDispatcherServlet.class);

    private static UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Override
    protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String requestUri = urlPathHelper.getRequestUri(request);
        if (logger.isWarnEnabled()) {
            logger.warn("No mapping found for HTTP request with URI [" + requestUri
                    + "] in DispatcherServlet with name '" + getServletName() + "'");
        }

        String header = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(header)) {
            JsonResultUtils.writeAjaxErrorMessage(404, "未找到此资源（"+requestUri+"）", response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/system/exception/error/404");
    }

}

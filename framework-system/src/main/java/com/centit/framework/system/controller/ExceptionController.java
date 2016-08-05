package com.centit.framework.system.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;

/**
 * 通用的处理控制器，主要用于处理异常情况，
 *
 * @author codefan
 * @create 2014年10月24日
 */
@Controller
@RequestMapping("/exception")
public class ExceptionController extends BaseController {

    private static Log logger = LogFactory.getLog(ExceptionController.class);
    
    /**
     * 访问当前无权限URL请求后返回地址
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/accessDenied")
    public String accessDenied(HttpServletRequest request, HttpServletResponse response) {
        return "forward:/system/exception/error/403";          
    }

    /**
     * web.xml中配置的异常捕捉
     * CentitMappingExceptionResolver类中异常捕捉也会跳转过来
     * web.xml中捕捉的异常不能得到异常的具体内容，CentitMappingExceptionResolver可以将异常信息传递过来
     *
     * @param code     错误码
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/error/{code}")
    public String errorPage(@PathVariable int code, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (WebOptUtils.isAjax(request)) {
            //框架前端AngularJs均由Ajax请求接收数据
            String errorMessage;
            
            switch (code) {
                case 404:
                    errorMessage = "未找到此资源";
                    break;
                case 403:
                    {
                        errorMessage =(String) request.getAttribute("CENTIT_SYSTEM_ERROR_MSG");
                        if(errorMessage==null){
                            Exception ex = (Exception) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
                            //触发异常的类
                            if (null != ex) {
                                errorMessage = ex.getMessage();
                            }else                           
                                errorMessage = "无权限访问此资源 !";
                        }
                    }
                    break;
                default:
                    errorMessage = "服务器内部错误";
                    Exception ex = (Exception) request.getAttribute("CENTIT_SYSTEM_ERROR_EXCEPTION");
                    //触发异常的类
                    if (null != ex) {
                        errorMessage = ex.getMessage();
                    }

                    HandlerMethod handler = (HandlerMethod) request.getAttribute("CENTIT_SYSTEM_ERROR_HANDLER");
                    if (null != handler) {
                        errorMessage = "异常信息由 " + handler.getBean().getClass().getName()
                                + " 类 " + handler.getMethod().getName() + " 方法触发异常，异常类型为 "
                                + ex.getClass().getName() + " 异常信息为 " + ex.getMessage();
                        logger.error(errorMessage);
                    }
                    break;
            }
            
            JsonResultUtils.writeAjaxErrorMessage(code, errorMessage, response);
            return null;
            //throw new ObjectException(errorMessage, ObjectException.ExceptionType.SYSTEM);
        } else {
            return "exception/" + code;
        }
    }


}

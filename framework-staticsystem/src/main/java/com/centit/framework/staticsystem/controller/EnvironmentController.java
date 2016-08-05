package com.centit.framework.staticsystem.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.staticsystem.service.StaticEnvironmentManager;

@Controller
@RequestMapping("/environment")
public class EnvironmentController extends BaseController {

	@Resource
    protected StaticEnvironmentManager platformEnvironment;
	
	@RequestMapping(value ="/reload/dictionary",method = RequestMethod.GET)
    public void reloadDictionary(
            HttpServletRequest request,HttpServletResponse response) {
		if(platformEnvironment.reloadDictionary())
			JsonResultUtils.writeSuccessJson(response);
		else
			JsonResultUtils.writeErrorMessageJson("reloadDictionary failed！", response);
	}
	
	@RequestMapping(value ="/reload/securitymetadata",method = RequestMethod.GET)
    public void reloadSecurityMetadata(
            HttpServletRequest request,HttpServletResponse response) {
		if(platformEnvironment.reloadSecurityMetadata())
			JsonResultUtils.writeSuccessJson(response);
		else
			JsonResultUtils.writeErrorMessageJson("reloadSecurityMetadata failed！", response);
	}
	
	@RequestMapping(value ="/reload/ipenvironment",method = RequestMethod.GET)
    public void reloadIPEnvironment(
            HttpServletRequest request,HttpServletResponse response) {
		if(platformEnvironment.reloadIPEnvironmen())
			JsonResultUtils.writeSuccessJson(response);
		else
			JsonResultUtils.writeErrorMessageJson("reloadIPEnvironmen failed！", response);
	}
	
	@RequestMapping(value ="/osinfos",method = RequestMethod.GET)
    public void listOsInfos(
            HttpServletRequest request,HttpServletResponse response) {
		
		ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, platformEnvironment.listOsInfos());
		JsonResultUtils.writeResponseDataAsJson(resData, response);
	}
	
	@RequestMapping(value ="/databaseinfos",method = RequestMethod.GET)
    public void listDatabaseInfos(
            HttpServletRequest request,HttpServletResponse response) {
		
		ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, platformEnvironment.listDatabaseInfo());
		JsonResultUtils.writeResponseDataAsJson(resData, response);
	}
}

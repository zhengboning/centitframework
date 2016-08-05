package com.centit.framework.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.dao.SysDaoOptUtils;
import com.centit.framework.model.basedata.OperationLog;
import com.centit.framework.system.po.UnitInfo;
import com.centit.framework.system.po.UserInfo;
import com.centit.framework.system.po.UserUnit;
import com.centit.framework.system.service.SysUnitManager;
import com.centit.framework.system.service.SysUserManager;
import com.centit.framework.system.service.SysUserUnitManager;
import com.centit.support.algorithm.ListOpt;
import com.centit.support.json.JsonPropertyUtils;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-10-28
 * Time: 下午1:32
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/unitinfo")
public class UnitInfoController extends BaseController {

    @Resource
    @NotNull
    private SysUnitManager sysUnitManager;
    
    @Resource
    @NotNull
    private SysUserManager sysUserMag;
    
    @Resource
    @NotNull
    private SysUserUnitManager sysUserUnitManager;
    
    /**
     * 系统日志中记录
     */
    private String optId = "UNITMAG";// CodeRepositoryUtil.getCode("OPTID", "unitInfo");

    /**
     * 查询所有机构信息
     *
     * @param field    需要显示的字段
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, boolean struct, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        List<UnitInfo> listObjects = null;
        listObjects = sysUnitManager.listObjects(searchColumn);
        /*JSONArray ja  = ListOpt.srotAsTreeAndToJSON(listObjects,
				new ListOpt.ParentChild<UnitInfo>(){
					@Override
					public boolean parentAndChild(UnitInfo p, UnitInfo c) {
						return StringUtils.equals(
								p.getUnitCode(),
								c.getParentUnit());
					}

    			}, "children");
    	 */
        JSONArray ja = SysDaoOptUtils.objectsToJSONArray(listObjects);
        if(struct){
        	ja = ListOpt.srotAsTreeAndToJSON(ja, 
    				new ListOpt.ParentChild<Object>(){
						@Override
						public boolean parentAndChild(Object p, Object c) {
							return StringUtils.equals(
									((JSONObject)p).getString("unitCode"),
									((JSONObject)c).getString("parentUnit"));
						}

        			}, "children");
        }
        JsonResultUtils.writeSingleDataJson(
        		ja,
        		response, JsonPropertyUtils.getIncludePropPreFilter(JSONObject.class, field));
        //JsonResultUtils.writeSingleDataJson(listObjects, response, JsonPropertyUtils.getIncludePropPreFilter(UnitInfo.class, field));
    }

    /**
     * 查询所有子机构信息
     *
     * @param field    需要显示的字段
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/subunits",method = RequestMethod.GET)
    public void list_sub(String[] field, boolean struct, HttpServletRequest request, HttpServletResponse response) {
        UserInfo user=sysUserMag.getObjectById(this.getLoginUser(request).getUserCode());
        List<UnitInfo> listObjects = null;
        listObjects = sysUnitManager.listAllSubObjects(user.getPrimaryUnit());
        JSONArray ja = SysDaoOptUtils.objectsToJSONArray(listObjects);
        if(struct){
        	ja = ListOpt.srotAsTreeAndToJSON(ja, 
    				new ListOpt.ParentChild<Object>(){
						@Override
						public boolean parentAndChild(Object p, Object c) {
							return StringUtils.equals(
									((JSONObject)p).getString("unitCode"),
									((JSONObject)c).getString("parentUnit"));
						}

        			}, "children");
        }
        JsonResultUtils.writeSingleDataJson(
        		ja,
        		response, JsonPropertyUtils.getIncludePropPreFilter(JSONObject.class, field));
      }
    
    
    /**
     * 查询单个机构信息
     *
     * @param unitCode 机构代码
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{unitCode}", method = RequestMethod.GET)
    public void getUnitInfo(@PathVariable String unitCode, HttpServletResponse response) {
        UnitInfo unitInfo = sysUnitManager.getObjectById(unitCode);

        JsonResultUtils.writeSingleDataJson(unitInfo, response);
    }
    
    /**
     * 删除机构
     *
     * @param unitCode
     * @param response
     */
    @RequestMapping(value = "/{unitCode}", method = {RequestMethod.DELETE})
    public void delete(@PathVariable String unitCode,HttpServletRequest request, HttpServletResponse response) {
        UnitInfo unitInfo = sysUnitManager.getObjectById(unitCode);
        if(unitInfo==null){
        	JsonResultUtils.writeErrorMessageJson("The object not found!", response);
        	return;
        }

        sysUnitManager.deleteUnitInfo(unitInfo);

        JsonResultUtils.writeBlankJson(response);
        /*********log*********/
        OperationLogCenter.logDeleteObject(request,this.optId,unitInfo.getUnitCode(),
        		OperationLog.P_OPT_LOG_METHOD_D,  "已删除",unitInfo);
        /*********log*********/
    }


    /**
     * 新建机构
     *
     * @param unitInfo UnitInfo
     * @param response HttpServletResponse
     */
    @RequestMapping(method = RequestMethod.POST)
    public void create(@Valid UnitInfo unitInfo, HttpServletRequest request,HttpServletResponse response) {

        unitInfo.setUnitCode(sysUnitManager.getNextKey());
        
        sysUnitManager.saveNewUnitInfo(unitInfo);

        JsonResultUtils.writeSingleDataJson(unitInfo, response);


        /*********log*********/
        OperationLogCenter.logNewObject(request,optId,unitInfo.getUnitCode(),
        		OperationLog.P_OPT_LOG_METHOD_C,  "新增机构" , unitInfo);
        /*********log*********/
    }

    /**
     * 更新机构信息
     *
     * @param unitCode 机构代码
     * @param unitInfo UnitInfo
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{unitCode}", method = RequestMethod.PUT)
    public void edit(@PathVariable String unitCode, @Valid UnitInfo unitInfo,
            HttpServletRequest request,HttpServletResponse response) {
    	
        UnitInfo dbUnitInfo = sysUnitManager.getObjectById(unitCode);
        if (null == dbUnitInfo) {
            JsonResultUtils.writeErrorMessageJson("机构不存在", response);

            return;
        }
        /*********log*********/
        UnitInfo oldValue = new UnitInfo();
        oldValue.copy(dbUnitInfo);
        /*********log*********/
       
        sysUnitManager.updateUnitInfo(unitInfo);

        JsonResultUtils.writeBlankJson(response);

        /*********log*********/
        OperationLogCenter.logUpdateObject(request,optId,dbUnitInfo.getUnitCode(),
        		OperationLog.P_OPT_LOG_METHOD_U,  "更新机构状态", dbUnitInfo,oldValue);
        /*********log*********/
    }

    /**
     * 更新机构及子机构的状态
     *
     * @param unitCode    机构代码
     * @param statusValue 状态码 T 或 F
     * @param response    HttpServletResponse
     */
    @RequestMapping(value = "/{unitCode}/status/{statusValue}", method = RequestMethod.PUT)
    public void changeStatus(@PathVariable String unitCode, @PathVariable String statusValue,
            HttpServletRequest request,HttpServletResponse response) {
        UnitInfo dbUnitInfo = sysUnitManager.getObjectById(unitCode);
        if (null == dbUnitInfo) {
            JsonResultUtils.writeErrorMessageJson("机构不存在", response);

            return;
        }

        if (!"T".equals(statusValue) && !"F".equals(statusValue)) {
            JsonResultUtils.writeErrorMessageJson("机构状态不正确", response);

            return;
        }

        sysUnitManager.changeStatus(unitCode, statusValue);

        JsonResultUtils.writeBlankJson(response);

        /*********log*********/
         String optContent = "更新机构状态,机构名称:" + CodeRepositoryUtil.getCode(CodeRepositoryUtil.UNIT_CODE, unitCode) + ",机构是否启用:" + ("T".equals
                (statusValue) ? "是" : "否");
        OperationLogCenter.log(request,optId,unitCode, OperationLog.P_OPT_LOG_METHOD_U,  optContent);
        /*********log*********/
    }

    /**
     * 保存前获取机构主键，通过序列生成
     *
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/nextunitcode", method = RequestMethod.GET)
    public void getNextUnitCode(HttpServletResponse response) {
        String nextKey = sysUnitManager.getNextKey();

        JsonResultUtils.writeSingleDataJson(nextKey, response);
    }

    /**
     * 获取单个机构下属子机构
     *
     * @param field    需要显示的字段
     * @param unitCode 机构代码
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{unitCode}/children", method = RequestMethod.GET)
    public void listChildren(@PathVariable String unitCode, String[] field, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        searchColumn.put("parentUnit", unitCode);

        List<UnitInfo> listObjects = sysUnitManager.listObjects(searchColumn);

        JsonResultUtils.writeSingleDataJson(listObjects, response, JsonPropertyUtils.getIncludePropPreFilter(UnitInfo.class, field));
    }


    /**
     * 当前机构下所有用户
     *
     * @param field    UserInfo需要显示的字段
     * @param unitCode 机构代码
     * @param primary  是否为主机构，可为空
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{unitCode}/users", method = RequestMethod.GET)
    public void listUnitUsers(@PathVariable String unitCode, String[] field, String primary, PageDesc pageDesc, HttpServletResponse response) {
       
        
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("unitCode", unitCode);
        if (StringUtils.isNotBlank(primary)) {
            filterMap.put("isPrimary", primary);
        }

        List<UserUnit> listObjects = sysUserUnitManager.listObjects(filterMap, pageDesc);

        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);
        if(null==field)
        	field=new String[]{};
        JsonResultUtils.writeResponseDataAsJson(resData, response, JsonPropertyUtils.getIncludePropPreFilter(UserUnit.class, field));
    }

    /**
     * 当前机构下用户
     *
     * @param unitCode    机构代码
     * @param userCode    用户代码
     * @param userStation 岗位
     * @param userRank    职务
     */
    @RequestMapping(value = "/unitusers/{userunitid}", method = RequestMethod.GET)
    public void getUnitUser(@PathVariable String userunitid,  HttpServletResponse response) {
        UserUnit userUnit = sysUserUnitManager.getObjectById(userunitid);

        if (null == userUnit) {

            JsonResultUtils.writeErrorMessageJson("当前机构中无此用户", response);
            return;
        }

        JsonResultUtils.writeSingleDataJson(userUnit, response);
    }
}

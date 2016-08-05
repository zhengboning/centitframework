package com.centit.framework.security.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.centit.framework.model.basedata.IUserInfo;

public interface CentitUserDetails extends 
		Authentication, UserDetails,IUserInfo {
    /**
     * 用户头衔，业务系统可以自定义这个字段的意义
     * @return
     */
    public String getUserWord();
    /**
     * 密码有效期时间
     * @return
     */
    public Date getPwdExpiredTime();
    /**
     * 用户的参数，是个Map对，有系统参数也有业务系统自定义的参数
     * @return
     */
    public Map<String, String> getUserSettings();
    /**
     * 用户某个具体的参数值
     * @param paramCode	参数代码
     * @return
     */
    public String getUserSettingValue(String paramCode);
    /**
     * 用户所有的可以执行操作方法，用于权限控制
     * @return
     */
    public Map<String, String> getUserOptList();
    /**
     * 判断用户是否有某个具体的操作方法权限
     * @param optId	业务ID
     * @param optMethod	方法名称
     * @return
     */
    public boolean checkOptPower(String optId, String optMethod);
    
    /**
     * 获得用户授权角色代码
     * @return
     */
    public List<String> getUserRoleCodes();
}

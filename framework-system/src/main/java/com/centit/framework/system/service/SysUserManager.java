package com.centit.framework.system.service;

import java.util.List;

import com.centit.framework.core.service.BaseEntityManager;
import com.centit.framework.system.po.FVUserOptList;
import com.centit.framework.system.po.RoleInfo;
import com.centit.framework.system.po.UserInfo;

public interface SysUserManager extends BaseEntityManager<UserInfo, String> {

    // public Collection<GrantedAuthority> loadUserAuthorities(String username);

    public void resetPwd(String userCode);

    public void resetPwd(String[] userCodes);

    public String encodePassword(String password, String userCode);

    public void setNewPassword(String userID, String oldPassword, String newPassword);

     /**
     * 保存用户信息，包括用户机构、用户角色信息
     * @param userinfo
     */
    public void saveNewUserInfo(UserInfo userinfo);
    
    public void updateUserInfo(UserInfo userinfo);
    
    public void updateUserProperities(UserInfo userinfo);
    
    
    public void deleteUserInfo(String userCode);
    
    
 
    public String getNextUserCode();

    public List<RoleInfo> listUserValidRoles(String userCode);
   
    /**
     * 模糊搜索
     * @param key 搜索关键字
     * @param field 在以下字段中搜索
     */
    public List<UserInfo> search(String key, String[] field);
    
    public UserInfo loadUserByLoginname(String userCode);
    public List<FVUserOptList> getAllOptMethodByUser(String userCode);

    public boolean rightPW(String userCode, String oldPassword);
}

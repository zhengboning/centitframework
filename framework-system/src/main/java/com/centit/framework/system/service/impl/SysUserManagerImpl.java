package com.centit.framework.system.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.centit.framework.core.common.ObjectException;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.system.dao.UnitInfoDao;
import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.dao.UserUnitDao;
import com.centit.framework.system.po.FVUserOptList;
import com.centit.framework.system.po.RoleInfo;
import com.centit.framework.system.po.UserInfo;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserUnit;
import com.centit.framework.system.service.SysUserManager;
import com.centit.support.algorithm.StringBaseOpt;

@Service("sysUserManager")
@Transactional
public class SysUserManagerImpl extends 
    BaseEntityManagerImpl<UserInfo, String, UserInfoDao> 
    implements SysUserManager {

    // 加密
    @Resource
    @NotNull
    private Md5PasswordEncoder passwordEncoder;

    @Resource
    @NotNull
    private UserUnitDao userUnitDao;

    @Resource
    @NotNull
    private UnitInfoDao unitInfoDao;

    @Resource
    @NotNull
    private UserRoleDao userRoleDao;

    @Override
    @Resource(name = "userInfoDao")
    protected void setBaseDao(UserInfoDao baseDao) {
        super.baseDao = baseDao;
        logger = LogFactory.getLog(SysUserManagerImpl.class);
    }

    public String encodePassword(String password, String userCode) {
        return passwordEncoder.encodePassword(password, userCode);
    }

    private String getDefaultPassword(String userCode) {
        final String defaultPassword = "000000";
        return encodePassword(defaultPassword, userCode);
    }

    @Override
    public List<RoleInfo> listUserValidRoles(String userCode) {
        List<RoleInfo> roles = userRoleDao.getSysRolesByUserId(userCode);
        return roles;
    }

    public List<UserRole> listUserValidRoles(String userCode, String rolePrefix) {
        return userRoleDao.getUserRolesByUserId(userCode, rolePrefix);
    }

    public List<UserRole> listUserAllRoles(String userCode, String rolePrefix) {
        return userRoleDao.getAllUserRolesByUserId(userCode, rolePrefix);
    }

    @Override
    public void resetPwd(String userCode) {
        UserInfo user = baseDao.getObjectById(userCode);
        user.setUserPin(getDefaultPassword(user.getUserCode()));
        baseDao.saveObject(user);
    }

    @Override
    public void resetPwd(String[] userCodes) {
        for (String userCode : userCodes) {
            resetPwd(userCode);
        }
    }


    public void setNewPassword(String userID, String oldPassword, String newPassword) {
        UserInfo user = baseDao.getObjectById(userID);
        if (!user.getUserPin().equals(encodePassword(oldPassword, user.getUserCode())))
            throw new ObjectException("旧密码不正确！");
        
        if (user.getUserPin().equals(encodePassword(newPassword, user.getUserCode())))
            throw new ObjectException("新密码和旧密码一致，请重新输入新密码！");

        user.setUserPin(encodePassword(newPassword, user.getUserCode()));
        baseDao.saveObject(user);
    }


    @Override
    @CacheEvict(value = "UserInfo",allEntries = true)
    public void mergeObject(UserInfo sysuser) {
        if (StringUtils.isBlank(sysuser.getUserCode())) {// 新添
            boolean hasExist = baseDao.checkIfUserExists(sysuser);// 查该登录名是不是已经被其他用户使
            if (hasExist) {
                throw new ObjectException("当前登录名已存在");
            }
            sysuser.setUserCode(getNextUserCode());
//            sysuser.setIsValid("T");
            sysuser.setUserPin(getDefaultPassword(sysuser.getUserCode()));
        }

        if (StringUtils.isBlank(sysuser.getUserPin())) {
            sysuser.setUserPin(getDefaultPassword(sysuser.getUserCode()));
        }
        sysuser.setUserWord(StringBaseOpt.getFirstLetter(sysuser.getUserName()));
        super.mergeObject(sysuser);
    }

    @CacheEvict(value = "UserInfo",allEntries = true)
    public void saveObject(UserInfo sysuser) {
        boolean hasExist = baseDao.checkIfUserExists(sysuser);// 查该登录名是不是已经被其他用户使

        if (StringUtils.isBlank(sysuser.getUserCode())) {// 新添
            // sysuser.setUsercode( getNextUserCode('u'));
            sysuser.setIsValid("T");
            sysuser.setUserPin(getDefaultPassword(sysuser.getUserCode()));
        }
        if (!hasExist && StringUtils.isBlank(sysuser.getUserPin()))
            sysuser.setUserPin(getDefaultPassword(sysuser.getUserCode()));
        sysuser.setUserWord(StringBaseOpt.getFirstLetter(sysuser.getUserName()));
        baseDao.saveObject(sysuser);
    }
   
    
    
    @Override
    @CacheEvict(value ={"UserInfo","UnitUsers","UserUnits","AllUserUnits"},allEntries = true)
    public void saveNewUserInfo(UserInfo userInfo){
        baseDao.saveNewObject(userInfo);
        if(null!=userInfo.getUserUnits()){
            for(UserUnit uu:userInfo.getUserUnits()){
                userUnitDao.saveNewObject(uu);
            }
        }
        if(null!=userInfo.listUserRoles()){
            for(UserRole ur:userInfo.listUserRoles()){
                userRoleDao.saveNewObject(ur);
            }
        }
    }

    
    @Override
    @CacheEvict(value ={"UserInfo","UnitUsers","UserUnits","AllUserUnits"},allEntries = true)
    public void updateUserInfo(UserInfo userinfo){
        
        baseDao.mergeObject(userinfo);
        
        /*List<UserUnit> oldUserUnits = userUnitDao.listUserUnitsByUserCode(userinfo.getUserCode());
         if(oldUserUnits!=null){
            for(UserUnit uu: oldUserUnits ){
                if(null ==userinfo.getUserUnits() ||
                        ! userinfo.getUserUnits().contains(uu)){
                    userUnitDao.deleteObject(uu);
                }
            }
        }
        
        if(userinfo.getUserUnits() !=null){
            for(UserUnit uu: userinfo.getUserUnits() ){
                 userUnitDao.mergeObject(uu);
            }
        }
        
        */
    }
    
    @Override
    @CacheEvict(value ="UserInfo",allEntries = true)
    public void updateUserProperities(UserInfo userinfo){
        baseDao.mergeObject(userinfo);
    }
    
    @Override
    @CacheEvict(value ={"UserInfo","UnitUsers","UserUnits","AllUserUnits"},allEntries = true)
    public void deleteUserInfo(String userCode){       
        userUnitDao.deleteUserUnitByUser(userCode);
        userRoleDao.deleteByUserId(userCode);        
        baseDao.deleteObjectById(userCode);        
    }
    

    public String getNextUserCode() {
        return baseDao.getNextKey();
    }

    @Override
    public List<UserInfo> search(String key, String[] field) {
        return baseDao.search(key, field);
    }

    @Override
    public UserInfo loadUserByLoginname(String userCode){
        return baseDao.getUserByLoginName(userCode);
    }
    @Override
    public List<FVUserOptList> getAllOptMethodByUser(String userCode){
        return baseDao.getAllOptMethodByUser(userCode);
    }

    @Override
    public boolean rightPW(String userCode, String oldPassword) {
        UserInfo user = baseDao.getObjectById(userCode);
        if (user.getUserPin().equals(encodePassword(oldPassword, user.getUserCode())))
            return true;
        else
            return false;
    }
}

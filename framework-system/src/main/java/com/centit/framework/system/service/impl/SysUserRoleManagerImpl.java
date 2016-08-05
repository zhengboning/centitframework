package com.centit.framework.system.service.impl;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;
import com.centit.framework.system.service.SysUserRoleManager;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-10-28
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class SysUserRoleManagerImpl extends BaseEntityManagerImpl<UserRole, UserRoleId, UserRoleDao> 
    implements SysUserRoleManager {
 
    @Resource(name = "userRoleDao")
    @NotNull
    @Override
    protected void setBaseDao(UserRoleDao baseDao) {
        this.baseDao=baseDao;
    }
    @Override
    public void mergeObject(UserRole dbUserRole, UserRole userRole) {
        baseDao.deleteObject(dbUserRole);

        baseDao.mergeObject(userRole);
    }
}

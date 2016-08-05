package com.centit.framework.system.service;

import com.centit.framework.core.service.BaseEntityManager;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;


/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-10-28
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public interface SysUserRoleManager extends BaseEntityManager<UserRole, UserRoleId> {
    public void mergeObject(UserRole dbUserRole, UserRole userRole);
}

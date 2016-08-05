package com.centit.framework.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.po.RoleInfo;
import com.centit.framework.system.po.VOptTree;
import com.centit.support.database.QueryUtils;

@Repository
public class RoleInfoDao extends BaseDaoImpl<RoleInfo, String> {

    @SuppressWarnings("unchecked")
    @Transactional
    public List<VOptTree> getVOptTreeList() {
        return (List<VOptTree>)DatabaseOptUtils.findObjectsByHql
                (this,"FROM VOptTree");
    }

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("ROLECODE", CodeBook.LIKE_HQL_ID);
            filterField.put("UNITROLE", "(roleCode like ? or roleCode like 'P-%')");
            filterField.put("NP_GLOBAL", "(roleCode like 'G-%' or roleCode like 'P-%')");
            filterField.put("ROLENAME", CodeBook.LIKE_HQL_ID);
            filterField.put("ROLEDESC", CodeBook.LIKE_HQL_ID);
            filterField.put("ISVALID", CodeBook.EQUAL_HQL_ID);
        }
        return filterField;
    }
   
    @SuppressWarnings("unchecked")
    @Transactional
    public List<Object> listRoleOptMethods(String rolecode) {
        String hql = "select new map(def.optName as def_optname, def.optCode as def_optcode) "
                + "from OptMethod def, RolePower pow where def.optCode = pow.id.optCode and pow.id.roleCode = ?";
        return (List<Object>)DatabaseOptUtils.findObjectsByHql
                (this,hql,  new Object[]{rolecode});
    }

    
    /**
     * 对角色信息进行模糊搜索，适用于带搜索条件的下拉框。
     *
     * @param key      搜索条件
     * @param field    需要搜索的字段，如为空，默认，roleCode,roleName
     * @param response HttpServletResponse
     */
    @Transactional
    public List<RoleInfo> search(String key, String[] field) {
        StringBuilder hql = new StringBuilder("from RoleInfo u where ");
        String params[] = new String[field.length];
        String sMatch = QueryUtils.getMatchString(key);
        for (int i = 0; i < field.length; i++) {
            hql.append("u." + field[i] + " like ? ");//'%" +  key + "%' ");
            if (i != field.length - 1) {
                hql.append(" or ");
            }
            params[i] = sMatch; 
        }
        return listObjects( hql.toString(),params);       
    }
}

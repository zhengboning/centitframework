package com.centit.framework.system.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.po.OptFlowNoPool;
import com.centit.framework.system.po.OptFlowNoPoolId;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.QueryUtils;

@Repository
public class OptFlowNoPoolDao extends BaseDaoImpl<OptFlowNoPool, OptFlowNoPoolId> {


    public static final Log log = LogFactory.getLog(OptFlowNoPoolDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("ownerCode", "cid.ownerCode=?");
            filterField.put("codeDate", "cid.codeDate=?");
            filterField.put("codeCode", "cid.codeCode=?");
            filterField.put("curNo", "cid.ownerCode=?");
        }
        return filterField;
    }

    @Transactional
    public long fetchFirstLsh(String ownerCode, String codeCode,
                              Date codeBaseDate) {
        return DatabaseOptUtils.getSingleIntBySql(this,
                "select min(CurNo) as MinNo from F_OptFlowNoPool" +
                " where OwnerCode = " + QueryUtils.buildStringForQuery(ownerCode) +
                " and CodeCode = " + QueryUtils.buildStringForQuery(ownerCode) +
                " and CodeDate = to_date(" + QueryUtils.buildStringForQuery(
                DatetimeOpt.convertDatetimeToString(codeBaseDate))
                + ",'YYYY-MM-DD HH:MI:SS')");
    }
}

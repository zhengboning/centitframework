package com.centit.framework.system.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.po.UnitInfo;
import com.centit.framework.system.po.UserInfo;

@Repository
public class UnitInfoDao extends BaseDaoImpl<UnitInfo, String> {
    public static final Log log = LogFactory.getLog(UnitInfoDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("UNITCODE", CodeBook.EQUAL_HQL_ID);
            filterField.put("UNITNAME", CodeBook.LIKE_HQL_ID);
            filterField.put("ISVALID", CodeBook.EQUAL_HQL_ID);
            filterField.put("UNITTAG", CodeBook.EQUAL_HQL_ID);
            filterField.put("UNITWORD", CodeBook.EQUAL_HQL_ID);
            filterField.put("PARENTUNIT", CodeBook.EQUAL_HQL_ID);
            filterField.put(CodeBook.ORDER_BY_HQL_ID, " unitorder, unitCode ");
        }
        return filterField;
    }

    @Transactional
    public String getNextKey() {
	/*	return getNextKeyByHqlStrOfMax("unitCode",
						"FUnitinfo WHERE unitCode !='99999999'",6);*/
        return DatabaseOptUtils.getNextKeyBySequence(this, "S_UNITCODE", 6);
    }

    @Transactional
    public String getUnitCode(String depno) {
        List<UnitInfo> ls = listObjects("FROM UnitInfo where depNo=?", depno);
        if (ls != null) {
            return ls.get(0).getUnitCode();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation=Propagation.MANDATORY) 
    public List<UserInfo> listUnitUsers(String unitCode) {
        String sSqlsen = "select a.* " +
                "from f_Userinfo a join f_userunit b on(a.userCode=b.userCode) " +
                "where b.unitcode =?";

        return (List<UserInfo>) DatabaseOptUtils.findObjectsBySql(this, sSqlsen, new Object[]{unitCode} ,UserInfo.class);
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation=Propagation.MANDATORY) 
    public List<UserInfo> listRelationUsers(String unitCode) {
        String sSqlsen = "select * FROM F_Userinfo ui where ui.userCode in " +
                "(select userCode from f_userunit where unitcode='" + unitCode + "') or " +
                "ui.userCode in (select userCode from f_userrole where rolecode like ? ";

        return (List<UserInfo>) DatabaseOptUtils.findObjectsBySql(this, sSqlsen,new Object[]{unitCode+ "-%"}, UserInfo.class);
    }

    @Transactional
    public String getUnitNameOfCode(String unitcode) {
       return String.valueOf( DatabaseOptUtils.getSingleObjectByHql(this, 
                "select unitname from f_unitinfo where unitcode=?", unitcode ));
    }
    @SuppressWarnings("unchecked")
    @Transactional
    public List<UnitInfo> listUnitinfoByUnitcodes(List<String> unitcodes) {

        return (List<UnitInfo>)  
                this.getCurrentSession().createCriteria(UnitInfo.class).
                    add(Restrictions.in("unitCode", unitcodes)).list();
    }

    /**
     * 批量添加或更新
     *
     * @param unitinfos
     */
    @Transactional
    public void batchSave(List<UnitInfo> unitinfos) {
        for (int i = 0; i < unitinfos.size(); i++) {
            saveObject(unitinfos.get(i));
        }
    }
    @Transactional
    public void batchMerge(List<UnitInfo> unitinfos) {
        for (int i = 0; i < unitinfos.size(); i++) {
            this.mergeObject(unitinfos.get(i));

            if (19 == i % 20) {
                DatabaseOptUtils.flush(this.getCurrentSession());
            }
        }
    }
    @Transactional
    public UnitInfo getUnitByName(String name) {
        if (StringUtils.isNotBlank(name)) {
            String hql = "from UnitInfo where unitName = ? or unitShortName = ?"
            			+ " order by unitOrder asc";
            List<UnitInfo> list = listObjects(hql,
            		new Object[]{name,name});
            if (list !=null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }
    
    @Transactional
    public UnitInfo getUnitByTag(String unitTag) {
    	return super.getObjectByProperty("unitTag", unitTag);
    }
    
    @Transactional
    public UnitInfo getUnitByWord(String unitWord) {
    	return super.getObjectByProperty("unitWord", unitWord);
    }
    
    @Transactional
    public List<UnitInfo> listSubUnits(String unitCode){
    	return super.listObjectByProperty("parentUnit", unitCode);
    	/*String hql = "from UnitInfo where parentUnit = ?";
    	return listObjects(hql,
    		new Object[]{unitCode,unitCode});*/
    }
    
    @SuppressWarnings("unchecked")
    @Transactional
    public List<UnitInfo> listSubUnitinfoByParentUnitcodes(List<String> parentunitcodes) {

        return (List<UnitInfo>)  
                this.getCurrentSession().createCriteria(UnitInfo.class).
                    add(Restrictions.in("parentUnit", parentunitcodes)).list();
    }
    
    @Transactional(propagation=Propagation.MANDATORY) 
    public List<UnitInfo> listAllSubUnits(String unitCode){
    	
    	List<UnitInfo> subUnits = listSubUnits(unitCode);
    	List<UnitInfo> allSubUnits = new ArrayList<UnitInfo>();
    	UnitInfo currUnit = getObjectById(unitCode);
    	if(currUnit==null)
    		return allSubUnits;
    	allSubUnits.add(currUnit);
    	//listUnitinfoByUnitcodes
    	while(subUnits!=null && subUnits.size()>0){
    		allSubUnits.addAll(subUnits);
    		List<String> subUnitCodes = new ArrayList<String>();
    		for(UnitInfo ui:subUnits){
    			subUnitCodes.add(ui.getUnitCode());
    		}
    		subUnits =
    				listSubUnitinfoByParentUnitcodes(subUnitCodes);
    	}
    	return allSubUnits;    	
    }
    
    @Transactional(propagation=Propagation.MANDATORY) 
    public List<UnitInfo> listSubUnitsByUnitPaht(String unitPath){
    	String hql = "from UnitInfo where unitPath like ?";
    	return listObjects(hql,
    		new Object[]{unitPath+"/%"});
    }
}

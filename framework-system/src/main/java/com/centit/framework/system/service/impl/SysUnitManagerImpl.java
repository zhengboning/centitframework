package com.centit.framework.system.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.system.dao.UnitInfoDao;
import com.centit.framework.system.dao.UserUnitDao;
import com.centit.framework.system.po.UnitInfo;
import com.centit.framework.system.po.UserInfo;
import com.centit.framework.system.service.SysUnitManager;
import com.centit.support.algorithm.StringBaseOpt;

@Service("sysUnitManager")
@Transactional
public class SysUnitManagerImpl extends BaseEntityManagerImpl<UnitInfo, String, UnitInfoDao> implements
        SysUnitManager {

    @Resource
    @NotNull
    private UserUnitDao userUnitDao;

    @Resource(name = "unitInfoDao")
    @NotNull
    @Override
    protected void setBaseDao(UnitInfoDao baseDao) {
        super.baseDao = baseDao;
        logger = LogFactory.getLog(SysUnitManagerImpl.class);
    }

    @Override
    public List<UnitInfo> listObjectsAsSort(Map<String, Object> searchColumn) {
        List<UnitInfo> listObjects = baseDao.listObjects(searchColumn);
        Iterator<UnitInfo> unitInfos = listObjects.iterator();
       
        while (unitInfos.hasNext()) {
            UnitInfo unitInfo = unitInfos.next();
            if (StringBaseOpt.isNvl(unitInfo.getParentUnit()) || "0".equals(unitInfo.getParentUnit())) {
                continue;
            }
            for (UnitInfo opt : listObjects) {
                if (opt.getUnitCode().equals(unitInfo.getParentUnit())) {
                    opt.getSubUnits().add(unitInfo);
                    break;
                }
            }
        }
        // 获取顶级的父级菜单
        List<UnitInfo> parentUnit = new ArrayList<>();
        for (UnitInfo unitInfo : listObjects) {
            if (StringBaseOpt.isNvl(unitInfo.getParentUnit()) || "0".equals(unitInfo.getParentUnit())) {
                parentUnit.add(unitInfo);
            }
        }
        return parentUnit;
    }

    /**
     * 查找对象，如果没有新建一个空对象，并附一个默认的编码
     */
    public UnitInfo getObject(UnitInfo object) {
        UnitInfo newObj = baseDao.getObjectById(object.getUnitCode());
        if (newObj == null) {
            newObj = object;
            newObj.setUnitCode(baseDao.getNextKey());
            newObj.setIsValid("T");
        }
        return newObj;
    }
    

  

    @Override
    public List<UserInfo> getUnitUsers(String unitCode) {
        return baseDao.listUnitUsers(unitCode);
    } 
    
    @Override
    public List<UserInfo> getRelationUsers(String unitCode) {
        return baseDao.listRelationUsers(unitCode);
    }

    @Override
    public String getUnitCode(String depno) {
        return baseDao.getUnitCode(depno);
    }

    @Override
    public String getNextKey() {
        return baseDao.getNextKey();
    }   
   
    @Override
    public UnitInfo getUnitByName(String name) {
        return baseDao.getUnitByName(name);
    }

    @Override
    @CacheEvict(value = "UnitInfo",allEntries = true)
    public void changeStatus(String unitCode, String isValid) {
        List<UnitInfo> allSubUnits = baseDao.listAllSubUnits(unitCode);
        for (UnitInfo subUnit : allSubUnits) {
            subUnit.setIsValid(isValid);
            baseDao.mergeObject(subUnit);
        }
    }
    
    @Override
    @CacheEvict(value="UnitInfo",allEntries = true)
    public void mergeObject(UnitInfo o) {
        baseDao.mergeObject(o);
    }
        
    @Override
    @CacheEvict(value = {"UnitInfo","UnitUsers","UserUnits","AllUserUnits"},allEntries = true)
    public void deleteUnitInfo(UnitInfo unitinfo){
    	String oldUnitPath = unitinfo.getUnitPath();
 		List<UnitInfo> subUnits = baseDao.listSubUnitsByUnitPaht(oldUnitPath);
		int noupl = oldUnitPath.length();
		for(UnitInfo ui : subUnits){
			if(unitinfo.getUnitCode().equals(ui.getParentUnit()))
				ui.setParentUnit("0");
			ui.setParentUnit(ui.getUnitPath().substring(noupl));
			baseDao.mergeObject(ui);
		}
    
        userUnitDao.deleteUserUnitByUnit(unitinfo.getUnitCode());        
        baseDao.deleteObjectById(unitinfo.getUnitCode());
    }
    
    @Override
    @CacheEvict(value = "UnitInfo",allEntries = true)
    public Serializable saveNewUnitInfo(UnitInfo unitinfo){
    	UnitInfo parentUnit = baseDao.getObjectById(unitinfo.getParentUnit());
    	if(parentUnit==null)
    		unitinfo.setUnitPath("/"+unitinfo.getUnitCode());
    	else
    		unitinfo.setUnitPath(parentUnit.getUnitPath()+"/"+unitinfo.getUnitCode());
        return baseDao.saveNewObject(unitinfo);
    }
    
    @Override
    @CacheEvict(value = "UnitInfo",allEntries = true)
    public void updateUnitInfo(UnitInfo unitinfo){
    	UnitInfo dbUnitInfo = baseDao.getObjectById(unitinfo.getUnitCode());
    	String oldParentUnit = dbUnitInfo.getParentUnit();
    	String oldUnitPath = dbUnitInfo.getUnitPath();
    	BeanUtils.copyProperties(unitinfo, dbUnitInfo, new String[]{"unitCode"});
    	if(!StringUtils.equals(oldParentUnit, unitinfo.getParentUnit())){
    		UnitInfo parentUnit = baseDao.getObjectById(unitinfo.getParentUnit());
    		if(parentUnit==null)
        		unitinfo.setUnitPath("/"+unitinfo.getUnitCode());
        	else
        		unitinfo.setUnitPath(parentUnit.getUnitPath()+"/"+unitinfo.getUnitCode());
    		List<UnitInfo> subUnits = baseDao.listSubUnitsByUnitPaht(oldUnitPath);
    		int noupl = oldUnitPath.length();
    		for(UnitInfo ui : subUnits){
    			ui.setParentUnit(unitinfo.getUnitPath()+ ui.getUnitPath().substring(noupl));
    			baseDao.mergeObject(ui);
    		}
    	}    	
        baseDao.mergeObject(dbUnitInfo);
    }

    @Override
	public List<UnitInfo> listAllSubObjects(String primaryUnit) {
		return baseDao.listAllSubUnits(primaryUnit);
    }
    
	@Override
	public List<UnitInfo> listAllSubObjectsAsSort(String primaryUnit) {
		List<UnitInfo> listObjects = baseDao.listAllSubUnits(primaryUnit);
        Iterator<UnitInfo> unitInfos = listObjects.iterator();        
        while (unitInfos.hasNext()) {
            UnitInfo unitInfo = unitInfos.next();
            if (StringBaseOpt.isNvl(unitInfo.getParentUnit()) || "0".equals(unitInfo.getParentUnit())) {
                continue;
            }
            for (UnitInfo opt : listObjects) {
                if (opt.getUnitCode().equals(unitInfo.getParentUnit())) {
                    opt.getSubUnits().add(unitInfo);

                    break;
                }
            }
        }
        List<UnitInfo> parentUnit = new ArrayList<>();
        // 获取顶级的父级菜单
        for (UnitInfo unitInfo : listObjects) {
            if (StringBaseOpt.isNvl(unitInfo.getParentUnit()) ||primaryUnit.equals(unitInfo.getUnitCode())) {
                parentUnit.add(unitInfo);
            }
        }
        return parentUnit;
	}
    
}

package com.centit.framework.staticsystem.service;

import java.util.List;

import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.po.OsInfo;

public interface StaticEnvironmentManager extends PlatformEnvironment {
	/**
	 * 刷新数据字典
	 * @return
	 */
	public boolean reloadDictionary();
	/**
	 * 刷新权限相关的元数据 
	 * @return
	 */
	public boolean reloadSecurityMetadata();
	/**
	 * 刷新集成环境相关信息
	 * 包括：业务系统、数据库信息
	 * @return
	 */
	public boolean reloadIPEnvironmen();
	
	
	public OsInfo getOsInfo(String osId);
	
	public DatabaseInfo getDatabaseInfo(String databaseCode);
	
	public List<OsInfo> listOsInfos();
	
	public List<DatabaseInfo> listDatabaseInfo();
}

package com.centit.framework.staticsystem.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.security.model.CentitSecurityMetadata;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.security.model.OptTreeNode;
import com.centit.framework.staticsystem.po.DataCatalog;
import com.centit.framework.staticsystem.po.DataDictionary;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.po.OptInfo;
import com.centit.framework.staticsystem.po.OptMethod;
import com.centit.framework.staticsystem.po.OsInfo;
import com.centit.framework.staticsystem.po.RoleInfo;
import com.centit.framework.staticsystem.po.RolePower;
import com.centit.framework.staticsystem.po.UnitInfo;
import com.centit.framework.staticsystem.po.UserInfo;
import com.centit.framework.staticsystem.po.UserRole;
import com.centit.framework.staticsystem.po.UserUnit;
import com.centit.framework.staticsystem.service.StaticEnvironmentManager;
import com.centit.support.file.FileIOOpt;

public class StaticSystemConfigImpl 
	implements StaticEnvironmentManager {
	private List<UserInfo> userinfos;
	private List<OptInfo> optinfos;
	private List<OptMethod> optmethods;
	private List<RoleInfo> roleinfos;
	private List<RolePower> rolepowers;
	private List<UserRole> userroles;
	private List<UserUnit> userunits;
	private List<DataCatalog> datacatalogs;
	private List<DataDictionary> datadictionaies;
	private List<UnitInfo> unitinfos;
	
	private Md5PasswordEncoder passwordEncoder;
	public void setPasswordEncoder(Md5PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	private DataCatalog getDataCatalog(String catalogCode){
		for(DataCatalog dc : datacatalogs){
			if(StringUtils.equals(dc.getCatalogCode(),catalogCode))
				return dc;
		}
		return null;
	}
	
	private List<OptInfo> getDirectOptInfo(){
		List<OptInfo> dirOptInfos = new ArrayList<OptInfo>();
		for(OptInfo oi : optinfos){
			if(StringUtils.equals(oi.getOptUrl(),"...")){
				OptInfo soi = new OptInfo();
				soi.copy(oi);
				soi.setOptId(oi.getOptId());
				dirOptInfos.add(soi);
			}
		}
		return dirOptInfos;
	}
	
	private OptInfo getOptInfo(String optId){
		for(OptInfo oi : optinfos){
			if(StringUtils.equals(oi.getOptId(),optId))
				return oi;
		}
		return null;
	}
	
	private OptMethod getOptMethod(String optCode){
		for(OptMethod om : optmethods){
			if(StringUtils.equals(om.getOptCode(),optCode))
				return om;
		}
		return null;
	}
	
	private RoleInfo getRoleInfo(String roleCode){
		for(RoleInfo ri : roleinfos){
			if(StringUtils.equals(ri.getRoleCode(),roleCode))
				return ri;
		}
		return null;
	}
	
	private UnitInfo getUnitInfo(String unitCode){
		for(UnitInfo ui : unitinfos){
			if(StringUtils.equals(ui.getUnitCode(),unitCode))
				return ui;
		}
		return null;
	}
	
	@Override
	public UserInfo getUserInfoByUserCode(String userCode){
		for(UserInfo ui : userinfos){
			if(StringUtils.equals(ui.getUserCode(),userCode))
				return ui;
		}
		return null;
	}
	
	@Override
	public UserInfo getUserInfoByLoginName(String loginName) {
		for(UserInfo ui : userinfos){
			if(StringUtils.equals(ui.getLoginName(),loginName))
				return ui;
		}
		return null;
	}

	@Override
	public boolean checkUserPassword(String userCode,String userPassword){
		UserInfo ui= getUserInfoByUserCode(userCode);
		if(ui==null)
			return false;
		return StringUtils.equals(ui.getUserPin(),  
				passwordEncoder.encodePassword(userPassword, userCode));
	}
	
	@Override
	public void changeUserPassword(String userCode, String userPassword) {
		UserInfo ui= getUserInfoByUserCode(userCode);
		if(ui==null)
			return;
		JSONObject json = null;
		String jsonFile = SysParametersUtils.getConfigHome()+"/static_system_user_pwd.json";
		
		try {			
	        String jsonstr = FileIOOpt.readStringFromFile(jsonFile,"UTF-8");	        
	        json = (JSONObject)JSON.parseObject(jsonstr);
		} catch (IOException e) {
		}
		
		if(json==null)
        	json = new JSONObject();
		try {
	        ui.setUserPin(passwordEncoder.encodePassword(userPassword, userCode));
	        json.put(userCode,ui.getUserPin());	        
	        FileIOOpt.writeStringToFile(json.toJSONString(),jsonFile);	        
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void init(){
		reloadDictionary();
		reloadSecurityMetadata();
		reloadIPEnvironmen();
	}

	@Override
	public String getSystemParameter(String paramCode) {
		return SysParametersUtils.getStringValue(paramCode);
	}

	@Override
	public String getUserSetting(String userCode, String paramCode) {
		UserInfo ud = getUserInfoByUserCode(userCode);
		if(ud==null)
			return null;
		return ud.getUserSettingValue(paramCode);
	}

	@Override
	public List<UserInfo> listAllUsers() {
		return userinfos;
	}

	@Override
	public List<UnitInfo> listAllUnits() {
		return unitinfos;
	}

	@Override
	public List<UserUnit> listAllUserUnits() {
		return userunits;
	}

	@Override
	public Map<String,RoleInfo> getRoleRepo() {
		Map<String,RoleInfo> roleRepo = new HashMap<String,RoleInfo>();
		for(RoleInfo role:roleinfos){
			roleRepo.put(role.getRoleCode(), role);
		}
		return roleRepo;
	}

	@Override
	public Map<String,OptMethod> getOptMethodRepo() {
		Map<String,OptMethod> methodRepo = new HashMap<String,OptMethod>();
		for(OptMethod method:optmethods){
			methodRepo.put(method.getOptCode(), method);
		}
		return methodRepo;
	}

	@Override
	public List<DataCatalog> listAllDataCatalogs() {
		return datacatalogs;
	}

	@Override
	public List<DataDictionary> listDataDictionaries(String catalogCode) {
		DataCatalog dc = getDataCatalog(catalogCode);
		if(dc!=null)
			return dc.getDataDictionaries();
		return null;
	}

	@Override
	public List<UserUnit> listUserUnits(String userCode) {
		UserInfo ui = getUserInfoByUserCode(userCode);
		if(ui!=null)
			return ui.getUserUnits();
		return null;
	}

	@Override
	public List<UserUnit> listUnitUsers(String unitCode) {
		UnitInfo ui = getUnitInfo(unitCode);
		if(ui!=null)
			return ui.getUnitUsers();
		return null;
	}

	@Override
	public Map<String,OptInfo> getOptInfoRepo() {
		Map<String,OptInfo> optRepo = new HashMap<String,OptInfo>();
		for(OptInfo opt:optinfos){
			optRepo.put(opt.getOptId(), opt);
		}
		return optRepo;
	}

	private static List<OptInfo> getMenuFuncs(List<OptInfo> preOpts, List<OptInfo> ls) {
        boolean isNeeds[] = new boolean[preOpts.size()];
        for (int i = 0; i < preOpts.size(); i++) {
            isNeeds[i] = false;
        }
        List<OptInfo> opts = new ArrayList<OptInfo>();

        for (OptInfo opm : ls) {
            opts.add(opm);
            for (int i = 0; i < preOpts.size(); i++) {
                if (opm.getPreOptId() != null && opm.getPreOptId().equals(preOpts.get(i).getOptId())) {
                    isNeeds[i] = true;
                    break;
                }
            }
        }

        List<OptInfo> needAdd = new ArrayList<OptInfo>();
        for (int i = 0; i < preOpts.size(); i++) {
            if (isNeeds[i]) {
                needAdd.add(preOpts.get(i));
            }
        }

        boolean isNeeds2[] = new boolean[preOpts.size()];
        while (true) {
            int nestedMenu = 0;
            for (int i = 0; i < preOpts.size(); i++)
                isNeeds2[i] = false;

            for (int i = 0; i < needAdd.size(); i++) {
                for (int j = 0; j < preOpts.size(); j++) {
                    if (!isNeeds[j] && needAdd.get(i).getPreOptId() != null
                            && needAdd.get(i).getPreOptId().equals(preOpts.get(j).getOptId())) {
                        isNeeds[j] = true;
                        isNeeds2[j] = true;
                        nestedMenu++;
                        break;
                    }
                }
            }
            if (nestedMenu == 0)
                break;

            needAdd.clear();
            for (int i = 0; i < preOpts.size(); i++) {
                if (isNeeds2[i]) {
                    needAdd.add(preOpts.get(i));
                }
            }

        }

        for (int i = 0; i < preOpts.size(); i++) {
            if (isNeeds[i]) {
                opts.add(preOpts.get(i));
            }
        }
        return opts;
    }
	
	 private static List<OptInfo> listObjectFormatAndFilterOptId(List<OptInfo> optInfos,String superOptId) {
	        // 获取当前菜单的子菜单
	        Iterator<OptInfo> menus = optInfos.iterator();
	        
	        OptInfo parentOpt = null;

	        List<OptInfo> parentMenu = new ArrayList<OptInfo>();
	        while (menus.hasNext()) {
	            
	            OptInfo optInfo = menus.next();
	            //去掉级联关系后需要手动维护这个属性
	           
	            if (superOptId!=null && superOptId.equals(optInfo.getOptId())) {
	            	parentOpt=optInfo;
	            }
	            boolean getParent = false;
	            for (OptInfo opt : optInfos) {
	                if (opt.getOptId().equals(optInfo.getPreOptId())) {
	                    opt.addChild(optInfo);
	                    getParent = true;
	                    break;
	                }
	            }
	            if(!getParent)
	            	parentMenu.add(optInfo);
	        }
	        
	        if (superOptId!=null && parentOpt!=null){
	        		return parentOpt.getChildren();
	        	//else
	        		//return null;
	        }else
	        	return parentMenu;
	    }
	 
	@Override
	public List<OptInfo> listUserMenuOptInfos(String userCode, boolean asAdmin) {
		UserInfo ud = getUserInfoByUserCode(userCode);
		if(ud==null)
			return null;
		Map<String, String> userOpts = ud.getUserOptList();
		List<OptInfo> userOptinfos = new ArrayList<OptInfo>();
		for(Map.Entry<String, String> uo : userOpts.entrySet()){
			OptInfo oi= this.getOptInfo(uo.getValue());
			if("Y".equals(oi.getIsInToolbar())){				
				OptInfo soi = new OptInfo();
				soi.copy(oi);
				soi.setOptId(oi.getOptId());				
				userOptinfos.add(soi);
			}
		}

		List<OptInfo> preOpts = getDirectOptInfo();
		
		List<OptInfo> allUserOpt = getMenuFuncs(preOpts,userOptinfos);
		
		return listObjectFormatAndFilterOptId(allUserOpt,null);
	}

	@Override
	public List<OptInfo> listUserMenuOptInfosUnderSuperOptId(
			String userCode,String superOptId , boolean asAdmin) {
		UserInfo ud = getUserInfoByUserCode(userCode);
		if(ud==null)
			return null;
		Map<String, String> userOpts = ud.getUserOptList();
		List<OptInfo> userOptinfos = new ArrayList<OptInfo>();
		for(Map.Entry<String, String> uo : userOpts.entrySet()){
			OptInfo oi= this.getOptInfo(uo.getValue());
			if("Y".equals(oi.getIsInToolbar())){				
				OptInfo soi = new OptInfo();
				soi.copy(oi);
				soi.setOptId(oi.getOptId());				
				userOptinfos.add(soi);
			}
		}

		List<OptInfo> preOpts = getDirectOptInfo();
		
		List<OptInfo> allUserOpt = getMenuFuncs(preOpts,userOptinfos);
		
		return listObjectFormatAndFilterOptId(allUserOpt,superOptId);
	}

	@Override
	public Map<String,UnitInfo> getUnitRepo() {
		Map<String,UnitInfo> unitRepo = new HashMap<String,UnitInfo>();
		for(UnitInfo unit:unitinfos){
			unitRepo.put(unit.getUnitCode(), unit);
		}
		return unitRepo;
	}

	@Override
	public Map<String,UserInfo> getUserRepo() {
		Map<String,UserInfo> userRepo = new HashMap<String,UserInfo>();
		for(UserInfo user:userinfos){
			userRepo.put(user.getUserCode(), user);
		}
		return userRepo;
	}

	@Override
	public Map<String,UserInfo> getLoginNameRepo() {
		Map<String,UserInfo> userRepo = new HashMap<String,UserInfo>();
		for(UserInfo user:userinfos){
			userRepo.put(user.getLoginName(), user);
		}
		return userRepo;
	}

	@Override
	public Map<String,UnitInfo> getDepNoRepo() {
		Map<String,UnitInfo> depnoRepo = new HashMap<String,UnitInfo>();
		for(UnitInfo unit:unitinfos){
			depnoRepo.put(unit.getDepNo(), unit);
		}
		return depnoRepo;
	}

	@Override
	public CentitUserDetails loadUserDetailsByLoginName(String loginName) {
		for(IUserInfo u : listAllUsers()){
			if(StringUtils.equals(u.getLoginName(),loginName))
				return (CentitUserDetails)u;
		}
		return null;
	}

	@Override
	public CentitUserDetails loadUserDetailsByUserCode(String userCode) {
		for(IUserInfo u :listAllUsers()){
			if(StringUtils.equals(u.getUserCode(),userCode))
				return (CentitUserDetails)u;
		}
		return null;
	}

	@Override
	public CentitUserDetails loadUserDetailsByRegEmail(String regEmail) {
		for(IUserInfo u :listAllUsers()){
			if(StringUtils.equals(u.getRegEmail(),regEmail))
				return (CentitUserDetails)u;
		}
		return null;
	}

	@Override
	public CentitUserDetails loadUserDetailsByRegCellPhone(String regCellPhone) {
		for(IUserInfo u :listAllUsers()){
			if(StringUtils.equals(u.getRegCellPhone(),regCellPhone))
				return (CentitUserDetails)u;
		}
		return null;
	}
	
	public void loadConfigFromJSONString(String jsonStr){
		
		JSONObject json = (JSONObject)JSON.parseObject(jsonStr);
		userinfos = (List<UserInfo>)JSON.parseArray(json.getString("userInfos"), UserInfo.class);
		optinfos = (List<OptInfo>)JSON.parseArray(json.getString("optInfos"), OptInfo.class);
		optmethods = (List<OptMethod>)JSON.parseArray(json.getString("optMethods"), OptMethod.class);
		roleinfos = (List<RoleInfo>)JSON.parseArray(json.getString("roleInfos"), RoleInfo.class);
		rolepowers = (List<RolePower>)JSON.parseArray(json.getString("rolePowers"), RolePower.class);
		userroles = (List<UserRole>)JSON.parseArray(json.getString("userRoles"), UserRole.class);
		unitinfos = (List<UnitInfo>)JSON.parseArray(json.getString("unitInfos"), UnitInfo.class);
		userunits = (List<UserUnit>)JSON.parseArray(json.getString("userUnits"), UserUnit.class);
		datacatalogs =(List<DataCatalog>)JSON.parseArray(json.getString("dataCatalogs"), DataCatalog.class); 
		datadictionaies = (List<DataDictionary>)JSON.parseArray(json.getString("dataDictionaries"), DataDictionary.class);

		for(DataDictionary dd:datadictionaies){
			DataCatalog dc = getDataCatalog(dd.getCatalogCode());
			if(dc!=null)
				dc.addDataDictionary(dd);
		}
		
		for(RoleInfo ri:roleinfos){
			for(RolePower rp:rolepowers){
				if(StringUtils.equals(rp.getRoleCode(),ri.getRoleCode())){
					ri.addRolePowers(rp);
					/*OptMethod om = getOptMethod(rp.getOptCode());
					if(om!=null)
						userOptList.put(om.getOptId()+"-"+om.getOptMethod(), "T");*/
				}
			}
		}
		
		for(UserInfo ui :userinfos){
			List<String> ris = new ArrayList<String>();
			Map<String, String> userOptList = new HashMap<String, String>();
			for(UserRole ur:userroles){
				if(StringUtils.equals(ur.getUserCode(), ui.getUserCode())){
					ris.add(ur.getRoleCode());
					RoleInfo ri = getRoleInfo(ur.getRoleCode());
					if(ri!=null){
						for(RolePower rp:ri.getRolePowers()){
							OptMethod om = getOptMethod(rp.getOptCode());
							if(om!=null)
								userOptList.put(om.getOptId()+"-"+om.getOptMethod(), om.getOptId());
						}
					}
				}
			}
			ui.setAuthoritiesByRoles(ris);
			ui.setUserOptList(userOptList);
		}
		
		for(UnitInfo ui: unitinfos){
			UnitInfo unit = getUnitInfo(ui.getParentUnit());
			if(unit!=null)
				unit.addSubUnit(ui);
		}
		for(UserUnit uu: userunits){
			UserInfo user = getUserInfoByUserCode(uu.getUserCode());
			if(user!=null)
				user.addUserUnit(uu);
			UnitInfo unit = getUnitInfo(uu.getUnitCode());
			if(unit!=null)
				unit.addUnitUser(uu);
		}
	}

	@Override
	public boolean reloadDictionary() {
		try {
			String jsonFile = SysParametersUtils.getConfigHome()+"/static_system_config.json";
			String jsonstr = FileIOOpt.readStringFromFile(jsonFile,"UTF-8");
	        loadConfigFromJSONString(jsonstr);
		} catch (IOException e) {
			userinfos = new ArrayList<UserInfo>();
			optinfos = new ArrayList<OptInfo>();
			optmethods = new ArrayList<OptMethod>();			
			roleinfos = new ArrayList<RoleInfo>();
			rolepowers = new ArrayList<RolePower>();
			userroles = new ArrayList<UserRole>();
			unitinfos = new ArrayList<UnitInfo>();
			userunits = new ArrayList<UserUnit>();
			datacatalogs = new ArrayList<DataCatalog>();
			datadictionaies = new ArrayList<DataDictionary>();
			e.printStackTrace();
		}
		//static_system_user_pwd.json
		try {
			String jsonFile = SysParametersUtils.getConfigHome()+"/static_system_user_pwd.json";
			String jsonstr = FileIOOpt.readStringFromFile(jsonFile,"UTF-8");
	        JSONObject json = (JSONObject)JSON.parseObject(jsonstr);
	        for(UserInfo u :userinfos){
				String spwd = json.getString(u.getUserCode());
				if(StringUtils.isNotBlank(spwd))
					u.setUserPin(spwd);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	
	@Override
	public boolean reloadSecurityMetadata() {
		CentitSecurityMetadata.optMethodRoleMap.clear();
        if(rolepowers==null || rolepowers.size()==0)
        	return false;
        for(RolePower rp: rolepowers ){
            List<ConfigAttribute/*roleCode*/> roles = CentitSecurityMetadata.optMethodRoleMap.get(rp.getOptCode());
            if(roles == null){
                roles = new ArrayList<ConfigAttribute/*roleCode*/>();
            }
            roles.add(new SecurityConfig(CentitSecurityMetadata.ROLE_PREFIX + StringUtils.trim(rp.getRoleCode())));
            CentitSecurityMetadata.optMethodRoleMap.put(rp.getOptCode(), roles);
        }
        //将操作和角色对应关系中的角色排序，便于权限判断中的比较
        CentitSecurityMetadata.sortOptMethodRoleMap();
        

        CentitSecurityMetadata.optTreeNode.setChildList(null);
        CentitSecurityMetadata.optTreeNode.setOptCode(null);
        for(OptMethod ou:optmethods){
        	OptInfo oi = getOptInfo(ou.getOptId());
        	if(oi!=null){
	        	String  optDefUrl = oi.getOptUrl()+ou.getOptUrl();
	            List<List<String>> sOpt = CentitSecurityMetadata.parseUrl(
	            		optDefUrl,ou.getOptReq());
	            
	            for(List<String> surls : sOpt){
	                OptTreeNode opt = CentitSecurityMetadata.optTreeNode;
	                for(String surl : surls)
	                    opt = opt.setChildPath(surl); 
	                opt.setOptCode(ou.getOptCode());
	            }
        	}
        } 		
		return true;
	}

	private List<OsInfo> osInfos;
	private List<DatabaseInfo> databaseInfos;
	
	@Override
	public boolean reloadIPEnvironmen() {
		try {
			String jsonFile = SysParametersUtils.getConfigHome()+"/ip_environmen.json";
			String jsonStr = FileIOOpt.readStringFromFile(jsonFile,"UTF-8");
			JSONObject json = (JSONObject)JSON.parseObject(jsonStr);
			osInfos = (List<OsInfo>)JSON.parseArray(json.getString("osInfos"), OsInfo.class);
			databaseInfos = (List<DatabaseInfo>)JSON.parseArray(json.getString("databaseInfos"),
					DatabaseInfo.class);
		} catch (IOException e) {
			osInfos = new ArrayList<OsInfo>();
			databaseInfos = new ArrayList<DatabaseInfo>();
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public OsInfo getOsInfo(String osId) {
		for(OsInfo oi : osInfos){
			if(StringUtils.equals(oi.getOsId(),osId))
				return oi;
		}
		return null;
	}

	@Override
	public DatabaseInfo getDatabaseInfo(String databaseCode) {
		for(DatabaseInfo di : databaseInfos){
			if(StringUtils.equals(di.getDatabaseCode(),databaseCode))
				return di;
		}
		return null;
	}

	@Override
	public List<OsInfo> listOsInfos() {
		return osInfos;
	}

	@Override
	public List<DatabaseInfo> listDatabaseInfo() {
		return databaseInfos;
	}
}

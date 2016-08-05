package com.centit.test;

import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.staticsystem.service.impl.IPClientSystemConfigImpl;

public class TestPlatSystem {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IPClientSystemConfigImpl clientSytem = new IPClientSystemConfigImpl();
		clientSytem.setPlatServerUrl("http://productsvr.centit.com:8880/centit-ip/service/platform");
		clientSytem.setTopOptId("METAFORM");
		
		CentitUserDetails userinfo = clientSytem.loadUserDetailsByLoginName("admin");
		System.out.println(userinfo.getUserName());
	}

}

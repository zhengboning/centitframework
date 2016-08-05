package com.centit.framework.security.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.centit.support.algorithm.DatetimeOpt;

public class AccessTokenMetadata{
	
	public static int tokenLifetime =  120;//minute
	private static String defaultChannel = "DEFAULT";//minute
	
	public static class TokenObject{
		private Date createTime;
		private CentitUserDetails tokenData;
		
		public TokenObject(){
			
		}
		
		public TokenObject(CentitUserDetails tokenData){
			this.createTime = DatetimeOpt.currentUtilDate();
			this.tokenData = tokenData;
		}

		public TokenObject(Date createTime,CentitUserDetails tokenData){
			this.createTime = createTime;
			this.tokenData = tokenData;
		}
		
		public boolean isAlive(){
			return DatetimeOpt.currentUtilDate().before(
					DatetimeOpt.addMinutes(this.createTime, tokenLifetime));					
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public CentitUserDetails getUserDetails() {
			return tokenData;
		}

		public void setUserDetails(CentitUserDetails tokenData) {
			this.tokenData = tokenData;
		}
	}
	
	public static Map<String,TokenObject> accessTokens = new HashMap<String,TokenObject>();
	public static Map<String,String> userTokens = new HashMap<String,String>();
	
	public static void addToken(String token,CentitUserDetails data){
		addToken(token,defaultChannel,data);
	}
	
	public static void addToken(String token,String channel,CentitUserDetails data){
		String userChannel = channel ==null ? data.getUserCode()+"-"+ defaultChannel
				: data.getUserCode()+"-"+channel;
		
		String oldToken = userTokens.get(userChannel);
		if(StringUtils.isNotBlank(oldToken))
			accessTokens.remove(oldToken);
		
		userTokens.put(userChannel, token);
		accessTokens.put(token, new TokenObject(data));
	}
	
	
	public static CentitUserDetails getTokenUserDetails(String token){
		TokenObject tokenData = accessTokens.get(token);
		
		if(tokenData == null)		
			return null;
		if(! tokenData.isAlive()){
			accessTokens.remove(token);
			return null;
		}
		return tokenData.getUserDetails();
	}
	
	public static void clearExpiredTokenData(String token){
		List<String> expiredToken = new ArrayList<String>();
		for(Map.Entry<String,TokenObject> ent : accessTokens.entrySet()){
			if(!ent.getValue().isAlive())
				expiredToken.add(ent.getKey());
		}
		
		for(String key:expiredToken)
			accessTokens.remove(key);
	}
	
}
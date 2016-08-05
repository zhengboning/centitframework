package com.centit.framework.security;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

//@Component("centitAccessDecisionManagerBean")
public class DaoAccessDecisionManager implements AccessDecisionManager {
    protected static final Log logger = LogFactory.getLog(DaoAccessDecisionManager.class);

    private boolean allResourceMustBeAudited = false;
    
	public void setAllResourceMustBeAudited(boolean allResourceMustBeAudited) {
		this.allResourceMustBeAudited = allResourceMustBeAudited;
	}
    
    // In this method, need to compare authentication with configAttributes.
    // 1, A object is a URL, a filter was find permission configuration by this
    // URL, and pass to here.
    // 2, Check authentication has attribute in permission configuration
    // (configAttributes)
    // 3, If not match corresponding authentication, throw a
    // AccessDeniedException.
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
    	
    	if (configAttributes==null || configAttributes.size()<1) {
            if(allResourceMustBeAudited){
            	FilterInvocation fi = (FilterInvocation) object;
                String requestUrl = fi.getRequestUrl();
                String sErrMsg = "资源:"+requestUrl+",必须要在角色中配置，以便于分配。";                
                fi.getRequest().setAttribute("CENTIT_SYSTEM_ERROR_MSG", sErrMsg);
                logger.error(sErrMsg);
                throw new AccessDeniedException(sErrMsg);
            }
            	
            return;
        }
    	//if(authentication!=null){
    	Collection<? extends GrantedAuthority> userRoles = authentication.getAuthorities();
    	if(userRoles!=null){    	
	    	Iterator<? extends GrantedAuthority> userRolesItr = userRoles.iterator();	    	
	        /*for(ConfigAttribute ca : configAttributes) {
	            if (ca == null) {
	                continue;
	            }
	            String needRole = ca.getAttribute();
	            for (GrantedAuthority ga : authentication.getAuthorities()) {
	                if (needRole.equals(ga.getAuthority())) { // ga is user's role.
	                    return;
	                }
	            }
	        }*/
	        //将两个集合排序 是可以提高效率的， 但考虑到这两个集合都比较小（一般应该不会大于3）所以优化的意义不大
	        Iterator<ConfigAttribute> needRolesItr = configAttributes.iterator();
	        String needRole = needRolesItr.next().getAttribute();
	        String userRole = userRolesItr.next().getAuthority();
	        while(true){
	            int n = needRole.compareTo(userRole);
	            if(n==0) return; // 匹配成功
	            
	            if(n<0){
	                if(!needRolesItr.hasNext())
	                    break;
	                needRole = needRolesItr.next().getAttribute();
	            }else{
	                if(!userRolesItr.hasNext())
	                    break;
	                userRole = userRolesItr.next().getAuthority();
	            }            
	        }       
    	}

        //没有权限，组织提示信息。
    	FilterInvocation fi = (FilterInvocation) object;
        String requestUrl = fi.getRequestUrl();
        
        StringBuilder needRoles = new StringBuilder();
        for(ConfigAttribute ca : configAttributes){
            needRoles.append(ca.getAttribute().substring(2)).append(" ");
        }
        String sErrMsg = "无权限访问资源:"+requestUrl+",需要角色 "+needRoles+"中的一个。";
        
        fi.getRequest().setAttribute("CENTIT_SYSTEM_ERROR_MSG", sErrMsg);
        logger.error(sErrMsg);
        throw new AccessDeniedException(sErrMsg);
    }

    public boolean supports(ConfigAttribute arg0) {
        return true;
    }

    public boolean supports(Class<?> arg0) {
        return true;
    }

}

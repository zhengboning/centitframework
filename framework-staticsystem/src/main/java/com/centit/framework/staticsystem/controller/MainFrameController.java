package com.centit.framework.staticsystem.controller;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.model.basedata.IOptInfo;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.staticsystem.po.OptInfo;
import com.centit.framework.staticsystem.service.StaticEnvironmentManager;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.image.CaptchaImageUtil;
import com.centit.support.json.JsonPropertyUtils;

@Controller
@RequestMapping("/mainframe")
public class MainFrameController extends BaseController {
	
    public static final String ENTRANCE_TYPE = "ENTRANCE_TYPE";
    public static final String NORMAL_LOGIN = "NORMAL";
    public static final String DEPLOY_LOGIN = "DEPLOY";
    public static final String LOGIN_AUTH_ERROR_MSG = "LOGIN_ERROR_MSG";
    
    @Resource
    protected CsrfTokenRepository csrfTokenRepository;
    
    @Resource
    protected StaticEnvironmentManager platformEnvironment;
    //实施人员入口开关
    @Value("${deploy.enabled}")
    private boolean deploy;
    //单点登录开关
    @Value("${cas.sso}")
    private boolean useCas;
    @Value("${local.home}")
    private String localHome ;
    @Value("${cas.home}")
    private String casHome ;// https://productsvr.centit.com:8443/cas
    @Value("${local.firstpage}")
    private String firstpage ;
   
    /**
     * 登录首页链接，具体登录完成后跳转路径由spring-security-dao.xml中配置
     */
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, HttpSession session) {
        //为了缩短普通管理员登录后首页URL，转发到 /service/
        return "sys/index";//"redirect:"+ firstpage;//
    }

    @RequestMapping(value = "/logincas")
    public String logincas(HttpServletRequest request, HttpSession session) {
        //为了缩短普通管理员登录后首页URL，转发到 /service/
        return "redirect:"+firstpage;//"sys/mainframe/index";
    }

    /**
     * 登录界面入口
     *
     * @return 登录界面
     */
    @RequestMapping("/login")
    public String login(HttpSession session) {
        //输入实施人员链接后未登录，后直接输入 普通用户登录链接
    	session.setAttribute(ENTRANCE_TYPE,NORMAL_LOGIN);
    	if(useCas)
	     {
			 return "redirect:/system/mainframe/logincas";
	     }else{	    	 
	    	 return "sys/login";
	     }       
    }
    
    @RequestMapping("/loginasadmin")
    public String loginAsAdmin(HttpSession session) {
        if (deploy) {
            //实施人员入口标记
            session.setAttribute(ENTRANCE_TYPE, DEPLOY_LOGIN);
        }
        if (useCas)
            return "redirect:/system/mainframe/logincas";
        else    
            return "sys/login";
    }
    

    @RequestMapping("/login/error")
    public String loginError(HttpSession session) {
        //在系统中设定Spring Security 相关的错误信息
        AuthenticationException authException = (AuthenticationException)
                session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        //设置错误信息
        if(authException!=null)
            session.setAttribute(LOGIN_AUTH_ERROR_MSG, authException.getMessage());
        //重新登录
        return login(session);
    }
    
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.setAttribute(ENTRANCE_TYPE,NORMAL_LOGIN);
        if(useCas)
        {
        	//return "sys/mainframe/index";
            session.invalidate();
            return "redirect:"+casHome+"/logout?service="+localHome+"/system/mainframe/index";
        }
        else
            return "redirect:/logout";//j_spring_security_logout
    }
    
    @RequestMapping(value ="/changepwd",method = RequestMethod.PUT)
    public void changepassword(String password, String newPassword,
            HttpServletRequest request,HttpServletResponse response) {
    	CentitUserDetails ud = WebOptUtils.getLoginUser(request);
    	if(ud==null){
    		JsonResultUtils.writeErrorMessageJson("用户没有登录，不能修改密码！", response);
    	}else{    		
	    	boolean bo=platformEnvironment.checkUserPassword(ud.getUserCode(), password);
	    	if(!bo){
	    		JsonResultUtils.writeErrorMessageJson("用户输入的密码错误，不能修改密码！", response);
	    	}else{
	    		platformEnvironment.changeUserPassword(ud.getUserCode(), newPassword);
	    		JsonResultUtils.writeSuccessJson(response);
	    	}
    	}
    }
    
    @RequestMapping(value ="/checkpwd",method = RequestMethod.POST)
    public void checkpassword(String password, 
            HttpServletRequest request,HttpServletResponse response) {
    	CentitUserDetails ud = WebOptUtils.getLoginUser(request);
    	if(ud==null){
    		JsonResultUtils.writeErrorMessageJson("用户没有登录，不能修改密码！", response);
    	}else{    		
	    	boolean bo=platformEnvironment.checkUserPassword(ud.getUserCode(), password);
	        JsonResultUtils.writeOriginalObject(bo, response);
    	}
    }
    
    @RequestMapping(value = "/csrf",method = RequestMethod.GET)
    public void getCsrfToken(HttpServletRequest request,HttpServletResponse response) {
    	if(csrfTokenRepository!=null){
    		CsrfToken token = csrfTokenRepository.generateToken(request);
  		
	    	response.setHeader("_csrf_parameter", token.getParameterName());
	    	response.setHeader("_csrf_header", token.getHeaderName());
	    	response.setHeader("_csrf", token.getToken());   
	    	
	    	csrfTokenRepository.saveToken( token,  request,
	    			 response);
	    	
	    	JsonResultUtils.writeSingleDataJson
				(token, response);
    	}else{
    		JsonResultUtils.writeErrorMessageJson(
    				"Bean csrfTokenRepository not found!", response);
    	}
    }

    @RequestMapping(value = "/captchaimage",method = RequestMethod.GET)
    public void captchaImage( HttpServletRequest request, HttpServletResponse response) {
  
        String checkcode = CaptchaImageUtil.getRandomString();
        request.getSession().setAttribute(
                CaptchaImageUtil.SESSIONCHECKCODE, checkcode);
        
        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/gif");
        //response.setContentType("application/json; charset=utf-8");
        try(ServletOutputStream os = response.getOutputStream()) {
            BufferedImage img = CaptchaImageUtil
                    .generateCaptchaImage(checkcode);
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(img, "gif", os);
            os.flush();
            os.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @RequestMapping(value = "/login/captchaimage",method = RequestMethod.GET)
    public void loginCaptchaImage( HttpServletRequest request, HttpServletResponse response) {  
    	captchaImage(  request,  response);
    }
    
    @RequestMapping(value = "/checkcaptcha/{checkcode}",method = RequestMethod.GET)
    public void checkCaptchaImage(@PathVariable String checkcode, HttpServletRequest request, HttpServletResponse response) {
  
        String sessionCode = StringBaseOpt.objectToString(
			        request.getSession().getAttribute(
			                CaptchaImageUtil.SESSIONCHECKCODE));
        
        JsonResultUtils.writeOriginalObject(StringUtils.equals(checkcode, sessionCode), response);
    }
    
    @RequestMapping("/currentuser")
    public void getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
    	CentitUserDetails ud = WebOptUtils.getLoginUser(request);
    	if(ud==null)
    		JsonResultUtils.writeSingleErrorDataJson(0,
    				"No user login on current session!",request.getSession().getId(), response);
    	else
    		JsonResultUtils.writeSingleDataJson(ud, response);
    }

    /**
     * 首页菜单
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/menu" , method = RequestMethod.GET)
    public void getMenu(HttpServletRequest request, HttpServletResponse response) {
        CentitUserDetails userDetails = super.getLoginUser(request);
        if(userDetails==null){
            JsonResultUtils.writeAjaxErrorMessage(302, "用户没有登录，请登录！", response);
            return;
        }
        Object obj = request.getSession().getAttribute(ENTRANCE_TYPE);  
        boolean asAdmin = obj!=null && DEPLOY_LOGIN.equals(obj.toString());
       
        List<? extends IOptInfo> menuFunsByUser = platformEnvironment.listUserMenuOptInfos(userDetails.getUserCode(),asAdmin );

        JsonResultUtils.writeSingleDataJson(menuFunsByUser, response, JsonPropertyUtils.getIncludePropPreFilter(OptInfo.class,
                "id", "pid", "text", "url", "icon", "attributes", "isInToolbar", "children"));

    }

    @RequestMapping(value = "/submenu" , method = RequestMethod.GET)
    public void getMenuUnderOptId(@RequestParam(value="optid", required=false)  String optid,
    		HttpServletRequest request,HttpServletResponse response) {
        CentitUserDetails userDetails = super.getLoginUser(request);
        if(userDetails==null){
            JsonResultUtils.writeAjaxErrorMessage(302, "用户没有登录，请登录！", response);
            return;
        }
        Object obj = request.getSession().getAttribute(ENTRANCE_TYPE);  
        boolean asAdmin = obj!=null && DEPLOY_LOGIN.equals(obj.toString());
       
        List<? extends IOptInfo> menuFunsByUser = platformEnvironment.listUserMenuOptInfosUnderSuperOptId(
        		userDetails.getUserCode(),optid ,asAdmin);

        JsonResultUtils.writeSingleDataJson(menuFunsByUser, response, JsonPropertyUtils.getIncludePropPreFilter(OptInfo.class,
                "id", "pid", "text", "url", "icon", "attributes", "isInToolbar", "children"));

    }
    
    @RequestMapping(value = "/getMenu/{userCode}" , method = RequestMethod.GET)
    public void getMemuByUsercode(@PathVariable String userCode,
            HttpServletRequest request, HttpServletResponse response) {
    	
        List<? extends IOptInfo> menuFunsByUser = platformEnvironment.listUserMenuOptInfos(userCode, false);

        JsonResultUtils.writeSingleDataJson(menuFunsByUser, response, JsonPropertyUtils.getIncludePropPreFilter(OptInfo.class,
                "id", "pid", "text", "url", "icon", "attributes", "isInToolbar", "children"));

    }
}

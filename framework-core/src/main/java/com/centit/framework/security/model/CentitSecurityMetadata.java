package com.centit.framework.security.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;

import com.centit.support.algorithm.StringBaseOpt;

public class CentitSecurityMetadata {
    public static final String ROLE_PREFIX = "R_";  
    public static OptTreeNode optTreeNode = new OptTreeNode();  
    public static Map<String/*optCode*/,List<ConfigAttribute/*roleCode*/>>
            optMethodRoleMap = new HashMap<String/*optCode*/,List<ConfigAttribute/*roleCode*/>>();
    
    public static List<String> parseUrl(String sUrl,HttpServletRequest request){
        List<String> swords = new ArrayList<String>();
        String sFunUrl ;
        int p = sUrl.indexOf('?');
        if(p<1)
            sFunUrl = sUrl;
        else
            sFunUrl = sUrl.substring(0,p);
        
        swords.add(request.getMethod().toUpperCase());
        for(String s:sFunUrl.split("/")){
            if(!StringBaseOpt.isNvl(s) /*&& !"*".equals(s)*/){
                swords.add(s.toUpperCase());
            }
        }
        return swords;
    }
    /**
     * C D R  U  :  POST DELETE GET PUT
     * @param sOptUrl
     * @param sDefUrl
     * @param sMethod
     * @return
     */
    public static List<List<String>> parseUrl(String sOptDefUrl,String sMethod){
        
        List<List<String>> swords = new ArrayList<List<String>>();
        String sUrls[] = (sOptDefUrl).split("/");
        if(sMethod.indexOf('C')>=0){
            List<String> sopts = new ArrayList<String>();
            sopts.add("POST");
            for(String s:sUrls){
                if(!StringBaseOpt.isNvl(s)/* && !"*".equals(s)*/)
                    sopts.add(s.toUpperCase());
            }
            swords.add(sopts);
        }
        if(sMethod.indexOf('D')>=0){
            List<String> sopts = new ArrayList<String>();
            sopts.add("DELETE");
            for(String s:sUrls){
                if(!StringBaseOpt.isNvl(s)/* && !"*".equals(s)*/)
                    sopts.add(s.toUpperCase());
            }
            swords.add(sopts);
        }
        if(sMethod.indexOf('R')>=0){
            List<String> sopts = new ArrayList<String>();
            sopts.add("GET");
            for(String s:sUrls){
                if(!StringBaseOpt.isNvl(s) /*&& !"*".equals(s)*/)
                    sopts.add(s.toUpperCase());
            }
            swords.add(sopts);
        }
        if(sMethod.indexOf('U')>=0){
            List<String> sopts = new ArrayList<String>();
            sopts.add("PUT");
            for(String s:sUrls){
                if(!StringBaseOpt.isNvl(s) /*&& !"*".equals(s)*/)
                    sopts.add(s.toUpperCase());
            }
            swords.add(sopts);
        }
       
        return swords;
    }    
    
    //public abstract void loadRoleSecurityMetadata();    
    public static String matchUrlToOpt(String sUrl,HttpServletRequest request){
       
        List<String> urls = parseUrl(sUrl,request);
        OptTreeNode curOpt = optTreeNode;
        for(String s: urls){
            if(curOpt.childList == null)
                return curOpt.optCode;
            OptTreeNode subOpt = curOpt.childList.get(s);
            if(subOpt == null){
                subOpt = curOpt.childList.get("*");
                if(subOpt == null)
                    return  curOpt.optCode;
            }
            curOpt = subOpt;
        }
        if(curOpt!=null)
            return curOpt.optCode;
        
        return null;
    }
    
    public static Collection<ConfigAttribute> matchUrlToRole(String sUrl,HttpServletRequest request){
        
       String sOptCode = matchUrlToOpt(sUrl,request);
       if(sOptCode==null)
           return null;
       return optMethodRoleMap.get(sOptCode);
    }
    
    public static void printOptdefRoleMap(){
        for(Map.Entry<String ,List<ConfigAttribute >> roleMap : optMethodRoleMap.entrySet()){
            if(roleMap.getValue().size()>1){
                System.out.print(roleMap.getKey());
                System.out.print(" : ");
                for(ConfigAttribute c : roleMap.getValue()){
                    System.out.print(c.getAttribute());
                    System.out.print("  ");
                }
                System.out.println();
            }
        }
        System.out.println("--------------------------------");
    }
    
    /**
     * 将操作和角色对应关系中的角色排序，便于权限判断中的比较
     */
    public static void sortOptMethodRoleMap(){
        //测试比较排序效果
        for(Map.Entry<String ,List<ConfigAttribute >> roleMap : optMethodRoleMap.entrySet()){
          //排序便于后面比较
            Collections.sort(roleMap.getValue(),
                    new Comparator<ConfigAttribute>(){
                        public int compare(ConfigAttribute o1, ConfigAttribute o2) {
                            return o1.getAttribute().compareTo(o2.getAttribute());
                        }
                      });            
        }
        //测试比较排序效果
    }
}

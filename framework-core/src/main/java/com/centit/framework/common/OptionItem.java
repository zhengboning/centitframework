package com.centit.framework.common;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.centit.support.algorithm.StringBaseOpt;

public class OptionItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Object value;
	private Object group;
	
	public OptionItem(){
		
	}
	public OptionItem(String name){
		this.name = name;
		this.value = name;
	}
	
	public OptionItem(String name,Object value){
		this.name = name;
		this.value = value;
	}
	
	public OptionItem(String name,Object value,Object group){
		this.name = name;
		this.value = value;
		this.group = group;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Object getGroup() {
		return group;
	}
	
	public void setGroup(Object group) {
		this.group = group;
	}

	@Override  
    public int hashCode() {
		if(name==null)
			return 0;
        return name.hashCode();  
    }  
      
    @Override  
    public boolean equals(Object obj) {
    	if(this==obj || value==obj)
    		return true;
    	if(obj==null)
    		return false;
    	
    	if(obj instanceof String){
    		return StringUtils.equals(StringBaseOpt.objectToString(value), 
    				(String)obj);
    	}
    	
    	if(obj instanceof OptionItem){
    		return StringUtils.equals(StringBaseOpt.objectToString(value), 
    				StringBaseOpt.objectToString(((OptionItem)obj).getValue()));
    	}
    	
    	return StringUtils.equals(StringBaseOpt.objectToString(value), 
				StringBaseOpt.objectToString(obj));
    }
}

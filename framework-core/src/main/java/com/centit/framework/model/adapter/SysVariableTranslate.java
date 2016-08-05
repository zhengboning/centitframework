package com.centit.framework.model.adapter;

import java.util.Set;

import com.centit.support.compiler.VariableTranslate;

public interface SysVariableTranslate extends VariableTranslate {


    /**
     * 返回权限表达式中的自定义变量对应的用户组
     *
     * @param varName 自定义变量
     * @return
     */
    public Set<String> getUsersVariable(String varName);

    /**
     * 返回机构表达式中的自定义变量对应的机构组
     *
     * @param varName 自定义变量
     * @return
     */
    public Set<String> getUnitsVariable(String varName);

}

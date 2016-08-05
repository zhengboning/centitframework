package com.centit.framework.components;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.framework.model.adapter.SysVariableTranslate;
import com.centit.framework.model.basedata.IUnitInfo;
import com.centit.support.algorithm.StringRegularOpt;
import com.centit.support.compiler.Formula;

/**
 * 结构引擎，相关说明文档参见 http://192.168.128.8/wiki/pages/viewpage.action?pageId=12746758
 *
 * @author codefan
 */
public class SysUnitFilterEngine implements Serializable {
    private SysUnitFilterEngine(){
        
    }
    
    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(SysUnitFilterEngine.class);

    /**
     * D(null+1) =>D1,D2
     */
    public static Set<String> nullSubUnits(int nTiers) {
        Set<String> units = new HashSet<String>();
        if (nTiers < 1)
            return units;
        // 找到所有根机构
        for (Map.Entry<String, IUnitInfo> unitEnt : CodeRepositoryUtil.getUnitRepo().entrySet()) {
            String puc = unitEnt.getValue().getParentUnit();
            if ((puc == null || "0".equals(puc) || "".equals(puc)) && "T".equals(unitEnt.getValue().getIsValid()))
                units.add(unitEnt.getKey());
        }

        return subUnits(units, nTiers - 1);
    }

    /**
     * D(null-1) =>D1111，D1112,D112,D12,D2
     */
    public static Set<String> nullParentUnits(int nTiers) {
        Set<String> units = new HashSet<String>();
        if (nTiers < 1)
            return units;
        // 找到所有的叶子机构
        for (Map.Entry<String, IUnitInfo> unitEnt : CodeRepositoryUtil.getUnitRepo().entrySet()) {
            List<? extends IUnitInfo> subUS = unitEnt.getValue().getSubUnits();
            if ((subUS == null || subUS.size() == 0) && "T".equals(unitEnt.getValue().getIsValid()))
                units.add(unitEnt.getKey());
        }

        return parentUnits(units, nTiers - 1);
    }

    /**
     * 查找有共同最上层机构的所有子机构
     *
     * @param units
     * @return
     */
    public static Set<String> allSeriesUnits(Set<String> units) {
        if (units == null || units.size() == 0)
            return units;

        Set<String> retUnits = new HashSet<String>();
        for (String unitCode : units) {
            String tu = topUnit(unitCode);
            if (tu != null)
                retUnits.add(tu);
        }
        retUnits.addAll(allSubUnits(retUnits));

        return allSubUnits(retUnits);
    }

    /**
     * 查找所有的下层机构
     *
     * @param units
     * @return
     */
    public static Set<String> allSubUnits(Set<String> units) {
        if (units == null || units.size() == 0)
            return units;
        int preSize = 0;
        Set<String> retUnits = new HashSet<String>();
        Set<String> midUnits = units;
        while (midUnits != null && midUnits.size() != 0) {
            retUnits.addAll(midUnits);
            // 排除机构层级设置中的循环问题
            if (preSize == retUnits.size())
                break;
            preSize = retUnits.size();
            midUnits = subUnits(midUnits, 1);
        }

        return retUnits;
    }

    /**
     * D(U--)
     *
     * @param units
     * @param nTiers
     * @return
     */
    public static Set<String> allParentUnits(Set<String> units) {
        if (units == null || units.size() == 0)
            return units;
        Set<String> midUnits = units;
        Set<String> parUnits = new HashSet<String>();
        while(true) {
            Set<String> retUnits = new HashSet<String>();
            
            for (String suc : midUnits) {
                IUnitInfo u = CodeRepositoryUtil.getUnitInfoByCode(suc);
                String puc = u.getParentUnit();
                if ((puc != null) && (!"0".equals(puc)) && (!"".equals(puc)))
                    retUnits.add(puc);
            }
            if(retUnits.size()<1)
                break;
            parUnits.addAll(midUnits);
            midUnits = retUnits;            
        }
        parUnits.addAll(midUnits);
        return parUnits;
    }
   
  
    /**
     * D(U+5)
     *
     * @param units
     * @param nTiers
     * @return
     */
    public static Set<String> subUnits(Set<String> units, int nTiers) {
        if (nTiers < 1 || units == null || units.size() == 0)
            return units;

        Set<String> midUnits = units;
        for (int i = 0; i < nTiers; i++) {
            Set<String> retUnits = new HashSet<String>();
            for (String suc : midUnits) {
                IUnitInfo u = CodeRepositoryUtil.getUnitInfoByCode(suc);
                for(IUnitInfo ui:u.getSubUnits())
                    retUnits.add(ui.getUnitCode());
            }
            midUnits = retUnits;
        }
        return midUnits;
    }

    /**
     * D(U-5)
     *
     * @param units
     * @param nTiers
     * @return
     */
    public static Set<String> parentUnits(Set<String> units, int nTiers) {
        if (nTiers < 1 || units == null || units.size() == 0)
            return units;
        Set<String> midUnits = units;
        for (int i = 0; i < nTiers; i++) {
            Set<String> retUnits = new HashSet<String>();
            for (String suc : midUnits) {
                IUnitInfo u = CodeRepositoryUtil.getUnitInfoByCode(suc);
                String puc = u.getParentUnit();
                if ((puc != null) && (!"0".equals(puc)) && (!"".equals(puc)))
                    retUnits.add(puc);
            }
            midUnits = retUnits;
        }
        return midUnits;
    }
    
  

    /**
     * D(U*1)
     *
     * @param unitCode
     * @return
     */
    public static String topUnit(String unitCode) {
        String topUnit = null;
        String retUnitCode = unitCode;
        while (true) {
            IUnitInfo u = CodeRepositoryUtil.getUnitInfoByCode(retUnitCode);
            if (u == null)
                return topUnit;
            topUnit = retUnitCode; // 记录下有小的机构代码
            String puc = u.getParentUnit();
            if (puc == null || "0".equals(puc) || "".equals(puc))
                return retUnitCode;
            retUnitCode = puc;
        }
    }

    /**
     * D(U*5)
     *
     * @param units
     * @param nTiers
     * @return
     */
    public static Set<String> topUnits(Set<String> units, int nTiers) {
        if (nTiers < 1)
            return units;
        Set<String> retUnits = new HashSet<String>();
        for (String unitCode : units) {
            String tu = topUnit(unitCode);
            if (tu != null)
                retUnits.add(tu);
        }
        return subUnits(retUnits, nTiers);
    }

    public static Set<String> seriesUnits(Set<String> units, int nTiers) {
        
        if ( units == null || units.size() == 0)
            return units;

        Set<String> retUnits = new HashSet<String>();
        for (String unitCode : units) {
            String tu = topUnit(unitCode);
            if (tu != null)
                retUnits.add(tu);
        }
        if(nTiers < 1)
            return retUnits;
        
        Set<String> midUnits = retUnits;
        Set<String> serUnits = new HashSet<String>();
        for (int i = 0; i < nTiers; i++) {
            retUnits = new HashSet<String>();
            for (String suc : midUnits) {
                IUnitInfo u = CodeRepositoryUtil.getUnitInfoByCode(suc);
                for(IUnitInfo ui:u.getSubUnits())
                    retUnits.add(ui.getUnitCode());                
            }
            serUnits.addAll(midUnits);
            midUnits = retUnits;
        }
        serUnits.addAll(midUnits);
        return serUnits;
    }
    
    /**
     * D(null) =>null D(all) => D1,D2,D11,D12,D111,D112,D1111,D1112 D("D12")
     * =>D12 D(null+1) =>D1,D2 D(all+1) => D11,D12,D111,D112,D1111,D1112 D(A) =>
     * D111 D(U+1) => D1111,D1112 D(U-1) => D11 D(P-1+1) => D111,D112 D(W*1) =>
     * D1
     *
     * @param simpleExp
     * @return
     */
    private static Set<String> calcSimpleExp(ExpCalcContext ecc) {
        // CodeRepositoryUtil.getUnitInfoByCode("");
        Set<String> units = new HashSet<String>();
        String w = ecc.getAWord();
        if (ExpCalcContext.isLabel(w)) { // 变量
            if ("all".equalsIgnoreCase(w)) {
                //for (String unitCode : CodeRepositoryUtil.getUnitRepo().keySet())
                    units.addAll(CodeRepositoryUtil.getUnitRepo().keySet());
            } else if ("empty".equalsIgnoreCase(w)) {
                w = ecc.getAWord();
                if ("-".equals(w)) {
                    w = ecc.getAWord();
                    if (!StringRegularOpt.isNumber(w)) {
                        ecc.setLastErrMsg(w + " is unexpected, expect number; calcSimpleUnit null- .");
                        return null;
                    }
                    units = nullParentUnits(Integer.valueOf(w));
                    
                } else if ("+".equals(w)) {
                    w = ecc.getAWord();
                    if (!StringRegularOpt.isNumber(w)) {
                        ecc.setLastErrMsg(w + " is unexpected, expect number; calcSimpleUnit null+ . ");
                        return null;
                    }
                    units = nullSubUnits(Integer.valueOf(w));
                } else
                    ecc.setPreword(w);
            } else {
                Set<String> us = ecc.getUnitCode(w);
                if (us != null)
                    units.addAll(us);
                else{
                    if( CodeRepositoryUtil.getUnitInfoByCode(w) !=null){
                        units.add(w);
                    }
                }
            }
        } else if (!Formula.isKeyWord(w)) { // 常量
            String unitCode = StringRegularOpt.trimString(w);
            if (CodeRepositoryUtil.getUnitInfoByCode(unitCode) != null)
                units.add(unitCode);
        } else { // 语法错误
            ecc.setLastErrMsg(w + " is unexpected, expect label or string [unitcode]; calcSimpleUnit label . ");
            return null;
        }
        // --------------------------------------------------------------//
        w = ecc.getAWord();
        if ("-".equals(w)) {
            ecc.setCanAcceptOpt(true);
            w = ecc.getAWord();           
            if (!StringRegularOpt.isNumber(w)) {
                ecc.setLastErrMsg(w + " is unexpected, expect number ; calcSimpleUnit - . ");
                return null;
            }            
            units = parentUnits(units, Integer.valueOf(w));
            w = ecc.getAWord();
            if("+".equals(w)){
                w = ecc.getAWord();           
                if (!StringRegularOpt.isNumber(w)) {
                    ecc.setLastErrMsg(w + " is unexpected, expect number ; calcSimpleUnit - A + B . ");
                    return null;
                }            
                units = subUnits(units, Integer.valueOf(w));
            }else
                ecc.setPreword(w);

        } else if ("+".equals(w)) {
            w = ecc.getAWord();
            if (!StringRegularOpt.isNumber(w)) {
                ecc.setLastErrMsg(w + " is unexpected, expect number; calcSimpleUnit + . ");
                return null;
            }
            units = subUnits(units, Integer.valueOf(w));

        } else if ("*".equals(w)) {
            ecc.setCanAcceptOpt(true);
            w = ecc.getAWord();
            if ("+".equals(w)) {// 所有同一系列 同一层节点               
                w = ecc.getAWord();
                if (!StringRegularOpt.isNumber(w)) {
                    ecc.setLastErrMsg(w + " is unexpected, expect number ; calcSimpleUnit *+.");
                    return null;
                }
                units = topUnits(units, Integer.valueOf(w));
                
            } else if ("-".equals(w)) {// 所有节点的上层节点中， 指定层次的节点
                
                w = ecc.getAWord();
                if (!StringRegularOpt.isNumber(w)) {
                    ecc.setLastErrMsg(w + " is unexpected, expect number ; calcSimpleUnit *-.");
                    return null;
                }
                Set<String> parUnits = allParentUnits(units);
                units = topUnits(units, Integer.valueOf(w));                
                units.retainAll(parUnits);
                
            } else{
                if (!StringRegularOpt.isNumber(w)) {
                    ecc.setLastErrMsg(w + " is unexpected, expect number ; calcSimpleUnit *.");
                    return null;
                }else
                    //所有同一系列最上面几层节点               
                    units = seriesUnits(units, Integer.valueOf(w));    
            }
        } else if ("++".equals(w)) {// 所有的下层节点

            units = allSubUnits(units);
        } else if ("--".equals(w)) {// 所有的上层节点
            units = allParentUnits(units);
        } else if ("**".equals(w)) {// 所有同一系列节点
            units = allSeriesUnits(units);
        } else
            ecc.setPreword(w);
        // w = ecc.getAWord();
        // if(")".equals(w)){ //语句结束
        return units;
    }

    /**
     * S(unitExp[,unitExp]* )
     *
     * @param ecc
     * @return
     */
    private static Set<String> calcSingleExp(ExpCalcContext ecc) {
        String w = ecc.getAWord();
        if (!"(".equals(w)) { // 语法错误
            ecc.setLastErrMsg(w + " is unexpected, expect '(' ; calcSingleUnit begin .");
            return null;
        }
        Set<String> units = calcUnitsExp(ecc);
        // if(units==null)//语法错误
        // return null;

        while (units == null || units.size() == 0) {
            w = ecc.getAWord();
            if (")".equals(w))
                return units;
            if (",".equals(w))
                units = calcUnitsExp(ecc);
            else {
                ecc.setLastErrMsg(w + " is unexpected, expect ',' or ')' ; calcSingleUnit end .");
                return null;
            }
        }
        ecc.seekToRightBracket();

        return units;
    }

    /**
     * (unitExp) | S(singleExp) | SimpleExp
     *
     * @param ecc
     * @return
     */
    private static Set<String> calcItemExp(ExpCalcContext ecc) {
        String w = ecc.getAWord();
        if (w == null || "".equals(w)) { // 语法错误
            ecc.setLastErrMsg("End of file is unexpected; calcItemUnit begin ");
            return null;
        }
        if ("(".equals(w)) {
            Set<String> units = calcUnitsExp(ecc);
            w = ecc.getAWord();
            if (")".equals(w))
                return units;
            else { // 语法错误
                ecc.setLastErrMsg(w + " is unexpected, expect ')'; calcItemUnit (unitExp) end ");
                return null;
            }
            // }else if("D".equals(w)){
            // return calcSimpleExp(ecc);
        } else if ("S".equalsIgnoreCase(w)) {
            return calcSingleExp(ecc);
        } else {
            ecc.setPreword(w);
            return calcSimpleExp(ecc);
        }
    }

    /**
     * itemExp ([||itemExp][&& itemExp][! itemExp])*
     *
     * @param ecc
     * @return
     */
    public static Set<String> calcUnitsExp(ExpCalcContext ecc) {
        Set<String> units = calcItemExp(ecc);
        if (units == null)
            return null;
        while (true) {
            String w = ecc.getAWord();
            if (w == null || "".equals(w))
                return units;

            if (",".equals(w) || ")".equals(w)) {
                ecc.setPreword(w);
                return units;
            }

            if ("|".equals(w) || "||".equals(w)) { // 并
                Set<String> units2 = calcItemExp(ecc);
                if (units2 == null)
                    return null;
                units.addAll(units2);
            } else if ("&".equals(w) || "&&".equals(w)) { // 交
                Set<String> units2 = calcItemExp(ecc);
                if (units2 == null)
                    return null;
                units.retainAll(units2);
            } else if ("!".equals(w)) {// 差
                Set<String> units2 = calcItemExp(ecc);
                if (units2 == null)
                    return null;
                units.removeAll(units2);
            } else {// 语法错误
                ecc.setLastErrMsg(w + " is unexpected, expect '||','&&','!',',' or ')'; calcUnitsExp end  ");
                return null;
            }
        }
    }

    public static Set<String> calcUnitsByExp(String unitExp, String lastSameNodeUnit, String userUnit,
                                             String previousNodeUnit, String flowUnit, SysVariableTranslate varTrans) {
        if (unitExp == null)
            return null;
        ExpCalcContext ecc = new ExpCalcContext();
        ecc.setFormula(unitExp);
        ecc.setVarTrans(varTrans);
        ecc.addUnitParam("L", lastSameNodeUnit);
        ecc.addUnitParam("U", userUnit);
        ecc.addUnitParam("P", previousNodeUnit);
        ecc.addUnitParam("F", flowUnit);
        Set<String> untis = calcUnitsExp(ecc);

        if (ecc.hasError())
            log.error(ecc.getLastErrMsg());

        return untis;
    }

    public static Set<String> calcUnitsByExp(String unitExp, String userUnit) {        
        return calcUnitsByExp(unitExp, null, userUnit,
                null, null, null);
    }
    
    public static Set<String> calcUnitsByExp(String unitExp) {
        return calcUnitsByExp(unitExp, null, null,
                null, null, null);
    }
    
    public static Set<String> calcUnitsExp(String unitExp, String lastSameNodeUnit, String userUnit,
                                           String previousNodeUnit, String flowUnit, SysVariableTranslate varTrans) {
        return calcUnitsByExp(unitExp, lastSameNodeUnit, userUnit, previousNodeUnit, flowUnit, varTrans);
    }

    public static String calcSingleUnitByExp(String unitExp, String lastSameNodeUnit, String userUnit,
                                             String previousNodeUnit, String flowUnit, SysVariableTranslate varTrans) {
        Set<String> untis = calcUnitsByExp(unitExp, lastSameNodeUnit, userUnit, previousNodeUnit, flowUnit, varTrans);
        if (untis == null || untis.size() == 0)
            return null;
        return untis.iterator().next();
    }

    public static String calcSingleUnitExp(String unitExp, String lastSameNodeUnit, String userUnit,
                                           String previousNodeUnit, String flowUnit, SysVariableTranslate varTrans) {

        return calcSingleUnitByExp(unitExp, lastSameNodeUnit, userUnit, previousNodeUnit, flowUnit, varTrans);
    }

    public static String validateUnitsExp(String unitExp) {
        ExpCalcContext ecc = new ExpCalcContext();
        ecc.setFormula(unitExp);
        calcUnitsExp(ecc);
        if (ecc.hasError())
            return ecc.getLastErrMsg();
        return "T";
    }

	/*
     * public static void main(String arg[]){ SysUnitFilterEngine sfue = new
	 * SysUnitFilterEngine();
	 * System.out.println("check 's(P U+1 || P*1)' error: " +
	 * sfue.checkUnitsExp("s(P U+1 || P*1)")); }
	 */
}

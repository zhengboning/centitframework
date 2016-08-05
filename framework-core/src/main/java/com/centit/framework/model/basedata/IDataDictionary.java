package com.centit.framework.model.basedata;

/**
 * @author codefan
 */
//数据字典表
public interface IDataDictionary{

	/**
	 * 类别代码
	 * @return
	 */
    public String getCatalogCode();
    /**
     * 数据字典代码
     * @return
     */
    public String getDataCode();

    /**
     * 数据字典扩展代码，在树形数据字典中位父类代码
     * @return
     */
    public String getExtraCode();
    /**
     * 数据字典扩展代码2, 业务自行解释
     * @return
     */
    public String getExtraCode2();
    
    /**
     * 字典内容，如果支持国际化这个将是一个json key为语言类别代码
     * @return
     */
    public String getDataValue();

    /**
     * 获得某个自定语言的描述
     * @param lang 语言类别代码
     * @return
     */
    public String getLocalDataValue(String lang);
    /**
     * 数据字典标记，用于分类只有一个字节，比如 D 为已删除 ，其他应用业务执行解释
     * @return
     */
    public String getDataTag();
    
  
    /**
     * 字典条目描述
     * @return
     */
    public String getDataDesc();
    
    /**
     * 字典排序号
     * @return
     */
    public Integer getDataOrder();

  
}
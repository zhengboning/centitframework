package com.centit.framework.hibernate.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.centit.framework.core.dao.PageDesc;

public interface BaseDao<T extends Serializable, PK extends Serializable> {

    public String getClassTName();

    public String getClassTShortName();

    public void setFilterField(Map<String, String> ffd);

    /**
     * 表单中查询Form中输入框名称和的过滤条件对应表。输入框的名字为s:名称 参见
     * http://fileserver.centit.com/wiki/pages/viewpage.action?pageId=7110664
     *
     * @return
     */
    public Map<String, String> getFilterField();

    /**
     * 查找表中的所有 isValid = 'T' 的记录
     *
     * @return
     */
    public List<T> listValidObjects();

    /**
     * 查找表中的所有记录， 包括禁用的 isValid = 'F' 的记录
     *
     * @return
     */
    public List<T> listObjects();

    /**
     * getObjects 为一组查找 T 数组的函数 根据表单中的过滤条件查找符合条件的对象集合
     *
     * @param filterDesc 过滤条件
     * @return
     */
    public List<T> listObjects(Map<String, Object> filterDesc);

    /**
     * 根据HQL语句获得所有的对象
     *
     * @param shql
     * @return
     */
    public List<T> listObjects(String shql);

    /**
     * 根据HQL语句获得所有的对象，并进行分页
     *
     * @param shql
     * @param startPos 其实条目
     * @param maxSize  最大返回条目数
     * @return
     */
    public List<T> listObjects(String shql, int startPos, int maxSize);

    /**
     * 根据带参数的HQL语句获得所有的对象
     *
     * @param shql
     * @param values 参数数组
     * @return
     */
    public List<T> listObjects(String shql, Object[] values);

    /**
     * 根据带一个参数的HQL语句获得所有的对象
     *
     * @param shql
     * @param value 参数
     * @return
     */
    public List<T> listObjects(String shql, Object value);

    /**
     * 根据HQL 和表单中过滤条件 组合查找符合条件的对象集合
     *
     * @param shql
     * @param filterDesc 过滤条件
     * @return
     */
    public List<T> listObjects(String shql, Map<String, Object> filterDesc);

    /**
     * 根据HQL 和表单中过滤条件 组合查找符合条件的对象集合，并进行分页
     *
     * @param shql
     * @param filterDesc 过滤条件
     * @param startPos   其实条目
     * @param maxSize    最大返回条目数
     * @return
     */
    public List<T> listObjects(String shql, Map<String, Object> filterDesc,
                               int startPos, int maxSize);

    /**
     * 根据对象的主键 获得数据库中对应的对象信息
     *
     * @param id
     * @return
     */
    public T getObjectById(PK id);

    public T getObjectByProperty(final String propertyName, final Object propertyValue);

    public T getObjectByProperties(Map<String, Object> properties);

    /**
     * codefan added at 2014-5-13
     * 这个saveObject 与下面的 updateObject 和父类的 saveOrUpdate 没有任何区别，
     * 不过还是应该有一个 单独的saveObject 方法的，这个方法应该 返回 自动生成主键的主键，
     * 就是针对hbm.xml中设定主键生成方案的有用。
     * 这样这个返回值 应该是一个 Serializable 类型。我在父类中修改
     */
    public Serializable saveNewObject(T o);

    /**
     * 保存泛型参数对象
     *
     * @param o
     */
    public void saveObject(T o);

    public void updateObject(T o);

    public void mergeObject(T o);

    /**
     * 删除泛型参数对象
     *
     * @param o
     */
    public void deleteObject(T o);

    /**
     * 根据主键删除泛型参数对象
     *
     * @param id
     */
    public void deleteObjectById(PK id);

    /**
     * 配合 EC Table 设计的一个查询语句 根据HQL语句获得所有的对象 ，并分页
     *
     * @param shql
     * @param pageDesc 分页属性
     * @return
     */
    public List<T> listObjects(String shql, PageDesc pageDesc);

    /**
     * 配合 EC Table 设计的一个查询语句 根据带参数的HQL语句获得所有的对象 ，并分页
     *
     * @param shql
     * @param values   参数数组
     * @param pageDesc 分页属性
     * @return
     */
    public List<T> listObjects(String shql, Object[] values, PageDesc pageDesc);

    /**
     * 配合 EC Table 设计的一个查询语句 根据带一个参数的HQL语句获得所有的对象 ，并分页
     *
     * @param shql     hibernate query language
     * @param value    参数
     * @param pageDesc 分页属性
     * @return
     */
    public List<T> listObjects(String shql, Object value, PageDesc pageDesc);

    /**
     * 配合 EC Table 设计的一个查询语句，将 filterMap 组装成对应的Hql语句 调用对应的 getObjects
     *
     * @param filterMap 过滤条件
     * @param pageDesc  分页属性
     * @return
     */
    public List<T> listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    /**
     * 配合 EC Table 设计的一个查询语句
     *
     * @param shql
     * @param filterMap 过滤条件
     * @param pageDesc  分页属性
     * @return
     */
    public List<T> listObjects(String shql, Map<String, Object> filterMap,
                               PageDesc pageDesc);

}

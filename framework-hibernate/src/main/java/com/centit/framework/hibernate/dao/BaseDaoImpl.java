package com.centit.framework.hibernate.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.centit.framework.core.common.ObjectException;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.po.EntityWithDeleteTag;
import com.centit.framework.core.po.EntityWithTimestamp;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.StringRegularOpt;
import com.centit.support.common.KeyValuePair;
import com.centit.support.database.QueryAndNamedParams;
import com.centit.support.database.QueryAndParams;
import com.centit.support.database.QueryUtils;

public abstract class BaseDaoImpl<T extends Serializable, PK extends Serializable> 
 {
    @Resource(name="sessionFactory")
    protected SessionFactory sessionFactory;
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

    }
    
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }
    
    /**
     * 获取当前事务上下文环境 session
     * @return
     */
    @Transactional(propagation=Propagation.MANDATORY)  
    public Session getCurrentSession(){
        Session s = this.sessionFactory.getCurrentSession();
        if(s==null||!s.isOpen())
            s = this.sessionFactory.openSession();        
        return s;
    }
    
    protected static Log log = LogFactory.getLog(BaseDaoImpl.class);

    protected Map<String, String> filterField = null;

   
    // ((SessionImpl)
    // getSession()).getFactory().getDialect().getClass().getName();
    private Class<?> poClass = null;

    /*
    
    private static String dialectClassName = null;
    @Transactional
    public final String getDialectName() {
        if (BaseDaoImpl.dialectClassName == null) {  
            //sessionFactory.getSessionFactoryOptions().getInterceptor().
            SessionFactoryImpl sesionFactoryimpl= (SessionFactoryImpl) sessionFactory;            
            String dbName = sesionFactoryimpl.getDialect().getClass().getName();
            int p = dbName.lastIndexOf('.');
            if (p >= 0)
                dbName = dbName.substring(p + 1);
            BaseDaoImpl.dialectClassName = dbName;
        }
        return BaseDaoImpl.dialectClassName;
    }*/

    public void setFilterField(Map<String, String> ffd) {
        filterField = ffd;
    }

    public Map<String, String> getFilterField() {
        return filterField;
    }

    public final Class<?> getPoClass() {
    	//return this.getClass().getTypeParameters()[0];
        if (poClass == null) {
            ParameterizedType genType = (ParameterizedType) getClass()
                    .getGenericSuperclass();
            Type[] params = genType.getActualTypeArguments();
            poClass = ((Class<?>) params[0]);
        }
        return poClass;
    }
    
    /**
     * 获取泛型参数对象的主键类型
     * @return
     */
    public final Class<?> getPkClass() {
        ParameterizedType genType = (ParameterizedType) getClass()
                .getGenericSuperclass();
        Type[] params = genType.getActualTypeArguments();
        return ((Class<?>) params[1]);
    }
    
    /**
     * 获取泛型参数对象全称 
     * @return
     */
    public final String getClassTName() {
        return getPoClass().getName();
    }

    /**
     * 获取泛型参数对象名称
     * @return
     */
    public final String getClassTShortName() {
        return getPoClass().getSimpleName();
    }

    @Transactional(propagation=Propagation.MANDATORY) 
    public void deleteObject(T o) {

        if(o instanceof EntityWithDeleteTag){
            ((EntityWithDeleteTag) o).setDeleted(true);
            saveObject(o);
        }else{
            try {   
                sessionFactory.getCurrentSession().delete(o);
                //sessionFactory.getCurrentSession().delete(o);
                // log.debug("delete successful");
            } catch (RuntimeException re) {
                log.error("delete failed", re);
                throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,re);
            }
        }
    }
    
    @Transactional(propagation=Propagation.MANDATORY) 
    public void deleteObjectForce(T o) {
        try {   
            sessionFactory.getCurrentSession().delete(o);
            //sessionFactory.getCurrentSession().delete(o);
            // log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,re);
        }
    }

    @Transactional(propagation=Propagation.MANDATORY) 
    public void deleteObjectForceById(PK id) {
        T o = getObjectById(id);
        deleteObjectForce(o);
    }
 
    @Transactional(propagation=Propagation.MANDATORY) 
    public void deleteObjectById(PK id) {
        T o = getObjectById(id);
        deleteObject(o);
    }

    @SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.MANDATORY) 
    public PK saveNewObject(T o) {
        try {
            if(o instanceof EntityWithTimestamp){
                EntityWithTimestamp ewto = (EntityWithTimestamp) o;
                ewto.setLastModifyDate(DatetimeOpt.currentUtilDate());
            }
            return (PK) sessionFactory.getCurrentSession().save(o);// .persist(o);//
            // log.debug("save or update successful");
        } catch (RuntimeException re) {
            log.error("save new object failed", re);
            throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,re);
        }
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.MANDATORY) 
    public List<PK> saveNewObjects(Collection<T> os) {
        try {
        	List<PK> pks = new ArrayList<PK>();
        	
        	for(T o : os){
	            if(o instanceof EntityWithTimestamp){
	                EntityWithTimestamp ewto = (EntityWithTimestamp) o;
	                ewto.setLastModifyDate(DatetimeOpt.currentUtilDate());
	            }
	            pks.add((PK)sessionFactory.getCurrentSession().save(o));// .persist(o);//
        	}
        	return pks;
            // log.debug("save or update successful");
        } catch (RuntimeException re) {
            log.error("save new object failed", re);
            throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,re);
        }
    }
    
    @SuppressWarnings("unchecked")
   	@Transactional(propagation=Propagation.MANDATORY) 
    public List<PK> saveNewObjects(T[] os) {
           try {
           	List<PK> pks = new ArrayList<PK>();           	
           	for(T o : os){
   	            if(o instanceof EntityWithTimestamp){
   	                EntityWithTimestamp ewto = (EntityWithTimestamp) o;
   	                ewto.setLastModifyDate(DatetimeOpt.currentUtilDate());
   	            }
   	            pks.add((PK)sessionFactory.getCurrentSession().save(o));// .persist(o);//
           	}
           	return pks;
               // log.debug("save or update successful");
           } catch (RuntimeException re) {
               log.error("save new object failed", re);
               throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,re);
           }
       }
    
    @Transactional(propagation=Propagation.MANDATORY) 
    public void updateRawObject(T o) {
        try {
            sessionFactory.getCurrentSession().update(o);// .persist(o);//
            // log.debug("save or update successful");
        } catch (RuntimeException re) {
            log.error("save new object failed", re);
            throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,re);
        }
    }
    
        
    @Transactional(propagation=Propagation.MANDATORY) 
    public void updateObject(T o) {
        try {
            if(o instanceof EntityWithTimestamp){
                ((EntityWithTimestamp) o).setLastModifyDate(DatetimeOpt.currentUtilDate());
            }
            sessionFactory.getCurrentSession().update(o);// .persist(o);//
            // log.debug("save or update successful");
        } catch (RuntimeException re) {
            log.error("save new object failed", re);
            throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,re);
        }
    }
    
    /**
     * 只更改部分属性，根据对象的主键来更新
     * @param o 这对对象中除了要有对应的属性值 ，还必须有主键值
     * @param properties 需要更的属性
     * @throws NoSuchFieldException 如果属性名输入的不对会报错
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation=Propagation.MANDATORY) 
    public void updateObjectProperties(T o,String... properties )
    		throws NoSuchFieldException {
        try {
 
        	T dbo = (T) getCurrentSession().get(o.getClass(),//getPoClass(), 
        			(Serializable)getPoObjectId(o));//{getCurrentSession().getIdentifier(o) );
        	if(dbo==null)
        		throw new ObjectException(ObjectException.NULL_EXCEPTION,"被更改对象找不到");
        	
        	for(String p:properties){
        		ReflectionOpt.forceSetProperty(dbo, p,
        				ReflectionOpt.forceGetProperty(o, p));
        	}
        
            if(dbo instanceof EntityWithTimestamp){
                ((EntityWithTimestamp) dbo).setLastModifyDate(DatetimeOpt.currentUtilDate());
            }
            sessionFactory.getCurrentSession().update(dbo);// .persist(o);//
            // log.debug("save or update successful");
        } catch (RuntimeException re) {
            log.error("save new object failed", re);
            throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,re);
        }
    }
    
    /**
     * 修改之前check一下版本号，不一致抛异常
     * @param o
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation=Propagation.MANDATORY) 
    public void updateObjectCheckTimestamp(T o) {
        try {
            if(o instanceof EntityWithTimestamp){
                T dbo = (T) sessionFactory.getCurrentSession().get(o.getClass(),//getPoClass(),
                		(Serializable)getPoObjectId(o));//getCurrentSession().getIdentifier(o));
                if(dbo==null)
            		throw new ObjectException(ObjectException.NULL_EXCEPTION,"被更改对象找不到");
                
                if(!  ((EntityWithTimestamp) o).getLastModifyDate()
                        .equals(((EntityWithTimestamp) dbo).getLastModifyDate())){
                    String errorMsg = "save or update object  failed,"+ 
                            getClassTName() +":" + o.toString() + " be modified out-sync.";
                    log.error(errorMsg);
                    throw new RuntimeException(errorMsg);
                }
                        
                ((EntityWithTimestamp) o).setLastModifyDate(DatetimeOpt.currentUtilDate());
            }
            sessionFactory.getCurrentSession().merge(o);// .persist(o);//
            // log.debug("save or update successful");
        } catch (RuntimeException re) {
            log.error("save new object failed", re);
            throw new ObjectException(ObjectException.DATABASE_OUT_SYNC_EXCEPTION,re);
        }
    }
    
    /**
     * 只更改部分属性，根据对象的主键来更新,同时检验 时间戳
     * @param o 这对对象中除了要有对应的属性值 ，还必须有主键值
     * @param properties 需要更的属性
     * @throws NoSuchFieldException 如果属性名输入的不对会报错
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation=Propagation.MANDATORY) 
    public void updateObjectPropertiesCheckTimestamp(T o,String... properties )
    		throws NoSuchFieldException {
        try { 
        	T dbo = (T) getCurrentSession().get(o.getClass(),//getPoClass(), 
        			(Serializable)getPoObjectId(o));//getCurrentSession().getIdentifier(o) );
        	
        	if(dbo==null)
        		throw new ObjectException(ObjectException.NULL_EXCEPTION,"被更改对象找不到");
        	
        	if(o instanceof EntityWithTimestamp){
	        	if(!  ((EntityWithTimestamp) o).getLastModifyDate()
	                    .equals(((EntityWithTimestamp) dbo).getLastModifyDate())){
	                String errorMsg = "save or update object failed,"+ 
	                        getClassTName() +":" + o.toString() + " be modified out-sync.";
	                log.error(errorMsg);
	                throw new RuntimeException(errorMsg);
	            }
        	}
        	
        	for(String p:properties){
        		ReflectionOpt.forceSetProperty(dbo, p,
        				ReflectionOpt.forceGetProperty(o, p));
        	}
        
            if(dbo instanceof EntityWithTimestamp){
                ((EntityWithTimestamp) dbo).setLastModifyDate(DatetimeOpt.currentUtilDate());
            }
            
            sessionFactory.getCurrentSession().update(dbo);// .persist(o);//
            // log.debug("save or update successful");
        } catch (RuntimeException re) {
            log.error("save new object failed", re);
            throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,re);
        }
    }   
    
    
    /**
     * 批量删除记录
     * @param dbObjects 数据库中旧的对象列表
     */
    @Transactional(propagation=Propagation.MANDATORY) 
    public void deleteObjectsAsTabulation(Collection<T> dbObjects){
    	
    	if(dbObjects==null||dbObjects.size()==0)
    		return;
    	Date replaceTime  = DatetimeOpt.currentUtilDate();
    	Session session =  getCurrentSession();
        //删除记录
        for(T deleteO : dbObjects){
        	if( deleteO instanceof EntityWithDeleteTag ){
        		//如果是逻辑删除，对那些已经删除的就不重复删除了
        		if(!((EntityWithDeleteTag)deleteO ).isDeleted()){        			
        			((EntityWithDeleteTag) deleteO).setDeleted(true);
        		
	        		if(deleteO instanceof EntityWithTimestamp){
	        			((EntityWithTimestamp) deleteO).setLastModifyDate(replaceTime);
	        		}
	        		
        			session.save(deleteO);
        		}
        	}else{//不是逻辑删除的直接删除
        		session.delete(deleteO);
        	}        	
        }
    }
    
    /**
     *  批量删除记录 根据单独外键查询
     * @param propertyName 外键关联字段
     * @param propertyValue 外键值（主表的主键）
     */
    @Transactional(propagation=Propagation.MANDATORY) 
    public void deleteObjectsAsTabulation(
    		final String propertyName,
            final Object propertyValue ){
    	List<T> dbObjects = this.listObjectByProperty(propertyName, propertyValue);
    	deleteObjectsAsTabulation(dbObjects);
    }
    
    /**
     *  批量删除记录 根据复合外键查询
     * @param properties 复合外键字段属性 和对应额值
     */
    @Transactional(propagation=Propagation.MANDATORY) 
    public void deleteObjectsAsTabulation(Map<String, Object> properties){
    	List<T> dbObjects = this.listObjectByProperties(properties);
    	deleteObjectsAsTabulation(dbObjects);
    }
    
    /**
     *  通过注解 @Id 或者  @EmbeddedId 获得主键
     * @param poObj
     * @return
     */
    public static Object getPoObjectId(Object poObj){
    	Field[] objFields = poObj.getClass().getDeclaredFields();       
        
        for(Field field :objFields){
            if(field.isAnnotationPresent(Id.class) ||
            		field.isAnnotationPresent(EmbeddedId.class)){
            	try {
					return ReflectionOpt.forceGetFieldValue(poObj, field);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
					return null;
				}
            }
        }
        return null;
    }
    
    /**
     * 用新的子表对象列表替换旧的子表对象列表
     * @param dbObjects 数据库中旧的对象列表
     * @param newObjects 新的对象列表
     * @param checkTimestamp 更新的记录是否检查更改时间戳
     */
    @SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.MANDATORY) 
    public List<PK> replaceObjectsAsTabulation(List<T> dbObjects,Collection<T> newObjects,
    		boolean checkTimestamp){
    	
    	if(newObjects==null){
    		throw new ObjectException(ObjectException.NULL_EXCEPTION,"请直接调用删除操作");
    	}
    	List<PK> pks = new ArrayList<PK>();
    	Date replaceTime  = DatetimeOpt.currentUtilDate();
    	//需要删除的记录
    	List<T> deleteObjects = new ArrayList<T>();
    	//需要新建的记录
    	List<T> insertObjects = new ArrayList<T>();
    	//需要更改的记录
    	List<T> updateObjects = new ArrayList<T>();
    	List<KeyValuePair<T,T>> updateObjectPairs = new ArrayList<KeyValuePair<T,T>>();
    	Session session =  getCurrentSession();
        for(T dbo : dbObjects){
            boolean found = false;
            for(T no : newObjects){
            	if( session.getIdentifier(dbo).equals(
            			getPoObjectId(no))){
            			//session.getIdentifier(no))){
            		//找到了交集，添加到修改列表中，
            		updateObjects.add(no);
            		//同时记录一个对应关系，用户后面检查时间戳
            		updateObjectPairs.add(new KeyValuePair<T,T>(no,dbo));
                    found = true;
                    break;
                }
            }
            if(! found)
            	deleteObjects.add(dbo);
        }
        for(T no : newObjects){        	
        	if(!updateObjects.contains(no))
        		insertObjects.add(no);
        }
        
        for(KeyValuePair<T,T> updateO : updateObjectPairs){
        	T no = updateO.getKey();
        	T dbo = updateO.getValue();
        	//检查时间戳
        	if(checkTimestamp && no instanceof EntityWithTimestamp ){
    			 if(!  ((EntityWithTimestamp) no).getLastModifyDate()
                         .equals(((EntityWithTimestamp) dbo).getLastModifyDate())){
                     String errorMsg = "save or update object failed,"+ 
                             getClassTName() +":" + no.toString() + " be modified out-sync.";
                     log.error(errorMsg);
                     throw new RuntimeException(errorMsg);
        		}               
        	}
        	//将新纪录中的熟悉copy到dbo中
        	BeanUtils.copyProperties(no, dbo);
        	if(no instanceof EntityWithTimestamp ){
        		((EntityWithTimestamp) dbo).setLastModifyDate(replaceTime);
        	}        	
       		session.update(dbo);
       		pks.add((PK)session.getIdentifier(dbo));
        }
        //删除记录
        for(T deleteO : deleteObjects){
        	if( deleteO instanceof EntityWithDeleteTag ){
        		//如果是逻辑删除，对那些已经删除的就不重复删除了
        		if(!((EntityWithDeleteTag)deleteO ).isDeleted()){        			
        			((EntityWithDeleteTag) deleteO).setDeleted(true);
        		
	        		if(deleteO instanceof EntityWithTimestamp){
	        			((EntityWithTimestamp) deleteO).setLastModifyDate(replaceTime);
	        			session.save(deleteO);
	        		}
        			
        			session.save(deleteO);
        		}
        	}else{//不是逻辑删除的直接删除
        		session.delete(deleteO);
        	}        	
        }
        //插入新值        
        for(T insretO : insertObjects){
        	if( insretO instanceof EntityWithTimestamp ){ 
                EntityWithTimestamp ewto = (EntityWithTimestamp) insretO;
                ewto.setLastModifyDate(replaceTime);
        	}
        	pks.add((PK)session.save(insretO));
        }
        return pks;
    }    

    /**
     *  用新的子表对象列表替换旧的子表对象列表 
     *  通过单主键查询数据库中的旧的类别
     * @param newObjects 新的对象列表
     * @param propertyName 外键关联字段
     * @param propertyValue 外键值（主表的主键）
     */
    @Transactional(propagation=Propagation.MANDATORY) 
    public List<PK> replaceObjectsAsTabulation(Collection<T> newObjects,
    		final String propertyName,
            final Object propertyValue ){
    	List<T> dbObjects = this.listObjectByProperty(propertyName, propertyValue);
    	return replaceObjectsAsTabulation(dbObjects,newObjects,false);
    }
    
    /**
     *  用新的子表对象列表替换旧的子表对象列表 
     *  通过复合主键查询数据库中的旧的类别
     * @param newObjects 新的对象列表
     * @param properties 复合外键字段属性 和对应额值
     */
    @Transactional(propagation=Propagation.MANDATORY) 
    public List<PK> replaceObjectsAsTabulation(Collection<T> newObjects,
    		Map<String, Object> properties){
    	List<T> dbObjects = this.listObjectByProperties(properties);
    	return replaceObjectsAsTabulation(dbObjects,newObjects,false);
    }
  
    /**
     *  用新的子表对象列表替换旧的子表对象列表 ,更新的记录需要检查更改时间戳
     *  通过单主键查询数据库中的旧的类别
     * @param newObjects 新的对象列表
     * @param propertyName 外键关联字段
     * @param propertyValue 外键值（主表的主键）
     */
    @Transactional(propagation=Propagation.MANDATORY) 
    public List<PK> replaceObjectsAsTabulationCheckTimestamp(Collection<T> newObjects,
    		final String propertyName,
            final Object propertyValue ){
    	List<T> dbObjects = this.listObjectByProperty(propertyName, propertyValue);
    	return replaceObjectsAsTabulation(dbObjects,newObjects,true);
    }
    /**
     *  用新的子表对象列表替换旧的子表对象列表 ,更新的记录需要检查更改时间戳
     *  通过复合主键查询数据库中的旧的类别
     * @param newObjects 新的对象列表
     * @param properties 复合外键字段属性 和对应额值
     */
    @Transactional(propagation=Propagation.MANDATORY) 
    public List<PK> replaceObjectsAsTabulationCheckTimestamp(Collection<T> newObjects,
    		Map<String, Object> properties){
    	List<T> dbObjects = this.listObjectByProperties(properties);
    	return replaceObjectsAsTabulation(dbObjects,newObjects,true);
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.MANDATORY) 
    public T mergeObject(T o) {
        try {
            if(o instanceof EntityWithTimestamp){
                ((EntityWithTimestamp) o).setLastModifyDate(DatetimeOpt.currentUtilDate());
            }
            return (T) sessionFactory.getCurrentSession().merge(o);// .persist(o);//
            // log.debug("save or update successful");
        } catch (RuntimeException re) {
            log.error("margin failed", re);
            throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,re);
        }
    }

    /**
     * 将对持久化对象的修改触发update数据库
     */
    @Transactional
    public void flush() {
        sessionFactory.getCurrentSession().flush();
    }
    
    @Transactional(propagation=Propagation.MANDATORY) 
    public void saveRawObject(T o) {
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(o);// .persist(o);//
            // log.debug("save or update successful");
        } catch (RuntimeException re) {
            log.error("save or update failed", re);
            throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,re);
        }
    }

    @Transactional(propagation=Propagation.MANDATORY) 
    public void saveObject(T o) {
        try {
            if(o instanceof EntityWithTimestamp){
            	((EntityWithTimestamp) o).setLastModifyDate(DatetimeOpt.currentUtilDate());
            }
            sessionFactory.getCurrentSession().saveOrUpdate(o);// .persist(o);//
            // log.debug("save or update successful");
        } catch (RuntimeException re) {
            log.error("save or update failed", re);
            throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,re);
        }
    }

    private String appendOrderBy(String shql) {
        Map<String, String> filterFieldDesc = getFilterField();
        if (filterFieldDesc != null) {
            String sOrderby = filterFieldDesc.get(CodeBook.ORDER_BY_HQL_ID);
            if (!StringBaseOpt.isNvl(sOrderby))
                return shql + " order by " + sOrderby;
        }
        return shql;
    }


    @SuppressWarnings("unchecked")   
    @Transactional
    public List<T> listObjects() {
        String shql = appendOrderBy("From " + getClassTShortName());

        try {
            return (List<T>) getCurrentSession().createQuery(shql).list();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 用命名参数代替这个
     */
    @Deprecated
    public QueryAndParams builderHqlAndParams(String shql,
            Map<String, Object> filterDesc) {

        StringBuffer hql = new StringBuffer(shql);
        List<Object> params = new ArrayList<Object>();
        Map<String, String> filterFieldDesc = getFilterField();
        String sOrderby = null;

        for (Map.Entry<String, Object> ent : filterDesc.entrySet()) {
            String skey = ent.getKey();
            String sSqlFormat = null;
            if (filterFieldDesc != null) {
                sSqlFormat = filterFieldDesc.get(skey);
                if (sSqlFormat == null)
                    sSqlFormat = filterFieldDesc.get(skey.toUpperCase());
            }
            if (sSqlFormat != null) {
                Object sValue = ent.getValue();

                if ((null == sValue)
                        || ((sValue instanceof String) && !StringUtils
                                .isNotBlank(sValue.toString()))) {
                    continue;
                }

                if (skey.startsWith(CodeBook.NO_PARAM_FIX)) {
                    if (StringRegularOpt.isTrue(sValue.toString()))
                        hql.append(" and ").append(sSqlFormat);
                } else {
                    if (sSqlFormat.equalsIgnoreCase(CodeBook.LIKE_HQL_ID)) {
                        sValue = QueryUtils.getMatchString(sValue.toString());
                        hql.append(String.format(" and %s like ? ", skey));
                    } else if (sSqlFormat
                            .equalsIgnoreCase(CodeBook.EQUAL_HQL_ID)) {
                        hql.append(String.format(" and %s = ? ", skey));
                    }  else if (sSqlFormat
                            .equalsIgnoreCase(CodeBook.IN_HQL_ID)) {
                        hql.append(String.format(" and %s in ? ", skey));
                    }else {
                        hql.append(" and ").append(sSqlFormat);
                    }
                    params.add(sValue);
                }

            } else if (CodeBook.SELF_ORDER_BY.equalsIgnoreCase(skey)) {
                // if(sOrderby==null)
                sOrderby = QueryUtils.trimSqlOrderByField(ent.getValue().toString());
                // else
                // sOrderby += ", "+ ent.getValue();
            }
        }
        if (sOrderby == null && filterFieldDesc != null)
            sOrderby = filterFieldDesc.get(CodeBook.ORDER_BY_HQL_ID);
        if (!StringBaseOpt.isNvl(sOrderby))
            hql.append(" order by ").append(sOrderby);

        return new QueryAndParams(hql.toString(), params.toArray());
    }
    /**
     * 创建一个查询语句 和 条件
     * @param shql
     * @param filterDesc
     * @return
     */
    public static QueryAndNamedParams builderHqlAndNamedParams(String shql,
            Map<String, Object> filterDesc ,Map<String, String> filterFieldDesc ) {

        StringBuffer hql = new StringBuffer(shql);
        Map<String, Object> params = new HashMap<>();
        //Map<String, String> filterFieldDesc = getFilterField();
        String sOrderby = null;
        String sOrderFiled = null;
        String sOrder = "asc";
        if(filterDesc!=null){
	        for (Map.Entry<String, Object> ent : filterDesc.entrySet()) {
	            String skey = ent.getKey();
	            // 从请求参数带入的排序字段
	            if (CodeBook.SELF_ORDER_BY.equalsIgnoreCase(skey)) {
	                sOrderby = QueryUtils.trimSqlOrderByField(ent.getValue().toString());
	            } 
	            // 从请求参数带入的排序顺序
	            else if (CodeBook.TABLE_SORT_FIELD.equalsIgnoreCase(skey)) {
	                sOrderFiled = QueryUtils.trimSqlOrderByField(ent.getValue().toString());
	            }else if (CodeBook.TABLE_SORT_ORDER.equalsIgnoreCase(skey)) {
	                String stemp = ent.getValue().toString();
	                if("asc".equalsIgnoreCase(stemp) || "desc".equalsIgnoreCase(stemp))
	                     sOrder = stemp;
	            }else{            
	                String sSqlFormat = null;
	                if (filterFieldDesc != null) {
	                    sSqlFormat = filterFieldDesc.get(skey);
	                    if (sSqlFormat == null)
	                        sSqlFormat = filterFieldDesc.get(skey.toUpperCase());
	                }
	                if (sSqlFormat != null) {
	                    Object sValue = ent.getValue();
	                    
	                    if ((null == sValue)
	                            || ((sValue instanceof String) && !StringUtils
	                                    .isNotBlank(sValue.toString()))) {
	                        continue;
	                    }
	                    if (skey.startsWith(CodeBook.NO_PARAM_FIX)) {
	                        if (StringRegularOpt.isTrue(sValue.toString()))
	                            hql.append(" and ").append(sSqlFormat);
	                    } else {
	                        if (sSqlFormat.equalsIgnoreCase(CodeBook.LIKE_HQL_ID)) {
	                            sValue = QueryUtils.getMatchString(sValue.toString());
	                            hql.append(" and " + skey + " like :" + skey + " ");
	                        } else if (sSqlFormat
	                                .equalsIgnoreCase(CodeBook.EQUAL_HQL_ID)) {
	                            hql.append(String.format("  and " + skey + " = :"
	                                    + skey + " "));
	                        } else if (sSqlFormat.equalsIgnoreCase(CodeBook.IN_HQL_ID)) {
	                            hql.append(String.format("  and " + skey + " in :"
	                                    + skey + " "));
	                        }
	                        else {
	                            hql.append(" and ").append(
	                                    sSqlFormat.replace("?", ":" + skey));
	                        }
	                        params.put(skey, sValue);
	                    }
	                } 
	            }
	        }
        }
        if( sOrderFiled !=null){
            sOrderby =  sOrderFiled +" "+ sOrder;
        }
        // 如果请求参数没有排序字段，从Dao中查找
        if (sOrderby == null && filterFieldDesc != null) {
            sOrderby = filterFieldDesc.get(CodeBook.ORDER_BY_HQL_ID);
        }        
        // 排序
        if (!StringBaseOpt.isNvl(sOrderby)) {
            hql.append(" order by ").append(sOrderby);            
        }
        
        return new QueryAndNamedParams(hql.toString(), params);
    }
    
    public QueryAndNamedParams builderHqlAndNamedParams(String shql,
            Map<String, Object> filterDesc) {
        return builderHqlAndNamedParams(shql,filterDesc,getFilterField());
    }
    /**
     * 创建一个 统计分析语句的 From 和 where 部分 不能包括 order by
     * @param shql
     * @param filterDesc
     * @return
     */
    public QueryAndNamedParams builderStatHqlAndNamedParams(String shql,
            Map<String, Object> filterDesc) {

        StringBuffer hql = new StringBuffer(shql);
        Map<String, Object> params = new HashMap<>();
        Map<String, String> filterFieldDesc = getFilterField();

        for (Map.Entry<String, Object> ent : filterDesc.entrySet()) {
            String skey = ent.getKey();
            
            if (! CodeBook.SELF_ORDER_BY.equalsIgnoreCase(skey) 
                   && ! CodeBook.TABLE_SORT_FIELD.equalsIgnoreCase(skey) 
                   && ! CodeBook.TABLE_SORT_ORDER.equalsIgnoreCase(skey)) {

                //从请求参数带入的排序字段                       
                String sSqlFormat = null;
                if (filterFieldDesc != null) {
                    sSqlFormat = filterFieldDesc.get(skey);
                    if (sSqlFormat == null)
                        sSqlFormat = filterFieldDesc.get(skey.toUpperCase());
                }
                
                if (sSqlFormat != null) {
                    Object sValue = ent.getValue();
                    
                    if ((null == sValue)
                            || ((sValue instanceof String) && !StringUtils
                                    .isNotBlank(sValue.toString()))) {
                        continue;
                    }
                    if (skey.startsWith(CodeBook.NO_PARAM_FIX)) {
                        if (StringRegularOpt.isTrue(sValue.toString()))
                            hql.append(" and ").append(sSqlFormat);
                    } else {
                        if (sSqlFormat.equalsIgnoreCase(CodeBook.LIKE_HQL_ID)) {
                            sValue = QueryUtils.getMatchString(sValue.toString());
                            hql.append(" and " + skey + " like :" + skey + " ");
                        } else if (sSqlFormat
                                .equalsIgnoreCase(CodeBook.EQUAL_HQL_ID)) {
                            hql.append(String.format("  and " + skey + " = :"
                                    + skey + " "));
                        } else if (sSqlFormat.equalsIgnoreCase(CodeBook.IN_HQL_ID)) {
                            hql.append(String.format("  and " + skey + " in :"
                                    + skey + " "));
                        }
                        else {
                            hql.append(" and ").append(
                                    sSqlFormat.replace("?", ":" + skey));
                        }
                        params.put(skey, sValue);
                    }
                }
            }
        }
        
        return new QueryAndNamedParams(hql.toString(), params);
    }

    /**
     * 此方法有SQL注入危险
     */
    @Deprecated
    public String builderHql(String shql, Map<String, String> filterDesc) {
        StringBuffer hql = new StringBuffer(shql);
        Map<String, String> filterFieldDesc = getFilterField();
        String sOrderby = null;

        for (Map.Entry<String, String> ent : filterDesc.entrySet()) {
            String skey = ent.getKey();
            String sSqlFormat = null;
            if (filterFieldDesc != null) {
                sSqlFormat = filterFieldDesc.get(skey);
                if (sSqlFormat == null)
                    sSqlFormat = filterFieldDesc.get(skey.toUpperCase());
            }

            if (sSqlFormat != null) {
                String sValue = ent.getValue();// .toString();

                if (sValue != null && !sValue.equals("")) {
                    if (sSqlFormat.startsWith(CodeBook.NO_PARAM_FIX)) {
                        if (StringRegularOpt.isTrue(ent.getValue()))
                            hql.append(" and ").append(sSqlFormat);
                    } else if (sSqlFormat
                            .equalsIgnoreCase(CodeBook.LIKE_HQL_ID)) {
                        sValue = QueryUtils.getMatchString(sValue);
                        hql.append(String.format(" and %s like %s ", skey,
                                QueryUtils.buildStringForQuery(sValue)));
                    } else if (sSqlFormat
                            .equalsIgnoreCase(CodeBook.EQUAL_HQL_ID)) {
                        hql.append(String.format(" and %s = %s ", skey,
                                QueryUtils.buildStringForQuery(sValue)));
                    } else {
                        hql.append(" and ").append(
                                String.format(sSqlFormat,
                                        QueryUtils.buildStringForQuery(sValue)));
                    }
                }
            } else if (CodeBook.SELF_ORDER_BY.equalsIgnoreCase(skey)) {
                if (sOrderby == null)
                    sOrderby = QueryUtils.trimSqlOrderByField(ent.getValue().toString());
                else
                    sOrderby += ", " + ent.getValue();
            }
        }

        if (sOrderby == null && filterFieldDesc != null)
            sOrderby = filterFieldDesc.get(CodeBook.ORDER_BY_HQL_ID);
        if (!StringBaseOpt.isNvl(sOrderby))
            hql.append(" order by ").append(sOrderby);

        return hql.toString();
    }

    @Transactional
    public List<T> listValidObjects() {
    	return listObjectByProperty("isvalid","T");
        /*String shql = "From " + getClassTShortName() + " where isvalid='T'";
        try {
            return (List<T>) sessionFactory.getCurrentSession().createQuery(appendOrderBy(shql)).list();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }*/
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<T> listObjects(String hql) {
        try {
            return (List<T>) sessionFactory.getCurrentSession().createQuery(hql).list();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<T> listObjects(String shql, Object[] values, PageDesc pageDesc) {
        @SuppressWarnings("unchecked")
        List<T> l = (List<T>) DatabaseOptUtils.findObjectsByHql(this, shql, values,
                pageDesc);
        return l;
    }
    @Transactional
    public List<T> listObjects(String shql, Object value, PageDesc pageDesc) {
        return listObjects(shql, new Object[] { value }, pageDesc);
    }
    @Transactional
    public List<T> listObjects(String shql, PageDesc pageDesc) {
        return listObjects(shql, (Object[]) null, pageDesc);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<T> listObjects(String shql, Object[] values) {
        try {
            
            Query q= getCurrentSession().createQuery(shql);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    q.setParameter(i, values[i]);
                }
            }            
            List<T> l =  q.list();
            // System.out.print(l);
            return l;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<T> listObjects(String shql, Object value) {
        try {
            Query q= getCurrentSession().createQuery(shql);
            q.setParameter(0, value);
            return  (List<T>)q.list();

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<T> listObjects(String shql, Map<String, Object> filterDesc) {

        return listObjects(shql, filterDesc,null);
    }
    
    @Transactional
    public List<T> listObjects(Map<String, Object> filterDesc) {
        String shql = "From " + getClassTShortName() + " where 1=1 ";
        return listObjects(shql, filterDesc);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<T> listObjectsByNamedHql(String shql,
            Map<String, Object> params, int startPos, int maxSize) {
        try {
            Query q= getCurrentSession().createQuery(shql);
            
            DatabaseOptUtils.setQueryParameters(q,params);
            
            if (maxSize > 0)
                q.setMaxResults(maxSize);
            if (startPos >= 0)
                q.setFirstResult(startPos);
            return  (List<T>)q.list();

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    
    @Transactional
    public List<T> listObjects(String shql, Map<String, Object> filterDesc,
            PageDesc pageDesc) {
       
    	int startPos = 0;
        int maxSize = 0;
        if(pageDesc!=null){
            startPos = pageDesc.getRowStart();
            maxSize = pageDesc.getPageSize();
        }
        
        QueryAndNamedParams hql = builderHqlAndNamedParams(shql, filterDesc);
        List<T> listObjs = listObjectsByNamedHql(hql.getHql(), hql.getParams(),
        		startPos, maxSize);
        
        if(listObjs != null && pageDesc!=null){
        	if(maxSize>0){       
		        Query q =  getCurrentSession().createQuery("SELECT COUNT(1) "
		                                + QueryUtils.removeOrderBy(hql.getHql()));
		        Map<String, Object> params = hql.getParams();
		        DatabaseOptUtils.setQueryParameters(q,params);
		        pageDesc.setTotalRows(Integer.valueOf(q.list().get(0).toString()));
        	} else
        		pageDesc.setTotalRows(listObjs.size());
        }
        return listObjs;
    }
    
    
    

    @Transactional
    public List<T> listObjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        String shql = "From " + getClassTShortName() + " where 1=1 ";
        return listObjects(shql, filterMap, pageDesc);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public T getObjectById(PK id) {
        if (id == null)
            return null;
        // Type[] params = getClass().getTypeParameters();
        try {
            return (T) getCurrentSession().get(getPoClass(), id);
            //return (T) getCurrentSession().get(getClassTName(), id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Transactional
    public List<T> listObjectByProperty(final String propertyName,
            final Object propertyValue) {
    	try {
    		Criteria c = this.sessionFactory.getCurrentSession()
    				.createCriteria(getPoClass());
    		c.add(Restrictions.eq(propertyName,propertyValue));
    		return c.list();
    	}catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    		
        /*try {
            return  this.listObjectsByNamedHql("From "
                    + getClassTShortName() + " obj where obj." + propertyName
                    + " = :" + propertyName,
                     QueryUtils.createSqlParamsMap(propertyName, propertyValue),
                     -1,-1);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }*/
    }


    @SuppressWarnings("unchecked")
	@Transactional
    public List<T> listObjectByProperties(Map<String, Object> properties) {

    	try {
    		Criteria c = this.sessionFactory.getCurrentSession()
    				.createCriteria(getPoClass());
    		
    		 for (Map.Entry<String, Object> ent : properties.entrySet()) {
    			 c.add(Restrictions.eq(ent.getKey(),ent.getValue()));
             }		 
    		
    		return c.list();
    	}catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    	
        /*try {
            StringBuilder sHql = new StringBuilder("from "
                    + getClassTShortName() + " obj where ");
            int nPs = 0;
            for (Map.Entry<String, Object> ent : properties.entrySet()) {
                if (nPs > 0) {
                    sHql.append(" and");
                }
                nPs++;
                sHql.append(" obj.").append(ent.getKey())
                	.append("=:").append(ent.getKey());
            }

            return this.listObjectsByNamedHql(
                    sHql.toString(), properties,-1,-1);

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }*/
    }
     
    @Transactional
    public T getObjectByProperty(final String propertyName,
            final Object propertyValue) {

       List<T> l = listObjectByProperty(  propertyName,
                 propertyValue);
       if(l==null || l.size()==0)
    	   return null;
       return l.get(0);
    }

    @Transactional
    public T getObjectByProperties(Map<String, Object> properties) {

    	List<T> l = listObjectByProperties( properties);
    	if(l==null || l.size()==0)
    		return null;
    	return l.get(0);
    }

    
}

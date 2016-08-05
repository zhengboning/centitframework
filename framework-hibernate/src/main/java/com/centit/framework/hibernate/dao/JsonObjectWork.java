package com.centit.framework.hibernate.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.SysParametersUtils;
import com.centit.support.database.jsonmaptable.DB2JsonObjectDao;
import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.jsonmaptable.MySqlJsonObjectDao;
import com.centit.support.database.jsonmaptable.OracleJsonObjectDao;
import com.centit.support.database.jsonmaptable.SqlSvrJsonObjectDao;
import com.centit.support.database.metadata.TableInfo;

/**
 * @author codefan
 *
 */
public class JsonObjectWork implements JsonObjectDao {

	private TableInfo tableInfo;
	private BaseDaoImpl<?, ?> baseDao;

	public JsonObjectWork(){
		
	}
	
	public JsonObjectWork(TableInfo tableInfo){
		this.tableInfo = tableInfo;
	}
	
	public JsonObjectWork(BaseDaoImpl<?, ?> baseDao,TableInfo tableInfo){
		this.tableInfo = tableInfo;
		this.baseDao = baseDao;
	}
	
	public void setBaseDao(BaseDaoImpl<?, ?> baseDao) {
		this.baseDao = baseDao;
	}
	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}
	
	private JsonObjectDao createDao(Connection connection){
		String dialectName = SysParametersUtils.getStringValue("jdbc.dialect");
		if (dialectName.indexOf("Oracle")>=0)
			return new OracleJsonObjectDao(connection ,tableInfo);
		if (dialectName.indexOf("DB2")>=0)
			return new DB2JsonObjectDao(connection ,tableInfo);
		if (dialectName.indexOf("SQLServer")>=0)
			return new SqlSvrJsonObjectDao(connection ,tableInfo);
		if (dialectName.indexOf("MySQL")>=0)
			return new MySqlJsonObjectDao(connection ,tableInfo);
		return new OracleJsonObjectDao(connection ,tableInfo);
	}
	
	@Override
	public TableInfo getTableInfo() {
		return tableInfo;
	}

	@Override
	public JSONObject getObjectById(final Object keyValue) throws SQLException, IOException {

		return (JSONObject)baseDao.getCurrentSession().doReturningWork(new ReturningWork<JSONObject>(){
			@Override
			public JSONObject execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.getObjectById(keyValue);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public JSONObject getObjectById(final Map<String, Object> keyValues) throws SQLException, IOException {
		return (JSONObject)baseDao.getCurrentSession().doReturningWork(new ReturningWork<JSONObject>(){
			@Override
			public JSONObject execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.getObjectById(keyValues);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public JSONObject getObjectByProperties(final Map<String, Object> properties) throws SQLException, IOException {
		return (JSONObject)baseDao.getCurrentSession().doReturningWork(new ReturningWork<JSONObject>(){
			@Override
			public JSONObject execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.getObjectByProperties(properties);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public JSONArray listObjectsByProperties(final Map<String, Object> properties) throws SQLException, IOException {
		return (JSONArray)baseDao.getCurrentSession().doReturningWork(new ReturningWork<JSONArray>(){
			@Override
			public JSONArray execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.listObjectsByProperties(properties);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public JSONArray listObjectsByProperties(final Map<String, Object> properties,
			final int startPos,final int maxSize)
			throws SQLException, IOException {
		return (JSONArray)baseDao.getCurrentSession().doReturningWork(new ReturningWork<JSONArray>(){
			@Override
			public JSONArray execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.listObjectsByProperties(properties,startPos,maxSize);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public Long fetchObjectsCount(final Map<String, Object> properties) throws SQLException, IOException {
		return (Long)baseDao.getCurrentSession().doReturningWork(new ReturningWork<Long>(){
			@Override
			public Long execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.fetchObjectsCount(properties);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public void saveNewObject(final Map<String, Object> object) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.saveNewObject(object);				
			}			
		});		
	}

	@Override
	public void updateObject(final Map<String, Object> object) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.updateObject(object);				
			}			
		});		
	}

	@Override
	public void mergeObject(final Map<String, Object> object) throws SQLException, IOException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					dao.mergeObject(object);
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}			
		});	
	}

	@Override
	public void updateObjectsByProperties(final Map<String, Object> fieldValues, final Map<String, Object> properties)
			throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.updateObjectsByProperties(fieldValues,properties);				
			}			
		});	
		
	}

	@Override
	public void deleteObjectById(final Object keyValue) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.deleteObjectById(keyValue);				
			}			
		});
	}

	@Override
	public void deleteObjectById(final Map<String, Object> keyValues) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.deleteObjectById(keyValues);				
			}			
		});
		
	}

	@Override
	public void deleteObjectsByProperties(final Map<String, Object> properties) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.deleteObjectsByProperties(properties);				
			}			
		});
	}

	@Override
	public void insertObjectsAsTabulation(final JSONArray objects) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.insertObjectsAsTabulation(objects);				
			}			
		});
	}

	@Override
	public void deleteObjects(final JSONArray objects) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.deleteObjects(objects);				
			}			
		});
	}

	@Override
	public void deleteObjectsAsTabulation(final String propertyName, final Object propertyValue) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.deleteObjectsAsTabulation( propertyName,propertyValue);				
			}			
		});
	}

	@Override
	public void deleteObjectsAsTabulation(final Map<String, Object> properties) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.deleteObjectsAsTabulation(properties);				
			}			
		});		
	}

	@Override
	public void replaceObjectsAsTabulation(final JSONArray newObjects, final JSONArray dbObjects) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.replaceObjectsAsTabulation(newObjects,dbObjects);				
			}			
		});
	}

	@Override
	public void replaceObjectsAsTabulation(final JSONArray newObjects, final String propertyName, final Object propertyValue)
			throws SQLException, IOException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					dao.replaceObjectsAsTabulation(newObjects,propertyName,propertyValue);
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}			
		});
	}

	@Override
	public void replaceObjectsAsTabulation(final JSONArray newObjects, final Map<String, Object> properties)
			throws SQLException, IOException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					dao.replaceObjectsAsTabulation(newObjects,properties);
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}			
		});
	}

	@Override
	public Long getSequenceNextValue(final String sequenceName) throws SQLException, IOException {
		return (Long)baseDao.getCurrentSession().doReturningWork(new ReturningWork<Long>(){
			@Override
			public Long execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.getSequenceNextValue(sequenceName);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public List<Object[]> findObjectsBySql(final String sSql, final Object[] values) throws SQLException, IOException {
		return (List<Object[]>)baseDao.getCurrentSession().doReturningWork(new ReturningWork<List<Object[]>>(){
			@Override
			public List<Object[]> execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.findObjectsBySql(sSql,values);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public List<Object[]> findObjectsBySql(final String sSql, final Object[] values, final int pageNo, final int pageSize)
			throws SQLException, IOException {
		return (List<Object[]>)baseDao.getCurrentSession().doReturningWork(new ReturningWork<List<Object[]>>(){
			@Override
			public List<Object[]> execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.findObjectsBySql(sSql,values,pageNo,pageSize);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public List<Object[]> findObjectsByNamedSql(final String sSql, final Map<String, Object> values)
			throws SQLException, IOException {
		return (List<Object[]>)baseDao.getCurrentSession().doReturningWork(new ReturningWork<List<Object[]>>(){
			@Override
			public List<Object[]> execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.findObjectsByNamedSql(sSql,values);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public List<Object[]> findObjectsByNamedSql(final String sSql, final Map<String, Object> values, 
			final int pageNo, final int pageSize)	
			throws SQLException, IOException {
		return (List<Object[]>)baseDao.getCurrentSession().doReturningWork(new ReturningWork<List<Object[]>>(){
			@Override
			public List<Object[]> execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.findObjectsByNamedSql(sSql,values,pageNo,pageSize);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public JSONArray findObjectsAsJSON(final String sSql, final Object[] values, final String[] fieldnames)
			throws SQLException, IOException {
		return (JSONArray)baseDao.getCurrentSession().doReturningWork(new ReturningWork<JSONArray>(){
			@Override
			public JSONArray execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.findObjectsAsJSON(sSql,values,fieldnames);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public JSONArray findObjectsAsJSON(final String sSql, final Object[] values, final String[] fieldnames, 
			final int pageNo, final int pageSize)
			throws SQLException, IOException {
		return (JSONArray)baseDao.getCurrentSession().doReturningWork(new ReturningWork<JSONArray>(){
			@Override
			public JSONArray execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.findObjectsAsJSON(sSql,values,fieldnames,pageNo,pageSize);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public JSONArray findObjectsByNamedSqlAsJSON(final String sSql, final Map<String, Object> values,
			final String[] fieldnames)
			throws SQLException, IOException {
		return (JSONArray)baseDao.getCurrentSession().doReturningWork(new ReturningWork<JSONArray>(){
			@Override
			public JSONArray execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.findObjectsByNamedSqlAsJSON(sSql,values,fieldnames);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public JSONArray findObjectsByNamedSqlAsJSON(final String sSql, final Map<String, Object> values, 
			final String[] fieldnames,final int pageNo, final int pageSize) throws SQLException, IOException {
		return (JSONArray)baseDao.getCurrentSession().doReturningWork(new ReturningWork<JSONArray>(){
			@Override
			public JSONArray execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				try {
					return dao.findObjectsByNamedSqlAsJSON(sSql,values,fieldnames,pageNo,pageSize);
				} catch (IOException e) {
					return null;
				}
			}			
		});
	}

	@Override
	public void doExecuteSql(final String sSql) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.doExecuteSql(sSql);				
			}			
		});
	}

	@Override
	public void doExecuteSql(final String sSql,final Object[] values) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.doExecuteSql(sSql,values);				
			}			
		});
	}

	@Override
	public void doExecuteNamedSql(final String sSql, final Map<String, Object> values) throws SQLException {
		baseDao.getCurrentSession().doWork(new Work(){
			@Override
			public void execute(Connection connection) throws SQLException {
				JsonObjectDao dao = createDao(connection);
				dao.doExecuteNamedSql(sSql,values);				
			}			
		});
	}
}

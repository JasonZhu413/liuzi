package com.liuzi.mybatis.server.service;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.liuzi.mybatis.currency.cond.Query;
import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.currency.consts.HandlerConsts;
import com.liuzi.mybatis.server.mapper.BaseDao;
import com.liuzi.mybatis.server.pojo.Page;




public abstract class BaseServiceImpl<T> implements BaseService<T>{
	
	public abstract BaseDao<T> dao();
	
	@Autowired
	private SqlSessionTemplate _template;
	
	//--------------------------------------------- INSERT ------------------------------------------
	/**
	 * 新增单条
	 * @param record 实体
	 * @return
	 */
	@Override
	public <K> int $insert(K record){
		return dao().$insert(record);
	}
		
	/**
	 * 批量新增
	 * @param records 实体数组
	 * @return
	 */
	@Override
	public <K> int $insertArray(K[] records){
		return dao().$insertArray(records);
	}
	
	/**
	 * 批量新增
	 * @param records 实体List
	 * @return
	 */
	@Override
	public <K> int $insertList(List<K> records){
		return dao().$insertList(records);
	}
	
	//--------------------------------------------- DELETE ------------------------------------------
	
	/**
	 * 根据主键值删除
	 * @param pkValue 主键值
	 * @return
	 */
	@Override
	public int $deleteByPk(Object pkValue){
		return dao().$deleteByPk(pkValue);
	}
	
	/**
	 * 根据主键值删除
	 * @param pkValue 多个主键值
	 * @return
	 */
	@Override
	public int $deleteByPkArray(Object[] pkValues){
		return dao().$deleteByPkArray(pkValues);
	}
	
	/**
	 * 根据主键值删除
	 * @param pkValues 多个主键值
	 * @return
	 */
	@Override
	public int $deleteByPkList(List<Object> pkValues){
		return dao().$deleteByPkList(pkValues);
	}
	
	/**
	 * 根据字段删除
	 * @param column 字段名称
	 * @param value 字段值
	 * @return
	 */
	@Override
	public int $deleteByColumn(String column, Object value){
		return dao().$deleteByColumn(column, value);
	}
	
	/**
	 * 根据字段多值删除
	 * @param column 字段名称
	 * @param values 多个字段值
	 * @return
	 */
	@Override
	public int $deleteByColumnValueArray(String column, Object[] values){
		return dao().$deleteByColumnValueArray(column, values);
	}
	
	/**
	 * 根据字段多值删除
	 * @param column 字段名称
	 * @param values 多个字段值
	 * @return
	 */
	@Override
	public int $deleteByColumnValueList(String column, List<Object> values){
		return dao().$deleteByColumnValueList(column, values);
	}
	
	/**
	 * 根据多字段同值删除
	 * @param columns 字段名称
	 * @param value 多个字段值
	 * @return
	 */
	@Override
	public int $deleteByColumnArray(String[] columns, Object value){
		return dao().$deleteByColumnArray(columns, value);
	}
	
	/**
	 * 根据多字段同值删除
	 * @param columns 字段名称
	 * @param value 多个字段值
	 * @return
	 */
	@Override
	public int $deleteByColumnList(List<String> columns, Object value){
		return dao().$deleteByColumnList(columns, value);
	}
	
	/**
	 * 根据多个参数删除
	 * @param columns (字段名key, 字段值value)
	 * @return
	 */
	@Override
	public int $deleteByColumnMap(Map<String, Object> columns){
		return dao().$deleteByColumnMap(columns);
	}
	
	//--------------------------------------------- UPDATE ------------------------------------------
	/**
	 * 根据主键更新/批量更新
	 * @param records 实体数据
	 * @return
	 */
	@Override
	public <K> int $updateByPk(K record){
		return dao().$updateByPk(record);
	}
	
	/**
	 * 根据字段更新
	 * @param record 实体数据
	 * @param column 字段名
	 * @return
	 */
	@Override
	public <K> int $updateByColumn(K record, String column){
		return dao().$updateByColumn(record, column);
	}
	
	/**
	 * 根据字段更新
	 * @param record 实体数据
	 * @param columns 多个字段名，
	 * @return
	 */
	@Override
	public <K> int $updateByColumnArray(K record, String[] columns){
		return dao().$updateByColumnArray(record, columns);
	}
	
	/**
	 * 根据多项字段更新
	 * @param record 实体数据
	 * @param columns 多个字段名
	 * @return
	 */
	@Override
	public <K> int $updateByColumnList(K record, List<String> columns){
		return dao().$updateByColumnList(record, columns);
	}
	
	/**
	 * 根据自定义字段值更新
	 * @param record 实体数据
	 * @param columns 字段名key，字段值value
	 * @return
	 */
	@Override
	public <K> int $updateByColumnMap(K record, Map<String, Object> columns){
		return dao().$updateByColumnMap(record, columns);
	}
	
	/**
	 * 根据主键值批量更新
	 * @param records 实体数据
	 * @return
	 */
	@Override
	public <K> int $updateListByPk(List<K> records){
		return dao().$updateListByPk(records);
	}
	
	/**
	 * 根据字段批量更新
	 * @param records 实体数据
	 * @param columns 字段名
	 * @return
	 */
	@Override
	public <K> int $updateListByColumn(List<K> records, String column){
		return dao().$updateListByColumn(records, column);
	}

	/**
	 * 根据字段批量更新
	 * @param records 实体数据
	 * @param columns 字段名
	 * @return
	 */
	@Override
	public <K> int $updateListByColumnArray(List<K> records, String[] columns){
		return dao().$updateListByColumnArray(records, columns);
	}
	
	/**
	 * 根据字段批量更新
	 * @param records 实体数据
	 * @param columns 字段名
	 * @return
	 */
	@Override
	public <K> int $updateListByColumnList(List<K> records, List<String> columns){
		return dao().$updateListByColumnList(records, columns);
	}
	
	//--------------------------------------------- SELECT ------------------------------------------
	/**
	 * 根据主键查询单条
	 * @param pkValue 主键值
	 * @return
	 */
	@Override
	public T $selectOneByPk(Object pkValue){
		return dao().$selectOneByPk(pkValue);
	}
	
	/**
	 * 根据字段查询单条
	 * @param columns 字段名，值
	 * @return
	 */
	@Override
	public T $selectOneByColumnMap(Map<String, Object> columns){
		return dao().$selectOneByColumnMap(columns);
	}
	
	/**
	 * 根据字段查询多条
	 * @param columns 字段名，值
	 * @return
	 */
	@Override
	public List<T> $selectListByColumnMap(Map<String, Object> columns){
		return dao().$selectListByColumnMap(columns);
	}
	
	/**
	 * 根据主键查询单条
	 * @param pkValue 主键值
	 * @param clazz 
	 * @return
	 */
	@Override
	public <K> K $selectOneTargetByPk(Object pkValue, Class<K> target){
		return dao().$selectOneTargetByPk(pkValue, target);
	}
	
	/**
	 * 根据字段查询单条
	 * @param record 实体数据
	 * @return
	 */
	@Override
	public <K> K $selectOneTargetByColumns(K target){
		return dao().$selectOneTargetByColumns(target);
	}
	
	/**
	 * 根据字段查询多条
	 * @param record 实体数据
	 * @return
	 */
	@Override
	public <K> List<K> $selectListTargetByColumns(K target){
		return dao().$selectListTargetByColumns(target);
	}
	
	/**
	 * 根据字段查询单条
	 * @param columns 字段名，值
	 * @param clazz 返回目标
	 * @return
	 */
	@Override
	public <K> K $selectOneTargetByColumnMap(Map<String, Object> columns, Class<K> target){
		return dao().$selectOneTargetByColumnMap(columns, target);
	}
	
	/**
	 * 根据字段查询多条
	 * @param columns 字段名，值
	 * @param clazz 返回目标
	 * @return
	 */
	@Override
	public <K> List<K> $selectListTargetByColumnMap(Map<String, Object> columns, Class<K> clazz){
		return dao().$selectListTargetByColumnMap(columns, clazz);
	}

	/**
     * 查询单条（SQL）
     * @param sql
     * @return
     */
	@Override
	public T $selectOneByQuery(Query query){
		return dao().$selectOneByQuery(query);
	}
	
	/**
     * 查询多条（SQL）
     * @param sql
     * @return
     */
	@Override
	public List<T> $selectListByQuery(Query query){
		return dao().$selectListByQuery(query);
	}
	
	/**
	 * 分页查询
	 * @param query 查询条件
	 * @return
	 */
	@Override
	public Page<T> $selectPage(Query query){
		return dao().$selectPage(query);
	}

	
	/**
     * 查询单条（SQL）
     * @param sql
     * @return
     */
	@Override
	public <K> K $selectOneTargetByQuery(Query query, Class<K> target){
		return dao().$selectOneTargetByQuery(query, target);
	}
	
	/**
     * 查询多条（SQL）
     * @param sql
     * @return
     */
	@Override
	public <K> List<K> $selectListTargetByQuery(Query query, Class<K> target){
		return dao().$selectListTargetByQuery(query, target);
	}
	
	/**
	 * 分页查询
	 * @param query 查询条件
	 * @param target 目标
	 * @return
	 */
	@Override
	public <K> Page<K> $selectTargetPage(Query query, Class<K> target){
		return dao().$selectTargetPage(query, target);
	}
	
	//--------------------------------------------- SQL ------------------------------------------
	/**
	 * 根据sql新增
	 * @param sql
	 * @return
	 */
	@Override
	public int $insertBySql(SQL sql){
		return dao().$insertBySql(sql);
	}
	/**
	 * 根据sql删除
	 * @param sql
	 * @return
	 */
	@Override
	public int $deleteBySql(SQL sql){
		return dao().$deleteBySql(sql);
	}
	/**
	 * 根据sql更新
	 * @param sql
	 * @return
	 */
	@Override
	public int $updateBySql(SQL sql){
		return dao().$updateBySql(sql);
	}
	/**
     * 根据sql查询单条
     * @param sql
     * @return
     */
	@Override
	public <K> K $selectOneBySql(SQL sql){
		return dao().$selectOneBySql(sql);
	}
	/**
     * 根据sql查询多条
     * @param sql
     * @return
     */
	@Override
	public <K> List<K> $selectListBySql(SQL sql){
		return dao().$selectListBySql(sql);
	}
	
	//--------------------------------------------- XML ------------------------------------------
	@Override
	public <K> int insert(K k){
		return dao().insert(k);
	}
	
	@Override
	public <K> int update(K k){
		return dao().update(k);
	}
	
	@Override
	public int delete(Object _parameter){
		return dao().delete(_parameter);
	}
	
	@Override
	public <K> K selectOne(Query query){
		return dao().selectOne(query);
	}
	
	@Override
	public <K> K selectOne(Map<String, Object> map){
		return dao().selectOne(map);
	}
	
	@Override
	public <K> List<K> selectList(Query query){
		return dao().selectList(query);
	}
	
	@Override
	public <K> List<K> selectList(Map<String, Object> map){
		return dao().selectList(map);
	}
	
	@Override
	public <K> Page<K> selectPage(Query query){
		return selectPage("selectPage", query);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <K> Page<K> selectPage(String mapperId, Query query){
		mapperId = StringUtils.isEmpty(mapperId) ? "selectPage" : mapperId;
		
		//创建分页对象
		Page<K> page = null;
		if(query == null || (page = (Page<K>) query.get(HandlerConsts.$PAGE)) == null){
			page = new Page<>();
		}
		
		return getPage(mapperId, page, (Map<String, Object>) query);
	}
	
	@Override
	public <K> Page<K> selectPage(int pageNo, int pageSize, Map<String, Object> map){
		return selectPage("selectPage", pageNo, pageSize, map);
	}
	
	@Override
	public <K> Page<K> selectPage(String mapperId, int pageNo, int pageSize, Map<String, Object> map){
		mapperId = StringUtils.isEmpty(mapperId) ? "selectPage" : mapperId;
		Page<K> page = new Page<>(pageNo, pageSize);
		return getPage(mapperId, page, map);
	}
	
	private <K> Page<K> getPage(String mapperId, Page<K> page, Map<String, Object> params){
		Class<?> interfaces = dao().getClass().getInterfaces()[0];
		String nameSpace = interfaces.getPackage().getName() + "." + 
				interfaces.getSimpleName() + "." + mapperId;
		
		//查询数据列表
		List<K> data = _template.selectList(nameSpace, params);
		//查询数据数量
		long totalCount = page.getTotalCount();
		//计算总页数
		int pageSize = page.getPageSize();
		int pageTotal = (int) (totalCount / pageSize);
		if (totalCount % pageSize > 0) {
	    	++ pageTotal;
	    }
		//当前页条数
		int number = data == null ? 0 : data.size();
		
		//组装返回实体
		page.setData(data);//返回数据
		page.setPageTotal(pageTotal);//总页数
		page.setNumber(number);//当前页条数
		return page;
	}
}

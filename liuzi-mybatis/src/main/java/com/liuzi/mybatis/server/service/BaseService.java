package com.liuzi.mybatis.server.service;

import java.util.List;
import java.util.Map;

import com.liuzi.mybatis.currency.cond.Query;
import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.server.pojo.Page;


/**
 * 服务基类
 * @author zsy
 */
public interface BaseService<T> {
	
	//--------------------------------------------- INSERT ------------------------------------------
	/**
	 * 新增单条
	 * @param record 实体
	 * @return
	 */
	<K> int $insert(K record);
	
	/**
	 * 批量新增
	 * @param records 实体数组
	 * @return
	 */
	<K> int $insertArray(K[] records);
	
	/**
	 * 批量新增
	 * @param records 实体List
	 * @return
	 */
	<K> int $insertList(List<K> records);
	
	//--------------------------------------------- DELETE ------------------------------------------
	
	/**
	 * 根据主键值删除
	 * @param pkValue 主键值
	 * @return
	 */
	int $deleteByPk(Object pkValue);
	
	/**
	 * 根据主键值删除
	 * @param pkValue 多个主键值
	 * @return
	 */
	int $deleteByPkArray(Object[] pkValues);
	
	/**
	 * 根据主键值删除
	 * @param pkValues 多个主键值
	 * @return
	 */
	int $deleteByPkList(List<Object> pkValues);
	
	/**
	 * 根据字段删除
	 * @param column 字段名称
	 * @param value 字段值
	 * @return
	 */
	int $deleteByColumn(String column, Object value);
	
	/**
	 * 根据字段多值删除
	 * @param column 字段名称
	 * @param values 多个字段值
	 * @return
	 */
	int $deleteByColumnValueArray(String column, Object[] values);
	
	/**
	 * 根据字段多值删除
	 * @param column 字段名称
	 * @param values 多个字段值
	 * @return
	 */
	int $deleteByColumnValueList(String column, List<Object> values);
	
	/**
	 * 根据多字段同值删除
	 * @param columns 字段名称
	 * @param value 多个字段值
	 * @return
	 */
	int $deleteByColumnArray(String[] columns, Object value);
	
	/**
	 * 根据多字段同值删除
	 * @param columns 字段名称
	 * @param value 多个字段值
	 * @return
	 */
	int $deleteByColumnList(List<String> columns, Object value);
	
	/**
	 * 根据多个参数删除
	 * @param columns (字段名key, 字段值value)
	 * @return
	 */
	int $deleteByColumnMap(Map<String, Object> columns);
	
	//--------------------------------------------- UPDATE ------------------------------------------
	/**
	 * 根据主键更新/批量更新
	 * @param records 实体数据
	 * @return
	 */
	<K> int $updateByPk(K record);
	
	/**
	 * 根据字段更新
	 * @param record 实体数据
	 * @param column 字段名
	 * @return
	 */
	<K> int $updateByColumn(K record, String column);
	
	/**
	 * 根据字段更新
	 * @param record 实体数据
	 * @param columns 多个字段名，
	 * @return
	 */
	<K> int $updateByColumnArray(K record, String[] columns);
	
	/**
	 * 根据多项字段更新
	 * @param record 实体数据
	 * @param columns 多个字段名
	 * @return
	 */
	<K> int $updateByColumnList(K record, List<String> columns);
	
	/**
	 * 根据自定义字段值更新
	 * @param record 实体数据
	 * @param columns 字段名key，字段值value
	 * @return
	 */
	<K> int $updateByColumnMap(K record, Map<String, Object> columns);
	
	/**
	 * 根据主键值批量更新
	 * @param records 实体数据
	 * @return
	 */
	<K> int $updateListByPk(List<K> records);
	
	/**
	 * 根据字段批量更新
	 * @param records 实体数据
	 * @param columns 字段名
	 * @return
	 */
	<K> int $updateListByColumn(List<K> records, String column);

	/**
	 * 根据字段批量更新
	 * @param records 实体数据
	 * @param columns 字段名
	 * @return
	 */
	<K> int $updateListByColumnArray(List<K> records, String[] columns);
	
	/**
	 * 根据字段批量更新
	 * @param records 实体数据
	 * @param columns 字段名
	 * @return
	 */
	<K> int $updateListByColumnList(List<K> records, List<String> columns);
	
	//--------------------------------------------- SELECT ------------------------------------------
	/**
	 * 根据主键查询单条
	 * @param pkValue 主键值
	 * @return
	 */
	T $selectOneByPk(Object pkValue);
	
	/**
	 * 根据字段查询单条
	 * @param columns 字段名，值
	 * @return
	 */
	T $selectOneByColumnMap(Map<String, Object> columns);
	
	/**
	 * 根据字段查询多条
	 * @param columns 字段名，值
	 * @return
	 */
	List<T> $selectListByColumnMap(Map<String, Object> columns);
	
	/**
	 * 根据主键查询单条
	 * @param pkValue 主键值
	 * @param clazz 
	 * @return
	 */
	<K> K $selectOneTargetByPk(Object pkValue, Class<K> target);
	
	/**
	 * 根据字段查询单条
	 * @param record 实体数据
	 * @return
	 */
	<K> K $selectOneTargetByColumns(K target);
	
	/**
	 * 根据字段查询多条
	 * @param record 实体数据
	 * @return
	 */
	<K> List<K> $selectListTargetByColumns(K target);
	
	/**
	 * 根据字段查询单条
	 * @param columns 字段名，值
	 * @param clazz 返回目标
	 * @return
	 */
	<K> K $selectOneTargetByColumnMap(Map<String, Object> columns, Class<K> target);
	
	/**
	 * 根据字段查询多条
	 * @param columns 字段名，值
	 * @param clazz 返回目标
	 * @return
	 */
	<K> List<K> $selectListTargetByColumnMap(Map<String, Object> columns, Class<K> clazz);

	/**
     * 查询单条（SQL）
     * @param sql
     * @return
     */
	T $selectOneByQuery(Query query);
	
	/**
     * 查询多条（SQL）
     * @param sql
     * @return
     */
	List<T> $selectListByQuery(Query query);
	
	/**
	 * 分页查询
	 * @param query 查询条件
	 * @return
	 */
	Page<T> $selectPage(Query query);

	
	/**
     * 查询单条（SQL）
     * @param sql
     * @return
     */
	<K> K $selectOneTargetByQuery(Query query, Class<K> target);
	
	/**
     * 查询多条（SQL）
     * @param sql
     * @return
     */
	<K> List<K> $selectListTargetByQuery(Query query, Class<K> target);
	
	/**
	 * 分页查询
	 * @param query 查询条件
	 * @param target 目标
	 * @return
	 */
	<K> Page<K> $selectTargetPage(Query query, Class<K> target);
	
	//--------------------------------------------- SQL ------------------------------------------
	/**
	 * 根据sql新增
	 * @param sql
	 * @return
	 */
	int $insertBySql(SQL sql);	
	/**
	 * 根据sql删除
	 * @param sql
	 * @return
	 */
	int $deleteBySql(SQL sql);
	/**
	 * 根据sql更新
	 * @param sql
	 * @return
	 */
	int $updateBySql(SQL sql);
	/**
     * 根据sql查询单条
     * @param sql
     * @return
     */
	<K> K $selectOneBySql(SQL sql);
	/**
     * 根据sql查询多条
     * @param sql
     * @return
     */
	<K> List<K> $selectListBySql(SQL sql);
	
	//--------------------------------------------- XML ------------------------------------------
	/**
	 * 新增
	 * @param k 实体对象
	 * @return
	 */
	<K> int insert(K k);
	
	/**
	 * 更新
	 * @param k 实体对象
	 * @return
	 */
	<K> int update(K k);
	
	/**
	 * 删除
	 * @param _parameter 参数
	 * @return
	 */
	int delete(Object _parameter);
	
	/**
	 * 查询单条
	 * @param map 参数
	 * @return
	 */
	<K> K selectOne(Map<String, Object> map);
	
	/**
	 * 查询单条
	 * @param query 参数
	 * @return
	 */
	<K> K selectOne(Query query);
	
	/**
	 * 查询多条
	 * @param map 参数
	 * @return
	 */
	<K> List<K> selectList(Map<String, Object> map);
	
	/**
	 * 查询多条
	 * @param query 参数
	 * @return
	 */
	<K> List<K> selectList(Query query);
	
	/**
	 * 分页（xml-id默认selectList）
	 * @param query 查询条件（Map形式）
	 * @return
	 */
	<K> Page<K> selectPage(Query query);
	
	/**
	 * 分页 
	 * @param mapperId xml-id（默认selectList）
	 * @param query 查询条件（Map形式）
	 * @return
	 */
	<K> Page<K> selectPage(String mapperId, Query query);
	
	/**
	 * 分页（xml-id默认selectList）
	 * @param query 查询条件（Map形式）
	 * @return
	 */
	<K> Page<K> selectPage(int pageNo, int pageSize, Map<String, Object> map);
	
	/**
	 * 分页 
	 * @param mapperId xml-id（默认selectList）
	 * @param query 查询条件（Map形式）
	 * @return
	 */
	<K> Page<K> selectPage(String mapperId, int pageNo, int pageSize, Map<String, Object> map);
}

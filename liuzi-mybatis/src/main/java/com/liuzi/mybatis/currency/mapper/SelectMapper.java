package com.liuzi.mybatis.currency.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.liuzi.mybatis.currency.cond.Query;
import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.currency.provider.BaseSqlProvider;
import com.liuzi.mybatis.currency.provider.SelectSqlProvider;
import com.liuzi.mybatis.server.pojo.Page;




/**
 * 基础查询接口
 * @author zsy
 */
public interface SelectMapper<T> {
	
	/**
	 * 根据主键查询单条
	 * @param pkValue 主键值
	 * @return
	 */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectOneByPk")
	T $selectOneByPk(@Param("pkValue") Object pkValue);
	
	/**
	 * 根据字段查询单条
	 * @param columns 字段名，值
	 * @return
	 */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectOneByColumnMap")
	T $selectOneByColumnMap(@Param("columns") Map<String, Object> columns);
	
	/**
	 * 根据字段查询多条
	 * @param columns 字段名，值
	 * @return
	 */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectListByColumnMap")
	List<T> $selectListByColumnMap(@Param("columns") Map<String, Object> columns);
	
	/**
	 * 根据主键查询单条
	 * @param pkValue 主键值
	 * @param clazz 
	 * @return
	 */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectOneTargetByPk")
	<K> K $selectOneTargetByPk(@Param("pkValue") Object pkValue, @Param("target") Class<K> target);
	
	/**
	 * 根据字段查询单条
	 * @param record 实体数据
	 * @return
	 */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectOneTargetByColumns")
	<K> K $selectOneTargetByColumns(@Param("target") K target);
	
	/**
	 * 根据字段查询多条
	 * @param record 实体数据
	 * @return
	 */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectListTargetByColumns")
	<K> List<K> $selectListTargetByColumns(@Param("target") K target);
	
	/**
	 * 根据字段查询单条
	 * @param columns 字段名，值
	 * @param clazz 返回目标
	 * @return
	 */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectOneTargetByColumnMap")
	<K> K $selectOneTargetByColumnMap(@Param("columns") Map<String, Object> columns, @Param("target") Class<K> target);
	
	/**
	 * 根据字段查询多条
	 * @param columns 字段名，值
	 * @param clazz 返回目标
	 * @return
	 */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectListTargetByColumnMap")
	<K> List<K> $selectListTargetByColumnMap(@Param("columns") Map<String, Object> columns, @Param("target") Class<K> clazz);

	//--------------------------------- SQL -----------------------------------------------
	
	/**
     * 查询单条（SQL）
     * @param sql
     * @return
     */
	@SelectProvider(type = BaseSqlProvider.class, method = "$commonBySql")
	<K> K $selectOneBySql(@Param("sql") SQL sql);
	/**
     * 查询多条（SQL）
     * @param sql
     * @return
     */
	@SelectProvider(type = BaseSqlProvider.class, method = "$commonBySql")
	<K> List<K> $selectListBySql(@Param("sql") SQL sql);
	/**
     * 查询Map结构（SQL）
     * @param sql
     * @return
     */
	@SelectProvider(type = BaseSqlProvider.class, method = "$commonBySql")
	<K, V> Map<K, V> $selectMapBySql(@Param("sql") SQL sql);
	
	//--------------------------------- Query -----------------------------------------------
	
	/**
     * 查询单条（SQL）
     * @param sql
     * @return
     */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectByQuery")
	T $selectOneByQuery(@Param("query") Query query);
	
	/**
     * 查询多条（SQL）
     * @param sql
     * @return
     */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectByQuery")
	List<T> $selectListByQuery(@Param("query") Query query);
	
	/**
	 * 分页查询
	 * @param query 查询条件
	 * @return
	 */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectByQuery")
	Page<T> $selectPage(@Param("query") Query query);

	
	/**
     * 查询单条（SQL）
     * @param sql
     * @return
     */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectByQuery")
	<K> K $selectOneTargetByQuery(@Param("query") Query query, @Param("target") Class<K> target);
	
	/**
     * 查询多条（SQL）
     * @param sql
     * @return
     */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectByQuery")
	<K> List<K> $selectListTargetByQuery(@Param("query") Query query, @Param("target") Class<K> target);
	
	/**
	 * 分页查询
	 * @param query 查询条件
	 * @param target 目标
	 * @return
	 */
	@SelectProvider(type = SelectSqlProvider.class, method = "$selectByQuery")
	<K> Page<K> $selectTargetPage(@Param("query") Query query, @Param("target") Class<K> target);
}

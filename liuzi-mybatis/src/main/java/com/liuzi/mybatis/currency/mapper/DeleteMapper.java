package com.liuzi.mybatis.currency.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;

import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.currency.provider.BaseSqlProvider;
import com.liuzi.mybatis.currency.provider.DeleteSqlProvider;




/**
 * 基础DAO
 * @author zsy
 */
public interface DeleteMapper<T> extends UpdateMapper<T>{
	
	/**
	 * 根据主键值删除
	 * @param pkValue 主键值
	 * @return
	 */
	@DeleteProvider(type = DeleteSqlProvider.class, method = "$deleteByPk")
	int $deleteByPk(@Param("pkValue") Object pkValue);
	
	/**
	 * 根据主键值删除
	 * @param pkValue 多个主键值
	 * @return
	 */
	@DeleteProvider(type = DeleteSqlProvider.class, method = "$deleteByPkArray")
	int $deleteByPkArray(@Param("pkValues") Object[] pkValues);
	
	/**
	 * 根据主键值删除
	 * @param pkValues 多个主键值
	 * @return
	 */
	@DeleteProvider(type = DeleteSqlProvider.class, method = "$deleteByPkList")
	int $deleteByPkList(@Param("pkValues") List<Object> pkValues);
	
	/**
	 * 根据字段删除
	 * @param column 字段名称
	 * @param value 字段值
	 * @return
	 */
	@DeleteProvider(type = DeleteSqlProvider.class, method = "$deleteByColumn")
	int $deleteByColumn(@Param("column") String column, @Param("value") Object value);
	
	/**
	 * 根据字段多值删除
	 * @param column 字段名称
	 * @param values 多个字段值
	 * @return
	 */
	@DeleteProvider(type = DeleteSqlProvider.class, method = "$deleteByColumnValueArray")
	int $deleteByColumnValueArray(@Param("column") String column, @Param("values") Object[] values);
	
	/**
	 * 根据字段多值删除
	 * @param column 字段名称
	 * @param values 多个字段值
	 * @return
	 */
	@DeleteProvider(type = DeleteSqlProvider.class, method = "$deleteByColumnValueList")
	int $deleteByColumnValueList(@Param("column") String column, @Param("values") List<Object> values);
	
	/**
	 * 根据多字段同值删除
	 * @param columns 字段名称
	 * @param value 多个字段值
	 * @return
	 */
	@DeleteProvider(type = DeleteSqlProvider.class, method = "$deleteByColumnArray")
	int $deleteByColumnArray(@Param("columns") String[] columns, @Param("value") Object value);
	
	/**
	 * 根据多字段同值删除
	 * @param columns 字段名称
	 * @param value 多个字段值
	 * @return
	 */
	@DeleteProvider(type = DeleteSqlProvider.class, method = "$deleteByColumnList")
	int $deleteByColumnList(@Param("columns") List<String> columns, @Param("value") Object value);
	
	/**
	 * 根据多个参数删除
	 * @param columns (字段名key, 字段值value)
	 * @return
	 */
	@DeleteProvider(type = DeleteSqlProvider.class, method = "$deleteByColumnMap")
	int $deleteByColumnMap(@Param("columns") Map<String, Object> columns);
	
	
	//--------------------------------- SQL -----------------------------------------------
	
	@DeleteProvider(type = BaseSqlProvider.class, method = "$commonBySql")
	int $insertBySql(@Param("sql") SQL sql);
}

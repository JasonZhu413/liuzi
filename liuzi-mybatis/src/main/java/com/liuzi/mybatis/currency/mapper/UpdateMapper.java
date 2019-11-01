package com.liuzi.mybatis.currency.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.currency.provider.BaseSqlProvider;
import com.liuzi.mybatis.currency.provider.UpdateSqlProvider;




/**
 * 基础更新接口
 * @author zsy
 */
public interface UpdateMapper<T> extends SelectMapper<T>{
	
	/**
	 * 根据主键更新/批量更新
	 * @param records 实体数据
	 * @return
	 */
	@UpdateProvider(type = UpdateSqlProvider.class, method = "$updateByPk")
	<K> int $updateByPk(@Param("record") K record);
	
	/**
	 * 根据字段更新
	 * @param record 实体数据
	 * @param column 字段名
	 * @return
	 */
	@UpdateProvider(type = UpdateSqlProvider.class, method = "$updateByColumn")
	<K> int $updateByColumn(@Param("record") K record, @Param("column") String column);
	
	/**
	 * 根据字段更新
	 * @param record 实体数据
	 * @param columns 多个字段名，
	 * @return
	 */
	@UpdateProvider(type = UpdateSqlProvider.class, method = "$updateByColumnArray")
	<K> int $updateByColumnArray(@Param("record") K record, @Param("columns") String[] columns);
	
	/**
	 * 根据多项字段更新
	 * @param record 实体数据
	 * @param columns 多个字段名
	 * @return
	 */
	@UpdateProvider(type = UpdateSqlProvider.class, method = "$updateByColumnList")
	<K> int $updateByColumnList(@Param("record") K record, @Param("columns") List<String> columns);
	
	/**
	 * 根据自定义字段值更新
	 * @param record 实体数据
	 * @param columns 字段名key，字段值value
	 * @return
	 */
	@UpdateProvider(type = UpdateSqlProvider.class, method = "$updateByColumnMap")
	<K> int $updateByColumnMap(@Param("record") K record, @Param("columns") Map<String, Object> columns);
	
	/**
	 * 根据主键值批量更新
	 * @param records 实体数据
	 * @return
	 */
	@UpdateProvider(type = UpdateSqlProvider.class, method = "$updateListByPk")
	<K> int $updateListByPk(@Param("records") List<K> records);
	
	/**
	 * 根据字段批量更新
	 * @param records 实体数据
	 * @param columns 字段名
	 * @return
	 */
	@UpdateProvider(type = UpdateSqlProvider.class, method = "$updateListByColumn")
	<K> int $updateListByColumn(@Param("records") List<K> records, @Param("column") String column);

	/**
	 * 根据字段批量更新
	 * @param records 实体数据
	 * @param columns 字段名
	 * @return
	 */
	@UpdateProvider(type = UpdateSqlProvider.class, method = "$updateListByColumnArray")
	<K> int $updateListByColumnArray(@Param("records") List<K> records, @Param("columns") String[] columns);
	
	/**
	 * 根据字段批量更新
	 * @param records 实体数据
	 * @param columns 字段名
	 * @return
	 */
	@UpdateProvider(type = UpdateSqlProvider.class, method = "$updateListByColumnList")
	<K> int $updateListByColumnList(@Param("records") List<K> records, @Param("columns") List<String> columns);


	
	//--------------------------------- SQL -----------------------------------------------
	
	@UpdateProvider(type = BaseSqlProvider.class, method = "$commonBySql")
	int $updateBySql(@Param("sql") SQL sql);
}

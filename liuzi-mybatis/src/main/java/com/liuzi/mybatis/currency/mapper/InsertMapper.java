package com.liuzi.mybatis.currency.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.currency.provider.BaseSqlProvider;
import com.liuzi.mybatis.currency.provider.InsertSqlProvider;




/**
 * 基础新增接口
 * @author zsy
 */
public interface InsertMapper<T> extends DeleteMapper<T>{
	
	/**
	 * 新增单条
	 * @param record 实体
	 * @return
	 */
	@InsertProvider(type = InsertSqlProvider.class, method = "$insert")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	<K> int $insert(@Param("record") K record);
	
	/**
	 * 批量新增
	 * @param records 实体数组
	 * @return
	 */
	@InsertProvider(type = InsertSqlProvider.class, method = "$insertArray")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	<K> int $insertArray(@Param("records") K[] records);
	
	/**
	 * 批量新增
	 * @param records 实体List
	 * @return
	 */
	@InsertProvider(type = InsertSqlProvider.class, method = "$insertList")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	<K> int $insertList(@Param("records") List<K> records);
	
	//--------------------------------- SQL -----------------------------------------------
	
	@InsertProvider(type = BaseSqlProvider.class, method = "$commonBySql")
	int $insertBySql(@Param("sql") SQL sql);
}

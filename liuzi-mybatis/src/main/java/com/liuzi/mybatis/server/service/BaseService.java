package com.liuzi.mybatis.server.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.liuzi.mybatis.currency.cond.Query;
import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.currency.provider.SelectSqlProvider;
import com.liuzi.mybatis.server.pojo.Page;


/**
 * 服务基类
 * @author zsy
 */
public interface BaseService<T> {
	
	<K> int $insert(K record);
	
	<K> int $updateByPk(K record);
	
	int $deleteByPk(Object pkValue);
	
	/**
	 * 根据主键查询单条
	 * @param pkValue 主键值
	 * @return
	 */
	T $selectOneByPk(Object pkValue);
	
	T $selectOneBySql(SQL sql);
	
	T $selectOneByQuery(Query query);
	
	
	<K> List<K> selectList(Query query);
	
	/**
	 * 分页
	 * @param query
	 * @return
	 */
	Page<T> $selectPage(Query query);
	
	/**
	 * 分页
	 * @param mapperId
	 * @param query
	 * @return
	 */
	<K> Page<K> selectPage(Query query);
	
	/**
	 * 分页
	 * @param mapperId
	 * @param query
	 * @return
	 */
	<K> Page<K> selectPage(String mapperId, Query query);
}

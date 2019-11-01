package com.liuzi.mybatis.server.mapper;

import java.util.List;
import java.util.Map;

import com.liuzi.mybatis.currency.cond.Query;
import com.liuzi.mybatis.currency.mapper.InsertMapper;




/**
 * 基础DAO
 * @author zsy
 */
public interface BaseDao<T> extends InsertMapper<T>{
	
	/**
	 * 更新（需XML中增加sql，默认mapperId为update）
	 * @param map 参数
	 * @return
	 */
	int update(Map<String, Object> map);
	
	/**
	 * 新增（需XML中增加sql，默认mapperId为insert）
	 * @param map 参数
	 * @return
	 */
	int insert(Map<String, Object> map);
	
	/**
	 * 删除（需XML中增加sql，默认mapperId为delete）
	 * @param map 参数
	 * @return
	 */
	int delete(Map<String, Object> map);
	
	/**
	 * 查询单条（需XML中增加sql，默认mapperId为selectOne）
	 * @param map 参数
	 * @return
	 */
	<K> K selectOne(Map<String, Object> map);
	
	/**
	 * 查询单条（需XML中增加sql，默认mapperId为selectOne）
	 * @param query 参数
	 * @return
	 */
	<K> K selectOne(Query query);
	
	/**
	 * 查询多条（需XML中增加sql，默认mapperId为selectList）
	 * @param map 参数
	 * @return
	 */
	<K> List<K> selectList(Map<String, Object> map);
	
	/**
	 * 查询多条（需XML中增加sql，默认mapperId为selectList）
	 * @param query 参数
	 * @return
	 */
	<K> List<K> selectList(Query query);
}

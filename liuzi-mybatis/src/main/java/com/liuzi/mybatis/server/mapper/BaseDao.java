package com.liuzi.mybatis.server.mapper;

import java.util.List;
import java.util.Map;

import com.liuzi.mybatis.currency.mapper.InsertMapper;




/**
 * 基础DAO
 * @author zsy
 */
public interface BaseDao<T> extends InsertMapper<T>{
	
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
	 * 查询多条（需XML中增加sql，默认mapperId为selectList）
	 * @param map 参数
	 * @return
	 */
	<K> List<K> selectList(Map<String, Object> map);
}

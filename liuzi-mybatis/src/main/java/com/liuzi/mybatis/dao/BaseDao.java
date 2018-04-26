package com.liuzi.mybatis.dao;

import java.util.List;
import java.util.Map;




public abstract interface BaseDao<T> {
	
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	public abstract T selectByPrimaryKey(Long id);
	
	/**
	 * 根据主键删除
	 * @param key
	 * @return
	 */
	public abstract int deleteByPrimaryKey(Long id);
	
	/**
	 * 新增
	 * @param record
	 * @return
	 */
	public abstract int insert(T record);

	/**
	 * 新增
	 * @param record
	 * @return
	 */
	public abstract int insertSelective(T record);
	
	/**
	 * 更新
	 * @param record
	 * @return
	 */
	public abstract int updateByPrimaryKeySelective(T record);
	
	/**
	 * 更新
	 * @param record
	 * @return
	 */
	public abstract int updateByPrimaryKey(T record);

	/**
	 * 查询列表（非排序）
	 * @param map 
	 * @return
	 */
	public abstract List<T> select(Map<String, Object> map);
	
	/**
	 * 查询数量
	 * @param map
	 * @return
	 */
	public abstract int selectCount(Map<String, Object> map);

	/**
	 * 批量新增
	 * @param record
	 * @return
	 */
	public abstract int insertList(List<T> record);
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public abstract int deleteByListKey(List<Long> ids);
}

package com.liuzi.mybatis.dao;

import java.util.List;
import java.util.Map;




public abstract interface BaseVoDao<T, Vo> extends BaseDao<T>{
	
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	public abstract Vo selectVoByPrimaryKey(Long id);
	
	/**
	 * 查询列表（非排序）
	 * @param map 
	 * @return
	 */
	public abstract List<Vo> selectVo(Map<String, Object> map);
	
	/**
	 * 查询数量
	 * @param map
	 * @return
	 */
	public abstract int selectCountVo(Map<String, Object> map);
	
	
}

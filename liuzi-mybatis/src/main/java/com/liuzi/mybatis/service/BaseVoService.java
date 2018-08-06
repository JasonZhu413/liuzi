package com.liuzi.mybatis.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.liuzi.mybatis.pojo.Page;



/**
 * 服务基类
 * @author zsy
 *
 * @param <T>
 * 
 * 说明（mybatis参数）：
 *  1.所有orm调用sql id为方法名
 * 	2.多参排序 map参数为sort_list xml中循环取key、val（若为非排序状态，默认为id倒序，无id则无需写排序）
 *  3.分页参数为 limit, offset
 *  4.若写分页，则xml中必须写selectCount
 *  5.若为单参，调用出错可使用_parameter接参调试
 */
public interface BaseVoService<T, Vo> extends BaseService<T>{
	
	/**
	 * 条件查询列表
	 * @param map 
	 * @return
	 */
	public List<Vo> selectVo(Map<String, Object> map);
	
	/**
	 * 条件查询列表（单参排序）
	 * @param map 
	 * @param sort 排序字段
	 * @param order asc（升序）/desc（降序）
	 * @return
	 */
	public List<Vo> selectVo(Map<String, Object> map, String sort, String order);
	
	/**
	 * 条件查询列表（多参排序）
	 * @param map 
	 * @param sort 排序 例：map.put("字段","asc/desc");
	 * @return
	 */
	public List<Vo> selectVo(Map<String, Object> map, LinkedHashMap<String, Object> sort);
	
	/**
	 * 条件查询数量
	 * @param map
	 * @return
	 */
	public int selectCountVo(Map<String, Object> map);

	/**
	 * 根据主键查询实体
	 * @param id
	 * @return
	 */
	public Vo selectVoByPrimaryKey(Long id);
	
	/**
	 * 条件分页查询（非排序）
	 * @param map 参数
	 * @param pageNo 页码
	 * @param pageSize 每页条数
	 * @return
	 */
	public Page<Vo> selectVo(Map<String, Object> map, Integer pageNo, 
			Integer pageSize);
	
	/**
	 * 条件分页查询（单参排序）
	 * @param map 参数
	 * @param pageNo 页码
	 * @param pageSize 每页条数
	 * @param sort 排序字段
	 * @param order asc（升序）/desc（降序）
	 * @return
	 */
	public Page<Vo> selectVo(Map<String, Object> map, Integer pageNo, 
			Integer pageSize, String sort, String order);
	
	/**
	 * 条件分页查询（多参排序）
	 * @param map 参数
	 * @param pageNo 页码
	 * @param pageSize 每页条数
	 * @param sort 排序 例：map.put("字段","asc/desc");
	 * @return
	 */
	public Page<Vo> selectVo(Map<String, Object> map, Integer pageNo, 
			Integer pageSize, LinkedHashMap<String, Object> sort);
}

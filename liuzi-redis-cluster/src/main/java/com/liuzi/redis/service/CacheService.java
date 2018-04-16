package com.liuzi.redis.service;

import java.util.List;


/**
 * 缓存业务
 * 根据代码值查询代码,
 * 优先从缓存获取，缓存获取不到 从数据库获取
 * 从数据库获取后 把存储的数据存储到缓存里
 * @author zhushiyao
 *
 */
public interface CacheService {
	
	/**
	 * 存储一对key-value
	 * Set the string value as value of the key
	 * The string can't be longer than 1073741824 bytes (1 GB). 
	 * @param key
	 * @param value
	 * @param  seconds 数据存储时间 单位秒
	 */
	public void save(String key,String value,Integer seconds);
	
	/**
	 * 存储一个列表,如果key对应的列表已经存在，则追加，如果不存在，存储
	 * @param key
	 * @param value 对象数组 此对象为非列表数组型对象
	 * @param  seconds 数据存储时间 单位秒
	 * @return //Integer reply, specifically, the number of elements inside the list after the push operation
	 */
	public Long saveArray(String key,List<Object> value,Integer seconds);
	
	
	/**
	 * 通过key获取一个字符串值
	 * @param key
	 * @return
	 */
	public String get(String key);
	
	/**
	 * 通过key获取指定区间的列表
	 * @param key
	 * @param start 区间起始位置 0开始
	 * @param end 区间结束位置
	 * @return
	 */
	public List<String> getArray(String key,Long start,Long end);
	
	/**
	 * 根据key删除
	 * @param key
	 */
	public void delete(String key);
	
	
	public Object getEntry(Object key,Class<?> c);
}

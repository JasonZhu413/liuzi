package com.liuzi.redis.service;

import java.util.List;
import java.util.Map;
import java.util.Set;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface RedisService {

	/**
	 * 是否存在
	 * @param key
	 * @return
	 */
	public boolean exists(String key);
	/**
	 * 删除
	 * @param key
	 */
	public void delete(String key);
	/**
	 * 删除
	 * @param key
	 */
	public void delete(String... key);

	/**
	 * String
	 */
	/**
	 * 给数据库中名称为key的string赋予值value
	 * @param key
	 * @param value
	 */
	public void save(String key, String value);
	/**
	 * 给数据库中名称为key的string赋予值value,并赋予缓存时间seconds
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public void save(String key, String value, Integer seconds);
	/**
	 * 返回数据库中名称为key的string的value
	 * @param key
	 * @return
	 */
	public String get(String key);
	/**
	 * 给名称为key的string赋予上一次的value
	 * @param key
	 * @param value
	 * @return
	 */
	public String saveLastValue(String key, String value);
	/**
	 * 如果不存在名称为key的string，则向库中添加string，名称为key，值为value
	 * @param key
	 * @param value
	 * @return
	 */
	public Long saveIfNotExit(String key, String value);
	/**
	 * 给数据库中名称为key的string赋予值obj,并赋予缓存时间seconds
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public void save(String key, Object obj, Integer seconds);
	
	/**
	 * 给数据库中名称为key的string赋予值obj
	 * @param key
	 * @param obj
	 */
	public void save(String key, Object obj);
	/**
	 * 名称为key的string增1操作
	 * @param key
	 * @return
	 */
	public Long incr(String key);
	/**
	 * 名称为key的string增加integer
	 * @param key
	 * @param integer
	 * @return
	 */
	public Long incrBy(String key, int integer);
	/**
	 * 名称为key的string减1操作
	 * @param key
	 * @return
	 */
	public Long decr(String key);
	/**
	 * 名称为key的string减少integer
	 * @param key
	 * @param val
	 * @return
	 */
	public Long decrBy(String key, long val);
	/**
	 * 名称为key的string的值附加value
	 * @param key
	 * @param value
	 * @return
	 */
	public Long append(String key, String value);
	/**
	 * 返回名称为key的string的value的子串
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public String substr(String key, int start, int end);
	
	/**
	 * List
	 */
	/**
	 * 返回名称为key的list的长度
	 * @param key
	 * @return
	 */
	public Long llen(String key);
	/**
	 * 返回名称为key的list中index位置的元素
	 * @param key
	 * @param index
	 * @return
	 */
	public String lindex(String key, int index);
	/**
	 * 给名称为key的list中index位置的元素赋值为value
	 * @param key
	 * @param index
	 * @param value
	 * @return
	 */
	public String lset(String key, int index, String value);
	/**
	 * 返回并删除名称为key的list中的首元素
	 * @param key
	 * @return
	 */
	public String lpop(String key);
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String rpop(String key);
	/**
	 * 返回并删除名称为key的list中的尾元素
	 * @param srckey
	 * @param dstkey
	 * @return
	 */
	public String rpoplpush(String srckey, String dstkey);
	/**
	 * 在名称为key的list尾添加一个值为value的元素
	 * @param key
	 * @param value
	 * @return
	 */
	public Long saveArray(String key, String value);
	/**
	 * 在名称为key的list尾添加一个值为value的元素,并赋值时间seconds
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public Long saveArray(String key, String value, Integer seconds);
	/**
	 * 保存
	 * @param key
	 * @param value
	 * @return
	 */
	public Long saveArray(String key, String[] value);
	/**
	 * 保存并赋时间
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public Long saveArray(String key, String[] value, Integer seconds);
	/**
	 * 保存
	 * @param key
	 * @param list
	 * @return
	 */
	public Long saveArray(String key, List<?> list);
	/**
	 * 保存并赋时间
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public Long saveArray(String key,List<?> value,Integer seconds);
	/**
	 * 返回名称为key的list中start至end之间的元素（下标从0开始，下同）
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> getArray(String key, Long start, Long end);
	/**
	 * 在名称为key的list头添加一个值为value的 元素
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lpush(String key, String value);
	/**
	 * 删除count个名称为key的list中值为value的元素。count为0，删除所有值为value的元素，count>0      从头至尾删除count个值为value的元素，count<0从尾到头删除|count|个值为value的元素。
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 */
	public Long lrem(String key, long count, String value);
	
	
	/**
	 * Hash
	 */
	/**
	 * 是否存在
	 * @param key
	 * @param field
	 * @return
	 */
	public Boolean hexists(String key, String field);
	/**
	 * 保存
	 * @param key
	 * @param map
	 */
	public void save(String key, Map<String, String> map);
	/**
	 * 向名称为key的hash中添加元素field<—>value
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public Long hset(String key, String field, String value);
	/**
	 * 返回名称为key的hash中field对应的value
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key, String field);
	/**
	 * 返回名称为key的hash中元素个数
	 * @param key
	 * @return
	 */
	public Long hlen(String key);
	/**
	 * 返回名称为key的hash中所有键
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key);
	/**
	 * 返回名称为key的hash中所有键对应的value
	 * @param key
	 * @return
	 */
	public List<String> hvals(String key);
	/**
	 * 返回名称为key的hash中所有的键（field）及其对应的value
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetall(String key);
	/**
	 * 将名称为key的hash中field的value增加integer
	 */
	public Long hincrBy(String key, String field, long val);
	/**
	 * 删除名称为key的hash中键为field的域
	 * @param key
	 * @param field
	 * @return
	 */
	public Long hdel(String key, String field);
	
	/**
	 * JSON
	 */
	/**
	 * 获取json
	 * @param key
	 * @return
	 */
	public JSONObject getJson(String key);
	/**
	 * 返回定长jsonaray
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public JSONArray getJSONArray(String key, Long start, Long end);
}

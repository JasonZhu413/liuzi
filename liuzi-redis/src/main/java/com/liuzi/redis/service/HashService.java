package com.liuzi.redis.service;


import java.util.Map;
import java.util.concurrent.TimeUnit;


public interface HashService{
	
    /**
     * hash get获取
     * @param key 键
     * @param item 项
     * @return T
     */
    <T> T hGet(String key, String item);

	/**
     * hash get获取所有
     * @param key 键
     * @return Map
     */
	Map<Object, Object> hGet(String key);

    /**
     * hash set设置多个
     * @param key 键
     * @param map 多个键值(项, 值)
     * @return true成功 false失败
     */
	boolean hSet(String key, Map<String, Object> map);

    /**
     * hash set time设置多个并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    boolean hSet(String key, Map<String, Object> map, long time);
    
    /**
     * hash set time设置多个并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return true成功 false失败
     */
    boolean hSet(String key, Map<String, Object> map, long time, TimeUnit timeUnit);

    /**
     * hash set设置
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true成功 false失败
     */
    boolean hSet(String key, String item, Object value);

    /**
     * hash set time设置
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒)
     * @return true成功  false失败
     */
    boolean hSet(String key, String item, Object value, long time);
    
    /**
     * hash set time设置
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return true成功  false失败
     */
    boolean hSet(String key, String item, Object value, long time, TimeUnit timeUnit);

    /**
     * hash delete删除
     * @param key 键
     * @param item 项(可以多个)
     */
    void hDel(String key, Object... item);

    /**
     * hash hasKey判断是否有该项的值
     * @param key 键
     * @param item 项
     * @return true存在 false不存在
     */
    boolean hHasKey(String key, String item);
    
    /**
     * hash increment delta递增
     * @param key 键
     * @param item 项
     * @param delta 递增因子(大于0)
     * @return
     */
    double hIncr(String key, String item, long delta);

    /**
     * hash increment delta递增
     * @param key 键
     * @param item 项
     * @param delta 递增因子(大于0)
     * @return
     */
    double hIncr(String key, String item, double delta);

    /**
     * hash decr delta递减
     * @param key 键
     * @param item 项
     * @param delta 递增因子(大于0)
     * @return
     */
    double hDecr(String key, String item, double delta);
    
    /**
     * hash decr delta递减
     * @param key 键
     * @param item 项
     * @param delta 递增因子(大于0)
     * @return
     */
    double hDecr(String key, String item, long delta);
}

package com.liuzi.redis.service;


import java.util.concurrent.TimeUnit;


public interface StringService extends SetService{
	
    /**
     * string get获取
     * @param key 键
     * @return 值
     */
    Object get(String key);

    /**
     * string set放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    boolean set(String key, Object value);

    /**
     * string set time放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) 若小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    boolean set(String key, Object value, long time);
    
    /**
     * string set time放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) 若小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    boolean set(String key, Object value, long time, TimeUnit timeUnit);
    
    /**
     * string increment递增
     * @param key 键
     * @return
     */
    long incr(String key);

    /**
     * string increment delta递增
     * @param key 键
     * @param delta 递增因子(大于0)
     * @return
     */
    long incr(String key, long delta);
    
    /**
     * string decrement递减
     * @param key 键
     * @return
     */
    long decr(String key);

    /**
     * string decrement delta递减
     * @param key 键
     * @param delta 递增因子(大于0)
     * @return
     */
    long decr(String key, long delta);

}

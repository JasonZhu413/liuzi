package com.liuzi.redis.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;


public interface SetService extends LockService{
	
    /**
     * set members获取Set中的所有值
     * @param key 键
     * @return
     */
    Set<Object> sMem(String key);

    /**
     * set isMember根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    boolean sIsMember(String key, Object value);

    /**
     * set add将数据放入set缓存
     * @param key 键
     * @param values 值（可多个）
     * @return 成功个数
     */
    long sAdd(String key, Object... values);

    /**
     * set add数据放入缓存,并设置整个Set时间
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    long sAdd(String key, long time, Object... values);
    
    /**
     * set add数据放入缓存,并设置整个Set时间
     * @param key 键
     * @param time 时间
     * @param timeUnit 时间单位
     * @param values 值 可以是多个
     * @return 成功个数
     */
    long sAdd(String key, long time, TimeUnit timeUnit, Object... values);

    /**
     * set size获取set缓存的长度
     * @param key 键
     * @return
     */
    long sSize(String key);

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    long sRemove(String key, Object... values);
}

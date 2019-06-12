package com.liuzi.redis.service;

import java.util.List;
import java.util.concurrent.TimeUnit;




public interface ListService extends HashService{
	
	/**
     * list range获取list所有值
     * @param key 键
     * @return
     */
    <T> List<T> lRange(String key);

    /**
     * list range获取list指定区间(start=0,end=-1代表所有值)
     * @param key 键
     * @param start 开始
     * @param end 结束
     * @return
     */
    List<Object> lRange(String key, long start, long end);
    
    /**
     * list size获取list长度
     * @param key 键
     * @return
     */
    long lSize(String key);

    /**
     * list index通过索引 获取list中的值
     * @param key 键
     * @param index 索引,若index<0，从list尾部取
     * @return
     */
    Object lIndex(String key, long index);

    /**
     * list lPush从list头部插入数据
     * @param key 键
     * @param value 值
     * @return
     */
    boolean lPush(String key, Object value);

    /**
     * list lPush time从list头部插入数据并设置整个list时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    boolean lPush(String key, Object value, long time);
    
    /**
     * list lPush time从list头部插入数据并设置整个list时间
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return
     */
    boolean lPush(String key, Object value, long time, TimeUnit timeUnit);

    /**
     * list lPush从list头部插入所有数据
     * @param key 键
     * @param value 值
     * @return
     */
    <T> boolean lPush(String key, List<T> value);

    /**
     * list lPush time从list头部插入所有数据并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    boolean lPush(String key, List<Object> value, long time);
    
    /**
     * list lPush time从list头部插入所有数据并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param time 时间单位
     * @return
     */
    boolean lPush(String key, List<Object> value, long time, TimeUnit timeUnit);

    /**
     * list lPush根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    boolean lPush(String key, long index, Object value);
    
    /**
     * list lPop取头部值并删除
     * @param key 键
     * @return
     */
    Object lPop(String key);
    
    /**
     * list lPop取头部值并删除，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param key 键
     * @param timeout 超时时间(秒)
     * @return
     */
    Object lPop(String key, long timeout);

    /**
     * list lPop取头部值并删除，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param key 键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return
     */
    Object lPop(String key, long timeout, TimeUnit unit);
    
    /**
     * list rPush从list尾部插入数据
     * @param key 键
     * @param value 值
     * @return
     */
    boolean rPush(String key, Object value);

    /**
     * list rPush从list尾部插入数据，并设置整个list时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    boolean rPush(String key, Object value, long time);
    
    /**
     * list rPush从list尾部插入数据，并设置整个list时间
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return
     */
    boolean rPush(String key, Object value, long time, TimeUnit timeUnit);

    /**
     * list rPush从list尾部插入所有数据
     * @param key 键
     * @param value 值
     * @return
     */
    <T> boolean rPush(String key, List<T> value);

    /**
     * list rPush从list尾部插入所有数据，并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    boolean rPush(String key, List<Object> value, long time);
    
    /**
     * list rPush从list尾部插入所有数据，并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return
     */
    boolean rPush(String key, List<Object> value, long time, TimeUnit timeUnit);

    /**
     * list rPush根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    boolean rPush(String key, long index, Object value);
    
    /**
     * list rPop取尾部值并删除
     * @param key 键
     * @return
     */
    Object rPop(String key);
    
    /**
     * list rPop取尾部值并删除，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param key 键
     * @param timeout 超时时间(秒)
     */
    Object rPop(String key, long timeout);

    /**
     * list rPop取尾部值并删除，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param key 键
     * @param timeout 超时时间
     * @param unit 时间单位
     */
    Object rPop(String key, long timeout, TimeUnit unit);
    
    /**
     * list rPopAndLPush尾部取值，头部存值
     * @param sourceKey
     * @param destinationKey
     * @return
     */
    Object rPopAndLPush(String sourceKey, String destinationKey);
    
    /**
     * list rPopAndLPush尾部取值，头部存值，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param sourceKey
     * @param destinationKey
     * @param timeout 超时时间(秒)
     * @return
     */
    Object rPopAndLPush(String sourceKey, String destinationKey, long timeout);
    
    /**
     * list rPopAndLPush尾部取值，头部存值，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param sourceKey
     * @param destinationKey
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return
     */
    Object rPopAndLPush(String sourceKey, String destinationKey, long timeout, TimeUnit unit);
    
    /**
     * list remove移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    long lRemove(String key, long count, Object value);

}

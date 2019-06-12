package com.liuzi.redis.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.liuzi.redis.service.ListService;




public class ListServiceImpl extends HashServiceImpl implements ListService{
	
	/**
     * list range获取list所有值
     * @param key 键
     * @return
     */
    @SuppressWarnings("unchecked")
	public <T> List<T> lRange(String key) {
		return (List<T>) lRange(key, 0, -1);
	}

    /**
     * list range获取list指定区间(start=0,end=-1代表所有值)
     * @param key 键
     * @param start 开始
     * @param end 结束
     * @return
     */
    public List<Object> lRange(String key, long start, long end) {
        try {
        	info("list range, key: " + key + ", start: " + start + ", end: " + end);
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
        	error("list range error, key: " + key + ", msg: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * list size获取list长度
     * @param key 键
     * @return
     */
    public long lSize(String key) {
        try {
        	info("list size, key: " + key);
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
        	error("list size error, key: " + key + ", msg: " + e.getMessage());
            return 0;
        }
    }

    /**
     * list index通过索引 获取list中的值
     * @param key 键
     * @param index 索引,若index<0，从list尾部取
     * @return
     */
    public Object lIndex(String key, long index) {
        try {
        	info("list index, key: " + key + ", index: " + index);
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
        	error("list index error, key: " + key + ", msg: " + e.getMessage());
            return null;
        }
    }

    /**
     * list lPush从list头部插入数据
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lPush(String key, Object value) {
        try {
        	info("list lPush, key: " + key + ", value: " + value);
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
        	error("list lPush error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }

    /**
     * list lPush time从list头部插入数据并设置整个list时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lPush(String key, Object value, long time) {
        try {
        	info("list lPush, key: " + key + ", value: " + value + ", time: " + time);
            redisTemplate.opsForList().leftPush(key, value);
            expire(key, time);
            return true;
        } catch (Exception e) {
        	error("list lPush error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * list lPush time从list头部插入数据并设置整个list时间
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return
     */
    public boolean lPush(String key, Object value, long time, TimeUnit timeUnit) {
        try {
        	info("list lPush, key: " + key + ", value: " + value + ", time: " + time);
            redisTemplate.opsForList().leftPush(key, value);
            expire(key, time, timeUnit);
            return true;
        } catch (Exception e) {
        	error("list lPush error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }

    /**
     * list lPush从list头部插入所有数据
     * @param key 键
     * @param value 值
     * @return
     */
    public <T> boolean lPush(String key, List<T> value) {
        try {
        	info("list lPushAll, key: " + key + ", value: " + value);
            redisTemplate.opsForList().leftPushAll(key, value);
            return true;
        } catch (Exception e) {
        	error("list lPushAll error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }

    /**
     * list lPush time从list头部插入所有数据并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lPush(String key, List<Object> value, long time) {
        try {
        	info("list lPushAll, key: " + key + ", value: " + value + ", time: " + time);
            redisTemplate.opsForList().leftPushAll(key, value);
            expire(key, time);
            return true;
        } catch (Exception e) {
        	error("list lPushAll error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * list lPush time从list头部插入所有数据并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param time 时间单位
     * @return
     */
    public boolean lPush(String key, List<Object> value, long time, TimeUnit timeUnit) {
        try {
        	info("list lPushAll, key: " + key + ", value: " + value + ", time: " + time);
            redisTemplate.opsForList().leftPushAll(key, value);
            expire(key, time, timeUnit);
            return true;
        } catch (Exception e) {
        	error("list lPushAll error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }

    /**
     * list lPush根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lPush(String key, long index, Object value) {
        try {
        	info("list lPush, key: " + key + ", index: " + index + "value: " + value);
            redisTemplate.opsForList().leftPush(key, index, value);
            return true;
        } catch (Exception e) {
        	error("list lPush error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * list lPop取头部值并删除
     * @param key 键
     * @return
     */
    public Object lPop(String key) {
        try {
        	info("list lPop, key: " + key);
        	return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
        	error("list lPop error, key: " + key + ", msg: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * list lPop取头部值并删除，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param key 键
     * @param timeout 超时时间(秒)
     * @return
     */
    public Object lPop(String key, long timeout) {
        try {
        	info("list lPop, key: " + key + ", timeout: " + timeout);
        	return redisTemplate.opsForList().leftPop(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
        	error("list lPop error, key: " + key + ", msg: " + e.getMessage());
            return null;
        }
    }

    /**
     * list lPop取头部值并删除，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param key 键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return
     */
    public Object lPop(String key, long timeout, TimeUnit unit) {
        try {
        	info("list lPop, key: " + key + ", timeout: " + timeout + ", unit: " + unit);
        	return redisTemplate.opsForList().leftPop(key, timeout, unit);
        } catch (Exception e) {
        	error("list lPop error, key: " + key + ", msg: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * list rPush从list尾部插入数据
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean rPush(String key, Object value) {
        try {
        	info("list rPush, key: " + key + ", value: " + value);
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
        	error("list rPush error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }

    /**
     * list rPush从list尾部插入数据，并设置整个list时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean rPush(String key, Object value, long time) {
        try {
        	info("list rPush, key: " + key + ", value: " + value + ", time: " + time);
            redisTemplate.opsForList().rightPush(key, value);
            expire(key, time);
            return true;
        } catch (Exception e) {
        	error("list rPush error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * list rPush从list尾部插入数据，并设置整个list时间
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return
     */
    public boolean rPush(String key, Object value, long time, TimeUnit timeUnit) {
        try {
        	info("list rPush, key: " + key + ", value: " + value + ", time: " + time);
            redisTemplate.opsForList().rightPush(key, value);
            expire(key, time, timeUnit);
            return true;
        } catch (Exception e) {
        	error("list rPush error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }

    /**
     * list rPush从list尾部插入所有数据
     * @param key 键
     * @param value 值
     * @return
     */
    public <T> boolean rPush(String key, List<T> value) {
        try {
        	info("list rPushAll, key: " + key + ", value: " + value);
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
        	error("list rPushAll error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }

    /**
     * list rPush从list尾部插入所有数据，并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean rPush(String key, List<Object> value, long time) {
        try {
        	info("list rPushAll, key: " + key + ", value: " + value + ", time: " + time);
            redisTemplate.opsForList().rightPushAll(key, value);
            expire(key, time);
            return true;
        } catch (Exception e) {
        	error("list rPushAll error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * list rPush从list尾部插入所有数据，并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return
     */
    public boolean rPush(String key, List<Object> value, long time, TimeUnit timeUnit) {
        try {
        	info("list rPushAll, key: " + key + ", value: " + value + ", time: " + time);
            redisTemplate.opsForList().rightPushAll(key, value);
            expire(key, time, timeUnit);
            return true;
        } catch (Exception e) {
        	error("list rPushAll error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }

    /**
     * list rPush根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean rPush(String key, long index, Object value) {
        try {
        	info("list rPush, key: " + key + ", index: " + index+ ", value: " + value);
            redisTemplate.opsForList().rightPush(key, index, value);
            return true;
        } catch (Exception e) {
        	error("list rPush error, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * list rPop取尾部值并删除
     * @param key 键
     * @return
     */
    public Object rPop(String key) {
        try {
        	info("list rPop, key: " + key);
        	return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
        	error("list rPop error, key: " + key + ", msg: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * list rPop取尾部值并删除，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param key 键
     * @param timeout 超时时间(秒)
     */
    public Object rPop(String key, long timeout) {
        try {
        	info("list rPop, key: " + key + ", timeout: " + timeout);
        	return redisTemplate.opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
        	error("list rPop error, key: " + key + ", msg: " + e.getMessage());
            return null;
        }
    }

    /**
     * list rPop取尾部值并删除，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param key 键
     * @param timeout 超时时间
     * @param unit 时间单位
     */
    public Object rPop(String key, long timeout, TimeUnit unit) {
        try {
        	info("list rPop, key: " + key + ", timeout: " + timeout+ ", unit: " + unit);
        	return redisTemplate.opsForList().rightPop(key, timeout, unit);
        } catch (Exception e) {
        	error("list rPop error, key: " + key + ", msg: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * list rPopAndLPush尾部取值，头部存值
     * @param sourceKey
     * @param destinationKey
     * @return
     */
    public Object rPopAndLPush(String sourceKey, String destinationKey) {
        try {
        	info("list rPopAndLPush, sourceKey: " + sourceKey + ", destinationKey: " + destinationKey);
        	return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
        } catch (Exception e) {
        	error("list rPopAndLPush error, sourceKey: " + sourceKey + ", msg: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * list rPopAndLPush尾部取值，头部存值，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param sourceKey
     * @param destinationKey
     * @param timeout 超时时间(秒)
     * @return
     */
    public Object rPopAndLPush(String sourceKey, String destinationKey, long timeout) {
        try {
        	info("list rPopAndLPush, sourceKey: " + sourceKey + ", destinationKey: " + destinationKey +
        			", timeout: " + timeout);
        	return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
        	error("list rPopAndLPush error, sourceKey: " + sourceKey + ", msg: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * list rPopAndLPush尾部取值，头部存值，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param sourceKey
     * @param destinationKey
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return
     */
    public Object rPopAndLPush(String sourceKey, String destinationKey, long timeout, TimeUnit unit) {
        try {
        	info("list rPopAndLPush, sourceKey: " + sourceKey + ", destinationKey: " + destinationKey +
        			", timeout: " + timeout + ", unit: " + unit);
        	return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
        } catch (Exception e) {
        	error("list rPopAndLPush error, sourceKey: " + sourceKey + ", msg: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * list remove移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
        	info("list lRemove, key: " + key + ", count: " + count + ", value: " + value);
        	return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
        	error("list lRemove error, key: " + key + ", msg: " + e.getMessage());
            return 0;
        }
    }

}

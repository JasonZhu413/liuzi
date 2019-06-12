package com.liuzi.redis.service.impl;


import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import redis.clients.jedis.Jedis;

import com.liuzi.redis.service.LockService;
import com.liuzi.redis.service.lock.CallBack;
import com.liuzi.redis.service.lock.RedisLock;
import com.liuzi.redis.service.lock.RedisLockImpl;


@Slf4j
public class LockServiceImpl extends ListServiceImpl implements LockService{
	
	/**
	 * 默认超时释放时间，2秒
	 */
	private static final long DEFAULT_LOCK_TIME = 2000L;
	
	/**
     * 分布式锁（不重试，超时释放时间默认2秒，函数执行完成释放锁）
	 * @param key 键
	 * @param value 值
     * @param callBack 回调函数
     */
    public boolean lock(String key, String value, CallBack callBack){
    	return lock(key, value, DEFAULT_LOCK_TIME, 0, 0, 0, callBack);
	}
    
    /**
     * 分布式锁（不重试，函数执行完成释放锁）
	 * @param key 键
	 * @param value 值
     * @param releaseTime 超时释放时间
     * @param callBack 回调函数
     */
    public boolean lock(String key, String value, long releaseTime, CallBack callBack){
    	return lock(key, value, releaseTime, 0, 0, 0, callBack);
	}
    
    /**
	 * 分布式锁（无间隔时间重试，函数执行完成释放锁）
	 * @param key 键
	 * @param value 值
	 * @param releaseTime 超时释放时间
	 * @param tryMaxTimes 最大重试次数
	 * @param callBack 回调函数
	 * @return
	 */
	public boolean lock(String key, String value, long releaseTime, int tryMaxTimes, CallBack callBack){
		return lock(key, value, releaseTime, 0, tryMaxTimes, 0, callBack);
    }
    
    /**
	 * 分布式锁（函数执行完成释放锁）
	 * @param key 键
	 * @param value 值
	 * @param releaseTime 超时释放时间
	 * @param tryTimes 重试次数
	 * @param tryMaxTimes 最大重试次数
	 * @param tryIMillis sleep下一次重试等待时间
	 * @param callBack 回调函数
	 * @return
	 */
	public boolean lock(String key, String value, long releaseTime, int tryTimes, int tryMaxTimes, long tryIMillis, CallBack callBack){
		RedisLock lock = null;
		try {
			lock = lock(key, value, releaseTime, tryTimes, tryMaxTimes, tryIMillis);
			if (lock != null) {
				callBack.call(key);
				return true;
			}
		} catch (Exception e) {
			log.error("lock error:" + e.getMessage());
		} finally{
			if(lock != null){
				lock.release();
			}
		}
		return false;
    }
	
    
    /**
	 * 分布式锁（不重试，超时释放时间默认2秒）
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public RedisLock lock(String key, String value){
        return lock(key, value, DEFAULT_LOCK_TIME, 0, 0, 0);
    }
	
	/**
	 * 分布式锁（不重试）
	 * @param key 键
	 * @param value 值
	 * @param releaseTime 超时释放时间
	 * @return
	 */
	public RedisLock lock(String key, String value, long releaseTime){
        return lock(key, value, releaseTime, 0, 0, 0);
    }
	
	/**
	 * 分布式锁（无间隔时间重试）
	 * @param key 键
	 * @param value 值
	 * @param releaseTime 超时释放时间
	 * @param tryMaxTimes 最大重试次数
	 * @return
	 */
	public RedisLock lock(String key, String value, long releaseTime, int tryMaxTimes){
		return lock(key, value, releaseTime, 0, tryMaxTimes, 0);
	}
	
	
	/**
	 * 分布式锁
	 * @param key 键
	 * @param value 值
	 * @param releaseTime 超时释放时间
	 * @param tryTimes 重试次数
	 * @param tryMaxTimes 最大重试次数
	 * @param tryIMillis sleep下一次重试等待时间
	 * @return
	 */
	public RedisLock lock(String key, String value, long releaseTime, int tryTimes, int tryMaxTimes, 
			long tryIMillis){
		info("try get lock, key: " + key + ", value: " + value + ", releaseTime: " + releaseTime + 
				", tryTimes: " + tryTimes + ", tryMaxTimes: " + tryMaxTimes + ", tryIMillis: " +
				tryIMillis);
		//获取锁
        String status = redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
            	Jedis jedis = (Jedis) connection.getNativeConnection();
            	//超时释放时间
            	long rTime = releaseTime <= 0 ? DEFAULT_LOCK_TIME : releaseTime;
                return jedis.set(key, value, "NX", "PX", rTime);
            }
        });
        
        //如果获取锁成功，返回对象
        if ("OK".equals(status)) {
        	info("get lock success, key: " + key + ", value: " + value);
        	return new RedisLockImpl(redisTemplate, key, value);
        }
        
        //如果重试次数大于最大重试次数，返回null失败
        if(tryTimes >= tryMaxTimes){
        	info("try get lock fail, key: " + key + ", value: " + value);
    		return null;
    	}
        
        //下一次重试等待时间
    	if(tryIMillis > 0){
    		try {
    			info("try get lock next time, sleep: " + tryIMillis);
                Thread.sleep(tryIMillis);
            } catch (InterruptedException e) {
            	return null;
            }
    		//当前线程如果中断，返回失败
    		if(Thread.currentThread().isInterrupted()){
                return null;
            }
    	}
    	
    	//重新尝试获取锁，重试次数tryTimes + 1
        return lock(key, value, releaseTime, tryTimes++, tryMaxTimes, tryIMillis);
    }

}

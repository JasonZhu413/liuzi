package com.liuzi.redis.service;




import com.liuzi.redis.service.lock.CallBack;
import com.liuzi.redis.service.lock.RedisLock;


public interface LockService extends ListService{
	
	/**
     * 分布式锁（不重试，超时释放时间默认2秒，函数执行完成释放锁）
	 * @param key 键
	 * @param value 值
     * @param callBack 回调函数
     */
    boolean lock(String key, String value, CallBack callBack);
    
    /**
     * 分布式锁（不重试，函数执行完成释放锁）
	 * @param key 键
	 * @param value 值
     * @param releaseTime 超时释放时间
     * @param callBack 回调函数
     */
    boolean lock(String key, String value, long releaseTime, CallBack callBack);
    
    /**
	 * 分布式锁（无间隔时间重试，函数执行完成释放锁）
	 * @param key 键
	 * @param value 值
	 * @param releaseTime 超时释放时间
	 * @param tryMaxTimes 最大重试次数
	 * @param callBack 回调函数
	 * @return
	 */
	boolean lock(String key, String value, long releaseTime, int tryMaxTimes, CallBack callBack);
    
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
	boolean lock(String key, String value, long releaseTime, int tryTimes, int tryMaxTimes, long tryIMillis, CallBack callBack);
	
    
    /**
	 * 分布式锁（不重试，超时释放时间默认2秒）
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	RedisLock lock(String key, String value);
	
	/**
	 * 分布式锁（不重试）
	 * @param key 键
	 * @param value 值
	 * @param releaseTime 超时释放时间
	 * @return
	 */
	RedisLock lock(String key, String value, long releaseTime);
	
	/**
	 * 分布式锁（无间隔时间重试）
	 * @param key 键
	 * @param value 值
	 * @param releaseTime 超时释放时间
	 * @param tryMaxTimes 最大重试次数
	 * @return
	 */
	RedisLock lock(String key, String value, long releaseTime, int tryMaxTimes);
	
	
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
	RedisLock lock(String key, String value, long releaseTime, int tryTimes, int tryMaxTimes, 
			long tryIMillis);

}

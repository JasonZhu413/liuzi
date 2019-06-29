package com.liuzi.redis.boot.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.SortParameters.Order;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import redis.clients.jedis.Jedis;

import com.liuzi.redis.boot.service.RedisService;
import com.liuzi.redis.boot.service.RedisCallBack;
import com.liuzi.redis.boot.service.RedisLock;
import com.liuzi.redis.boot.service.impl.RedisLockImpl;
import com.liuzi.util.common.DateUtil;


@Slf4j
@Service("redisService")
public class RedisServiceImpl implements RedisService {
	
	private final static long LOCK_TIME = 2000L;//锁默认超时时间
	private final static String LOCK_SUCCESS = "OK";//获取锁成功标志
	private final static String IF_NOT_EXIST = "NX";
    private final static String WITH_EXPIRE_TIME = "PX";
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private ValueOperations<String, Object> valueOperations;
	@Autowired
	private HashOperations<String, String, Object> hashOperations;
	@Autowired
	private ListOperations<String, Object> listOperations;
	@Autowired
	private SetOperations<String, Object> setOperations;
	@Autowired
	private ZSetOperations<String, Object> zSetOperations;
    
	/**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
	@Override
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
	@Override
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
	@Override
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
	@Override
    public void del(String... key) {
    	if (key == null || key.length == 0) {
    		return;
    	}
    	
    	int lenth = key.length;
    	String[] keys = new String[lenth];
        if (lenth == 1) {
            redisTemplate.delete(keys[0]);
        } else {
            redisTemplate.delete(CollectionUtils.arrayToList(keys));
        }
    }
    
    // ============================String=============================
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    @Override
    public Object get(String key) {
        return key == null ? null : valueOperations.get(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    @Override
    public boolean set(String key, Object value) {
        try {
            valueOperations.set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    @Override
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                valueOperations.set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 递增
     * @param key 键
     * @return
     */
    @Override
    public long incr(String key) {
        return valueOperations.increment(key, 1L);
    }

    /**
     * 递增
     * @param key 键
     * @param by 要增加几(大于0)
     * @return
     */
    @Override
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        
        return valueOperations.increment(key, delta);
    }
    
    /**
     * 递减
     * @param key 键
     * @return
     */
    @Override
    public long decr(String key) {
        return valueOperations.increment(key, -1L);
    }

    /**
     * 递减
     * @param key 键
     * @param by 要减少几(小于0)
     * @return
     */
    @Override
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        
        return valueOperations.increment(key, -delta);
    }

    // ================================Hash=================================
    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    @SuppressWarnings("unchecked")
	@Override
    public <T> T hget(String key, String item) {
        return (T) hashOperations.get(key, item);
    }
    
    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
	@Override
	@SuppressWarnings("unchecked")
    public <T> List<T> hgetList(String key, String item) {
        return (List<T>) hashOperations.get(key, item);
    }

	/**
     * HashGet 所有
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
	@Override
    public Map<String, Object> hgetAll(String key) {
    	return hashOperations.entries(key);
    }
	
    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    @Override
    public Map<String, Object> hmget(String key) {
        return hashOperations.entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    @Override
	public boolean hmset(String key, Map<String, Object> map) {
        try {
            hashOperations.putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    @Override
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            hashOperations.putAll(key, map);
            if (time > 0) {
            	redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    @Override
    public boolean hset(String key, String item, Object value) {
        try {
            hashOperations.put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    @Override
    public boolean hset(String key, String item, Object value, long time) {
        try {
            hashOperations.put(key, item, value);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    @Override
    public void hdel(String key, Object... item) {
        hashOperations.delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    @Override
    public boolean hHasKey(String key, String item) {
        return hashOperations.hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return
     */
    @Override
    public double hincr(String key, String item, double by) {
        return hashOperations.increment(key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return
     */
    @Override
    public double hdecr(String key, String item, double by) {
        return hashOperations.increment(key, item, -by);
    }

    // ============================Set=============================
    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    @Override
    public Set<Object> sGet(String key) {
        try {
            return setOperations.members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    @Override
    public boolean sHasKey(String key, Object value) {
        try {
            return setOperations.isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @Override
    public long sSet(String key, Object... values) {
        try {
            return setOperations.add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @Override
    public long sSet(String key, long time, Object... values) {
        try {
            Long count = setOperations.add(key, values);
            if (time > 0)
            	redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    @Override
    public long sSize(String key) {
        try {
            return setOperations.size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    @Override
    public long setRemove(String key, Object... values) {
        try {
            Long count = setOperations.remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // ===============================List=================================

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束 0 到 -1代表所有值
     * @return
     */
    @Override
    public List<Object> lGet(String key, long start, long end) {
        try {
            return listOperations.range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    

    @Override
	@SuppressWarnings("unchecked")
	public <T> List<T> lGet(String key) {
		return (List<T>) lGet(key, 0, -1);
	}

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    @Override
    public long lSize(String key) {
        try {
            return listOperations.size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    @Override
    public Object lGet(String key, long index) {
        try {
            return listOperations.index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    @Override
    public boolean lSet(String key, Object value) {
        try {
            listOperations.rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    @Override
    public boolean lSet(String key, Object value, long time) {
        try {
            listOperations.rightPush(key, value);
            if (time > 0)
            	redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    @Override
    public <T> boolean lSet(String key, List<T> value) {
        try {
            listOperations.rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    @Override
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            listOperations.rightPushAll(key, value);
            if (time > 0)
            	redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    @Override
    public boolean lSet(String key, long index, Object value) {
        try {
            listOperations.set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    @Override
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = listOperations.remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public void lock(String key, String value, RedisCallBack callBack){
    	lock(key, value, LOCK_TIME, callBack);
	}
    
    public void lock(String key, String value, long time, RedisCallBack callBack){
    	RedisLock lock = null;
		try {
			lock = lock(key, value);
			if (lock != null) {
				callBack.call(key);
			}
		} catch (Exception e) {
			log.error("lock error:" + e.getMessage());
		} finally{
			if(lock != null){
				lock.release();
			}
		}
	}
	
	public RedisLock lock(String key, String value){
        return lock(key, value, LOCK_TIME, 0, 0L, 0);
    }
	
	public RedisLock lock(String key, String value, long time){
        return lock(key, value, time, 0, 0L, 0);
    }
	
	public RedisLock lock(String key, String value, long seconds, int tryTimes, long tryIMillis, int times){
        String status = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
            	Jedis jedis = (Jedis) connection.getNativeConnection();
                return jedis.set(key, value, IF_NOT_EXIST, WITH_EXPIRE_TIME, seconds);
            }
        });
        
        if (LOCK_SUCCESS.equals(status)) {
        	return new RedisLockImpl(redisTemplate, key, value);
        }
 
        if(tryTimes >= times){
    		return null;
    	}
        
    	if(tryIMillis > 0){
    		try {
                Thread.sleep(tryIMillis);
            } catch (InterruptedException e) {
            	return null;
            }
    		if(Thread.currentThread().isInterrupted()){
                return null;
            }
    	}
    	
        return lock(key, value, seconds, tryTimes + 1, tryIMillis, times);
    }

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> page(String key, String order, boolean desc, 
			Integer pageNo, Integer pageSize) {
		
		pageNo = pageNo == null || pageNo == 0 ? 1 : pageNo;
		pageSize = pageSize == null || pageSize == 0 ? 20 : pageSize;
		int limit = (pageNo - 1) * pageSize;
		
		SortQueryBuilder<String> builder = SortQueryBuilder.sort(key);
		builder.limit(limit, pageSize);
		builder.alphabetical(true);
		builder.by(order);
		if(desc){
			builder.order(Order.DESC);
		}
		
		builder.get("#");
		
		return (List<T>) redisTemplate.sort(builder.build());
	}
	
	
	@Override
	public long getKey(String key) {
		return this.getKey(key, 1);
	}
	
	@Override
	public long getKey(String key, long delta) {
		delta = delta == 0 ? 1 : delta;
		
		String time = DateUtil.date2Str(new Date(), "yyMMddHHmmss");
		String date = DateUtil.date2Str(new Date(), "yyMMdd");
		
		key = "PRIMARY_KEY:" + date + ":" + key;
		
		long incre = valueOperations.increment(key, delta);
		redisTemplate.expire(key, 24 * 60 * 60, TimeUnit.SECONDS);
		
		return Long.parseLong(time + incre);
	}
}

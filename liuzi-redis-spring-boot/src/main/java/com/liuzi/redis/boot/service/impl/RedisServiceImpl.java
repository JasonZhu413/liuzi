package com.liuzi.redis.boot.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.SortParameters.Order;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
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
import com.liuzi.util.DateUtil;
import com.liuzi.util.MD5;


@Service("redisService")
public class RedisServiceImpl implements RedisService {
	
	@Resource
    private RedisTemplate<String, Object> redisTemplate;
    
	/**
     * 指定缓存失效时间
     * 
     * @param key
     *            键
     * @param time
     *            时间(秒)
     * @return
     */
	@Override
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
            	key = MD5.crypt(key);
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * 
     * @param key
     *            键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
	@Override
    public long getExpire(String key) {
    	key = MD5.crypt(key);
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * 
     * @param key
     *            键
     * @return true 存在 false不存在
     */
	@Override
    public boolean hasKey(String key) {
        try {
        	key = MD5.crypt(key);
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * 
     * @param key
     *            可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
	@Override
    public void del(String... key) {
    	if (key == null || key.length == 0) {
    		return;
    	}
    	
    	int lenth = key.length;
    	String[] keys = new String[lenth];
    	for(int i = 0, e = lenth; i < e; i ++){
    		keys[i] = MD5.crypt(key[i]);
    	}
    	
        if (lenth == 1) {
            redisTemplate.delete(keys[0]);
        } else {
            redisTemplate.delete(CollectionUtils.arrayToList(keys));
        }
    }
    
    // ============================String=============================
    /**
     * 普通缓存获取
     * 
     * @param key
     *            键
     * @return 值
     */
    @Override
    public Object get(String key) {
    	key = MD5.crypt(key);
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @return true成功 false失败
     */
    @Override
    public boolean set(String key, Object value) {
        try {
        	key = MD5.crypt(key);
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @param time
     *            时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    @Override
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
            	key = MD5.crypt(key);
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
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
     * 
     * @param key
     *            键
     * @param by
     *            要增加几(大于0)
     * @return
     */
    @Override
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        key = MD5.crypt(key);
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * 
     * @param key
     *            键
     * @param by
     *            要减少几(小于0)
     * @return
     */
    @Override
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        key = MD5.crypt(key);
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ================================Map=================================
    /**
     * HashGet
     * 
     * @param key
     *            键 不能为null
     * @param item
     *            项 不能为null
     * @return 值
     */
    @SuppressWarnings("unchecked")
	@Override
    public <T> T hget(String key, String item) {
    	key = MD5.crypt(key);
        return (T) redisTemplate.opsForHash().get(key, item);
    }
    
	@Override
	@SuppressWarnings("unchecked")
    public <T> List<T> hgetList(String key, String item) {
    	key = MD5.crypt(key);
        return (List<T>) redisTemplate.opsForHash().get(key, item);
    }

	@Override
    public Map<Object, Object> hgetAll(String key) {
    	key = MD5.crypt(key);
    	return redisTemplate.opsForHash().entries(key);
    }
	
    /**
     * 获取hashKey对应的所有键值
     * 
     * @param key
     *            键
     * @return 对应的多个键值
     */
    @Override
    public Map<Object, Object> hmget(String key) {
    	key = MD5.crypt(key);
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * 
     * @param key
     *            键
     * @param map
     *            对应多个键值
     * @return true 成功 false 失败
     */
    @Override
	public boolean hmset(String key, Map<String, Object> map) {
        try {
        	key = MD5.crypt(key);
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * 
     * @param key
     *            键
     * @param map
     *            对应多个键值
     * @param time
     *            时间(秒)
     * @return true成功 false失败
     */
    @Override
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
        	key = MD5.crypt(key);
            redisTemplate.opsForHash().putAll(key, map);
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
     * 
     * @param key
     *            键
     * @param item
     *            项
     * @param value
     *            值
     * @return true 成功 false失败
     */
    @Override
    public boolean hset(String key, String item, Object value) {
        try {
        	key = MD5.crypt(key);
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * 
     * @param key
     *            键
     * @param item
     *            项
     * @param value
     *            值
     * @param time
     *            时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    @Override
    public boolean hset(String key, String item, Object value, long time) {
        try {
        	key = MD5.crypt(key);
            redisTemplate.opsForHash().put(key, item, value);
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
     * 
     * @param key
     *            键 不能为null
     * @param item
     *            项 可以使多个 不能为null
     */
    @Override
    public void hdel(String key, Object... item) {
    	key = MD5.crypt(key);
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * 
     * @param key
     *            键 不能为null
     * @param item
     *            项 不能为null
     * @return true 存在 false不存在
     */
    @Override
    public boolean hHasKey(String key, String item) {
    	key = MD5.crypt(key);
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * 
     * @param key
     *            键
     * @param item
     *            项
     * @param by
     *            要增加几(大于0)
     * @return
     */
    @Override
    public double hincr(String key, String item, double by) {
    	key = MD5.crypt(key);
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * 
     * @param key
     *            键
     * @param item
     *            项
     * @param by
     *            要减少记(小于0)
     * @return
     */
    @Override
    public double hdecr(String key, String item, double by) {
    	key = MD5.crypt(key);
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================
    /**
     * 根据key获取Set中的所有值
     * 
     * @param key
     *            键
     * @return
     */
    @Override
    public Set<Object> sGet(String key) {
        try {
        	key = MD5.crypt(key);
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @return true 存在 false不存在
     */
    @Override
    public boolean sHasKey(String key, Object value) {
        try {
        	key = MD5.crypt(key);
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * 
     * @param key
     *            键
     * @param values
     *            值 可以是多个
     * @return 成功个数
     */
    @Override
    public long sSet(String key, Object... values) {
        try {
        	key = MD5.crypt(key);
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * 
     * @param key
     *            键
     * @param time
     *            时间(秒)
     * @param values
     *            值 可以是多个
     * @return 成功个数
     */
    @Override
    public long sSetAndTime(String key, long time, Object... values) {
        try {
        	key = MD5.crypt(key);
            Long count = redisTemplate.opsForSet().add(key, values);
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
     * 
     * @param key
     *            键
     * @return
     */
    @Override
    public long sGetSetSize(String key) {
        try {
        	key = MD5.crypt(key);
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * 
     * @param key
     *            键
     * @param values
     *            值 可以是多个
     * @return 移除的个数
     */
    @Override
    public long setRemove(String key, Object... values) {
        try {
        	key = MD5.crypt(key);
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     * 
     * @param key
     *            键
     * @param start
     *            开始
     * @param end
     *            结束 0 到 -1代表所有值
     * @return
     */
    @Override
    public List<Object> lGet(String key, long start, long end) {
        try {
        	key = MD5.crypt(key);
            return redisTemplate.opsForList().range(key, start, end);
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
     * 
     * @param key
     *            键
     * @return
     */
    @Override
    public long lGetListSize(String key) {
        try {
        	key = MD5.crypt(key);
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * 
     * @param key
     *            键
     * @param index
     *            索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    @Override
    public Object lGetIndex(String key, long index) {
        try {
        	key = MD5.crypt(key);
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @param time
     *            时间(秒)
     * @return
     */
    @Override
    public boolean lSet(String key, Object value) {
        try {
        	key = MD5.crypt(key);
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @param time
     *            时间(秒)
     * @return
     */
    @Override
    public boolean lSet(String key, Object value, long time) {
        try {
        	key = MD5.crypt(key);
            redisTemplate.opsForList().rightPush(key, value);
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
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @param time
     *            时间(秒)
     * @return
     */
    @Override
    public <T> boolean lSet(String key, List<T> value) {
        try {
        	key = MD5.crypt(key);
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @param time
     *            时间(秒)
     * @return
     */
    @Override
    public boolean lSet(String key, List<Object> value, long time) {
        try {
        	key = MD5.crypt(key);
            redisTemplate.opsForList().rightPushAll(key, value);
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
     * 
     * @param key
     *            键
     * @param index
     *            索引
     * @param value
     *            值
     * @return
     */
    @Override
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
        	key = MD5.crypt(key);
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     * 
     * @param key
     *            键
     * @param count
     *            移除多少个
     * @param value
     *            值
     * @return 移除的个数
     */
    @Override
    public long lRemove(String key, long count, Object value) {
        try {
        	key = MD5.crypt(key);
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    
    @Override
	public void setNX(String key, RedisCallBack callBack)  {
		RedisConnection conn = getConnection();
		String newkey = MD5.crypt(key);
		
		String value = UUID.randomUUID().toString();
		if (conn.setNX(newkey.getBytes(), value.getBytes())) {
			try {
				callBack.call(newkey);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("call back error" + e.getMessage());
			}
			redisTemplate.delete(newkey);
			return;
        } else {
        	setNX(key, callBack);
        } 
	}
    
	public void setNX(String key, long seconds, RedisCallBack callBack)  {
		try(RedisLock lock = lock(key, seconds)) {
			if (lock != null) {
				callBack.call(key);
			}
		}catch (Exception e) {
			
		}
	}
    
    public RedisLock lock(final String key, final long seconds){
        return lock(key, seconds, 0, 0);
    }
    
    public RedisLock lock(final String key, final long seconds, int maxRetryTimes){
        return lock(key, seconds, maxRetryTimes, 0);
    }
    
    public RedisLock lock(String key, final long seconds, int maxRetryTimes, long retryIntervalTimeMillis){
        return lock_object(MD5.crypt(key), seconds, maxRetryTimes, retryIntervalTimeMillis);
    }
    
    private RedisLock lock_object(final String key, final long seconds, int maxRetryTimes, long retryIntervalTimeMillis){
        final String value = UUID.randomUUID().toString();
 
        int maxTimes = maxRetryTimes + 1;
        for(int i = 0; i < maxTimes; i++) {
            String status = redisTemplate.execute(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    Jedis jedis = (Jedis) connection.getNativeConnection();
                    return jedis.set(key, value, "nx", "ex", seconds);
                }
            });
            
            if ("OK".equals(status)) {
                return new RedisLockImpl(redisTemplate, key, value);
            }
 
            if(retryIntervalTimeMillis > 0) {
                try {
                    Thread.sleep(retryIntervalTimeMillis);
                } catch (InterruptedException e) {
                    break;
                }
            }
            if(Thread.currentThread().isInterrupted()){
                break;
            }
        }
 
        return null;
    }

	@Override
	public HashOperations<String, String, Object> opsForHash() {
		return redisTemplate.opsForHash();
	}
	
	@Override
	public SetOperations<String, Object> opsForSet() {
		return redisTemplate.opsForSet();
	}
	
	@Override
	public ValueOperations<String, Object> opsForValue() {
		return redisTemplate.opsForValue();
	}
	
	@Override
	public ZSetOperations<String, Object> opsForZSet() {
		return redisTemplate.opsForZSet();
	}
	
	@Override
	public HyperLogLogOperations<String, Object> opsForHyperLogLog() {
		return redisTemplate.opsForHyperLogLog();
	}
	
	@Override
	public ClusterOperations<String, Object> opsForCluster() {
		return redisTemplate.opsForCluster();
	}
	
	@Override
	public ListOperations<String, Object> opsForList() {
		return redisTemplate.opsForList();
	}
	
	@Override
    public RedisTemplate<String, Object> getTemplate(){
    	return redisTemplate;
    }
    
    @Override
    public RedisConnection getConnection(){
    	return redisTemplate.getConnectionFactory().getConnection();
    }

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> page(String key, String order, boolean desc, 
			Integer pageNo, Integer pageSize) {
		
		key = MD5.crypt(key);
		
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
		//key = MD5.crypt(key);
		
		long incre = redisTemplate.opsForValue().increment(key, delta);
		redisTemplate.expire(key, 24 * 60 * 60, TimeUnit.SECONDS);
		
		return Long.parseLong(time + incre);
		
	}
}

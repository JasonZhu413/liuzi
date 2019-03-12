package com.liuzi.util.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.json.JSONObject;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.util.Hashing;


/**
 * 
 * 在这个方法中，构造函数先初始化了分片规则得到了每个主节点和160个虚拟节点的映射关系
 * TreeMap<Long, JedisShardInfo> nodes，然后初始化了JedisSentinelPool数组，对于每
 * 个需要根据key获取jedis的用户，先经过hash(key),然后nodes.get(hash(key))得到对应的
 * JedisShardInfo，然后根据JedisShardInfo对象获取服务主节点的ip和端口号组成masterName,
 * 这样就可以根据masterName去哨兵池JedisSentinelPool动态获取真正可用的jedis对象。
 * 这样一来如果某个redis服务主节点挂了，哨兵集群会发现并且把从节点当作新的主节点。
 * 这样就能达到高可用的效果。
 * 
 * 其中的一致性哈希算法过程稍微解释下：
 * 当我们传入一个JedisShardInfo数组进去后，就会为每个JedisShardInfo对象分配160个虚拟节点，
 * 每个虚拟节点的可key由this.algo.hash("SHARD-" + i +"-NODE-" + n)或者
 * this.algo.hash(shardInfo.getName() +"*" + shardInfo.getWeight() + n)计算得出，
 * 这样就形成160个分散的虚拟key和1个JedisShardInfo的映射关系的map, JedisShardInfo数组里
 * 每个JedisShardInfo都会得到160个这样的映射关系（虚拟节点），并且最后都全部加入大的
 * map(TreeMap<Long, JedisShardInfo> nodes)中。当我们需要获取某个真实key值对应的真正
 * JedisShardInfo（里面包含服务节点的ip和port信息），就对该key也执行hash操作algo.hash(key)，
 * 然后通过TreeMap.tailMap获取虚拟节点的key大于或等于algo.hash(key)的第一个虚拟节点已经该虚拟
 * 节点对应的JedisShardInfo，如果algo.hash(key)大于所有虚拟节点的key ,那么由于一致性哈希环的概
 * 念，该key将取所有虚拟节点中的第一个（也就是最小那一个）并获取第一个虚拟节点对应的JedisShardInfo
 * 
 * @author zsy
 *
 */
public class ShardedJedisSentinelPool {
	private static final int LOCK_TIME = 1000;
	private static final String LOCK_SUCCESS = "OK";
	private static final Long RELEASE_SUCCESS = 1L;
	private static final String IF_NOT_EXIST = "NX";
    private static final String WITH_EXPIRE_TIME = "PX";
    private static final String SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] " + 
    		"then return redis.call('del', KEYS[1]) else return 0 end";
	private Map<String, JedisSentinelPool> poolMap = new HashMap<>();
	private Hashing algo = Hashing.MURMUR_HASH;
	private TreeMap<Long, JedisShardInfo> nodes;
	private static final String MASTER_NAME = "master-";

	public ShardedJedisSentinelPool(Set<HostAndPort> hostInfo, Set<String> sentinels){
		List<JedisShardInfo> list = new ArrayList<>();
		for(HostAndPort iter : hostInfo){
			list.add(new JedisShardInfo(iter.getHost(),iter.getPort()));
		}
		initialize(list);
		for(HostAndPort iter : hostInfo){
			String masterName = MASTER_NAME + iter.getHost() + ":" + iter.getPort();
			poolMap.put(masterName, new JedisSentinelPool(masterName, sentinels));
		}
	}

	private void initialize(List<JedisShardInfo> shards) {
		nodes = new TreeMap<Long, JedisShardInfo>();
		for (int i = 0; i != shards.size(); ++i) {
			final JedisShardInfo shardInfo = shards.get(i);
			if (shardInfo.getName() == null) 
				for (int n = 0; n < 160 * shardInfo.getWeight(); n++) {
					nodes.put(this.algo.hash("SHARD-" + i + "-NODE-" + n), shardInfo);
				}
			else 
				for (int n = 0; n < 160 * shardInfo.getWeight(); n++) {
					nodes.put(this.algo.hash(shardInfo.getName() + "*" + shardInfo.getWeight() + n), 
							shardInfo);
				}
		}
	}

	private JedisShardInfo getShardInfo(byte[] key) {
		SortedMap<Long, JedisShardInfo> tail = nodes.tailMap(algo.hash(key));
		if (tail.isEmpty()) {
			return nodes.get(nodes.firstKey());
		}
		return tail.get(tail.firstKey());
	}
	
	public Jedis getResource(String key){
		JedisShardInfo shardInfo = getShardInfo(key.getBytes());
		String masterName = MASTER_NAME + shardInfo.getHost() + ":" + shardInfo.getPort();
		return poolMap.get(masterName).getResource();
	}
	
	public void close(){
		for(Entry<String, JedisSentinelPool> iter : poolMap.entrySet()){
			iter.getValue().destroy();
		}
	}
	
	public boolean exists(String key){
    	Jedis jedis = getResource(key);
    	try {
	    	return jedis.exists(key); 
	    } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close();
        }
    	return false;
    }
    
	public void set(String key, String value){
		Jedis jedis = getResource(key);
	    try {
	    	jedis.set(key, value); 
	    } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close();
        }
	}
	
	public void set(String key, String value, int time){
		Jedis jedis = getResource(key);
	    try {
	    	jedis.setex(key, time, value);
	    }catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close();
        }
	}
	
	public <T> void hset(String key, String field, T t){
		Jedis jedis = getResource(key);
	    try {
 	    	jedis.hset(key, field, JSONObject.fromObject(t).toString());
 	    } catch (Exception e) {
             e.printStackTrace();
        } finally {
        	close();
        }
	}
	
	public <T> void hset(String key, String field, T t, int time){
		Jedis jedis = getResource(key);
	    try {
	    	jedis.hset(key, field, JSONObject.fromObject(t).toString());
	    	jedis.expire(key, time);
 	    }catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close();
        }
	}
	
	public <T> void lpush(String key, List<T> list){
		Jedis jedis = getResource(key);
	    try {
			for(T t : list){
				jedis.lpush(key, JSONObject.fromObject(t).toString());
			}
 	    }catch (Exception e) {  
            e.printStackTrace();  
        } finally {
        	close();
        }
	}
	
	public <T> void lpush(String key, int time, List<T> list){
		Jedis jedis = getResource(key);
	    try {
 	    	for(T t : list){
 	    		jedis.lpush(key, JSONObject.fromObject(t).toString());
			}
 	    	jedis.expire(key, time);
 	    }catch (Exception e) {  
             e.printStackTrace();  
        } finally {
        	close();
        }
	}
	
	public <T> void rpush(String key, List<T> list){
		Jedis jedis = getResource(key);
	    try {
			for(T t : list){
				jedis.rpush(key, JSONObject.fromObject(t).toString());
			}
 	    }catch (Exception e) {  
            e.printStackTrace();  
        } finally {
        	close();
        }
	}
	
	public <T> void rpush(String key ,int time, List<T> list){
		Jedis jedis = getResource(key);
	    try {
 	    	for(T t : list){
 	    		jedis.rpush(key, JSONObject.fromObject(t).toString());
			}
 	    	jedis.expire(key, time);
 	    }catch (Exception e) {  
             e.printStackTrace();  
        } finally {
        	close();
        }
	}
	
	public String get(String key){
		Jedis jedis = getResource(key);
	    try {
	    	return jedis.get(key);
	    }catch (Exception e) {  
            e.printStackTrace();
        } finally {
        	close();
        }
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T hget(String key, String filed){
		Jedis jedis = getResource(key);
	    try {
	    	return (T) jedis.hget(key, filed);
	    }catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close();
        }
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> hgetAll(String key, String filed){
		Jedis jedis = getResource(key);
	    try {
	    	Map<String, T> rMap = new HashMap<>();
	    	Map<String, String> map = jedis.hgetAll(key);
	    	for(Map.Entry<String, String> entry : map.entrySet()){
	    		JSONObject json = JSONObject.fromObject(entry.getValue());
	    		rMap.put(entry.getKey(), (T) json);
	    	}
	    	return rMap;
	    }catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close();
        }
	    return null;
	}
	
	public Long llen(String key){
		Jedis jedis = getResource(key);
	    try { 
	    	return jedis.llen(key);
 	    } catch (Exception e) {  
             e.printStackTrace();  
        } finally {
        	close();
        }
 	    return 0L;
	}
	
	public <T> List<T> lrange(String key){
		return lrange(key, 0, -1);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T lpop(String key){
		Jedis jedis = getResource(key);
	    try {
	    	return (T)jedis.lpop(key);
	    }catch (Exception e) {  
            e.printStackTrace();  
        } finally {
        	close();
        }
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T rpop(String key){
		Jedis jedis = getResource(key);
	    try {
	    	return (T)jedis.rpop(key);
	    }catch (Exception e) {  
            e.printStackTrace();  
        } finally {
        	close();
        }
	    return null;
	}
	
	public String ltrim(String key){
	    return ltrim(key, 0, 0);
	}
	
	public String ltrim(String key, int start, int stop){
		Jedis jedis = getResource(key);
	    try {
	    	return jedis.ltrim(key, start, stop);
	    }catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close();
        }
	    return null;
	}
	
	public void del(String key){
		Jedis jedis = getResource(key);
	    try {
 	    	jedis.del(key);
 	    }catch (Exception e) {  
            e.printStackTrace();  
 	    } finally {
        	close();
        }
	}
	
	public Long incr(String key){
		Jedis jedis = getResource(key);
	    try { 
 	    	return jedis.incr(key);
 	    }catch (Exception e) {
            e.printStackTrace();  
        } finally {
        	close();
        }
		return 0L;
	}
	
	public Long incr(String key, int by){
		Jedis jedis = getResource(key);
	    try { 
 	    	return jedis.incrBy(key, by);
 	    }catch (Exception e) {
            e.printStackTrace();
        }
 	    return 0L;
	}
	
	public Long zcard(String key){
		Jedis jedis = getResource(key);
	    try { 
 	    	return jedis.zcard(key);
 	    }catch (Exception e) {
            e.printStackTrace();  
        } finally {
        	close();
        }
 	    return 0L;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> lrange(String key, long start, long end){
		List<T> list = new ArrayList<>();
		Jedis jedis = getResource(key);
	    try { 
 	    	List<String> l = jedis.lrange(key, start, end);
 	    	l.forEach(s -> list.add((T) s));
 	    } catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close();
        }
 	    return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> zrange(String key, long start, long end){
		List<T> list = new ArrayList<>();
		Jedis jedis = getResource(key);
	    try { 
 	    	Set<String> set = jedis.zrange(key, start, end);
 	    	set.forEach(s -> list.add((T) s));
 	    } catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close();
        }
 	    return list;
	}
   
	@SuppressWarnings("unchecked")
	public <T> List<T> zrangeByScore(String key, double start, double end){
		List<T> list = new ArrayList<>();
		Jedis jedis = getResource(key);
	    try { 
 	    	Set<String> set = jedis.zrangeByScore(key, start, end);
 	    	set.forEach(s -> list.add((T)s));
 	    }catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close();
        }
 	    return list;
	}
	
	public Long zremrangeByScore(String key, double start, double end){
		Jedis jedis = getResource(key);
	    try {
 	    	return jedis.zremrangeByScore(key, start, end);
 	    }catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close();
        }
 	    return 0L;
	}
	
	public void zadd(String key, String value, double score){
		Jedis jedis = getResource(key);
	    try { 
 	    	jedis.zadd(key, score, value);
 	    } catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close();
        }
	}
	
	public Long zcount(String key, double start, double end){
		Jedis jedis = getResource(key);
	    try { 
 	    	Long count = jedis.zcount(key, start, end);
 	    	return count == null ? 0 : count;
 	    } catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close();
        }
 	    return 0L;
	}
	
	/**
	 * 锁
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean lock(String key, String value){
		return lock(key, value, LOCK_TIME);
	}
	
	/**
	 * 锁
	 * @param key
	 * @param value
	 * @param time 锁持续时长（毫秒）
	 * @return
	 */
	public boolean lock(String key, String value, int time){
		Jedis jedis = getResource(key);
	    try { 
			String ok = jedis.set(key, value, IF_NOT_EXIST, WITH_EXPIRE_TIME, time);
	    	if (LOCK_SUCCESS.equals(ok)) {
	    		return true;
	    	}
 	    } catch (Exception e) {
 	    	e.printStackTrace();
 	    } finally {
        	close();
        }
 	   	return false;
	}
	
	/**
	 * 释放锁，匹配value
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean release(String key, String value) {
		Jedis jedis = getResource(key);
	    try { 
 	        Object result = jedis.eval(SCRIPT, Collections.singletonList(key), 
 	        		Collections.singletonList(value));
 	        if (RELEASE_SUCCESS.equals(result)) {
 	            return true;
 	        }
 	    } catch (Exception e) {
 	    	e.printStackTrace();
 	    } finally {
        	close();
        }
		return false;
    }
}

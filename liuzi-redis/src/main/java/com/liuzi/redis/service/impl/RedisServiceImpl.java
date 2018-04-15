package com.liuzi.redis.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;















import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuzi.redis.service.RedisService;

import redis.clients.jedis.JedisCluster;


@Repository("redisService")
public class RedisServiceImpl implements RedisService {
	private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
	
	@Resource
	private JedisCluster jedisCluster;
	
	@Override
	public boolean exists(String key) {
		logger.debug("redis get String key-->{}", new Object[]{key});
		return jedisCluster.exists(key);
	}
	@Override
	public void delete(String key) {
		logger.debug("redis del key-->{} ;",new Object[]{key});
		jedisCluster.del(key);
	}
	@Override
	public void delete(String... key) {
		logger.debug("redis del key-->{} ;",new Object[]{key});
		jedisCluster.del(key);
	}

	/**
	 * String操作 
	 * @param key
	 * @return
	 */
	@Override
	public void save(String key, String value) {
		if(StringUtils.isEmpty(key)){
			return;
		}
		String s = jedisCluster.set(key, value);
		logger.debug("redis set status-->{} ; key-->{} ; value-->{};",new Object[]{s,key,value});
	}
	@Override
	public void save(String key, String value, Integer seconds){
		if(StringUtils.isEmpty(key)){
			return;
		}
		if(seconds==null){
			save( key,value);
		}
		String s = jedisCluster.setex(key, seconds, value);
		logger.debug("redis set status-->{} ; key-->{} ; value-->{}; seconds-->{}",new Object[]{s,key,value,seconds});
	}
	@Override
	public String get(String key) {
		logger.debug("redis get String key-->{}", new Object[]{key});
		return jedisCluster.get(key);
	}
	@Override
	public String saveLastValue(String key, String value) {
		return jedisCluster.getSet(key, value);
	}
	@Override
	public Long saveIfNotExit(String key, String value) {
		return jedisCluster.setnx(key, value);
	}
	@Override
	public void save(String key, Object obj) {
		if(key == null){
			return;
		}
		
		String json = JSONObject.toJSON(obj).toString();
		
		String s = jedisCluster.set(key, json);
		
		logger.debug("redis set status-->{} ; key-->{} ; value-->{};",new Object[]{s,key,obj});
	}
	
	@Override
	public void save(String key, Object obj, Integer seconds) {
		if(key == null){
			return;
		}
		
		String json = JSONObject.toJSON(obj).toString();
		jedisCluster.setex(key, seconds, json);
	}
	
	@Override
	public Long incr(String key){
		return jedisCluster.incr(key);
	}
	@Override
	public Long incrBy(String key, int integer){
		return jedisCluster.incrBy(key, integer);
	}
	@Override
	public Long decr(String key){
		return jedisCluster.decr(key);
	}
	@Override
	public Long decrBy(String key, long val){
		return jedisCluster.decrBy(key, val);
	}
	@Override
	public Long append(String key, String value){
		return jedisCluster.append(key, value);
	}
	@Override
	public String substr(String key, int start, int end){
		return jedisCluster.substr(key, start, end);
	}
	
	/**
	 * List操作
	 */
	@Override
	public Long llen(String key){
		return jedisCluster.llen(key);
	}
	@Override
	public String lindex(String key, int index){
		return jedisCluster.lindex(key, index);
	}
	@Override
	public String lset(String key, int index, String value){
		return jedisCluster.lset(key, index, value);
	}
	@Override
	public String lpop(String key){
		return jedisCluster.lpop(key);
	}
	@Override
	public String rpop(String key){
		return jedisCluster.rpop(key);
	}
	@Override
	public String rpoplpush(String srckey, String dstkey){
		return jedisCluster.rpoplpush(srckey, dstkey);
	}
	@Override
	public Long saveArray(String key, String value) {
		if(StringUtils.isEmpty(key) || StringUtils.isEmpty(value)){
			return 0L;
		}
		return jedisCluster.rpush(key, value);
	}
	@Override
	public Long saveArray(String key, String value, Integer seconds) {
		Long l = saveArray(key, value);
		if(seconds != null){
			jedisCluster.expire(key, seconds);
		}
		return l;
	}
	@Override
	public Long saveArray(String key, String[] value) {
		if(StringUtils.isEmpty(key)){
			return 0L;
		}
		if(value.length > 0){
			long l = jedisCluster.rpush(key, value);
			logger.debug("redis list save  key-->{},value-->{},length-->{};",new Object[]{key,value,l});
			return l;
		}else{
			delete(key);
			return 0l;
		}
	}
	@Override
	public Long saveArray(String key, String[] value, Integer seconds){
		Long l = saveArray(key, value);
		if(seconds != null){
			jedisCluster.expire(key, seconds);
		}
		return l;
	}
	@Override
	public Long saveArray(String key, List<?> list){
		String[] strs = new String[list.size()];
		Object o;
		for(int i = 0; i < list.size(); i++){
			o = list.get(i);
			if(o != null){
				strs[i] = JSONObject.toJSONString(o);
			}else{
				strs[i] = null;
			}
		}
		
		return saveArray(key, strs);
		
	}
	@Override
	public Long saveArray(String key,List<?> value,Integer seconds){
		Long l = saveArray(key, value);
		if(seconds != null){
			jedisCluster.expire(key, seconds);
		}
		
		return l ;
	}
	@Override
	public List<String> getArray(String key, Long start, Long end) {
		start = start == null ? 0 : start;
		end = end == null ? Long.MAX_VALUE : end;
		List<String> strs = jedisCluster.lrange(key, start, end);
		logger.debug("redis get list key-->{}; start-->{}; end-->{}", new Object[]{key, start, end});
		return strs;
	}
	@Override
	public Long lpush(String key, String value){
		return jedisCluster.lpush(key, value);
	}
	@Override
	public Long lrem(String key, long count, String value){
		return jedisCluster.lrem(key, count, value);
	}
	
	
	/**
	 * Hash
	 */
	@Override
	public Boolean hexists(String key, String field){
		return jedisCluster.hexists(key, field);
	}
	@Override
	public void save(String key, Map<String, String> map) {
		if(key == null){
			return;
		}
		
		String s = jedisCluster.hmset(key, map);
		
		logger.debug("redis set status-->{} ; key-->{} ; value-->{};",new Object[]{s, key, map});
	}
	@Override
	public Long hset(String key, String field, String value){
		return jedisCluster.hset(key, field, value);
	}
	@Override
	public String hget(String key, String field){
		return jedisCluster.hget(key, field);
	}
	@Override
	public Long hlen(String key){
		return jedisCluster.hlen(key);
	}
	@Override
	public Set<String> hkeys(String key){
		return jedisCluster.hkeys(key);
	}
	@Override
	public List<String> hvals(String key){
		return jedisCluster.hvals(key);
	}
	@Override
	public Map<String, String> hgetall(String key){
		return jedisCluster.hgetAll(key);
	}
	@Override
	public Long hincrBy(String key, String field, long val){
		return jedisCluster.hincrBy(key, field, val);
	}
	@Override
	public Long hdel(String key, String field){
		return jedisCluster.hdel(key, field);
	}
	
	/**
	 * JSON
	 */
	@Override
	public JSONObject getJson(String key) {
		if(StringUtils.isEmpty(key)){
			return null;
		}
		String content = jedisCluster.get(key);
		if(StringUtils.isEmpty(content)){
			return null;
		}
		
		logger.debug("redis get json  key-->{}",new Object[]{key});
		
		return JSONObject.parseObject(content);
	}
	@Override
	public JSONArray getJSONArray(String key, Long start, Long end) {
		start = start == null ? 0 : start;
		end = end == null ? Long.MAX_VALUE : end;
		List<String> strs = jedisCluster.lrange(key, start, end);
		
		JSONArray jsonArray = new JSONArray();
		strs.forEach(s -> jsonArray.add(s));
		
		logger.debug("redis get list  key-->{} ; start-->{}; end-->{}",new Object[]{key,start,end});
		
		return jsonArray;
	}
	
	/**
	 * Set
	 */
	 /*sadd(key, member)：向名称为key的set中添加元素member

     srem(key, member) ：删除名称为key的set中的元素member

     spop(key) ：随机返回并删除名称为key的set中一个元素

     smove(srckey, dstkey, member) ：将member元素从名称为srckey的集合移到名称为dstkey的集合

     scard(key) ：返回名称为key的set的基数

     sismember(key, member) ：测试member是否是名称为key的set的元素

     sinter(key1, key2,…key N) ：求交集

     sinterstore(dstkey, key1, key2,…key N) ：求交集并将交集保存到dstkey的集合

     sunion(key1, key2,…key N) ：求并集

     sunionstore(dstkey, key1, key2,…key N) ：求并集并将并集保存到dstkey的集合

     sdiff(key1, key2,…key N) ：求差集

     sdiffstore(dstkey, key1, key2,…key N) ：求差集并将差集保存到dstkey的集合

     smembers(key) ：返回名称为key的set的所有元素

     srandmember(key) ：随机返回名称为key的set的一个元素*/
	
	/**
	 * zset
	 */
	/*zadd(key, score, member)：向名称为key的zset中添加元素member，score用于排序。如果该元素已经存在，则根据score更新该元素的顺序。

    zrem(key, member) ：删除名称为key的zset中的元素member

    zincrby(key, increment, member) ：如果在名称为key的zset中已经存在元素member，则该元素的score增加increment；否则向集合中添加该元素，其score的值为increment

    zrank(key, member) ：返回名称为key的zset（元素已按score从小到大排序）中member元素的rank（即index，从0开始），若没有member元素，返回“nil”

    zrevrank(key, member) ：返回名称为key的zset（元素已按score从大到小排序）中member元素的rank（即index，从0开始），若没有member元素，返回“nil”

    zrange(key, start, end)：返回名称为key的zset（元素已按score从小到大排序）中的index从start到end的所有元素

    zrevrange(key, start, end)：返回名称为key的zset（元素已按score从大到小排序）中的index从start到end的所有元素

    zrangebyscore(key, min, max)：返回名称为key的zset中score >= min且score <= max的所有元素

    zcard(key)：返回名称为key的zset的基数

    zscore(key, element)：返回名称为key的zset中元素element的score

    zremrangebyrank(key, min, max)：删除名称为key的zset中rank >= min且rank <= max的所有元素

    zremrangebyscore(key, min, max) ：删除名称为key的zset中score >= min且score <= max的所有元素*/

    
    
}

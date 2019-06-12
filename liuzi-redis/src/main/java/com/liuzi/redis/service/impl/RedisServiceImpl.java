package com.liuzi.redis.service.impl;

import java.util.List;

import org.springframework.data.redis.connection.SortParameters.Order;
import org.springframework.data.redis.core.query.SortQueryBuilder;

import com.liuzi.redis.service.RedisService;


public class RedisServiceImpl extends ZSetServiceImpl implements RedisService{
	
	/**
	 * 分页
	 */
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
}

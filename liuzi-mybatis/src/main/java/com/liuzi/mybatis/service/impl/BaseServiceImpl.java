package com.liuzi.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liuzi.mybatis.dao.BaseDao;
import com.liuzi.mybatis.service.BaseService;
import com.liuzi.util.Page;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
	
	//private static Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);
	
    public abstract BaseDao<T> getBaseDao();
    
	@Override
	public List<T> select(Map<String, Object> map){
		return this.select(map, null);
	}
	
	@Override
	public List<T> select(Map<String, Object> map, String sort, String order){
		Map<String, Object> sortList = new HashMap<>();
		sortList.put(sort, order);
		return this.select(map, sortList);
	}
	
	@Override
	public List<T> select(Map<String, Object> map, Map<String, Object> sort){
		map.put("sort_list", sort);
		
		return getBaseDao().select(map);
	}
	
	@Override
	public int selectCount(Map<String, Object> map){
		return getBaseDao().selectCount(map);
	}

	@Override
	public T selectByPrimaryKey(Long id) {
		return getBaseDao().selectByPrimaryKey(id);
	}
	
	@Override
	public Page<T> select(Map<String, Object> map, Integer pageNo, 
			Integer pageSize){
		return this.select(map, pageNo, pageSize, null, null);
	}
	
	@Override
	public Page<T> select(Map<String, Object> map, Integer pageNo, 
			Integer pageSize, String sort, String order){
		Map<String, Object> sortList = new HashMap<>();
		sortList.put(sort, order);
		return this.select(map, pageNo, pageSize, sortList);
	}
	
	@Override
	public Page<T> select(Map<String, Object> map, Integer pageNo, 
			Integer pageSize, Map<String, Object> sort){
		
		Page<T> page = new Page<>(pageNo, pageSize);
		
		map.put("limit", page.getLimit());
		map.put("offset", page.getOffset());
		
		List<T> list = this.select(map, sort);
		int totalCount = this.selectCount(map);
		
		page.setResult(list);//返回数据
		page.setTotalCount(totalCount);//查询总数
		
		int count = totalCount / page.getPageSize();
		if (totalCount % page.getPageSize() > 0) {
	    	++ count;
	    }
		page.setPageTotal(count);//总页数
		
		return page;
	}

	@Override
	public int insert(T record) {
		return getBaseDao().insert(record);
	}

	@Override
	public int insertSelective(T record) {
		return getBaseDao().insertSelective(record);
	}
	
	@Override
	public int insertList(List<T> record){
		return getBaseDao().insertList(record);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return getBaseDao().deleteByPrimaryKey(id);
	}
	
	@Override
	public int deleteByListKey(List<Long> ids){
		return getBaseDao().deleteByListKey(ids);
	}

	@Override
	public int updateByPrimaryKey(T record) {
		return getBaseDao().updateByPrimaryKey(record);
	}

	@Override
	public int updateByPrimaryKeySelective(T record) {
		return getBaseDao().updateByPrimaryKeySelective(record);
	}
	
}

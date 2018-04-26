package com.liuzi.mybatis.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.liuzi.mybatis.dao.BaseVoDao;
import com.liuzi.mybatis.service.BaseVoService;
import com.liuzi.util.Page;

public abstract class BaseVoServiceImpl<T, Vo> extends BaseServiceImpl<T> 
	implements BaseVoService<T, Vo>{
	
    public abstract BaseVoDao<T, Vo> getBaseDao();
    
	@Override
	public List<Vo> selectVo(Map<String, Object> map){
		return this.selectVo(map, null);
	}
	
	@Override
	public List<Vo> selectVo(Map<String, Object> map, String sort, String order){
		LinkedHashMap<String, Object> sortList = null;
		if(!StringUtils.isEmpty(sort)){
			sortList = new LinkedHashMap<>();
			sortList.put(sort, order);
		}
		return this.selectVo(map, sortList);
	}
	
	@Override
	public List<Vo> selectVo(Map<String, Object> map, LinkedHashMap<String, Object> sort){
		map.put("sort_list", sort);
		return getBaseDao().selectVo(map);
	}
	
	@Override
	public int selectCountVo(Map<String, Object> map){
		return getBaseDao().selectCountVo(map);
	}
	
	@Override
	public Vo selectVoByPrimaryKey(Long id) {
		return getBaseDao().selectVoByPrimaryKey(id);
	}

	@Override
	public Page<Vo> selectVo(Map<String, Object> map, Integer pageNo, 
			Integer pageSize){
		return this.selectVo(map, pageNo, pageSize, null, null);
	}
	
	@Override
	public Page<Vo> selectVo(Map<String, Object> map, Integer pageNo, 
			Integer pageSize, String sort, String order){
		LinkedHashMap<String, Object> sortList = null;
		if(!StringUtils.isEmpty(sort)){
			sortList = new LinkedHashMap<>();
			sortList.put(sort, order);
		}
		return this.selectVo(map, pageNo, pageSize, sortList);
	}
	
	@Override
	public Page<Vo> selectVo(Map<String, Object> map, Integer pageNo, 
			Integer pageSize, LinkedHashMap<String, Object> sort){
		
		Page<Vo> page = new Page<>(pageNo, pageSize);
		
		map.put("limit", page.getLimit());
		map.put("offset", page.getOffset());
		
		List<Vo> list = this.selectVo(map, sort);
		int totalCount = this.selectCountVo(map);
		
		page.setResult(list);//返回数据
		page.setTotalCount(totalCount);//查询总数
		
		int count = totalCount / page.getPageSize();
		if (totalCount % page.getPageSize() > 0) {
	    	++ count;
	    }
		page.setPageTotal(count);//总页数
		
		return page;
	}
}

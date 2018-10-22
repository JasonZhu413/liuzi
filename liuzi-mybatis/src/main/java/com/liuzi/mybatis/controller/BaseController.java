package com.liuzi.mybatis.controller;


import org.springframework.web.servlet.ModelAndView;

import com.liuzi.mybatis.pojo.BaseEntity;
import com.liuzi.util.Result;



public interface BaseController<Q extends BaseEntity, T extends BaseEntity> {
	public Result selectList(Q query);

	public Result selectList(Q query, String sort, String order);
   
	public Result selectPage(Q query, Integer pageNo, Integer pageSize);
   
	public Result selectPage(Q query, Integer pageNo, Integer pageSize,
		   String sort, String order);
   
	public ModelAndView addView();
   
	public ModelAndView editView(Long id);

	public ModelAndView detailView(Long id);
   
	public Result save(T entity);
   
	public Result edit(T entity);
   
	public Result delete(Long id);
   
	public Result deleteByIds(String id);

}
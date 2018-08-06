package com.liuzi.mybatis.controller;


import org.springframework.web.servlet.ModelAndView;

import com.liuzi.mybatis.pojo.BaseEntity;
import com.liuzi.util.Result;



public interface BaseController<Q extends BaseEntity, T extends BaseEntity> {
	public ModelAndView selectList(Q query);

	public ModelAndView selectList(Q query, String sort, String order);
   
	public ModelAndView selectPage(Q query, Integer pageNo, Integer pageSize);
   
	public ModelAndView selectPage(Q query, Integer pageNo, Integer pageSize,
		   String sort, String order);
   
	public ModelAndView addView();
   
	public ModelAndView editView(Long id);

	public ModelAndView detailView(Long id);
   
	public Result add(T entity);
   
	public Result edit(T entity);
   
	public Result delete(Long id);
   
	public Result deleteByIds(String id);

}
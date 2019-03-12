package com.liuzi.mybatis.controller;


import org.springframework.web.servlet.ModelAndView;

import com.liuzi.mybatis.pojo.BaseEntity;
import com.liuzi.util.Result;



public interface BaseController<Q extends BaseEntity, T extends BaseEntity> {
	public Result2 selectList(Q query);

	public Result2 selectList(Q query, String sort, String order);
   
	public Result2 selectPage(Q query, Integer pageNo, Integer pageSize);
   
	public Result2 selectPage(Q query, Integer pageNo, Integer pageSize,
		   String sort, String order);
   
	public ModelAndView addView();
   
	public ModelAndView editView(Long id);

	public ModelAndView detailView(Long id);
   
	public Result2 save(T entity);
   
	public Result2 edit(T entity);
   
	public Result2 delete(Long id);
   
	public Result2 deleteByIds(String id);

}
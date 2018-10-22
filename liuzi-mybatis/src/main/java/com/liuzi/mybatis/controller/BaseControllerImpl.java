package com.liuzi.mybatis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.liuzi.mybatis.pojo.BaseEntity;
import com.liuzi.mybatis.pojo.Page;
import com.liuzi.mybatis.service.BaseService;
import com.liuzi.util.LiuziUtil;
import com.liuzi.util.Result;

import java.util.List;
import java.util.Map;


public abstract class BaseControllerImpl<Q extends BaseEntity, T extends BaseEntity> implements BaseController<Q, T> {
	
   private Logger log = LoggerFactory.getLogger(BaseControllerImpl.class);
   
   /**
    * @fields path 页面路径信息
    */
   protected ControllerPath path = new ControllerPath(this.getClass());

   /**
    * 获取基础的服务
    * @return BaseService
    */
   protected abstract BaseService<T> getBaseService();
   
   @Override
   @RequestMapping(method = RequestMethod.POST)
   @ResponseBody
   public Result selectList(Q query) {
	  Map<String, Object> map = LiuziUtil.object2Map(query);
      List<T> list = getBaseService().select(map);
      return new Result(list);
   }
   
   @Override
   @RequestMapping(method = RequestMethod.POST)
   @ResponseBody
   public Result selectList(Q query, String sort, String order) {
	  Map<String, Object> map = LiuziUtil.object2Map(query);
      List<T> list = getBaseService().select(map, sort, order);
      return new Result(list);
   }
   
   @Override
   @RequestMapping(method = RequestMethod.POST)
   @ResponseBody
   public Result selectPage(Q query, Integer pageNo, Integer pageSize) {
	  Map<String, Object> map = LiuziUtil.object2Map(query);
      Page<T> page = getBaseService().select(map, pageNo, pageSize);
      return new Result(page);
   }
   
   @Override
   @RequestMapping(method = RequestMethod.POST)
   @ResponseBody
   public Result selectPage(Q query, Integer pageNo, Integer pageSize,
		   String sort, String order) {
	  Map<String, Object> map = LiuziUtil.object2Map(query);
      Page<T> page = getBaseService().select(map, pageNo, pageSize, sort, order);
      return new Result(page);
   }
   
   @Override
   @RequestMapping(value = "/add", method = RequestMethod.GET)
   public ModelAndView addView() {
      return new ModelAndView(path.getAddViewPath());
   }
   
   @Override
   @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
   public ModelAndView editView(@PathVariable("id") Long id) {
      Object obj = getBaseService().selectByPrimaryKey(id);
      return new ModelAndView(path.getEditViewPath(), path.getEntityName(), obj);
   }

   @Override
   @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
   public ModelAndView detailView(@PathVariable("id") Long id) {
      Object obj = getBaseService().selectByPrimaryKey(id);
      return new ModelAndView(path.getOneViewPath(), path.getEntityName(), obj);
   }
   
   /*
    * ajax Post添加数据
    */
   @Override
   @ResponseBody
   @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
   public Result save(T entity){
      getBaseService().insertSelective(entity);
      return new Result();
   }

   /*
    * ajax Post修改数据
    */
   @Override
   @ResponseBody
   @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
   public Result edit(T entity){
      getBaseService().updateByPrimaryKeySelective(entity);
      return new Result(1);
   }
   
   @Override
   @ResponseBody
   @RequestMapping(value = "/delete", method = RequestMethod.DELETE, 
   	produces = MediaType.APPLICATION_JSON_VALUE)
   public Result delete(@PathVariable("id") Long id) {
      if (id == null || id == 0) {
         log.error("要删除的ID号为null或空字符串！对象：{}", path.getEntityName());
         return new Result(0, "要删除的记录不存在！");
      }
      int count = getBaseService().deleteByPrimaryKey(id);
      if (count == 0){
    	  return new Result(0, "要删除的记录不存在！");
      }
      return new Result();
   }
   
   
   @Override
   @ResponseBody
   @RequestMapping(value = "/delete/by/ids", method = RequestMethod.DELETE, 
   	produces = MediaType.APPLICATION_JSON_VALUE)
   public Result deleteByIds(String id) {
	   List<Long> ids = LiuziUtil.ids(id);
	   if (ids == null) {
		   log.error("未设置批量删除对象的ID号！对象：{}", path.getEntityName());
		   return new Result(0, "要删除的记录不存在！");
	   }
	   try {
		   getBaseService().deleteByListKey(ids);
	   } catch (Exception e) {
		   log.error("批量删除对象失败！对象:" + path.getEntityName(), e);
		   return new Result(0, "批量删除失败！");
      }
      return new Result();
   }

   

   
   
}
package com.liuzi.mybatis.currency.cond;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.liuzi.mybatis.currency.consts.HandlerConsts;
import com.liuzi.mybatis.currency.data.Sort;
import com.liuzi.mybatis.server.pojo.Page;

import lombok.Getter;
import lombok.Setter;


/**
 * 条件
 * @author zsy
 */
public class Query extends HashMap<String, Object>{
	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private List<Condition> condition;
	
	public static QueryBuilder of(){
		return new QueryBuilder();
	}
	
	public static <T> Query ofNull(){
		return new Query();
	}
	
	public static class QueryBuilder{
		
		private Query query;
		private List<Condition> condition;
		private Map<String, String> orderBy;
		private List<String> groupBy;
		
		public QueryBuilder(){
			this.query = new Query();
			
			this.condition = new ArrayList<>();
			this.orderBy = new LinkedHashMap<>();
			this.groupBy = new ArrayList<>();
		}
		
		public QueryBuilder eq(String column, Object value){
			this.query.put(column, value);
			this.condition.add(new Condition(column, value, "eq"));
			return this;
		}
		
		public QueryBuilder neq(String column, Object value){
			this.query.put(column, value);
			this.condition.add(new Condition(column, value, "neq"));
			return this;
		}
		
		public QueryBuilder like(String column, Object value){
			this.query.put(column, value);
			this.condition.add(new Condition(column, value, "like"));
			return this;
		}
		
		public QueryBuilder gt(String column, Object value){
			this.query.put(column, value);
			this.condition.add(new Condition(column, value, "gt"));
			return this;
		}
		
		public QueryBuilder lt(String column, Object value){
			this.query.put(column, value);
			this.condition.add(new Condition(column, value, "lt"));
			return this;
		}
		
		public QueryBuilder gte(String column, Object value){
			this.query.put(column, value);
			this.condition.add(new Condition(column, value, "gte"));
			return this;
		}
		
		public QueryBuilder lte(String column, Object value){
			this.query.put(column, value);
			this.condition.add(new Condition(column, value, "lte"));
			return this;
		}
		
		public QueryBuilder in(String column, Object... values){
			this.query.put(column, values);
			this.condition.add(new Condition(column, values, "in"));
			return this;
		}
		
		public QueryBuilder notIn(String column, Object... values){
			this.query.put(column, values);
			this.condition.add(new Condition(column, values, "notIn"));
			return this;
		}
		
		public QueryBuilder groupBy(String... columns){
			this.groupBy.addAll(Arrays.asList(columns));
			return this;
		}
		
		public QueryBuilder orderBy(Sort sort){
			List<Sort> list = sort.getAll();
			for(Sort s : list){
				this.orderBy.put(s.getSort(), s.getOrder().toString());
			}
			return this;
		}
		
		public QueryBuilder page(int pageNo, int pageSize){
			//创建分页对象
			Page<?> page = new Page<>(pageNo, pageSize);
			
			this.query.put(HandlerConsts.$LIMIT, page.getLimit());
			this.query.put(HandlerConsts.$OFFSET, page.getOffset());
			
			//传入分页参数对象
			this.query.put(HandlerConsts.$PAGE, page);
			return this;
		}
		
		public Query build(){
			this.query.put(HandlerConsts.$GROUP_BY, this.groupBy);
			this.query.put(HandlerConsts.$ORDER_BY, this.orderBy);
			this.query.setCondition(this.condition);
			return this.query;
		}
	}
		
}

package com.liuzi.mybatis.currency.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Sort implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 排序字段
	 */
	private String sort;
	/**
	 * 顺序
	 */
	private Order order;
	
	private List<Sort> all;
	
	public Sort(){
		all = new ArrayList<>();
	}
	
	public Sort(String sort, Order order){
		this.sort = sort;
		this.order = order;
		all = new ArrayList<>();
		all.add(this);
	}
	
	public Sort add(String sort, Order order){
		order = order == null ? Order.DESC : order;
		all.add(new Sort(sort, order));
		return this;
	}
	
	public Sort add(Sort sort){
		all.addAll(sort.getAll());
		return this;
	}
}

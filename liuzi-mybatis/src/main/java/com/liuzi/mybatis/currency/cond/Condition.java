package com.liuzi.mybatis.currency.cond;

import java.io.Serializable;

import com.liuzi.mybatis.currency.cond.Query.QueryBuilder;

import lombok.Getter;


/**
 * 条件
 * @author zsy
 */
@Getter
public class Condition implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String column;
	
	private Object value;
	
	private String cond;
	
	public Condition(String column, Object value, String cond){
		this.column = column;
		this.value = value;
		this.cond = cond;
	}
	
	public SQL condition(SQL sql){
		if(sql == null){
			return null;
		}
		switch(this.getCond()){
			case "eq":
				sql.EQ(this.getColumn(), this.getValue());
				break;
			case "neq":
				sql.NEQ(this.getColumn(), this.getValue());
				break;
			case "like":
				sql.LIKE(this.getColumn(), this.getValue());
				break;
			case "gt":
				sql.GT(this.getColumn(), this.getValue());
				break;
			case "lt":
				sql.LT(this.getColumn(), this.getValue());
				break;
			case "gte":
				sql.GTE(this.getColumn(), this.getValue());
				break;
			case "lte":
				sql.LTE(this.getColumn(), this.getValue());
				break;
			case "in":
				sql.IN(this.getColumn(), this.getValue());
				break;
			case "notIn":
				sql.NOT_IN(this.getColumn(), this.getValue());
				break;
			default:
				break;
		}
		
		return sql;
	}
}

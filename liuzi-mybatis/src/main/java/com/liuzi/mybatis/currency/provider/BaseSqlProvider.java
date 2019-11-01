package com.liuzi.mybatis.currency.provider;

import java.util.Map;

import org.apache.ibatis.builder.annotation.ProviderContext;

import com.liuzi.mybatis.currency.cond.SQL;


/**
 * 通用实现
 * @author Administrator
 *
 */
public class BaseSqlProvider{
	
	/**
	 * 根据SQL
	 */
	public String $commonBySql(ProviderContext context, Map<String, Object> para){
		SQL sql = (SQL) para.get("sql");
        return sql.toString();
    }
	
}

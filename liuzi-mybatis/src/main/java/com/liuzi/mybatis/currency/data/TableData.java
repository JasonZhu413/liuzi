package com.liuzi.mybatis.currency.data;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.liuzi.mybatis.currency.anno.Table;
import com.liuzi.mybatis.currency.util.DataUtil;

import lombok.Data;

@Data
public class TableData implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 表名称
	 */
	private String name;
	/**
	 * 表别名
	 */
	private String as;
	
	public TableData(){}
	
	public TableData(Class<?> clazz){
		this(clazz, null);
	}
	
	public TableData(Class<?> clazz, String as){
		//表名，默认为类名转换下划线
		this.name = DataUtil.camelToUnderline(clazz.getSimpleName());
		//别名，默认表名
		this.as = this.name;
		//获取注解
        Table table = clazz.getAnnotation(Table.class);
        if(table != null){
	        //表名
	        String name = table.name();
	        this.name = !StringUtils.isBlank(name) ? name : this.name;
	        //别名
	        String tas = table.as();
	        this.as = !StringUtils.isBlank(tas) ? tas : this.as;
        }
        
        this.as = !StringUtils.isBlank(as) ? as : this.as;
	}
} 

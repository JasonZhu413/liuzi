package com.liuzi.mybatis.currency.data;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;

import com.liuzi.mybatis.currency.util.DataUtil;

import lombok.Data;

@Data
public class ColumnData implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 属性名称
	 */
	private String property;
	/**
	 * 数据库字段名称
	 */
	private String column;
	/**
	 * 数据库字段别名
	 */
	private String as;
	/**
	 * 是否为主键
	 */
	private boolean pk = false;
	/**
	 * 属性类型
	 */
	private Class<?> fieldType;
	
	public ColumnData(){}
	
	public ColumnData(String property, String column, String as, boolean pk, Class<?> fieldType){
		//属性名
		this.property = property;
		//字段名，默认转换下划线
		this.column = StringUtils.isBlank(column) ? DataUtil.camelToUnderline(property) : column;
		//字段别名
		this.as = StringUtils.isBlank(as) ? this.property : as;
		//是否为主键
		this.pk = pk;
		//属性类型
		this.fieldType = fieldType;
	}
	
	public ColumnData(Field field){
		//属性名称
		this.property = field.getName();
		//数据库字段名称，默认类字段名
		this.column = DataUtil.camelToUnderline(this.property);
		//别名，默认属性名称
		this.as = this.property;
		//是否为主键
		this.pk = false;
		
		//获取注解
		com.liuzi.mybatis.currency.anno.Field myField = 
				field.getAnnotation(com.liuzi.mybatis.currency.anno.Field.class);
        if (myField != null) {
        	//字段名
        	String myFieldColumn = myField.column();
        	this.column = !StringUtils.isBlank(myFieldColumn) ? myFieldColumn : this.column;
        	//别名
        	String myFieldAs = myField.as();
        	this.as = !StringUtils.isBlank(myFieldAs) ? myFieldAs : this.as;
        	//是否为主键
        	this.pk = myField.pk();
        }
        this.fieldType = field.getType();
	}
} 

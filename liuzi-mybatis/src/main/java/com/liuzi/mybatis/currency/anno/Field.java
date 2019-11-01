package com.liuzi.mybatis.currency.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
	
	/**
	 * 是否使用（做crud等操作，默认使用）
	 * @return
	 */
	public boolean used() default true;
	/**
	 * 是否为主键
	 * @return
	 */
	public boolean pk() default false;
	/**
	 * 指定数据库字段名称
	 * @return
	 */
	public String column() default "";
	/**
	 * 别名
	 * @return
	 */
	public String as() default "";
	/**
	 * 描述（暂未使用）
	 * @return
	 */
	public String desc() default "";
	
}

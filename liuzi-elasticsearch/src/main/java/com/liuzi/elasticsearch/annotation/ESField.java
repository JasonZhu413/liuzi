package com.liuzi.elasticsearch.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.liuzi.elasticsearch.enums.Analyzer;
import com.liuzi.elasticsearch.enums.FieldType;
import com.liuzi.elasticsearch.enums.DateFormat;

/**
 * @description: 对应索引结构mapping的注解，在es entity field上添加
 * @author: zsy
 * @create: 2019-01-25 16:57
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ESField {
	/**
     * 数据类型（包含 关键字类型）
     */
	FieldType type() default FieldType.Auto;
    /**
     * 日期
     */
    DateFormat format() default DateFormat.none;
    /**
     * 时间格式
     * @return
     */
    String pattern() default "";
    /**
     * 间接关键字
     */
    boolean keyword() default true;
    /**
     * 关键字忽略字数
     */
    int ignoreAbove() default 256;
    /**
     * 是否支持autocomplete，高效全文搜索提示
     */
    boolean autocomplete() default false;
    /**
     * 是否支持suggest，高效前缀搜索提示
     */
    boolean suggest() default false;
    /**
     * 索引分词器设置（研究类型）
     */
    Analyzer analyzer() default Analyzer.standard;
    /**
     * 搜索内容分词器设置
     */
    Analyzer searchAnalyzer() default Analyzer.standard;
    /**
     * 是否允许被搜索
     */
    boolean allowSearch() default true;

    /**
     * 拷贝到哪个字段，代替_all
     */
    String copyTo() default "";
}

package com.liuzi.elasticsearch.util;

import lombok.Data;

import com.liuzi.elasticsearch.enums.Analyzer;
import com.liuzi.elasticsearch.enums.DateFormat;
import com.liuzi.elasticsearch.enums.FieldType;

/**
 * @description: mapping注解对应的数据载体类
 * @author: zsy
 * @create: 2019-01-29 15:09
 **/
@Data
public class MappingData {
	/**
	 * 字段名
	 */
    String fieldName;
    /**
     * 数据类型（包含 关键字类型）
     * @return
     */
    FieldType type;
    /**
     * 日期
     */
    DateFormat format;
    /**
     * 时间格式
     * @return
     */
    String pattern;
    /**
     * 间接关键字
     * @return
     */
    boolean keyword;
    /**
     * 关键字忽略字数
     * @return
     */
    int ignoreAbove;
    /**
     * 是否支持autocomplete，高效全文搜索提示
     * @return
     */
    boolean autocomplete;
    /**
     * 是否支持suggest，高效前缀搜索提示
     * @return
     */
    boolean suggest;
    /**
     * 索引分词器设置
     * @return
     */
    Analyzer analyzer;
    /**
     * 搜索内容分词器设置
     * @return
     */
    Analyzer searchAnalyzer;

    private boolean allowSearch = true;

    private String copyTo;
}

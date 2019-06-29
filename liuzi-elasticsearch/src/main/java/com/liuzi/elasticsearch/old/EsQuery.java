package com.liuzi.elasticsearch.old;

import lombok.Builder;
import lombok.Data;

import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;

/**
 * 查询参数
 * @author zsy
 */
@Data
@Builder
public class EsQuery{
	
	/**
	 * tables
	 * index_table*
	 * index_table_date(yyyyMMdd)
	 */
	private EsTable[] tables;
	/**
	 * 查询字段
	 */
	private String[] includes;
	/**
	 * 不包含字段
	 */
	private String[] excludes;
	/**
	 * 第几页
	 */
	private int pageNo;
	/**
	 * 每页多少条
	 */
	private int pageSize;
	/**
	 * 高亮
	 */
	private String[] highlight;
	/**
	 * 是否匹配所有文档
	 */
	private boolean matchAllQuery;
	/**
	 * 条件匹配
	 * EsConditions.AND.terms(name, value)
	 */
	private EsConditions[] conditions;
	/**
	 * 排序
	 * EsSort.defaultSort()
	 */
	private SortBuilder<?>[] sort;
	/**
	 * 聚合
	 * 嵌套1.EsAggregation.groupBy().subAggregation(EsAggregation.groupBy())
	 * 嵌套2.EsAggregation.groupBy().subAggregation(EsAggregation.having())
	 */
	private AbstractAggregationBuilder<?> aab;
}

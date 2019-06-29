package com.liuzi.elasticsearch.old;

import java.util.Map;

import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;



/**
 * 聚合
 * @author zsy
 */
public class EsAggregation {
	
	/**
	 * group by
	 * @param name
	 * @param field
	 * @return
	 */
	public static AggregationBuilder groupBy(String name, String field){
		return AggregationBuilders.terms(name).field(field);
	}
	
	/**
	 * 管道聚合，类似having count(*) > 10
	 * @param name
	 * @param bucketsPathsMap
	 * @param script
	 * @return
	 */
	public static PipelineAggregationBuilder having(String name, Map<String,String> bucketsPathsMap, 
			String script){
		return PipelineAggregatorBuilders.bucketScript(name, bucketsPathsMap, new Script(script));
	}
}

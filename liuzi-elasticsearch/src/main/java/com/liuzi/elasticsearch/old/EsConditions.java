package com.liuzi.elasticsearch.old;

import java.util.Map;

import lombok.Getter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.springframework.util.StringUtils;


/**
 * 检索条件
 * @author zsy
 *
 */
public enum EsConditions{
	
	AND("filter"),
	AND_WITH_SCORE("must"),
	OR("should"),
	NOT("mustNot");
	
	@Getter private String type;
	@Getter private QueryBuilder queryBuilder;
	
	private EsConditions(String type){
		this.type = type;
	}
	
	/**
	 * 一属性，多值
	 * @param name
	 * @param values
	 */
	public EsConditions terms(String name, Object... values){
		return terms(name, 0, values);
	}
	
	/**
	 * 一属性，多值
	 * @param name
	 * @param boost
	 * @param values
	 */
	public EsConditions terms(String name, float boost, Object... values){
		if(StringUtils.isEmpty(name) || values == null || values.length == 0){
			return null;
		}
		queryBuilder = QueryBuilders.termsQuery(name, values).boost(boost);
		return this;
	}
	
	/**
	 * 多属性，一值
	 * @param value
	 * @param names
	 */
	public EsConditions matchs(Object value, String... names){
		return matchs(value, 0, "standard", names);
	}
	
	/**
	 * 多属性，一值
	 * @param value
	 * @param analyzer
	 * @param names
	 */
	public EsConditions matchs(Object value, String analyzer, String... names){
		return matchs(value, 0, analyzer, names);
	}
	
	/**
	 * 多属性，一值
	 * @param value
	 * @param boost
	 * @param names
	 */
	public EsConditions matchs(Object value, float boost, String... names){
		return matchs(value, boost, "standard", names);
	}
	
	/**
	 * 多属性，一值
	 * @param value
	 * @param boost
	 * @param analyzer
	 * @param names
	 */
	public EsConditions matchs(Object value, float boost, String analyzer, String... names){
		if(StringUtils.isEmpty(value) || names == null || names.length == 0){
			return null;
		}
		queryBuilder = QueryBuilders.multiMatchQuery(value, names)
				.boost(boost)
				.analyzer(analyzer);
		return this;
	}
	
	/**
	 * 大于
	 * @param name
	 * @param gt
	 */
	public EsConditions rangGt(String name, Object gt){
		return rangGt(name, gt, 0); 
	}
	
	/**
	 * 大于
	 * @param name
	 * @param gt
	 * @param boost
	 */
	public EsConditions rangGt(String name, Object gt, float boost){
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(gt)){
			return null;
		}
		
		queryBuilder = QueryBuilders.rangeQuery(name).gt(gt).boost(boost); 
		return this;
	}
	
	/**
	 * 大于等于
	 * @param name
	 * @param gte
	 */
	public EsConditions rangGte(String name, Object gte){
		return rangGte(name, gte, 0);
	}
	
	/**
	 * 大于等于
	 * @param name
	 * @param gte
	 * @param boost
	 */
	public EsConditions rangGte(String name, Object gte, float boost){
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(gte)){
			return null;
		}
		
		queryBuilder = QueryBuilders.rangeQuery(name).gte(gte).boost(boost); 
		return this;
	}
	
	/**
	 * 小于
	 * @param name
	 * @param lt
	 */
	public EsConditions rangLt(String name, Object lt){
		return rangLt(name, lt, 0); 
	}
	/**
	 * 小于
	 * @param name
	 * @param lt
	 * @param boost
	 */
	public EsConditions rangLt(String name, Object lt, float boost){
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(lt)){
			return null;
		}
		
		queryBuilder = QueryBuilders.rangeQuery(name).lt(lt).boost(boost); 
		return this;
	}
	
	/**
	 * 小于等于
	 * @param name
	 * @param lte
	 */
	public EsConditions rangLte(String name, Object lte){
		return rangLte(name, lte, 0); 
	}
	/**
	 * 小于等于
	 * @param name
	 * @param lte
	 * @param boost
	 */
	public EsConditions rangLte(String name, Object lte, float boost){
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(lte)){
			return null;
		}
		
		queryBuilder = QueryBuilders.rangeQuery(name).lte(lte).boost(boost);
		return this;
	}
	
	/**
	 * 区间
	 */
	public EsConditions rang(String name, Object from, Object to){
		return rang(name, from, to, false, false, 0);
	}
	/**
	 * 区间
	 */
	public EsConditions rang(String name, Object from, Object to, float boost){
		return rang(name, from, to, false, false, boost);
	}
	/**
	 * 区间
	 */
	public EsConditions rang(String name, Object from, Object to, boolean includeLower, 
			boolean includeUpper){
		return rang(name, from, to, includeLower, includeUpper, 0);
	}
	/**
	 * 区间
	 */
	public EsConditions rang(String name, Object from, Object to, boolean includeLower, 
			boolean includeUpper, float boost){
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(from) || StringUtils.isEmpty(to)){
			return null;
		}
		
		queryBuilder = QueryBuilders.rangeQuery(name)
				.from(from)
				.to(to)
				.includeLower(includeLower)
	            .includeUpper(includeUpper)
	            .boost(boost);
		return this;
	}
	
	/**
	 * 前缀
	 */
	public EsConditions prefix(String name, String prefix){
		return prefix(name, prefix, 0);
	}
	/**
	 * 前缀
	 */
	public EsConditions prefix(String name, String prefix, float boost){
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(prefix)){
			return null;
		}
		queryBuilder = QueryBuilders.prefixQuery(name, prefix).boost(boost);
		return this;
	}
	
	/**
	 * 模糊查询
	 */
	public EsConditions fuzzy(String name, Object value){
		return fuzzy(name, value, 0);
	}
	/**
	 * 模糊查询
	 */
	public EsConditions fuzzy(String name, Object value, float boost){
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(value)){
			return null;
		}
		queryBuilder = QueryBuilders.fuzzyQuery(name, value).boost(boost);

		return this;
	}
	
	/**
	 * 通配符
	 */
	public EsConditions wildcard(String name, String query){
		return wildcard(name, query, 0);
	}
	/**
	 * 通配符
	 */
	public EsConditions wildcard(String name, String query, float boost){
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(query)){
			return null;
		}
		queryBuilder = QueryBuilders.wildcardQuery(name, query).boost(boost);
		return this;
	}
	
	/**
	 * 正则
	 */
	public EsConditions regexp(String name, String regexp){
		return regexp(name, regexp, 0);
	}
	/**
	 * 正则
	 */
	public EsConditions regexp(String name, String regexp, float boost){
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(regexp)){
			return null;
		}
		queryBuilder = QueryBuilders.regexpQuery(name, regexp).boost(boost);
		return this;
	}
	
	/**
	 * script
	 */
	public EsConditions script(String script, Map<String, Object> params){
		return script(script, params, 0);
	}
	
	/**
	 * script
	 */
	public EsConditions script(String script, Map<String, Object> params, float boost){
		if(StringUtils.isEmpty(script)){
			return null;
		}
		queryBuilder = QueryBuilders.scriptQuery(
	            new Script(Script.DEFAULT_SCRIPT_TYPE, 
	                    Script.DEFAULT_SCRIPT_LANG, 
	                    script, 
	                    params)).boost(boost);
		return this;
	}
	
	/**
	 * script
	 */
	public EsConditions script(EsScript esScript){
		if(esScript == null || StringUtils.isEmpty(esScript.getScript())){
			return null;
		}
		queryBuilder = QueryBuilders.scriptQuery(
	            new Script(Script.DEFAULT_SCRIPT_TYPE, 
	                    Script.DEFAULT_SCRIPT_LANG, 
	                    esScript.getScript(), 
	                    esScript.getParams()))
	                    .boost(esScript.getBoost());
		return this;
	}
	
}

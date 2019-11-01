package com.liuzi.elasticsearch.old;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.util.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.liuzi.mybatis.pojo.BaseEntity;
import com.liuzi.util.common.Log;



/**
 * index = database-${table}*或database-${table}-date(yyyyMMdd)
 * type = _doc
 * @author zsy
 */
public class Elasticsearch extends ElasticsearchBase{
	
	public Elasticsearch(String server, String clusterName, String database) {
		super(server, clusterName, database, null, null);
	}
	public Elasticsearch(String server, String clusterName, String database, String type) {
		super(server, clusterName, database, type, null);
	}
	public Elasticsearch(String server, String clusterName, String database, Long scrollTimeInMillis) {
		super(server, clusterName, database, null, scrollTimeInMillis);
	}
	public Elasticsearch(String server, String clusterName, String database, String type, 
			Long scrollTimeInMillis) {
		super(server, clusterName, database, type, scrollTimeInMillis);
	}
	
	/**
	 * 创建索引
	 * @param clazz
	 * @return boolean
	 */
	@SuppressWarnings({"rawtypes" })
	public <T> boolean create(Class<T> clazz){
		Map setting = this.getSetting(clazz);
		
		Document doc = getDocument(clazz);
		doc.replicas();
		doc.shards();
		
		//处理index的配置
		IndexMetaData.Builder builder = IndexMetaData.builder("");
		
		
		
        /*//加入模板中的setting
        for (int i = templates.size() - 1; i >= 0; i--) {
            indexSettingsBuilder.put(templates.get(i).settings());
        }
        // 加入request中的mapping，request中设置会覆盖模板中的设置
        indexSettingsBuilder.put(request.settings());
//处理shard，shard数量不能小于1，因此这里需要特殊处理，如果没有则要使用默认值
        if (request.index().equals(ScriptService.SCRIPT_INDEX)) {
            indexSettingsBuilder.put(SETTING_NUMBER_OF_SHARDS, settings.getAsInt(SETTING_NUMBER_OF_SHARDS, 1));
        } else {
            if (indexSettingsBuilder.get(SETTING_NUMBER_OF_SHARDS) == null) {
                if (request.index().equals(riverIndexName)) {
                    indexSettingsBuilder.put(SETTING_NUMBER_OF_SHARDS, settings.getAsInt(SETTING_NUMBER_OF_SHARDS, 1));
                } else {
                    indexSettingsBuilder.put(SETTING_NUMBER_OF_SHARDS, settings.getAsInt(SETTING_NUMBER_OF_SHARDS, 5));
                }
            }
        }
        if (request.index().equals(ScriptService.SCRIPT_INDEX)) {
            indexSettingsBuilder.put(SETTING_NUMBER_OF_REPLICAS, settings.getAsInt(SETTING_NUMBER_OF_REPLICAS, 0));
            indexSettingsBuilder.put(SETTING_AUTO_EXPAND_REPLICAS, "0-all");
        }
        else {
            if (indexSettingsBuilder.get(SETTING_NUMBER_OF_REPLICAS) == null) {
                if (request.index().equals(riverIndexName)) {
                    indexSettingsBuilder.put(SETTING_NUMBER_OF_REPLICAS, settings.getAsInt(SETTING_NUMBER_OF_REPLICAS, 1));
                } else {
                    indexSettingsBuilder.put(SETTING_NUMBER_OF_REPLICAS, settings.getAsInt(SETTING_NUMBER_OF_REPLICAS, 1));
                }
            }
        }
//处理副本
        if (settings.get(SETTING_AUTO_EXPAND_REPLICAS) != null && indexSettingsBuilder.get(SETTING_AUTO_EXPAND_REPLICAS) == null) {
            indexSettingsBuilder.put(SETTING_AUTO_EXPAND_REPLICAS, settings.get(SETTING_AUTO_EXPAND_REPLICAS));
        }

        if (indexSettingsBuilder.get(SETTING_VERSION_CREATED) == null) {
            DiscoveryNodes nodes = currentState.nodes();
            final Version createdVersion = Version.smallest(version, nodes.smallestNonClientNodeVersion());
            indexSettingsBuilder.put(SETTING_VERSION_CREATED, createdVersion);
        }

        if (indexSettingsBuilder.get(SETTING_CREATION_DATE) == null) {
            indexSettingsBuilder.put(SETTING_CREATION_DATE, System.currentTimeMillis());
        }

        indexSettingsBuilder.put(SETTING_UUID, Strings.randomBase64UUID());
//创建setting
        Settings actualIndexSettings = indexSettingsBuilder.build();
		
		*/
		String indexName = doc.indexName();
		this.createIndex(clazz);
		boolean success = this.createIndex(clazz, setting);
		if(success){
			success = this.putMapping(clazz);
		}
		return success;
	}
	
	
	public <T> int saveOrUpdate(BaseEntity... baseEntity) {
		if (CollectionUtils.isEmpty(baseEntity)) {
			return 0;
		}
		
		UpdateQuery uq;
		List<UpdateQuery> bulkIndex = new ArrayList<>();
		for (BaseEntity be : baseEntity) {
			uq = new UpdateQuery();
			uq.setIndexName(getDocument(be).indexName());
			uq.setType(_type);
			uq.setId(be.getId() + "");
			uq.setUpdateRequest(new UpdateRequest(
					uq.getIndexName(), 
					uq.getType(), 
					uq.getId()).doc(JSONObject.toJSONString(be, SerializerFeature.WriteMapNullValue)));
			uq.setDoUpsert(true);
			uq.setClazz(be.getClass());
			bulkIndex.add(uq);
		}
		this.bulkUpdate(bulkIndex);
		
		return baseEntity.length;
	}
	
	
	/**
	 * 新增数据
	 * @param table
	 * @param baseEntity
	 * @return String
	 */
	public String add(String table, BaseEntity baseEntity){
		return add(table, null, baseEntity);
	}
	
	/**
	 * 新增数据
	 * @param table
	 * @param date
	 * @param baseEntity
	 * @return String
	 */
	public String add(String table, String date, BaseEntity baseEntity){
		if(baseEntity == null || baseEntity.getId() == null){
			throw new IllegalArgumentException("item \"data\" is null");
		}
		try{
			IndexQuery indexQuery = new IndexQueryBuilder()
	        	.withIndexName(_index(table, date))
	        	.withType(_type)
	        	.withId(baseEntity.getId() + "")
	        	.withObject(baseEntity)
	        	.build();
			return this.index(indexQuery);
		}catch(Exception e){
			Log.error(e, "ES add error");
		}
		return null;
	}
	
	/**
	 * 删除database-${table}下所有数据
	 * @param table
	 */
	public void delete(String table){
		delete(table, null);
	}
	
	/**
	 * 删除database-${table}-${date}下所有数据
	 * @param table
	 * @param date
	 */
	public void delete(String table, String date){
		try{
	        DeleteQuery deleteQuery = new DeleteQuery();
	        deleteQuery.setIndex(_index(table, date));
	        deleteQuery.setType(_type);
	        this.delete(deleteQuery);
		}catch(Exception e){
			Log.error(e, "ES delete error");
		}
	}
	
	/**
	 * 删除查询出的所有数据
	 * @param matchQueryBuilder
	 */
	public void delete(MatchQueryBuilder matchQueryBuilder){
		try{
			DeleteQuery deleteQuery = new DeleteQuery();
			deleteQuery.setQuery(matchQueryBuilder);
		    this.delete(deleteQuery);
		}catch(Exception e){
			Log.error(e, "ES delete error");
		}
	}
	
	/**
	 * 修改数据
	 * @param table
	 * @param baseEntity
	 * @return UpdateResponse
	 */
	public UpdateResponse update(String table, BaseEntity baseEntity){
		return update(table, null);
	}
	
	
	/**
	 * 修改数据
	 * @param table
	 * @param date
	 * @param baseEntity
	 * @return UpdateResponse
	 */
	public UpdateResponse update(String table, String date, BaseEntity baseEntity){
		if(baseEntity == null || baseEntity.getId() == null){
			throw new IllegalArgumentException("item \"data\" is null");
		}
		
		try{
			UpdateQuery updateQuery = new UpdateQueryBuilder()
	        	.withIndexName(_index(table, date))
	        	.withType(_type)
	        	.withId(baseEntity.getId() + "")
	        	.build();
	
			return this.update(updateQuery);
		}catch(Exception e){
			Log.error(e, "ES update error");
		}
		return null;
	}
	
	/**
	 * 查询列表
	 * @param esQuery
	 * @param clazz
	 * @return
	 */
	public <T> List<T> list(EsQuery esQuery, Class<T> clazz){
		SearchQuery searchQuery = _query(esQuery).build();
	    Log.info("[ES search list] query DSL: {}", searchQuery.getQuery().toString());
	    return this.queryForList(searchQuery, clazz);
	}
	
	/**
	 * 分页查询
	 * @param esQuery
	 * @param clazz
	 * @return
	 */
	public <T> com.liuzi.mybatis.pojo.Page<T> page(EsQuery esQuery, Class<T> clazz){
		//log.info("[ES search page] start...");
		
		//查询builder
	    NativeSearchQueryBuilder builder = _query(esQuery);
	    //log.info("[ES search page] query builder...");
	    
	    //分页，默认第1页，每页20条
	    int pn = esQuery.getPageNo() <= 0 ? 1 : esQuery.getPageNo();
	    int ps = esQuery.getPageSize() <= 0 ? 20 : esQuery.getPageSize();
	    
	    builder.withPageable(_page(pn - 1, ps));
	    //log.info("[ES search page] start page...");
	    
	    SearchQuery searchQuery = builder.build();
	    //log.info("[ES search page] query DSL: ", searchQuery.getQuery().toString());
	    
	    //分页查询
	    //Page<T> queryPage = this.queryForPage(builder.build(), clazz);
	    ScrolledPage<T> queryPage = (ScrolledPage<T>) this.startScroll(_scrollTimeInMillis, 
	    		searchQuery, clazz);
	    //log.info("[ES search page] query result elements：" + queryPage.getTotalElements());
	    
        while (queryPage.hasContent()) {
            //取下一页，scrollId在es服务器上可能会发生变化，需要用最新的。
        	//发起continueScroll请求会重新刷新快照保留时间
        	queryPage = (ScrolledPage<T>) this.continueScroll(queryPage.getScrollId(), 
        			_scrollTimeInMillis, clazz);
        }
        //释放资源
        this.clearScroll(queryPage.getScrollId());
        //log.info("[ES search page] search page clear scroll...");
		
	    //自定义分页
	  	com.liuzi.mybatis.pojo.Page<T> page = new com.liuzi.mybatis.pojo.Page<T>(pn, ps);
	    //总页数
	    page.setPageTotal(queryPage.getTotalPages());
	    //总条数
	    page.setTotalCount(queryPage.getTotalElements());
	    //当前页的条数
	    page.setNumber(queryPage.getNumberOfElements());
	    //当前第几页
	    //queryPage.getNumber();
	    //数据
	    page.setData(queryPage.getContent());
	    
	    //log.info("[ES search page] end, result page...");
	    return page;
	}
	
	/**
	 * 查询
	 * @param esQuery
	 * @return
	 */
	private NativeSearchQueryBuilder _query(EsQuery esQuery){
		if(esQuery == null){
			esQuery = EsQuery.builder().build();
		}
		
		//查询builder
	    NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
	    //index
	    String[] index = _indices(esQuery.getTables());
	    if(index != null && index.length > 0){
	    	builder.withIndices(index);
	    	builder.withTypes(_type);
	    }
	    //字段
	    SourceFilter sourceFilter = _fields(esQuery.getIncludes(), esQuery.getExcludes());
	    if(sourceFilter != null){
	    	builder.withSourceFilter(sourceFilter);
	    }
	    //条件
	    builder.withQuery(_conditions(esQuery.isMatchAllQuery(), esQuery.getConditions()));
		//排序
	    SortBuilder<?>[] sort = _sort(esQuery.getSort());
	    for(SortBuilder<?> sbr : sort){
	    	builder.withSort(sbr);
	    }
		//聚合
		AbstractAggregationBuilder<?> aab = esQuery.getAab();
		if(aab != null){
			builder.addAggregation(aab);
		}
	    //高亮
		Field[] highlightFields = _highlight(esQuery.getHighlight());
	    if(highlightFields != null && highlightFields.length > 0){
	    	builder.withHighlightFields(highlightFields);
	    }
	    
	    return builder;
	}
	
	/**
	 * 条件匹配
	 * @param conditions
	 * @return
	 */
	private static SourceFilter _fields(String[] includes, String[] excludes){
		if((includes == null || includes.length == 0) && 
				(excludes == null || excludes.length == 0)){
			return null;
		}
		
		FetchSourceFilterBuilder fetchSourceFilterBuilder = new FetchSourceFilterBuilder();
	    if(includes != null && includes.length > 0){
	    	fetchSourceFilterBuilder.withIncludes(includes);
	    }
	    if(excludes != null && excludes.length > 0){
	    	fetchSourceFilterBuilder.withExcludes(excludes);
	    }
	    return fetchSourceFilterBuilder.build();
	}
	
	/**
	 * 条件匹配
	 * @param conditions
	 * @return
	 */
	private static QueryBuilder _conditions(boolean matchAllQuery, EsConditions[] conditions){
	    //匹配所有文档
	    if(matchAllQuery || conditions == null || conditions.length == 0){
	    	return QueryBuilders.matchAllQuery();
	    }else{
	    	BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
	    	for(EsConditions ct : conditions){
		    	QueryBuilder queryBuilder = ct.getQueryBuilder();
	    		String type = ct.getType();
	    		
	    		switch(type){
		    		case "must":
						boolQueryBuilder.must(queryBuilder);
						break;
					case "should":
						boolQueryBuilder.should(queryBuilder);
						break;
					case "mustNot":
						boolQueryBuilder.mustNot(queryBuilder);
						break;
					default:
						boolQueryBuilder.filter(queryBuilder);
						break;
				}
	    	}
	    	return boolQueryBuilder;
	    }
	}
	
	/**
	 * index
	 * @param tables
	 * @return
	 */
	private static String[] _indices(EsTable[] tables){
		String[] index = null;
		int length;
	    if(tables != null && (length = tables.length) > 0){
	    	index = new String[length];
	    	for(int i = 0; i < length; i ++){
	    		index[i] = _index(tables[i].getTable(), tables[i].getDate());
	    	}
	    }
	    return index;
	}
	
	/**
	 * 排序
	 * @param sortBuilder
	 * @return
	 */
	private static SortBuilder<?>[] _sort(SortBuilder<?>[] sortBuilder){
		if(sortBuilder == null || sortBuilder.length == 0){
			return new SortBuilder<?>[]{EsSort.defaultSort()};
		}
		return sortBuilder;
	}
	
	/**
	 * 分页
	 * @param pn
	 * @param ps
	 * @return
	 */
	private static PageRequest _page(int pn, int ps){
		return PageRequest.of(pn, ps);
	}
	
	/**
	 * 高亮
	 * @param highlight
	 * @return
	 */
	private static Field[] _highlight(String[] highlight){
		Field[] highlightFields = null;
		int highlightSize;
	    if(highlight != null && (highlightSize = highlight.length) > 0){
	    	highlightFields = new Field[highlightSize];
	    	for(int i = 0; i < highlightSize; i ++){
	    		highlightFields[i] = new Field(highlight[i]);
	    	}
	    }
	    return highlightFields;
	}
	
	/**
	 * 索引规则
	 * 1. database-table*
	 * 2. database-table-date(yyyyMMdd)
	 * @param table index
	 * @param date 日期
	 * @return
	 */
	public static String _index(String table, String date){
		return _index + "-" + table + (StringUtils.isBlank(date) ? "*" : "-" + date);
	}
}

package com.liuzi.elasticsearch.repository;


import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.util.StringUtils;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;

import com.liuzi.elasticsearch.data.EsEntity;
import com.liuzi.elasticsearch.data.EsFilter;
import com.liuzi.elasticsearch.data.EsHighLight;
import com.liuzi.elasticsearch.data.EsQuery;
import com.liuzi.elasticsearch.data.EsTable;
import com.liuzi.elasticsearch.data.EsConditions;
import com.liuzi.elasticsearch.data.EsSort;
import com.liuzi.elasticsearch.util.Constant;
import com.liuzi.elasticsearch.util.JsonUtils;
import com.liuzi.mybatis.pojo.Page;

/**
 * 搜索操作
 * @author zsy
 */
@Slf4j
public class ElasticsearchSearchImpl extends ElasticsearchDocumentImpl{

	/**
	 * 分页搜索
	 */
	public <T extends EsEntity> Page<T> searchBySimple(EsQuery esQuery, Class<T> clazz) throws Exception{
    	SearchSourceBuilder searchSourceBuilder = _query(esQuery);
    	SearchRequest searchRequest = _request(esQuery);
    	//分页
  		int pageNo = esQuery.getPageNo() <= 0 ? Constant.PAGE_NO : esQuery.getPageNo();
        int pageSize = esQuery.getPageSize() <= 0 ? Constant.PAGE_SIZE : esQuery.getPageSize();
        
        searchSourceBuilder.from((pageNo - 1) * pageSize);
        searchSourceBuilder.size(pageSize);
        
        searchRequest.source(searchSourceBuilder);
        
        log.info("[ES search page by from] query DSL: ", searchSourceBuilder.toString());
        
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        List<T> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
        	T t = JsonUtils.string2Obj(hit.getSourceAsString(), clazz);
        	list.add(t);
        }
        
        //自定义分页
	  	Page<T> page = new Page<>(pageNo, pageSize);
	  	//总条数
	  	long total = hits.getTotalHits();
	    page.setTotalCount(total);
	    //总页数
	    page.setPageTotal((int)(total / pageSize));
	    //当前页的条数
	    page.setNumber(searchHits.length);
	    //数据
	    page.setData(list);
	    
	    log.info("[ES search page] end, result page...");
	    return page;
    }

	/**
	 * 分页搜索
	 */
    public <T extends EsEntity> Page<T> searchByScorll(EsQuery esQuery, Class<T> clazz) throws Exception{
    	SearchSourceBuilder searchSourceBuilder = _query(esQuery);
    	SearchRequest searchRequest = _request(esQuery);
    	
    	//分页
  		int pageNo = esQuery.getPageNo() <= 0 ? Constant.PAGE_NO : esQuery.getPageNo();
        int pageSize = esQuery.getPageSize() <= 0 ? Constant.PAGE_SIZE : esQuery.getPageSize();
        //searchSourceBuilder.from(0);
        searchSourceBuilder.size(pageSize);
    	
	    //source
	    searchRequest.source(searchSourceBuilder);
	    //查询缓存时间
    	long aliveTime = esQuery.getSearchAliveTime() <= 0 ? _searchAliveTime() : esQuery.getSearchAliveTime();
    	Scroll scroll = new Scroll(TimeValue.timeValueMinutes(aliveTime));
        searchRequest.scroll(scroll);
	    
	    log.info("[ES search page by scroll] query DSL: ", searchSourceBuilder.toString());
	    
		//查询结果
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	
		SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        
        List<T> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            T t = JsonUtils.string2Obj(hit.getSourceAsString(), clazz);
            list.add(t);
        }
        
        String scrollId = searchResponse.getScrollId();
        SearchScrollRequest scrollRequest;
        while (searchHits != null && searchHits.length > 0) {
            scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchResponse.getScrollId();
            searchHits = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHits) {
                T t = JsonUtils.string2Obj(hit.getSourceAsString(), clazz);
                list.add(t);
            }
        }
        
        //清除scrollid
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        boolean succeeded = clearScrollResponse.isSucceeded();
        log.info("[ES search page] clear scroll is success: ", succeeded);
		
        //自定义分页
	  	Page<T> page = new Page<T>(pageNo, pageSize);
	  	//总条数
	  	long total = hits.getTotalHits();
	    page.setTotalCount(total);
	    //总页数
	    page.setPageTotal((int)(total / pageSize));
	    //当前页的条数
	    page.setNumber(searchHits.length);
	    //数据
	    page.setData(list);
	    
	    log.info("[ES search page] end, result page...");
	    return page;
    }
	
    private static SearchRequest _request(EsQuery esQuery) throws Exception{
    	//索引
        String[] indexNames = _indices(esQuery.getTables());
        SearchRequest searchRequest;
        if(indexNames == null || indexNames.length == 0){
        	searchRequest = new SearchRequest();
        }else{
        	searchRequest = new SearchRequest(indexNames);
        }
        return searchRequest;
    }
    
    private static SearchSourceBuilder _query(EsQuery esQuery){
    	//查询builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //字段
	    EsFilter filter = esQuery.getFilter();
	    if(filter != null){
	    	searchSourceBuilder.fetchSource(filter.getIncludes(), 
	    			filter.getExcludes());
	    }
        //条件
        QueryBuilder queryBuilder = _conditions(esQuery.isMatchAllQuery(), esQuery.getConditions());
        searchSourceBuilder.query(queryBuilder);
        //排序
	    SortBuilder<?>[] sort = _sort(esQuery.getSort());
	    for(SortBuilder<?> sbr : sort){
	    	searchSourceBuilder.sort(sbr);
	    }
	    //聚合
  		AbstractAggregationBuilder<?> aab = esQuery.getAab();
  		if(aab != null){
  			searchSourceBuilder.aggregation(aab);
  		}
	    //高亮
	    HighlightBuilder highlightBuilder = _highlight(esQuery.getHighlight());
  	    if(highlightBuilder != null){
  	    	searchSourceBuilder.highlighter(highlightBuilder);
  	    }
  	    return searchSourceBuilder;
    }
    
	
	/**
	 * 条件匹配
	 * @param esConditions
	 * @return
	 */
	private static QueryBuilder _conditions(boolean matchAllQuery, EsConditions[] esConditions){
	    //匹配所有文档
	    if(matchAllQuery || esConditions == null || esConditions.length == 0){
	    	return QueryBuilders.matchAllQuery();
	    }else{
	    	BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
	    	for(EsConditions ct : esConditions){
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
	 * @throws Exception 
	 */
	private static String[] _indices(EsTable[] tables) throws Exception{
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
	 * 高亮
	 * @param highlight
	 * @return
	 */
	private static HighlightBuilder _highlight(EsHighLight[] highlight){
		if(highlight == null || highlight.length == 0){
			return null;
		}
		
		HighlightBuilder builder = new HighlightBuilder(); 
		HighlightBuilder.Field field;
		for(EsHighLight ehl : highlight){
			String[] name = ehl.getName();
			String type = StringUtils.isEmpty(ehl.getType()) ? Constant.HIGH_LIGHT_TYPE : ehl.getType();
			if(name != null && name.length > 0){
				for(String n : name){
					field = new HighlightBuilder.Field(n);
					field.highlighterType(type);
					builder.field(field);
				}
			}
		}
	    return builder;
	}
}

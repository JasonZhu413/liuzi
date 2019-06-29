package com.liuzi.elasticsearch.repository;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.util.StringUtils;

import com.liuzi.elasticsearch.data.EsEntity;
import com.liuzi.elasticsearch.enums.Analyzer;
import com.liuzi.elasticsearch.enums.DateFormat;
import com.liuzi.elasticsearch.enums.FieldType;
import com.liuzi.elasticsearch.util.IndexTools;
import com.liuzi.elasticsearch.util.MappingData;
import com.liuzi.elasticsearch.util.MetaData;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 索引结构基础方法实现类
 * @author: zsy
 * @create: 2019-01-29 10:05
 **/
@Slf4j
public class ElasticsearchIndexImpl extends ElasticsearchBaseImpl{

	/**
	 * 创建索引
	 * @param clazz
	 * @throws Exception
	 */
    public <T extends EsEntity> void indexCreate(Class<T> clazz) throws Exception{
        MetaData metaData = IndexTools.getMetaData(clazz);
        String indexName = metaData.getIndexName();
        String indexType = metaData.getIndexType();
        
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        
        /*{
        	"_doc" : {
        		"properties" : {
        			"id" : {
        				"type" : "",
        				"copy_to" : "",
        				"index" : "",
        				"analyzer" : "",
        				"search_analyzer" : "",
        				"fields" : {
        					"keyword" : {
        						"type" : "",
        						"ignore_above" : ""
        					}
        				}
        			}
        		}
        	}
        }*/
        
        
        MappingData[] mappingDataList = IndexTools.getMappingData(clazz);

        boolean isAutocomplete = false;
        
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject(indexType);
            {
                builder.startObject("properties");
                {
                	String copyTo;
                	FieldType type;
                	DateFormat dateFormat;
                	String datePattern;
                	Analyzer analyzer;
                	Analyzer searchAnalyzer;
                	for (MappingData mappingData : mappingDataList) {
	                    builder.startObject(mappingData.getFieldName());
	                    {
	                    	type = mappingData.getType();
	                    	if (type != FieldType.Auto) {
	                			builder.field("type", type.name().toLowerCase());

	                			dateFormat = mappingData.getFormat();
	                			datePattern = mappingData.getPattern();
	                			if (type == FieldType.Date && dateFormat != DateFormat.none) {
	                				builder.field("format", dateFormat == DateFormat.custom ? datePattern : dateFormat.toString());
	                			}
	                		}
	                    	copyTo = mappingData.getCopyTo();
	                        if(!StringUtils.isEmpty(copyTo)){
	                        	builder.field("copy_to", copyTo);
	                        }
	                        builder.field("index", mappingData.isAllowSearch());
	                        
	                        analyzer = mappingData.getAnalyzer();
	                        searchAnalyzer = mappingData.getSearchAnalyzer();
	                        if(mappingData.isAutocomplete() && (type == FieldType.Text || type == FieldType.Keyword)){
	                        	builder.field("analyzer", "autocomplete");
	                        	builder.field("search_analyzer", "standard");
	                            isAutocomplete = true;
	                        }else if(type == FieldType.Text){
	                        	if(!StringUtils.isEmpty(analyzer)){
	                        		builder.field("analyzer", analyzer);
	                        	}
	                        	if (!StringUtils.isEmpty(searchAnalyzer)) {
	                        		builder.field("search_analyzer", searchAnalyzer);
		                		}
	                        }
	                        
	                		if(mappingData.isKeyword() && !type.equals("keyword")){
	                			builder.startObject("fields");
	                			{
	                				builder.startObject("keyword");
	                				{
	                					builder.field("type", "keyword");
	                					builder.field("ignore_above", mappingData.getIgnoreAbove());
	                				}
	                				builder.endObject();
	                			}
	                			builder.endObject();
	                        }else if(mappingData.isSuggest()){
	                        	builder.startObject("fields");
	                			{
	                				builder.startObject("suggest");
	                				{
	                					builder.field("type", "completion");
	                					if(!StringUtils.isEmpty(analyzer)){
	    	                        		builder.field("analyzer", analyzer);
	    	                        	}
	                				}
	                				builder.endObject();
	                			}
	                			builder.endObject();
	                        }
	                    }
	                    builder.endObject();
	                }
	            }
	            builder.endObject();
	        }
	        builder.endObject();
        }
        builder.endObject();
        
        /*StringBuffer source = new StringBuffer();
        source.append("  {\n" +
                "    \""+ metaData.getIndexType() + "\": {\n" +
                "      \"properties\": {\n");
        for (int i = 0; i < mappingDataList.length; i++) {
            MappingData mappingData = mappingDataList[i];
            if(mappingData == null || mappingData.getFieldName() == null){
                continue;
            }
            
            source.append(" \"" + mappingData.getFieldName() + "\": {\n");
            source.append(" \"type\": \"" + mappingData.getType() + "\"\n");
            if(!StringUtils.isEmpty(mappingData.getCopyTo())){
                source.append(" ,\"copy_to\": \"" + mappingData.getCopyTo() + "\"\n");
            }
            if(!mappingData.isAllowSearch()){
                source.append(" ,\"index\": false\n");
            }
            if(mappingData.isAutocomplete() && (mappingData.getType().equals("text") || mappingData.getType().equals("keyword"))){
                source.append(" ,\"analyzer\": \"autocomplete\"\n");
                source.append(" ,\"search_analyzer\": \"standard\"\n");
                isAutocomplete = true;
            }else if(mappingData.getType().equals("text")){
                source.append(" ,\"analyzer\": \"" + mappingData.getAnalyzer() + "\"\n");
                source.append(" ,\"search_analyzer\": \"" + mappingData.getSearchAnalyzer() + "\"\n");
            }
            if(mappingData.isKeyword() && !mappingData.getType().equals("keyword")){
                source.append(" \n");
                source.append(" ,\"fields\": {\n");
                source.append(" \"keyword\": {\n");
                source.append(" \"type\": \"keyword\",\n");
                source.append(" \"ignore_above\": " + mappingData.getIgnoreAbove());
                source.append(" }\n");
                source.append(" }\n");
            }else if(mappingData.isSuggest()){
                source.append(" \n");
                source.append(" ,\"fields\": {\n");
                source.append(" \"suggest\": {\n");
                source.append(" \"type\": \"completion\",\n");
                source.append(" \"analyzer\": \"" + mappingData.getAnalyzer() + "\",\n");
                source.append(" }\n");
                source.append(" }\n");
            }
            if(i == mappingDataList.length - 1){
                source.append(" }\n");
            }else{
                source.append(" },\n");
            }
        }
        source.append(" }\n");
        source.append(" }\n");
        source.append(" }\n");*/
        
        if(isAutocomplete){
            request.settings(Settings.builder()
                    .put("index.number_of_shards", metaData.getShards())
                    .put("index.number_of_replicas", metaData.getReplicas())
                    .put("analysis.filter.autocomplete_filter.type","edge_ngram")
                    .put("analysis.filter.autocomplete_filter.min_gram",1)
                    .put("analysis.filter.autocomplete_filter.max_gram",20)
                    .put("analysis.analyzer.autocomplete.type","custom")
                    .put("analysis.analyzer.autocomplete.tokenizer","standard")
                    .putList("analysis.analyzer.autocomplete.filter",new String[]{"lowercase","autocomplete_filter"})
            );
        }else{
            request.settings(Settings.builder()
                    .put("index.number_of_shards", metaData.getShards())
                    .put("index.number_of_replicas", metaData.getReplicas())
            );
        }

        request.mapping(indexType, builder);
        /*request.mapping(metaData.getIndexType(),
                source.toString(),
                XContentType.JSON);*/
        
        try {
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            //返回的CreateIndexResponse允许检索有关执行的操作的信息，如下所示：
            boolean acknowledged = createIndexResponse.isAcknowledged();//指示是否所有节点都已确认请求
            log.info("index create done: " + acknowledged);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("index create error: " + e.getMessage());
        }
    }
    
    /**
     * 删除索引
     * @param table
     * @throws Exception
     */
    public void indexDrop(String table) throws Exception {
    	String index = IndexTools._index(table);
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        client.indices().delete(request, RequestOptions.DEFAULT);
    }
    
    /**
     * 删除索引
     * @param table
     * @param date
     * @param pattern
     * @throws Exception
     */
    public void indexDrop(String table, Object date, String pattern) throws Exception {
    	String index = IndexTools._index(table, date, pattern);
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    /**
     * 删除索引
     * @param clazz
     * @throws Exception
     */
    public <T extends EsEntity> void indexDrop(Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        DeleteIndexRequest request = new DeleteIndexRequest(indexname);
        client.indices().delete(request, RequestOptions.DEFAULT);
    }
    
    /**
     * 索引是否存在
     * @param table
     * @return
     * @throws Exception
     */
    public boolean indexExists(String table) throws Exception{
        GetIndexRequest request = new GetIndexRequest();
        request.indices(IndexTools._index(table));
        request.types(IndexTools._type());
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }
    
    /**
     * 索引是否存在
     * @param table
     * @param date
     * @param pattern
     * @return
     * @throws Exception
     */
    public boolean indexExists(String table, Object date, String pattern) throws Exception{
        GetIndexRequest request = new GetIndexRequest();
        request.indices(IndexTools._index(table, date, pattern));
        request.types(IndexTools._type());
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 索引是否存在
     * @param clazz
     * @return
     * @throws Exception
     */
    public <T extends EsEntity> boolean indexExists(Class<T> clazz) throws Exception{
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        String indextype = metaData.getIndexType();
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexname);
        request.types(indextype);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }
    
    
}

package com.liuzi.elasticsearch.repository;


import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.util.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;

import com.liuzi.elasticsearch.data.EsEntity;
import com.liuzi.elasticsearch.util.BeanTools;
import com.liuzi.elasticsearch.util.IndexTools;
import com.liuzi.elasticsearch.util.JsonUtils;
import com.liuzi.elasticsearch.util.MetaData;
import com.liuzi.elasticsearch.util.Tools;

/**
 * 文档操作
 * @author zsy
 */
@Slf4j
public class ElasticsearchDocumentImpl extends ElasticsearchAggregationsImpl{
	
	/**
	 * 保存文档
	 */
	public <T extends EsEntity> boolean save(T t) throws Exception{
		MetaData metaData = IndexTools.getIndexType(t.getClass());
        String indexname = metaData.getIndexName();
        String indextype = metaData.getIndexType();
        String id = Tools.getESId(t);
        //String id = t.getId() + "";
        IndexRequest indexRequest = null;
        if (StringUtils.isEmpty(id)) {
            indexRequest = new IndexRequest(indexname, indextype);
        } else {
        	if(exists(id, t.getClass())){
            	return updateById(t);
            }
        	
            indexRequest = new IndexRequest(indexname, indextype, id);
        }
        indexRequest.source(JsonUtils.obj2String(t), XContentType.JSON);
        IndexResponse indexResponse = null;
        indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            log.info("INDEX CREATE SUCCESS");
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            log.info("INDEX UPDATE SUCCESS");
        } else {
            return false;
        }
        return true;
    }
	
	/**
	 * 批量保存文档
	 */
	public <T extends EsEntity> BulkResponse save(List<T> list) throws Exception {
        if (list == null || list.isEmpty()) {
            return null;
        }
        
        T t = list.get(0);
        MetaData metaData = IndexTools.getIndexType(t.getClass());
        String indexname = metaData.getIndexName();
        String indextype = metaData.getIndexType();
        BulkRequest rrr = new BulkRequest();
        for (int i = 0; i < list.size(); i++) {
        	T tt = list.get(i);
            String id = Tools.getESId(tt);
            //String id = tt.getId() + "";
            rrr.add(new IndexRequest(indexname, indextype, id).source(BeanTools.objectToMap(tt)));
        }
        return client.bulk(rrr, RequestOptions.DEFAULT);
    }

	/**
	 * 更新文档
	 */
	public <T extends EsEntity> boolean updateById(T t) throws Exception {
		String id = Tools.getESId(t);
		//String id = t.getId() + "";
        if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
        
        MetaData metaData = IndexTools.getIndexType(t.getClass());
        String indexname = metaData.getIndexName();
        String indextype = metaData.getIndexType();
        
        UpdateRequest updateRequest = new UpdateRequest(indexname, indextype, id);
        updateRequest.doc(Tools.getFieldValue(t));
        UpdateResponse updateResponse = null;
        updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
            log.info("INDEX CREATE SUCCESS");
        } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            log.info("INDEX UPDATE SUCCESS");
        } else {
            return false;
        }
        return true;
    }

	/**
	 * 获取文档
	 */
    public <T extends EsEntity> T getById(String id, Class<T> clazz) throws Exception {
    	if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
    	
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        String indextype = metaData.getIndexType();
        
        GetRequest getRequest = new GetRequest(indexname, indextype, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            return JsonUtils.string2Obj(getResponse.getSourceAsString(), clazz);
        }
        return null;
    }

	/**
	 * 批量获取文档
	 */
    public <T extends EsEntity> List<T> getByIds(String[] ids, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        String indextype = metaData.getIndexType();
        MultiGetRequest request = new MultiGetRequest();
        for (int i = 0; i < ids.length; i++) {
            request.add(new MultiGetRequest.Item(indexname, indextype, ids[i]));
        }
        MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
        List<T> list = new ArrayList<>();
        for (int i = 0; i < response.getResponses().length; i++) {
            MultiGetItemResponse item = response.getResponses()[i];
            GetResponse getResponse = item.getResponse();
            if (getResponse.isExists()) {
                list.add(JsonUtils.string2Obj(getResponse.getSourceAsString(), clazz));
            }
        }
        return list;
    }

	/**
	 * 文档是否存在
	 */
    public boolean exists(String id, String table) throws Exception {
    	if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
    	
        String indexname = IndexTools._index(table);
        String indextype = IndexTools._type();
        
        GetRequest getRequest = new GetRequest(indexname, indextype, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            return true;
        }
        return false;
    }

	/**
	 * 文档是否存在
	 */
    public boolean exists(String id, String table, Object date, String pattern) throws Exception {
    	if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
    	
        String indexname = IndexTools._index(table, date, pattern);
        String indextype = IndexTools._type();
        
        GetRequest getRequest = new GetRequest(indexname, indextype, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            return true;
        }
        return false;
    }

	/**
	 * 文档是否存在
	 */
    public <T extends EsEntity> boolean exists(String id, Class<T> clazz) throws Exception {
    	if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
    	
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        String indextype = metaData.getIndexType();
        
        GetRequest getRequest = new GetRequest(indexname, indextype, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            return true;
        }
        return false;
    }

	/**
	 * 删除文档
	 */
    public boolean deleteById(String id, String table) throws Exception {
    	if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
    	String indexname = IndexTools._index(table);
        String indextype = IndexTools._type();
        DeleteRequest deleteRequest = new DeleteRequest(indexname, indextype, id);
        DeleteResponse deleteResponse = null;
        deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
            log.info("INDEX DELETE SUCCESS");
        } else {
            return false;
        }
        return true;
    }

	/**
	 * 删除文档
	 */
    public boolean deleteById(String id, String table, Object date, String pattern) throws Exception {
    	if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
    	String indexname = IndexTools._index(table, date, pattern);
        String indextype = IndexTools._type();
        DeleteRequest deleteRequest = new DeleteRequest(indexname, indextype, id);
        DeleteResponse deleteResponse = null;
        deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
            log.info("INDEX DELETE SUCCESS");
        } else {
            return false;
        }
        return true;
    }

	/**
	 * 删除文档
	 */
    public <T extends EsEntity> boolean deleteById(String id, Class<T> clazz) throws Exception {
    	if (StringUtils.isEmpty(id)) {
            throw new Exception("ID cannot be empty");
        }
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        String indextype = metaData.getIndexType();
        DeleteRequest deleteRequest = new DeleteRequest(indexname, indextype, id);
        DeleteResponse deleteResponse = null;
        deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
            log.info("INDEX DELETE SUCCESS");
        } else {
            return false;
        }
        return true;
    }
}

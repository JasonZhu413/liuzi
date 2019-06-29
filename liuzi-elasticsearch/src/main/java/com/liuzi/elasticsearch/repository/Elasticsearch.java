package com.liuzi.elasticsearch.repository;


import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.liuzi.elasticsearch.data.EsEntity;
import com.liuzi.elasticsearch.data.EsQuery;
import com.liuzi.mybatis.pojo.Page;



public interface Elasticsearch{
	
	/**
	 * 低版本客户端
	 * @return
	 */
	RestClient getLowLevelClient();
	
	/**
	 * 关闭连接
	 * @throws Exception
	 */
    void close() throws Exception;
    
    /**
     * 请求
     * @param request
     * @return
     * @throws Exception
     */
    Response request(Request request) throws Exception;
	
    /**
     * 查询请求
     * @param searchRequest
     * @return
     * @throws IOException
     */
    SearchResponse search(SearchRequest searchRequest) throws IOException;

    /**
     * 创建索引
     * @param clazz
     * @throws Exception
     */
    <T extends EsEntity> void indexCreate(Class<T> clazz) throws Exception;
    
    /**
     * 删除索引
     * @param table
     * @throws Exception
     */
    void indexDrop(String table) throws Exception;
    
    /**
     * 删除索引
     * @param table
     * @param date
     * @param pattern
     * @throws Exception
     */
    void indexDrop(String table, Object date, String pattern) throws Exception;

    /**
     * 删除索引
     * @param clazz
     * @throws Exception
     */
    <T extends EsEntity> void indexDrop(Class<T> clazz) throws Exception;
    
    /**
     * 索引是否存在
     * @param table
     * @return
     * @throws Exception
     */
    boolean indexExists(String table) throws Exception;
    
    /**
     * 索引是否存在
     * @param table
     * @param date
     * @param pattern
     * @return
     * @throws Exception
     */
    boolean indexExists(String table, Object date, String pattern) throws Exception;

    /**
     * 索引是否存在
     * @param clazz
     * @return
     * @throws Exception
     */
    <T extends EsEntity> boolean indexExists(Class<T> clazz) throws Exception;
    
    /**
     * 保存数据，如果存在则更新
     * @param t
     * @return
     * @throws Exception
     */
    public <T extends EsEntity> boolean save(T t) throws Exception;
	
    /**
     * 批量保存数据
     * @param list
     * @return
     * @throws Exception
     */
	<T extends EsEntity> BulkResponse save(List<T> list) throws Exception;
	
	/**
	 * 更新数据
	 * @param t
	 * @return
	 * @throws Exception
	 */
	<T extends EsEntity> boolean updateById(T t) throws Exception;
    
	/**
	 * 根据id获取数据
	 * @param id
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
    <T extends EsEntity> T getById(String id, Class<T> clazz) throws Exception;
    
    /**
     * 根据id批量获取数据
     * @param ids
     * @param clazz
     * @return
     * @throws Exception
     */
    <T extends EsEntity> List<T> getByIds(String[] ids, Class<T> clazz) throws Exception;
    
    /**
     * 根据文档id查询文档是否存在
     * @param id
     * @param table
     * @return
     * @throws Exception
     */
    boolean exists(String id, String table) throws Exception;
    
    /**
     * 根据文档id查询文档是否存在
     * @param id
     * @param table
     * @param date
     * @param pattern
     * @return
     * @throws Exception
     */
    boolean exists(String id, String table, Object date, String pattern) throws Exception;
    
    /**
     * 根据文档id查询文档是否存在
     * @param id
     * @param clazz
     * @return
     * @throws Exception
     */
    <T extends EsEntity> boolean exists(String id, Class<T> clazz) throws Exception;
    
    /**
     * 根据文档id删除
     * @param id
     * @param table
     * @return
     * @throws Exception
     */
    boolean deleteById(String id, String table) throws Exception;
    
    /**
     * 根据文档id删除
     * @param id
     * @param table
     * @param date
     * @param pattern
     * @return
     * @throws Exception
     */
    boolean deleteById(String id, String table, Object date, String pattern) throws Exception;
    
    /**
     * 根据文档id删除
     * @param id
     * @param clazz
     * @return
     * @throws Exception
     */
    <T extends EsEntity> boolean deleteById(String id, Class<T> clazz) throws Exception;
    
    /**
     * 分页查询
     * @param esQuery
     * @param clazz
     * @return
     * @throws Exception
     */
	<T extends EsEntity> Page<T> searchBySimple(EsQuery esQuery, Class<T> clazz) throws Exception;
	
	/**
	 * 分页查询
	 * @param esQuery
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
    <T extends EsEntity> Page<T> searchByScorll(EsQuery esQuery, Class<T> clazz) throws Exception;
    
}

package com.liuzi.elasticsearch.repository;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.liuzi.elasticsearch.util.Constant;



/**
 * 基础
 * @author zsy
 */
public class ElasticsearchBaseImpl extends Constant{
	
	@Autowired
    protected RestHighLevelClient client;
    
    public RestClient getLowLevelClient() {
        return client.getLowLevelClient();
    }
	
    public void close() throws Exception{
        if (client != null) {
        	client.close();
        }
    }
    
    public Response request(Request request) throws Exception{
		return getLowLevelClient().performRequest(request);
    }
	
    public SearchResponse search(SearchRequest searchRequest) throws IOException {
    	return client.search(searchRequest, RequestOptions.DEFAULT);
    }
}

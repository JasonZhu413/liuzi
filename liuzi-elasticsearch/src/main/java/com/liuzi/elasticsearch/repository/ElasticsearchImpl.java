package com.liuzi.elasticsearch.repository;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.liuzi.elasticsearch.annotation.ESData;
import com.liuzi.elasticsearch.annotation.ESID;
import com.liuzi.elasticsearch.data.EsEntity;
import com.liuzi.elasticsearch.util.IndexTools;
import com.liuzi.elasticsearch.util.MappingData;






public class ElasticsearchImpl extends ElasticsearchSearchImpl implements Elasticsearch{
	
	@Data
	@ESData(indexName="test")
	@EqualsAndHashCode(callSuper=false)
	static class test extends EsEntity{
		private static final long serialVersionUID = 1L;
		
		@ESID
		private String testId;
		
		private String test;
		
		private Integer state;
	}
	
	public static void main(String[] args) throws Exception {
		test t = new test();
		t.setTest("haha");
		t.setId(1L);
		t.setTestId("abc");
		Elasticsearch e = new ElasticsearchImpl();
		//e.save(t);
		
		MappingData[] mappingDataList = IndexTools.getMappingData(test.class);
		for(MappingData m : mappingDataList){
			System.out.println(m.toString());
		}
		
		e.indexCreate(test.class);
	}
	
}

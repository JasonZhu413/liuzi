package com.liuzi.elasticsearch.util;

import lombok.Data;

/**
 * 元数据载体类
 */
@Data
public class MetaData{
	
	private String indexName = "";
    private String indexType = "";
    private int shards;
    private int replicas;
    private boolean log = false;
    
    public MetaData(String indexName, String indexType) {
        this.indexName = indexName;
        this.indexType = indexType;
    }

    public MetaData(String indexName, String indexType, int shards, int replicas) {
        this.indexName = indexName;
        this.indexType = indexType;
        this.shards = shards;
        this.replicas = replicas;
    }

    public MetaData(int shards, int replicas) {
        this.shards = shards;
        this.replicas = replicas;
    }
}
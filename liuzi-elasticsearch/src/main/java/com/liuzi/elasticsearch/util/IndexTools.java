package com.liuzi.elasticsearch.util;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.liuzi.elasticsearch.annotation.ESData;
import com.liuzi.elasticsearch.annotation.ESField;
import com.liuzi.elasticsearch.data.EsEntity;
import com.liuzi.elasticsearch.enums.Analyzer;
import com.liuzi.elasticsearch.enums.DateFormat;
import com.liuzi.elasticsearch.enums.FieldType;

/**
 * @description: 索引信息操作工具类
 * @author: zsy
 * @create: 2019-01-29 14:29
 **/
public class IndexTools extends Constant{
	
    /**
     * 获取索引元数据：indexName、indexType
     * @param clazz
     * @return
     * @throws Exception 
     */
    public static MetaData getIndexType(Class<?> clazz) throws Exception{
        ESData data = clazz.getAnnotation(ESData.class);
        if(data != null){
        	//索引
        	String indexName = data.indexName();
        	//String indexType = clazz.getAnnotation(ESData.class).indexType();
        	//是否使用时间
        	boolean useDate = data.date();
        	MetaData metaData;
        	if(useDate){
        		//时间格式
        		String pattern = data.pattern();
        		metaData = new MetaData(Constant._index(indexName, new Date(), pattern), Constant._type());
        	}else{
        		metaData = new MetaData(Constant._index(indexName), Constant._type());
        	}
            metaData.setLog(data.log());
            return metaData;
        }
        return null;
    }

    /**
     * 获取索引元数据：主分片、备份分片数的配置
     * @param clazz
     * @return
     */
    public static MetaData getShardsConfig(Class<?> clazz){
        ESData data = clazz.getAnnotation(ESData.class);
        if(data != null){
        	int shards = data.shards() <= 0 ? 5 : data.shards();
        	int replicas = data.replicas() <= 0 ? 1 : data.replicas();
            MetaData metaData = new MetaData(shards, replicas);
            metaData.setLog(data.log());
            return metaData;
        }
        return null;
    }

    /**
     * 获取索引元数据：indexname、indextype、主分片、备份分片数的配置
     * @param clazz
     * @return
     * @throws Exception 
     */
    public static MetaData getMetaData(Class<?> clazz) throws Exception{
        ESData data = clazz.getAnnotation(ESData.class);
        if(data != null){
        	//索引
        	String indexName = data.indexName();
        	//String indexType = clazz.getAnnotation(ESData.class).indexType();
        	//是否使用时间
        	boolean useDate = data.date();
        	int shards = data.shards() <= 0 ? 5 : data.shards();
        	int replicas = data.replicas() <= 0 ? 1 : data.replicas();
        	
        	MetaData metaData;
        	if(useDate){
        		//时间格式
        		String pattern = data.pattern();
        		metaData = new MetaData(Constant._index(indexName, new Date(), pattern), 
        				Constant._type(), shards, replicas);
        	}else{
        		metaData = new MetaData(Constant._index(indexName), Constant._type(), shards, replicas);
        	}
            metaData.setLog(data.log());
            return metaData;
        }
        return null;
    }

    /**
     * 获取配置于Field上的mapping信息，如果未配置注解，则给出默认信息
     * @param field
     * @return
     */
    public static MappingData getMappingData(Field field){
        if(field == null){
            return null;
        }
        field.setAccessible(true);
        MappingData mappingData = new MappingData();
        mappingData.setFieldName(field.getName());
        
        ESField eSField = field.getAnnotation(ESField.class);
        if(eSField != null){
            mappingData.setType(eSField.type());
            mappingData.setFormat(eSField.format());
            mappingData.setPattern(eSField.pattern());
            //mappingData.setAnalyzedtype(esMapping.analyzedtype().toString());
            mappingData.setAnalyzer(eSField.analyzer());
            mappingData.setAutocomplete(eSField.autocomplete());
            mappingData.setIgnoreAbove(eSField.ignoreAbove());
            mappingData.setSearchAnalyzer(eSField.searchAnalyzer());
            mappingData.setKeyword(eSField.keyword());
            mappingData.setSuggest(eSField.suggest());
            mappingData.setAllowSearch(eSField.allowSearch());
            mappingData.setCopyTo(eSField.copyTo());
        }else{
            mappingData.setType(FieldType.Auto);
            mappingData.setFormat(DateFormat.none);
            mappingData.setPattern("");
            //mappingData.setAnalyzedtype("analyzed");
            mappingData.setAnalyzer(Analyzer.standard);
            mappingData.setAutocomplete(false);
            mappingData.setIgnoreAbove(256);
            mappingData.setSearchAnalyzer(Analyzer.standard);
            mappingData.setKeyword(true);
            mappingData.setSuggest(false);
            mappingData.setAllowSearch(true);
            mappingData.setCopyTo("");
        }
        return mappingData;
    }

    /**
     * 批量获取配置于Field上的mapping信息，如果未配置注解，则给出默认信息
     * @param clazz
     * @return
     */
    public static MappingData[] getMappingData(Class<?> clazz){
        /*Field[] fields = clazz.getDeclaredFields();
        MappingData[] mappingDataList = new MappingData[fields.length];
        for (int i = 0; i < fields.length; i++) {
            if(fields[i].getName().equals("serialVersionUID")){
                continue;
            }
            mappingDataList[i] = getMappingData(fields[i]);
        }*/
        
    	List<MappingData> list = new ArrayList<>();
        do{
        	//是否为其超类
        	if(!EsEntity.class.isAssignableFrom(clazz)){
        		continue;
        	}
        	
        	Field[] fields = clazz.getDeclaredFields();
        	for(Field f : fields){
        		//取出静态及final属性
        		int modifiers = f.getModifiers();
        		if(Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)){
        			continue;
        		}
        		list.add(getMappingData(f));
            }
        }while((clazz = clazz.getSuperclass()) != null);
        
        MappingData[] mappingDataList = null;
        if(!list.isEmpty()){
        	mappingDataList = new MappingData[list.size()];
            list.toArray(mappingDataList);
        }
        return mappingDataList;
    }
}

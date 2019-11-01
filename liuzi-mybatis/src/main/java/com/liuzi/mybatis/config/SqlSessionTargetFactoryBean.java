package com.liuzi.mybatis.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import com.liuzi.util.common.Log;

/**
 * typeAliasesPackage支持通配符
 * @author zsy
 */
public class SqlSessionTargetFactoryBean extends SqlSessionFactoryBean {
	
	private static final String DEFAULT_RESOURCE_PATTERN = "/*.class";  
	
	private static final String BASE_TAEGET_PATTERN = ".**";  
  
    @Override  
    public void setTypeAliasesPackage(String typeAliasesPackage) {  
    	String basePage = null;
    	if(typeAliasesPackage != null){
    		basePage = typeAliasesPackage;
    		while(basePage.endsWith(BASE_TAEGET_PATTERN)){
    			basePage = basePage.substring(0, basePage.length() - 3);
        	}
    	}
    	
        ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();  
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);  
        
        typeAliasesPackage = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +  
                ClassUtils.convertClassNameToResourcePath(typeAliasesPackage) + DEFAULT_RESOURCE_PATTERN;  
  
        //将加载多个绝对匹配的所有Resource  
        //将首先通过ClassLoader.getResource("META-INF")加载非模式路径部分  
        //然后进行遍历模式匹配  
        try {  
            List<String> result = new ArrayList<String>();  
            Resource[] resources =  resolver.getResources(typeAliasesPackage);  
            if(resources != null && resources.length > 0){  
                MetadataReader metadataReader = null;  
                for(Resource resource : resources){  
                    if(resource.isReadable()){  
                       metadataReader =  metadataReaderFactory.getMetadataReader(resource);  
                        try {  
                        	String className = metadataReader.getClassMetadata().getClassName();
                        	Class<?> clazz = Class.forName(className);
                        	String packageName = clazz.getPackage().getName();
                            result.add(packageName);
                        } catch (ClassNotFoundException e) {  
                            Log.error(e, "Mybatis setTypeAliasesPackage error");
                        }  
                    }  
                }  
            }  
            if(result.size() > 0) {  
            	Set<String> set = new HashSet<>(result);
            	if(basePage != null){
            		set.remove(basePage);
            	}
                super.setTypeAliasesPackage(StringUtils.join(set.toArray(), ","));  
            }else{  
                Log.warn("Mybatis typeAliasesPackage warning: {} package not found", typeAliasesPackage);  
            }  
        } catch (IOException e) {
        	Log.error(e, "Mybatis setTypeAliasesPackage error");
        }  
    }
}

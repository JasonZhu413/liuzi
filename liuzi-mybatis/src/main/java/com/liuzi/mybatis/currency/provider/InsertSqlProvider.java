package com.liuzi.mybatis.currency.provider;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.annotation.ProviderContext;

import com.liuzi.mybatis.currency.cond.SQL;


/**
 * 通用新增实现类
 * @author zsy
 */
public class InsertSqlProvider {
	
	
	/**
	 * 根据主键查询单条
	 * @param pkValue 主键值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $insert(ProviderContext context, Map<String, Object> para){
        K record = (K) para.get("record");
        return new SQL(){{
        	INSERT(record);
		}}.toString();
	}
	
	/**
	 * 批量新增
	 * @param records 实体数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $insertArray(ProviderContext context, Map<String, Object> para){
        K[] records = (K[]) para.get("records");
        return new SQL(){{
        	INSERT(records);
		}}.toString();
	}
	
	/**
	 * 批量新增
	 * @param records 实体List
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $insertList(ProviderContext context, Map<String, Object> para){
		List<K> records = (List<K>) para.get("records");
        return new SQL(){{
        	INSERT(records);
		}}.toString();
	}
}

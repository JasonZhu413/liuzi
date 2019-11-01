package com.liuzi.mybatis.currency.provider;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.annotation.ProviderContext;

import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.currency.consts.SQLConsts;
import com.liuzi.mybatis.currency.data.TableMataData;


/**
 * 通用更新实现类
 * @author zsy
 */
public class UpdateSqlProvider {
	
	/**
	 * 根据主键更新/批量更新
	 * @param records 实体数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $updateByPk(ProviderContext context, Map<String, Object> para){
		K record = (K) para.get("record");
		
		return new SQL(){{
			UPDATE_BY_PK(record);
		}}.toString();
	}
	
	/**
	 * 根据字段更新
	 * @param record 实体数据
	 * @param column 字段名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $updateByColumn(ProviderContext context, Map<String, Object> para){
		K record = (K) para.get("record");
		String column = (String) para.get("column");
		
		return new SQL(){{
			UPDATE_BY_COLUMNS(record, column);
		}}.toString();
	}
	
	/**
	 * 根据字段更新
	 * @param record 实体数据
	 * @param columns 多个字段名，
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $updateByColumnArray(ProviderContext context, Map<String, Object> para){
		K record = (K) para.get("record");
		String[] columns = (String[]) para.get("columns");
		
		return new SQL(){{
			UPDATE_BY_COLUMNS(record, columns);
		}}.toString();
	}
	
	/**
	 * 根据多项字段更新
	 * @param record 实体数据
	 * @param columns 多个字段名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $updateByColumnList(ProviderContext context, Map<String, Object> para){
		K record = (K) para.get("record");
		List<String> columns = (List<String>) para.get("columns");
		
		return new SQL(){{
			UPDATE_BY_COLUMNS(record, columns);
		}}.toString();
	}
	
	/**
	 * 根据自定义字段值更新
	 * @param record 实体数据
	 * @param columns 字段名key，字段值value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $updateByColumnMap(ProviderContext context, Map<String, Object> para){
		K record = (K) para.get("record");
		Map<String, Object> columns = (Map<String, Object>) para.get("columns");
		
		return new SQL(){{
			UPDATE_BY_COLUMNS(record, columns);
		}}.toString();
	}
	
	/**
	 * 根据主键值批量更新
	 * @param records 实体数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $updateListByPk(ProviderContext context, Map<String, Object> para){
		List<K> records = (List<K>) para.get("records");
		
		return new SQL(){{
			for(K k : records){
				UPDATE_BY_PK(k);
				CUSTOM(SQLConsts.END);
			}
		}}.toString();
	}
	
	/**
	 * 根据字段批量更新
	 * @param records 实体数据
	 * @param columns 字段名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $updateListByColumn(ProviderContext context, Map<String, Object> para){
		List<K> records = (List<K>) para.get("records");
		String column = (String) para.get("column");
		
		return new SQL(){{
			for(K k : records){
				UPDATE_BY_COLUMNS(k, column);
				CUSTOM(SQLConsts.END);
			}
		}}.toString();
	}

	/**
	 * 根据字段批量更新
	 * @param records 实体数据
	 * @param columns 字段名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $updateListByColumnArray(ProviderContext context, Map<String, Object> para){
		List<K> records = (List<K>) para.get("records");
		String[] columns = (String[]) para.get("columns");
		
		return new SQL(){{
			for(K k : records){
				UPDATE_BY_COLUMNS(k, columns);
				CUSTOM(SQLConsts.END);
			}
		}}.toString();
	}
	
	/**
	 * 根据字段批量更新
	 * @param records 实体数据
	 * @param columns 字段名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> String $updateListByColumnList(ProviderContext context, Map<String, Object> para){
		List<K> records = (List<K>) para.get("records");
		List<String> columns = (List<String>) para.get("columns");
		
		return new SQL(){{
			for(K k : records){
				UPDATE_BY_COLUMNS(k, columns);
				CUSTOM(SQLConsts.END);
			}
		}}.toString();
	}
	
}

package com.liuzi.mybatis.currency.provider;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.builder.annotation.ProviderContext;

import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.currency.data.TableMataData;


/**
 * 通用删除实现类
 * @author zsy
 */
public class DeleteSqlProvider {
	
	/**
	 * 根据主键值删除
	 * @param pkValue 主键值
	 * @return
	 */
	public String $deleteByPk(ProviderContext context, Map<String, Object> para){
		TableMataData data = TableMataData.getData(context);
		Object pkValue = para.get("pkValue");
		
        return new SQL(){{
        	DELETE_FROM(data.getTable());
        	WHERE();
        	EQ(data.getPk(), pkValue);
		}}.toString();
	}
	
	/**
	 * 根据主键值删除
	 * @param pkValue 多个主键值
	 * @return
	 */
	public String $deleteByPkArray(ProviderContext context, Map<String, Object> para){
		TableMataData data = TableMataData.getData(context);
		Object[] pkValues = (Object[]) para.get("pkValues");
		
        return new SQL(){{
        	DELETE_FROM(data.getTable());
        	WHERE();
        	if(pkValues.length > 1){
        		IN(data.getPk(), pkValues);
        	}else{
        		EQ(data.getPk(), pkValues[0]);
        	}
		}}.toString();
	}
	
	/**
	 * 根据主键值删除
	 * @param pkValues 多个主键值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String $deleteByPkList(ProviderContext context, Map<String, Object> para){
		TableMataData data = TableMataData.getData(context);
		List<Object> pkValues = (List<Object>) para.get("pkValues");
		
        return new SQL(){{
        	DELETE_FROM(data.getTable());
        	WHERE();
        	if(pkValues.size() > 1){
        		IN(data.getPk(), pkValues);
        	}else{
        		EQ(data.getPk(), pkValues.get(0));
        	}
		}}.toString();
	}
	
	/**
	 * 根据字段删除
	 * @param column 字段名称
	 * @param value 字段值
	 * @return
	 */
	public String $deleteByColumn(ProviderContext context, Map<String, Object> para){
		TableMataData data = TableMataData.getData(context);
		String column = (String) para.get("column");
		Object value = para.get("value");
		
        return new SQL(){{
        	DELETE_FROM(data.getTable());
        	WHERE();
        	EQ(column, value);
		}}.toString();
	}
	
	/**
	 * 根据字段多值删除
	 * @param column 字段名称
	 * @param values 多个字段值
	 * @return
	 */
	public String $deleteByColumnValueArray(ProviderContext context, Map<String, Object> para){
		TableMataData data = TableMataData.getData(context);
		String column = (String) para.get("column");
		Object[] values = (Object[]) para.get("values");
		
        return new SQL(){{
        	DELETE_FROM(data.getTable());
        	WHERE();
        	if(values.length > 1){
        		IN(column, values);
        	}else{
        		EQ(column, values[0]);
        	}
		}}.toString();
	}
	
	/**
	 * 根据字段多值删除
	 * @param column 字段名称
	 * @param values 多个字段值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String $deleteByColumnValueList(ProviderContext context, Map<String, Object> para){
		TableMataData data = TableMataData.getData(context);
		String column = (String) para.get("column");
		List<Object> values = (List<Object>) para.get("values");
		
        return new SQL(){{
        	DELETE_FROM(data.getTable());
        	WHERE();
        	if(values.size() > 1){
        		IN(column, values);
        	}else{
        		EQ(column, values.get(0));
        	}
		}}.toString();
	}
	
	/**
	 * 根据多字段同值删除
	 * @param columns 字段名称
	 * @param value 多个字段值
	 * @return
	 */
	public String $deleteByColumnArray(ProviderContext context, Map<String, Object> para){
		TableMataData data = TableMataData.getData(context);
		String[] columns = (String[]) para.get("columns");
		Object value =  para.get("value");
		
        return new SQL(){{
        	DELETE_FROM(data.getTable());
        	WHERE();
        	for(int i = 0, j = columns.length; i < j; i ++){
        		if(i > 0){
        			AND();
        		}
        		EQ(columns[i], value);
        	}
		}}.toString();
	}
	
	/**
	 * 根据多字段同值删除
	 * @param columns 字段名称
	 * @param value 多个字段值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String $deleteByColumnList(ProviderContext context, Map<String, Object> para){
		TableMataData data = TableMataData.getData(context);
		List<String> columns = (List<String>) para.get("columns");
		Object value =  para.get("value");
		
        return new SQL(){{
        	DELETE_FROM(data.getTable());
        	WHERE();
        	for(int i = 0, j = columns.size(); i < j; i ++){
        		if(i > 0){
        			AND();
        		}
        		EQ(columns.get(i), value);
        	}
		}}.toString();
	}
	
	/**
	 * 根据多个参数删除
	 * @param columns (字段名key, 字段值value)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String $deleteByColumnMap(ProviderContext context, Map<String, Object> para){
		TableMataData data = TableMataData.getData(context);
		Map<String, Object> columns = (Map<String, Object>) para.get("columns");
		
        return new SQL(){{
        	DELETE_FROM(data.getTable());
        	WHERE();
        	int i = 0;
        	for(Entry<String, Object> entry : columns.entrySet()){
        		if(i > 0){
        			AND();
        		}
        		EQ(entry.getKey(), entry.getValue());
        		i++;
        	}
		}}.toString();
	}
}

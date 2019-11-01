package com.liuzi.mybatis.currency.cond;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.liuzi.mybatis.currency.consts.SQLConsts;
import com.liuzi.mybatis.currency.data.ColumnData;
import com.liuzi.mybatis.currency.data.TableData;
import com.liuzi.mybatis.currency.util.DataUtil;
import com.liuzi.util.common.Log;


/**
 * SQL创建
 * @author zsy
 */
public class SQL extends SQLBase{
	
	public SQL(){
		this.sql = new StringBuffer();
	}
	/**
	 * 创建
	 * @param sql
	 * @return
	 */
	public SQL CREATE(String sql){
		this.append(sql);
		return this;
	}
	
	public SQL SELECT(Class<?> clazz){
		return SELECT(clazz, null);
	}
	
	public <T> SQL SELECT(T t){
		Class<?> clazz = t.getClass(); 
		//获取表信息
		TableData tableData = DataUtil.getTable(clazz);
		//表名
		String tableName = tableData.getName();
		//别名
		String tableAs = tableData.getAs();
		
		Map<String, Object> values = new HashMap<>();
		
		this.append(SQLConsts.SELECT);
		List<Field> fields = DataUtil.getFields(clazz);
		if(fields == null || fields.isEmpty()){
			this.put(tableAs, SQLConsts.POINT, SQLConsts.ALL);
		}else{
			int length = fields.size();
			if(length == 0){
				this.put(tableAs, SQLConsts.POINT, SQLConsts.ALL);
			}else{
				for(int i = 0; i < length; i ++){
					if(i > 0){
						this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
					}
					Field field = fields.get(i);
					//获取字段信息
					ColumnData columnData = DataUtil.getColumn(field);
					//字段名
					String column = columnData.getColumn();
					//字段别名
					String fas = columnData.getAs();
					
					field.setAccessible(true);
					//查询条件
					try {
						Object value = field.get(t);
						if(value != null){
							values.put(column, value);
						}
					} catch (Exception e) {
						Log.error(e, "INSERT SQL BUILD ERROR");
					}
					
					this.put(tableAs, SQLConsts.POINT, column, SQLConsts.AS, fas);
				}
			}
		}
		this.put(SQLConsts.FROM, tableName, SQLConsts.AS, tableAs);
		
		if(values != null && !values.isEmpty()){
			this.append(SQLConsts.WHERE);
			Iterator<Entry<String, Object>> itor = values.entrySet().iterator();
			int i = 0;
			while(itor.hasNext()){
				if(i > 0){
					this.append(SQLConsts.AND);
				}
				i++;
				Entry<String, Object> entry = itor.next();
				this.put(tableAs, SQLConsts.POINT, entry.getKey(), SQLConsts.SPACE,
						SQLConsts.EQ, SQLConsts.SPACE);
				this.decorate(entry.getValue());
			}
		}
		
		return this;
	}
	
	public SQL SELECT(Class<?> clazz, String tableAsName){
		//获取表信息
		TableData tableData = DataUtil.getTable(clazz, tableAsName);
		//表名
		String tableName = tableData.getName();
		//别名
		String tableAs = tableData.getAs();
		
		this.append(SQLConsts.SELECT);
		List<Field> fields = DataUtil.getFields(clazz);
		if(fields == null || fields.isEmpty()){
			this.put(tableAs, SQLConsts.POINT, SQLConsts.ALL);
		}else{
			int length = fields.size();
			if(length == 0){
				this.put(tableAs, SQLConsts.POINT, SQLConsts.ALL);
			}else{
				for(int i = 0; i < length; i ++){
					if(i > 0){
						this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
					}
					//获取字段信息
					ColumnData columnData = DataUtil.getColumn(fields.get(i));
					//字段名
					String column = columnData.getColumn();
					//字段别名
					String fas = columnData.getAs();
					
					this.put(tableAs, SQLConsts.POINT, column, SQLConsts.AS, fas);
				}
			}
		}
		this.put(SQLConsts.FROM, tableName, SQLConsts.AS, tableAs);
		return this;
	}
	
	public SQL SELECT(String colums){
		this.put(SQLConsts.SELECT, colums);
		return this;
	}
	
	public SQL SELECT_BY_PK(Class<?> clazz, Object pkVal){
		return SELECT_BY_PK(clazz, null, null, pkVal);
	}
	
	public SQL SELECT_BY_PK(Class<?> clazz, String pkName, Object pkVal){
		return SELECT_BY_PK(clazz, null, pkName, pkVal);
	}
	
	public SQL SELECT_BY_PK(Class<?> clazz, String tableAsName, String pkName, Object pkVal){
		//获取表信息
		TableData tableData = DataUtil.getTable(clazz, tableAsName);
		//表名
		String tableName = tableData.getName();
		//别名
		String tableAs = tableData.getAs();
		
		String pk = "id";
		
		this.append(SQLConsts.SELECT);
		List<Field> fields = DataUtil.getFields(clazz);
		if(fields == null || fields.isEmpty()){
			this.put(tableAs, SQLConsts.POINT, SQLConsts.ALL);
		}else{
			int length = fields.size();
			if(length == 0){
				this.put(tableAs, SQLConsts.POINT, SQLConsts.ALL);
			}else{
				for(int i = 0; i < length; i ++){
					if(i > 0){
						this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
					}
					//获取字段信息
					ColumnData columnData = DataUtil.getColumn(fields.get(i));
					//字段名
					String column = columnData.getColumn();
					//字段别名
					String fas = columnData.getAs();
					
					if(columnData.isPk()){
						pk = column;
					}
					
					this.put(tableAs, SQLConsts.POINT, column, SQLConsts.AS, fas);
				}
			}
		}
		pk = !StringUtils.isEmpty(pkName) ? pkName : pk;
		this.put(SQLConsts.FROM, tableName, SQLConsts.AS, tableAs, SQLConsts.WHERE, 
				tableAs, SQLConsts.POINT, pk, SQLConsts.SPACE, SQLConsts.EQ, SQLConsts.SPACE);
		this.decorate(pkVal);
		return this;
	}
	
	public SQL SELECT_COUNT_1(String table){
		this.put(SQLConsts.SELECT, SQLConsts.COUNT$1$, SQLConsts.FROM, table);
		return this;
	}
	
	public SQL SELECT_COUNT_ALL(String table){
		this.put(SQLConsts.SELECT, SQLConsts.COUNT$ALL$, SQLConsts.FROM, table);
		return this;
	}
	
	public <T> SQL INSERT(T t){
		if(t == null){
			return this;
		}
		
		Class<?> clazz = t.getClass();
		
		//获取表信息
		TableData tableData = DataUtil.getTable(clazz);
		//表名
		String tableName = tableData.getName();
		
		this.put(SQLConsts.INSERT_INTO, tableName, SQLConsts.OPEN);
		
		List<Field> fields = DataUtil.getFields(clazz);
		List<Object> values = new ArrayList<>();
		
		for(int i = 0, j = fields.size(); i < j; i ++){
			Field field = fields.get(i);
			field.setAccessible(true);
			Object value = null;
			try {
				value = field.get(t);
			} catch (Exception e) {
				Log.error(e, "INSERT SQL BUILD ERROR");
			}
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			values.add(value);
			ColumnData columnData = DataUtil.getColumn(field);
			this.put(columnData.getColumn());
		}
		this.put(SQLConsts.CLOSE, SQLConsts.VALUES, SQLConsts.OPEN);
		
		for(int i = 0, j = values.size(); i < j; i ++){
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			this.decorate(values.get(i));
		}
		this.append(SQLConsts.CLOSE);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T> SQL INSERT(T... array){
		if(array == null || array.length == 0){
			return this;
		}
		
		T t = array[0];
		Class<?> clazz = t.getClass();
		
		//获取表信息
		TableData tableData = DataUtil.getTable(clazz);
		//表名
		String tableName = tableData.getName();
		
		this.put(SQLConsts.INSERT_INTO, tableName, SQLConsts.OPEN);
		
		List<Field> fields = DataUtil.getFields(clazz);
		for(int i = 0, j = fields.size(); i < j; i ++){
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			ColumnData columnData = DataUtil.getColumn(fields.get(i));
			this.append(columnData.getColumn());
		}
		this.put(SQLConsts.CLOSE, SQLConsts.VALUES);
		
		for(int i = 0, j = array.length; i < j; i ++){
			T a = array[i];
			
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			this.append(SQLConsts.OPEN);
			
			for(int ii = 0, jj = fields.size(); ii < jj; ii ++){
				if(ii > 0){
					this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
				}
				Field field = fields.get(ii);
				
				field.setAccessible(true);
				Object value = null;
				try {
					value = field.get(a);
				} catch (Exception e) {
					Log.error(e, "INSERT SQL BUILD ERROR");
				}
				this.decorate(value);
			}
			this.append(SQLConsts.CLOSE);
		}
		return this;
	}
	
	public <T> SQL INSERT(List<T> list){
		if(list == null || list.isEmpty()){
			return this;
		}
		
		T t = list.get(0);
		Class<?> clazz = t.getClass();
		
		//获取表信息
		TableData tableData = DataUtil.getTable(clazz);
		//表名
		String tableName = tableData.getName();
		
		this.put(SQLConsts.INSERT_INTO, tableName, SQLConsts.OPEN);
		
		List<Field> fields = DataUtil.getFields(clazz);
		for(int i = 0, j = fields.size(); i < j; i ++){
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			ColumnData columnData = DataUtil.getColumn(fields.get(i));
			this.append(columnData.getColumn());
		}
		this.put(SQLConsts.CLOSE, SQLConsts.VALUES);
		
		for(int i = 0, j = list.size(); i < j; i ++){
			T a = list.get(i);
			
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			this.append(SQLConsts.OPEN);
			
			for(int ii = 0, jj = fields.size(); ii < jj; ii ++){
				if(ii > 0){
					this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
				}
				
				Field field = fields.get(ii);
				field.setAccessible(true);
				Object value = null;
				try {
					value = field.get(a);
				} catch (Exception e) {
					Log.error(e, "INSERT SQL BUILD ERROR");
				}
				this.decorate(value);
			}
			this.put(SQLConsts.CLOSE);
		}
		return this;
	}
	
	public SQL INSERT_INTO(String table){
		if(StringUtils.isBlank(table)){
			return this;
		}
		this.put(SQLConsts.INSERT_INTO, table);
		return this;
	}
	
	public SQL COLUMNS(String... columns){
		int length;
		if(columns != null && (length = columns.length) > 0){
			this.append(SQLConsts.OPEN);
			for(int i = 0; i < length; i ++){
				if(i > 0){
					this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
				}
				this.append(columns[i]);
			}
			this.append(SQLConsts.CLOSE);
		}
		return this;
	}
	
	public SQL VALUES(Object... values){
		this.put(SQLConsts.VALUES, SQLConsts.OPEN);
		for(int i = 0; i < values.length; i ++){
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			this.decorate(values[i]);
		}
		this.append(SQLConsts.CLOSE);
		return this;
	}
	
	public <T> SQL UPDATE(T t){
		if(t == null){
			return this;
		}
		Class<?> clazz = t.getClass();
		
		//获取表信息
		TableData tableData = DataUtil.getTable(clazz);
		//表名
		String tableName = tableData.getName();
		
		this.put(SQLConsts.UPDATE, tableName, SQLConsts.SET);
		List<Field> fields = DataUtil.getFields(clazz);
		for(int i = 0; i < fields.size(); i ++){
			Field field = fields.get(i);
			field.setAccessible(true);
			Object value = null;
			try {
				value = field.get(t);
			} catch (Exception e) {
				Log.error(e, "UPDATE SQL BUILD ERROR");
			}
			if(value == null){
				continue;
			}
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			ColumnData columnData = DataUtil.getColumn(fields.get(i));
			this.put(columnData.getColumn(), SQLConsts.SPACE, 
					SQLConsts.EQ, SQLConsts.SPACE);
			this.decorate(value);
		}
		return this;
	}
	
	public <T> SQL UPDATE_BY_PK(T t){
		if(t == null){
			return this;
		}
		Class<?> clazz = t.getClass();
		
		//获取表信息
		TableData tableData = DataUtil.getTable(clazz);
		//表名
		String tableName = tableData.getName();
		
		this.put(SQLConsts.UPDATE, tableName, SQLConsts.SET);
		List<Field> fields = DataUtil.getFields(clazz);
		
		//主键
		String pk = "id";
		//主键值
		Object val = DataUtil.getMethodValue(t, pk);
		
		for(int i = 0; i < fields.size(); i ++){
			Field field = fields.get(i);
			field.setAccessible(true);
			Object value = null;
			try {
				value = field.get(t);
			} catch (Exception e) {
				Log.error(e, "UPDATE SQL BUILD ERROR");
			}
			if(value == null){
				continue;
			}
			
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			
			ColumnData columnData = DataUtil.getColumn(field);
			String c = columnData.getColumn();
			
			if(columnData.isPk()){
				pk = c;
				val = value;
			}
			
			this.put(c, SQLConsts.SPACE, SQLConsts.EQ, SQLConsts.SPACE);
			this.decorate(value);
		}
		
		this.put(SQLConsts.WHERE, pk, SQLConsts.SPACE, SQLConsts.EQ, SQLConsts.SPACE);
		this.decorate(val);
		
		return this;
	}
	
	public <T> SQL UPDATE_BY_COLUMNS(T t, String... columns){
		return UPDATE_BY_COLUMNS(t, Arrays.asList(columns));
	}
	
	public <T> SQL UPDATE_BY_COLUMNS(T t, List<String> columns){
		if(t == null){
			return this;
		}
		Class<?> clazz = t.getClass();
		
		//获取表信息
		TableData tableData = DataUtil.getTable(clazz);
		//表名
		String tableName = tableData.getName();
		
		this.put(SQLConsts.UPDATE, tableName, SQLConsts.SET);
		List<Field> fields = DataUtil.getFields(clazz);
		
		Map<String, Object> whereColumns = new HashMap<>();
		
		for(int i = 0; i < fields.size(); i ++){
			Field field = fields.get(i);
			field.setAccessible(true);
			Object value = null;
			try {
				value = field.get(t);
			} catch (Exception e) {
				Log.error(e, "UPDATE SQL BUILD ERROR");
			}
			ColumnData columnData = DataUtil.getColumn(field);
			String c = columnData.getColumn();
			
			if(value == null){
				if(columns != null && columns.contains(c)){
					whereColumns.put(c, value);
				}
				continue;
			}
			
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			
			if(columns != null && columns.contains(c)){
				whereColumns.put(c, value);
			}
			
			this.put(c, SQLConsts.SPACE, SQLConsts.EQ, SQLConsts.SPACE);
			this.decorate(value);
		}
		
		if(!whereColumns.isEmpty()){
			this.append(SQLConsts.WHERE);
			int i = 0;
			Iterator<Entry<String, Object>> it = whereColumns.entrySet().iterator();
			while(it.hasNext()){
				if(i > 0){
					this.append(SQLConsts.AND);
				}
				i ++;
				Entry<String, Object> entry = it.next();
				this.put(entry.getKey(), SQLConsts.SPACE, SQLConsts.EQ, SQLConsts.SPACE);
				this.decorate(entry.getValue());
			}
		}
		
		return this;
	}
	
	public <T> SQL UPDATE_BY_COLUMNS(T t, Map<String, Object> whereColumns){
		if(t == null){
			return this;
		}
		Class<?> clazz = t.getClass();
		
		//获取表信息
		TableData tableData = DataUtil.getTable(clazz);
		//表名
		String tableName = tableData.getName();
		
		this.put(SQLConsts.UPDATE, tableName, SQLConsts.SET);
		List<Field> fields = DataUtil.getFields(clazz);
		
		for(int i = 0; i < fields.size(); i ++){
			Field field = fields.get(i);
			field.setAccessible(true);
			Object value = null;
			try {
				value = field.get(t);
			} catch (Exception e) {
				Log.error(e, "UPDATE SQL BUILD ERROR");
			}
			if(value == null){
				continue;
			}
			if(i > 0){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			ColumnData columnData = DataUtil.getColumn(field);
			this.put(columnData.getColumn(), SQLConsts.SPACE, SQLConsts.EQ, SQLConsts.SPACE);
			this.decorate(value);
		}
		
		if(!whereColumns.isEmpty()){
			this.append(SQLConsts.WHERE);
			int i = 0;
			Iterator<Entry<String, Object>> it = whereColumns.entrySet().iterator();
			while(it.hasNext()){
				if(i > 0){
					this.append(SQLConsts.AND);
				}
				i ++;
				Entry<String, Object> entry = it.next();
				this.put(entry.getKey(), SQLConsts.SPACE, SQLConsts.EQ, SQLConsts.SPACE);
				this.decorate(entry.getValue());
			}
		}
		
		return this;
	}
	
	public SQL UPDATE(String table){
		this.put(SQLConsts.UPDATE, table, SQLConsts.SET);
		return this;
	}
	
	public SQL SET(String column, Object value){
		if(value != null){
			if(!this.sql.toString().endsWith(SQLConsts.SET)){
				this.put(SQLConsts.PAUSE, SQLConsts.SPACE);
			}
			this.put(column, SQLConsts.SPACE, SQLConsts.EQ, SQLConsts.SPACE);
			this.decorate(value);
		}
		return this;
	}
	
	public SQL DELETE_FROM(String table){
		this.put(SQLConsts.DELETE_FROM, table);
		return this;
	}
}

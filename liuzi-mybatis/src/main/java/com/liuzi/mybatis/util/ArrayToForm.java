package com.liuzi.mybatis.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.liuzi.util.common.Log;



public class ArrayToForm{
	
	private Class<?> clazz;
	private List<Object> list;
	
	public <T> ArrayToForm(Class<T> clazz){
		this.clazz = clazz;
	}
	
	public <T> ArrayToForm put(String name, Object[] values){
		if(StringUtils.isEmpty(name)){
			Log.warn("BatchForm build error: name/values is empty");
			return this;
		}
		
		if(list != null && !list.isEmpty()){
			setField(name, values);
			return this;
		}
		
		list = new ArrayList<>();
		for(Object o : values){
			try {
				list.add(setField(name, o, this.clazz.newInstance()));
			} catch (Exception e) {
				Log.error(e, "BatchForm build error: set fields error");
				throw new IllegalArgumentException("BatchForm build error: set fields error", e);
			}
		}
		return this;
	}
		
	private <T> T setField(String name, Object value, T t){
		if(t == null){
			return null;
		}
		
		try {
			Field field = t.getClass().getDeclaredField(name);
			field.setAccessible(true);
		    field.set(t , value);
		} catch (Exception e) {
			Log.error(e, "BatchForm build error: set fields error");
			throw new IllegalArgumentException("BatchForm build error: set fields error", e);
		}
		return t;
	}
	
	private void setField(String name, Object[] values){
		for(int i = 0; i < values.length; i ++){
			setField(name, values[i], list.get(i));
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> convert(){
		return (List<T>) this.list;
	}
}

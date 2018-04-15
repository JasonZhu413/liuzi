package com.liuzi.mybatis;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class SuperClassGenricType {
	public static Class<?> get(Class<?> clazz) {  
	    Type genType = clazz.getGenericSuperclass();  
	    if (!(genType instanceof ParameterizedType)) {  
	        return Object.class;  
	    }  
	  
	    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
	    if (!(params[0] instanceof Class)) {  
	        return Object.class;  
	    }  
	    return (Class<?>) params[0];  
	}  
}

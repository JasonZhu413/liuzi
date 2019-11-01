package com.liuzi.mybatis.util;


import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;


public class HandlerUtil {
	
	public static MetaObject getMetaObject(StatementHandler statementHandler){
		return MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, 
				SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
	}
	
	public static MappedStatement getMappedStatement(MetaObject metaObject){
		return (MappedStatement) metaObject.getValue("delegate.mappedStatement");
	}
	
	public static ParameterHandler getParameterHandler(MetaObject metaObject){
		return (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
	}
}

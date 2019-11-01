package com.liuzi.mybatis.currency.data;

import java.sql.Connection;

import lombok.Data;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;

import com.liuzi.mybatis.util.HandlerUtil;



@Data
public class HandlerMapper {
	
	private Invocation invocation;
	
	private Object target;
	/**
	 * statementHandler
	 */
	private StatementHandler statementHandler;
	/**
	 * executor
	 */
	private Executor executor;
	/**
	 * metaObject
	 */
	private MetaObject metaObject;
	/**
	 * mappedStatement
	 */
	private MappedStatement mappedStatement;
	/**
	 * 参数parameterHandler
	 */
	private ParameterHandler parameterHandler;
	/**
	 * 参数parameterObject
	 */
	private Object parameterObject;
	/**
	 * boundSql
	 */
	private BoundSql boundSql;
	/**
	 * 连接
	 */
	private Connection connection;
	
	public static HandlerMapper statement(Invocation invocation){
		HandlerMapper mapper = new HandlerMapper();
		mapper.setInvocation(invocation);
		mapper.setTarget(invocation.getTarget());
		mapper.setStatementHandler((StatementHandler) invocation.getTarget());
		mapper.setMetaObject(HandlerUtil.getMetaObject(mapper.getStatementHandler()));
		mapper.setMappedStatement(HandlerUtil.getMappedStatement(mapper.getMetaObject()));
		mapper.setParameterHandler(HandlerUtil.getParameterHandler(mapper.getMetaObject()));
		mapper.setParameterObject(mapper.getParameterHandler().getParameterObject());
		mapper.setBoundSql(mapper.getMappedStatement().getBoundSql(mapper.getParameterObject()));
		mapper.setConnection((Connection) invocation.getArgs()[0]);
		return mapper;
	}
	
	public static HandlerMapper executor(Invocation invocation){
		HandlerMapper mapper = new HandlerMapper();
		mapper.setInvocation(invocation);
		mapper.setTarget(invocation.getTarget());
		mapper.setExecutor((Executor) invocation.getTarget());
		mapper.setMappedStatement((MappedStatement) invocation.getArgs()[0]);
		mapper.setParameterObject(invocation.getArgs()[1]);
		mapper.setBoundSql(mapper.getMappedStatement().getBoundSql(mapper.getParameterObject()));
		
		//mapper.setMetaObject(HandlerUtil.getMetaObject(mapper.getStatementHandler()));
		//mapper.setParameterHandler(HandlerUtil.getParameterHandler(mapper.getMetaObject()));
		//mapper.setParameterObject(mapper.getParameterHandler().getParameterObject());
		//mapper.setBoundSql(mapper.getMappedStatement().getBoundSql(mapper.getParameterObject()));
		//mapper.setConnection((Connection) invocation.getArgs()[0]);
		return mapper;
	}
}

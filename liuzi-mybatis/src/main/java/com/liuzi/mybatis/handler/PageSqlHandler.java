package com.liuzi.mybatis.handler;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.plugin.Intercepts;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import com.liuzi.mybatis.currency.data.ColumnData;
import com.liuzi.mybatis.currency.data.HandlerMapper;
import com.liuzi.mybatis.currency.data.TableMataData;

/**
 * 分页sql拦截器
 * @author zsy
 */
@Intercepts({
	@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class , 
		Integer.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, 
    	RowBounds.class, ResultHandler.class}),
})
public class PageSqlHandler implements Interceptor {
	
	@Override
    public Object intercept(Invocation invocation) throws Throwable {
    	Object target = invocation.getTarget();
    	if(target instanceof StatementHandler){
    		//分页
    		HandlerMapper mapper = HandlerMapper.statement(invocation);
    		SqlPageExecutor.sqlPage(mapper);
    	}
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

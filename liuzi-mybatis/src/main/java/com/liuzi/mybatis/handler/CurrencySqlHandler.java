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
 * 通用sql拦截器
 * mybatis只能拦截一下几个接口的方法
	Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
	ParameterHandler (getParameterObject, setParameters)
	ResultSetHandler (handleResultSets, handleOutputParameters)
	StatementHandler (prepare, parameterize, batch, update, query)
	只有StatementHandler#prepare()可以得到connection
 * @author zsy
 */
@Intercepts({
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, 
    	RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, 
    	RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class CurrencySqlHandler implements Interceptor {
	
	private static final String RESOURCE = ".xml";

	@Override
    public Object intercept(Invocation invocation) throws Throwable {
    	Object target = invocation.getTarget();
    	if(target instanceof Executor){//通用sql处理
    		
    		MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
    		//HandlerMapper mapper = HandlerMapper.executor(invocation);
    		//MappedStatement statement = mapper.getMappedStatement();
    		
    		//xml sql
            if (statement.getResource().contains(RESOURCE)) {
            	//通用分页处理
            	//return SqlPageExecutor.currencyPage(mapper);
                return invocation.proceed();
            }
            
            //通用其他sql处理
            
    		ResultMap resultMap = statement.getResultMaps().iterator().next();
            if (!CollectionUtils.isEmpty(resultMap.getResultMappings())) {
                return invocation.proceed();
            }
            
            Class<?> mapType = resultMap.getType();
            if (ClassUtils.isAssignable(mapType, Collection.class)) {
                return invocation.proceed();
            }
            
            TableMataData data = TableMataData.getData(mapType);
            List<ColumnData> columns = data.getColumns();
            
            List<ResultMapping> resultMappings = new ArrayList<>(columns.size());
            for(ColumnData cd : columns){
                resultMappings.add(new ResultMapping.Builder(statement.getConfiguration(), 
                		cd.getProperty(), 
                		cd.getColumn(), 
                		cd.getFieldType()
                	).build());
            }
            /*Map<String, Class<?>> fieldTypeMap = data.getFieldTypeMap();
            List<ResultMapping> resultMappings = new ArrayList<>(fieldTypeMap.size());
            for (Map.Entry<String, String> entry : mataDate.getFieldColumnMap().entrySet()) {
                ResultMapping resultMapping = new ResultMapping.Builder(ms.getConfiguration(), 
                		entry.getKey(), entry.getValue(), fieldTypeMap.get(entry.getKey())).build();
                resultMappings.add(resultMapping);
            }*/
            
            ResultMap newRm = new ResultMap.Builder(statement.getConfiguration(), resultMap.getId(), 
            		mapType, resultMappings).build();
            Field field = ReflectionUtils.findField(MappedStatement.class, "resultMaps");
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, statement, Collections.singletonList(newRm));
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

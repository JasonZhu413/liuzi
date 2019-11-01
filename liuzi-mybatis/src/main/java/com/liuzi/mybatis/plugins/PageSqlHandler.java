package com.liuzi.mybatis.plugins;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.plugin.Intercepts;

import com.liuzi.mybatis.currency.cond.Query;
import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.currency.consts.HandlerConsts;
import com.liuzi.mybatis.currency.consts.SQLConsts;
import com.liuzi.mybatis.currency.data.HandlerMapper;
import com.liuzi.mybatis.server.pojo.Page;
import com.liuzi.util.common.Log;

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
		sqlPage(HandlerMapper.statement(invocation));
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
    	//只拦截StatementHandler对象
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {}
    
    /**
	 * 用户自定义xml分页
	 * @param mapper
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> void sqlPage(HandlerMapper mapper) throws InvocationTargetException, 
		IllegalAccessException{
		//获取参数
		Object parameterObject = mapper.getParameterObject();
		Query query = null;
		if (parameterObject instanceof MapperMethod.ParamMap){
			MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameterObject;
			Set<Map.Entry<String, Object>> set = paramMap.entrySet();
			for(Entry<String, Object> entry : set){
				if(entry.getKey().equals("query") && entry.getValue() instanceof Query){
					query = (Query) entry.getValue();
					break;
				}
			}
		} else if (parameterObject instanceof Query){
			query = (Query) parameterObject;
		} else {
			return;
		}
		
		if(query == null || !query.containsKey(HandlerConsts.$PAGE)){
			return;
		}
		
		//获取page参数
		Page<T> page = (Page<T>) query.get(HandlerConsts.$PAGE);
    	if(page == null){
    		return;
    	}
    	
    	//查询总数
    	page.setTotalCount($selectCount(mapper));
    	
    	//组装新sql
    	/*SQL newSql = new SQL(){{
    		SELECT();
    		CUSTOM(HandlerConsts.$PAGE, SQLConsts.POINT, SQLConsts.ALL);
    		FROM();
    		OPEN();
    		CUSTOM(mapper.getBoundSql().getSql());
    		CLOSE();
    		AS(HandlerConsts.$PAGE);
    		LIMIT(page.getLimit(), page.getOffset());
    	}};*/
    	SQL newSql = new SQL(){{
    		CUSTOM(mapper.getBoundSql().getSql());
    		LIMIT(page.getLimit(), page.getOffset());
    	}};
		//更新分页参数
    	mapper.getMetaObject().setValue("delegate.boundSql.sql", newSql.toString());
	}
    
    /**
     * 查询数量
     */
    public static long $selectCount(HandlerMapper mapper){
    	BoundSql boundSql = mapper.getBoundSql();
    	MappedStatement mappedStatement = mapper.getMappedStatement();
    	Object parameterObject = mapper.getParameterObject();
    	Connection connection = mapper.getConnection();
    	
    	//查询总页数sql
    	/*SQL countSql = new SQL(){{
    		SELECT();
    		CUSTOM(SQLConsts.COUNT$1$);
    		FROM();
    		OPEN();
    		CUSTOM(boundSql.getSql());
    		CLOSE();
    		AS("page_count");
    	}};*/
    	SQL countSql = new SQL(){{
    		SELECT();
    		CUSTOM(SQLConsts.COUNT$1$);
    		
    		String oldSql = boundSql.getSql();
    		int index = oldSql.toLowerCase().indexOf("from");
    		
            CUSTOM(oldSql.substring(index));
    	}};
		
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql.toString(), 
				parameterMappings, parameterObject);
		
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, 
				parameterObject, countBoundSql);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = connection.prepareStatement(countSql.toString());
			parameterHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		}catch(Exception e){
			Log.error(e, "查询总条数异常");
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					Log.error(e, "查询总条数异常");
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					Log.error(e, "查询总条数异常");
				}
			}
		}
		return 0;
	}
}

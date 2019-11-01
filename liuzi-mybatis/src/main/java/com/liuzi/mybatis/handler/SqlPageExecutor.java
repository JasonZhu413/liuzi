package com.liuzi.mybatis.handler;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import com.liuzi.mybatis.currency.cond.Query;
import com.liuzi.mybatis.currency.cond.SQL;
import com.liuzi.mybatis.currency.consts.HandlerConsts;
import com.liuzi.mybatis.currency.consts.SQLConsts;
import com.liuzi.mybatis.currency.data.HandlerMapper;
import com.liuzi.mybatis.server.pojo.Page;
import com.liuzi.util.common.Log;


/**
 * 分页sql处理
 * @author zsy
 */
public class SqlPageExecutor{
	
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
    	SQL newSql = new SQL(){{
    		SELECT();
    		CUSTOM(HandlerConsts.$PAGE, SQLConsts.POINT, SQLConsts.ALL);
    		FROM();
    		OPEN();
    		CUSTOM(mapper.getBoundSql().getSql());
    		CLOSE();
    		AS(HandlerConsts.$PAGE);
    		LIMIT(page.getLimit(), page.getOffset());
    	}};
		//更新分页参数
    	mapper.getMetaObject().setValue("delegate.boundSql.sql", newSql.toString());
	}
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Object currencyPage(HandlerMapper mapper)
			throws InvocationTargetException, IllegalAccessException{
    	
    	Invocation invocation = mapper.getInvocation();
    	MappedStatement statement = mapper.getMappedStatement();
    	
        String mappedid = statement.getId();
        if(mappedid == null || (!mappedid.endsWith(".$selectPage") &&
        		!mappedid.endsWith(".$selectTargetPage"))){
        	return invocation.proceed();
        }

        //参数
    	Object parameterObject = mapper.getParameterObject();
		if(!(parameterObject instanceof MapperMethod.ParamMap)) {
			return invocation.proceed();
		}
		
		MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameterObject;
		//通用分页
		if(!paramMap.containsKey(HandlerConsts.$PAGE)){
			return invocation.proceed();
		}
		
		Page<T> page = (Page<T>) paramMap.get(HandlerConsts.$PAGE);
    	if(page == null){
    		return invocation.proceed();
    	}
    	
    	//查询总数
    	long totalCount = $selectCount(mapper);
    	page.setTotalCount(totalCount);
    	
    	//组装新sql
    	SQL newSql = new SQL(){{
    		SELECT();
    		CUSTOM(HandlerConsts.$PAGE, SQLConsts.POINT, SQLConsts.ALL);
    		FROM();
    		OPEN();
    		CUSTOM(mapper.getBoundSql().getSql());
    		CLOSE();
    		AS(HandlerConsts.$PAGE);
    		LIMIT(page.getLimit(), page.getOffset());
    	}};
    	
		//更新分页参数
    	mapper.getMetaObject().setValue("delegate.boundSql.sql", newSql.toString());
    	
		Object val = invocation.proceed();
		
		if(val != null){
			List<T> data = (List<T>) val; 
			//计算总页数
			int pageSize = page.getPageSize();
			int pageTotal = (int) (totalCount / pageSize);
			if (totalCount % pageSize > 0) {
		    	++ pageTotal;
		    }
			//当前页条数
			int number = data == null ? 0 : data.size();
			//组装返回实体
			page.setData(data);//返回数据
			page.setPageTotal(pageTotal);//总页数
			page.setNumber(number);//当前页条数
		}
    	return mapper.getInvocation().proceed();
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
    	SQL countSql = new SQL(){{
    		SELECT();
    		CUSTOM(SQLConsts.COUNT$1$);
    		FROM();
    		OPEN();
    		CUSTOM(boundSql.getSql());
    		CLOSE();
    		AS("page_count");
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

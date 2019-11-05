package com.liuzi.mybatis.plugins;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.plugin.Intercepts;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.fastjson.JSON;
import com.liuzi.util.date.DateUtil;



@Intercepts({
	@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
	@Signature(type = Executor.class, method = "query",	args = {MappedStatement.class, Object.class, 
		RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, 
    	RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class SqlExecuteInfoHandler implements Interceptor {
	
	private SqlExecuteInfo sqlExecuteInfo;
	
	public SqlExecuteInfoHandler(){
		this(null);
	}
	
	public SqlExecuteInfoHandler(SqlExecuteInfo sqlExecuteInfo){
		if(sqlExecuteInfo == null){
			sqlExecuteInfo = new SqlExecuteInfo();
			sqlExecuteInfo.init();
		}
		this.sqlExecuteInfo = sqlExecuteInfo;
	}
	
	@Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();

        MappedStatement ms = (MappedStatement) args[0];
        ms.getStatementType();
        
        //获取Mapper类
        String mapper = ms.getResource();
        //获取配置信息
        Configuration configuration = ms.getConfiguration();
        //获取Mapper id
        String mapperId = ms.getId();
        //获取数据源
        DruidDataSource dataSource = (DruidDataSource) configuration.getEnvironment().getDataSource();
        //获取数据库类型[即mysql, 或者oracle等等]
        Date createdTime = dataSource.getCreatedTime();
        String dbType = dataSource.getDataSourceStat().getDbType();
        //SQL的参数对象
        Object parameterObject = args[1];
        Object target = invocation.getTarget();
        StatementHandler handler = configuration.newStatementHandler((Executor) target, 
        		ms, parameterObject, RowBounds.DEFAULT, null, null);
        //获取操作类型名称
        //commandName.startsWith(增/删/改/查)，可以得到crud的具体类型[得到的是大写的INSERT UPDATE]
        String commandName = ms.getSqlCommandType().name();
        Method method = invocation.getMethod();
        //可能为update, query, flushStatements, commit, rollback, getTransaction, close, isClosed
        String methodName = method.getName();
        //获取sql信息
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        //获取绑定参数及参数类型
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        //将参数值转成json字符串
        String parameterObjects = JSON.toJSONString(boundSql.getParameterObject());
        //执行SQL（不带参数）
        String srcSQL = boundSql.getSql();
        //组装SQL
        String retSQL = SQLUtils.format(srcSQL, dbType, new ArrayList<Object>());
        //String retSQL = formatSQL(srcSQL, dbType, parameterObjects);
        
        //开始执行时间
        long start = System.currentTimeMillis();
        //执行SQL
        Object result = invocation.proceed();
        //执行结束时间
        long end = System.currentTimeMillis();
        //执行时间（毫秒）
        long time = end - start;

        //影响行数
        //Integer integer = Integer.valueOf(Integer.parseInt(result.toString()));
        //TODO 还可以记录参数，或者单表id操作时，记录数据操作前的状态
        //获取insertSqlLog方法
        //ms = ms.getConfiguration().getMappedStatement("insertSqlLog");
        //替换当前的参数为新的ms
        //args[0] = ms;
        //insertSqlLog 方法的参数为 log
        //args[1]=log;

        //组装自己的SQL记录类
        /*SpAuditDbLog spAuditDbLog = new SpAuditDbLog();
        spAuditDbLog.setId(UUID.randomUUID().toString());
        //记录SQL
        spAuditDbLog.setSqlContent(retSQL);
        //入参
        spAuditDbLog.setInParam(parameterObjects);
        //sql开始执行时间
        spAuditDbLog.setStartTime(DateUtils.dateParse(start, "yyyy-MM-dd HH:mm:ss"));
        //sql执行结束时间
        spAuditDbLog.setEndTime(DateUtils.dateParse(end, "yyyy-MM-dd HH:mm:ss"));
        //耗时
        spAuditDbLog.setCostTime(time);
        //执行结果
        spAuditDbLog.setResutlTupe(StringUtil.isNotEmpty(result) ? "01" : "02");
        SpAuditDbLog save = spAuditDbLogMapper.save(spAuditDbLog);
        */
        
        //处理sql
        sqlExecuteInfo.excute(dbType, mapperId, commandName, methodName, 
        		DateUtil.localDateTimeToString(DateUtil.timestampToLocalDateTime(start), "yyyyMMddHHmmssSSS"),
        		DateUtil.localDateTimeToString(DateUtil.timestampToLocalDateTime(end), "yyyyMMddHHmmssSSS"),
        		time + "", "{" + sqlExecuteInfo.passEntr(retSQL) + "}", parameterObjects);
        
        //返回执行结果
        return result;
    }

    /**
     * plugin方法是拦截器用于封装目标对象的，通过该方法我们可以返回目标对象本身，也可以返回一个它的代理。
     * 当返回的是代理的时候我们可以对其中的方法进行拦截来调用intercept方法，当然也可以调用其他方法
     * 对于plugin方法而言，其实Mybatis已经为我们提供了一个实现。Mybatis中有一个叫做Plugin的类，
     * 里面有一个静态方法wrap(Object target,Interceptor interceptor)，通过该方法可以决定要返回的对象是目标对象还是对应的代理。
     */
    @Override
    public Object plugin(Object o) {
    	//只拦截Executor对象,减少目标被代理的次数
        if (o instanceof Executor && sqlExecuteInfo.allowLog()) {
            return Plugin.wrap(o, this);
        }
        return o;
    }

    /**
     * setProperties方法是用于在Mybatis配置文件中指定一些属性的
     * 这个方法在Configuration初始化当前的Interceptor时就会执行
     */
    @Override
    public void setProperties(Properties properties) {

    }
}

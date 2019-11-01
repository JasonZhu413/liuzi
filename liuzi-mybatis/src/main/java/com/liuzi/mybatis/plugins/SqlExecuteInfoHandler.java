package com.liuzi.mybatis.plugins;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
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
import com.liuzi.util.common.Log;
import com.liuzi.util.date.DateUtil;



@Intercepts({
	@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, 
    	RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})})
public class SqlExecuteInfoHandler implements Interceptor {
	//项目目录
	private static final String LOG_DIR = System.getProperty("user.dir");
	//目录标志
	private static final String FILESEPARATOR = System.getProperty("file.separator");
	//换行
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	//文件后缀
	private static final String SUFFIX = ".txt";
	//文件名前缀
	private static final String LOG_NAME = "sql-excute-log-";
	//操作线程核心线程数
	private static final int THREAD_NUM_MIN = 50;
	//操作线程最大线程数
	private static final int THREAD_NUM_MAX = 100;
	//操作线程超时等待秒数
	private static final int THREAD_WAIT_TIME = 60;
	
	private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(THREAD_NUM_MIN, 
			THREAD_NUM_MAX, THREAD_WAIT_TIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());
	
	/**
	 * 类型
	 * CONSOLE 控制台打印
	 * LOG 日志
	 */
	@Setter
	private String type = "NONE";
	/**
	 * 日志目录
	 */
	@Setter
	private String logPath = LOG_DIR;
	/**
	 * 日志文件名
	 */
	@Setter
	private String logName = LOG_NAME;
	/**
	 * 日志文件名
	 * YEAR, MONTH, DATE, HOUR, MINUTE, SECOND
	 */
	@Setter private String logWriteType = "DATE";
	
	private int corePoolSize;
	private int maximumPoolSize;
	
	private String lp;
	private String ln;
	private String p;
	
	
	public void setCorePoolSize(int corePoolSize) {
		if(corePoolSize <= 0){
			return;
		}
		this.corePoolSize = corePoolSize;
		executor.setCorePoolSize(corePoolSize);
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		if(maximumPoolSize <= 0){
			return;
		}
		this.maximumPoolSize = maximumPoolSize;
		executor.setMaximumPoolSize(maximumPoolSize);
	}
	
	@Override
    public Object intercept(Invocation invocation) throws Throwable {
		int excuteType = type();
		if(excuteType == 0){
			return invocation.proceed();
		}
		
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
        excuteLog(excuteType, dbType, mapperId, commandName, methodName, 
        		time + "", retSQL, parameterObjects);
        
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
        if (o instanceof Executor) {
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
    
    /**
     * 日志操作类型
     * @return
     */
    private int type(){
    	if(type == null){
    		return 0;
    	}
    	switch(this.type){
    		case "NONE":
    			return 0;
    		case "CONSOLE":
    			return 1;
    		case "LOG":
    			initLog();
    			return 2;
    		default:
    			return 0;
    	}
    }
    
    private void initLog(){
    	//文件目录
    	if(this.lp == null){
    		this.lp = StringUtils.isBlank(this.logPath) ? LOG_DIR : this.logPath;
    	}
    	//文件名
    	if(this.ln == null){
    		this.ln = StringUtils.isBlank(this.logName) ? LOG_NAME : this.logName;
    	}
    	//写日志文件格式
		if(this.p == null){
			if(logWriteType == null){
				this.p = "yyyy-MM-dd";
				return;
			}
			switch(logWriteType){
				case "YEAR":
					this.p = "yyyy";
					break;
				case "MONTH":
					this.p = "yyyyMM";
					break;
				case "DATE":
					this.p = "yyyyMMdd";
					break;
				case "HOUR":
					this.p = "yyyyMMddHH";
					break;
				case "MINUTE":
					this.p = "yyyyMMddHHmm";
					break;
				case "SECOND":
					this.p = "yyyyMMddHHmmss";
					break;
				default:
					this.p = "yyyy-MM-dd";
					break;
			}
		}
    }
    
    private void excuteLog(int excuteType, String... o){
        switch(excuteType){
        	case 1:
        		Log.info(String.join(",", o));
        		break;
        	case 2:
        		writeLog(String.join(",", o));
        		break;
        }
    }
	
	private void writeLog(String content){
		executor.execute(() -> {
			String filePath = getFileName();
			
			// 打开一个随机访问文件流，按读写方式 
			try (RandomAccessFile randomFile = new RandomAccessFile(filePath, "rw")) {     
	            //文件字节长度
	            long fileLength = randomFile.length();
	            // 将写文件指针移到文件尾
	            randomFile.seek(fileLength);
	            //文件内容
	            String c = String.format("%s%s", content, LINE_SEPARATOR);
	            randomFile.writeBytes(c);
	            
	            Log.info("Log write success({})...", filePath);
	        } catch (IOException e) {
	        	Log.warn(e, "WRITE LOG ERROR");
	        }
		});
	}
	
	private String getFileName(){
		String pattern = DateUtil.dateToString(new Date(), this.p);
		return String.format("%s%s%s%s%s", this.lp, FILESEPARATOR, this.ln, pattern, SUFFIX);
	}

	
	
}

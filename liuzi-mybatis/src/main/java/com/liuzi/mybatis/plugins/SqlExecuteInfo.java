package com.liuzi.mybatis.plugins;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.liuzi.util.common.Log;
import com.liuzi.util.date.DateUtil;



public class SqlExecuteInfo{
	//操作类型
	public static final String TYPE_NONE = "NONE";
	public static final String TYPE_CONSOLE = "CONSOLE";
	public static final String TYPE_LOG = "LOG";
	//换行符
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	//目录分隔符
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	//项目目录
	private static final String LOG_DIR = System.getProperty("user.dir");
	//文件默认后缀
	private static final String LOG_FILE_NAME_SUFFIX_TXT = "txt";
	private static final String LOG_FILE_NAME_SUFFIX_XML = "xml";
	private static final String LOG_FILE_NAME_SUFFIX_HTML = "html";
	//文件名默认前缀
	private static final String LOG_FILE_NAME_PREFIX = "mybatis-sql-log";
	//文件到达限制后创建新日志
	private static final long LOG_MAX_SIZE = 2 * 1024;
	//log记录类型
	private static final String LOG_FILE_RECORD_TYPE_YEAR = "YEAR";
	private static final String LOG_FILE_RECORD_TYPE_MONTH = "MONTH";
	private static final String LOG_FILE_RECORD_TYPE_DATE = "DATE";
	private static final String LOG_FILE_RECORD_TYPE_HOUR = "HOUR";
	private static final String LOG_FILE_RECORD_TYPE_MINUTE = "MINUTE";
	private static final String LOG_FILE_RECORD_TYPE_SECOND = "SECOND";
	//log记录类型后缀
	private static final String LFRTP_FORMAT_YEAR = "yyyy";
	private static final String LFRTP_FORMAT_MONTH = "yyyyMM";
	private static final String LFRTP_FORMAT_DATE = "yyyyMMdd";
	private static final String LFRTP_FORMAT_HOUR = "yyyyMMddHH";
	private static final String LFRTP_FORMAT_MINUTE = "yyyyMMddHHmm";
	private static final String LFRTP_FORMAT_SECOND = "yyyyMMddHHmmss";
	//文件名连接符
	private static final char CONN = '-';
	//操作线程核心线程数
	private static final int CORE_POOL_SIZE = 50;
	//操作线程最大线程数
	private static final int MAXIMUM_POOL_SIZE = 100;
	//操作线程超时等待秒数
	private static final int THREAD_WAIT_TIME = 60;
	//sql换行符
	private static final Pattern SQL_PATTERN = Pattern.compile("\\r|\n");
	//操作线程
	private ThreadPoolExecutor executor;
	//
	private AtomicLong ato = new AtomicLong(0);
	
	/**
	 * 类型，默认NONE-无操作
	 * NONE-无操作、CONSOLE-控制台打印、LOG-生成日志文件，
	 */
	@Getter private String type = TYPE_NONE;
	/**
	 * 日志目录，默认user.dir
	 */
	@Getter private String logDir = LOG_DIR;
	/**
	 * 日志文件名前缀，默认mybatis-sql-log
	 */
	@Getter private String logFileNamePrefix = LOG_FILE_NAME_PREFIX;
	/**
	 * 日志文件名类型，默认DATE-年月日
	 * YEAR-年, MONTH-年月, DATE-年月日, HOUR-年月日时, 
	 * MINUTE-年月日时分, SECOND-年月日时分秒
	 */
	@Getter private String logFileRecordType = LOG_FILE_RECORD_TYPE_DATE;
	/**
	 * 日志大小，单位M，默认2G
	 * 文件到达限制后创建新日志
	 */
	@Getter private long logMaxSize = LOG_MAX_SIZE;
	/**
	 * 日志文件后缀，默认txt
	 */
	@Getter private String logFileNameSuffix = LOG_FILE_NAME_SUFFIX_TXT;
	/**
	 * 线程核心数，默认50
	 */
	@Getter private int corePoolSize = CORE_POOL_SIZE;
	/**
	 * 线程最大数，默认100
	 */
	@Getter private int maximumPoolSize = MAXIMUM_POOL_SIZE;
	/**
	 * 日志后缀，默认
	 */
	@Getter private String lfrtpFormat = LFRTP_FORMAT_DATE;
	
	public void init(){
		if(TYPE_LOG.equalsIgnoreCase(this.type) && executor == null){
			executor = new ThreadPoolExecutor(this.corePoolSize, 
					this.maximumPoolSize, THREAD_WAIT_TIME, TimeUnit.SECONDS, 
					new LinkedBlockingQueue<Runnable>());
			//Log.info("Mybatis sql execute thread corePoolSize {}", corePoolSize);
		}
	}
	
	public boolean allowLog() {
		if(SqlExecuteInfo.TYPE_CONSOLE.equals(this.type)
				|| SqlExecuteInfo.TYPE_LOG.equals(this.type)){
			return true;
		}
		return false;
	}
	
	public String passEntr(String sql) {
		Matcher matcher = SQL_PATTERN.matcher(sql);
		return matcher.replaceAll(" ");
	}
	
	public void excute(String... o){
		switch(this.type){
	    	case TYPE_CONSOLE:
	    		Log.info(String.join(",", o));
	    		break;
	    	case TYPE_LOG:
	    		writeLog(o);
	    		break;
	    }
	}
	
	private void writeLog(String... o){
		final String logId = Log.getLogId();
		executor.execute(new RFile(logId, getInfo(o)));
	}
	
	private static class RFile implements Runnable{
		
		private String logId;
		private Map<String, String> info;
		
		public RFile(String logId, Map<String, String> info){
			this.logId = logId;
			this.info = info;
		}

		@Override
		public void run() {
			if(info != null && !info.isEmpty()){
				Log.setLogId(logId);
				
				String content = info.get("content");
				String fileName = info.get("fileName");
				if(fileName != null){
					// 打开一个随机访问文件流，按读写方式 
					try (RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw")) {
						//读所有行
						Path path = Paths.get(fileName);
						List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
						
						//content = String.format("%s%s", SqlExecuteInfo.LINE_SEPARATOR, content);
						if(lines == null || lines.isEmpty()){
							//将写文件指针移到文件头
				            //randomFile.seek(0);
							//写文件
				            randomFile.writeBytes(content);
						}else{
							lines.add(lines.size() - 1, content);
							content = String.join("\r\n", lines);
							randomFile.writeBytes(content);
						}
						
			            Log.info("Log write success: {}", fileName);
			        } catch (IOException e) {
			        	Log.warn(e, "WRITE LOG ERROR");
			        }
				}
			}
		}
	}
	
	private Map<String, String> getInfo(String... o){
		//文件目录
		String filePath = String.format("%s%s", this.logDir, FILE_SEPARATOR);
		//判断是否存在文件夹
		File file = new File(filePath);
		if (!file.exists() || !file.isDirectory()){
			Log.error("Mybatis SQL target({}) dir is not exists or not a directory", filePath);
            return null;
        }
		
		if(LOG_DIR.equals(filePath)){
			filePath += "mybatis-logs" + FILE_SEPARATOR;
		}
		
		//文件名称日期
		String pattern = DateUtil.dateToString(new Date(), this.lfrtpFormat);
		//文件名
		String fileName = String.format("%s%s%s%s%s", filePath, this.logFileNamePrefix, CONN, pattern, CONN);
		//文件索引
		long fileIndex = ato.get();
		//是否需要创建新文件
		boolean createNew = false;
		
		//索引文件名
		String indexFileName = String.format("%s%s%s%s", fileName, fileIndex, ".", this.logFileNameSuffix);
		//索引文件
		File indexFile = new File(indexFileName);
		//索引不存在
		if(!indexFile.exists()){
			int index = 1;
			//当天索引为1文件
			indexFileName = String.format("%s%s%s%s", fileName, 1, ".", this.logFileNameSuffix);
			indexFile = new File(indexFileName);
			//如果存在则继续向下找
			if(indexFile.exists()){
				while(true){
					//查找下一个索引文件
					String nextFileName = String.format("%s%s%s%s", fileName, index++, ".", this.logFileNameSuffix);
					File nextFile = new File(nextFileName);
					//如果下一个索引文件不存在，退出循环
					if(!nextFile.exists()){
						break;
					}
					//如果存在，更新索引文件
					indexFileName = nextFileName;
					indexFile = nextFile;
				}
				index--;
			}
			
			//更新索引
			ato.getAndSet(index);
		}
		
		//如果文件存在
		if(indexFile.exists()){
			//如果设置大小限制，判断是否超出限制
			if(this.logMaxSize > 0){
				//文件大小M
				long size = indexFile.length() / 1024 / 1024;
				//如果大于指定大小，新建文件
				if(size >= this.logMaxSize){
					//更新索引+1
					ato.getAndIncrement();
					createNew = true;
				}
			}
		}else{
			//如果不存在需要创建新文件
			createNew = true;
		}
		
		String content = getContent(createNew, indexFileName, o);
		
		Map<String, String> map = new HashMap<>();
		map.put("content", content);
		map.put("fileName", indexFileName);
		return map;
	}
	
	private String getContent(boolean createNew, String indexFileName, String... o){
		switch(this.logFileNameSuffix.toLowerCase()){
			case LOG_FILE_NAME_SUFFIX_XML:
				StringBuilder xml = new StringBuilder();
				if(createNew){
					xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
					xml.append("<mybatis>\r\n");
				}
				xml.append("\t<data>\r\n");
				xml.append("\t\t<traceLogId>").append(Log.getLogId()).append("</traceLogId>\r\n");
				xml.append("\t\t<dataSource>").append(o[0]).append("</dataSource>\r\n");
				xml.append("\t\t<command>").append(o[2]).append("</command>\r\n");
				xml.append("\t\t<method>").append(o[3]).append("</method>\r\n");
				xml.append("\t\t<startTime>").append(o[4]).append("</startTime>\r\n");
				xml.append("\t\t<endTime>").append(o[5]).append("</endTime>\r\n");
				xml.append("\t\t<takeUpTimeMillis>").append(o[6]).append("</takeUpTimeMillis>\r\n");
				xml.append("\t\t<mapper>").append(o[1]).append("</mapper>\r\n");
				String sql = o[7];
				sql = sql.substring(1, sql.length() - 1);
				xml.append("\t\t<sql>").append(sql).append("</sql>\r\n");
				xml.append("\t\t<params>").append(o[8]).append("</params>\r\n");
				xml.append("\t</data>");
				if(createNew){
					xml.append("\r\n</mybatis>");
				}
				return xml.toString();
			case LOG_FILE_NAME_SUFFIX_HTML:
				StringBuilder html = new StringBuilder();
				if(createNew){
					html.append("<html>\r\n");
					html.append("\t<head>\r\n");
					html.append("\t\t<meta charset=\"utf-8\">\r\n");
					html.append("\t\t<title>Mybatis Log</title>\r\n");
					html.append("\t\t<meta name=\"renderer\" content=\"webkit\">\r\n");
					html.append("\t\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\r\n");
					html.append("\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\">\r\n");
					html.append("\t\t<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\r\n");
					html.append("\t\t<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\r\n");
					html.append("\t\t<meta name=\"format-detection\" content=\"telephone=no\">\r\n");
					html.append("\t\t<style type=\"text/css\">\r\n");
					html.append("\t\t\ttable.gridtable {font-family: verdana,arial,sans-serif;font-size:11px;color:#333333;border-width: 1px;border-color: #666666;border-collapse: collapse;}\r\n");
					html.append("\t\t\ttable.gridtable th {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #dedede;}\r\n");
					html.append("\t\t\ttable.gridtable td {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;}\r\n");
					html.append("\t\t</style>\r\n");
					html.append("\t</head>\r\n");
					html.append("\t<body>\r\n");
					html.append("\t\t<h1>Mybatis SQL Logs: ").append(indexFileName).append("</h1>\r\n");
					html.append("\t\t<table class=\"gridtable\">\r\n");
					html.append("\t\t\t<tr>");
					html.append("\t\t\t\t<th>traceLogId</th>\r\n");
					html.append("\t\t\t\t<th>dataSource</th>\r\n");
					html.append("\t\t\t\t<th>command</th>\r\n");
					html.append("\t\t\t\t<th>method</th>\r\n");
					html.append("\t\t\t\t<th>startTime</th>\r\n");
					html.append("\t\t\t\t<th>endTime</th>\r\n");
					html.append("\t\t\t\t<th>takeUpTimeMillis</th>\r\n");
					html.append("\t\t\t\t<th>mapper</th>\r\n");
					html.append("\t\t\t\t<th>sql</th>\r\n");
					html.append("\t\t\t\t<th>params</th>\r\n");
					html.append("\t\t\t</tr>");
				}
				html.append("\t\t\t<tr>\t\n");
				html.append("\t\t\t\t<td>").append(Log.getLogId()).append("</td>\r\n");
				html.append("\t\t\t\t<td>").append(o[0]).append("</td>\r\n");
				html.append("\t\t\t\t<td>").append(o[2]).append("</td>\r\n");
				html.append("\t\t\t\t<td>").append(o[3]).append("</td>\r\n");
				html.append("\t\t\t\t<td>").append(o[4]).append("</td>\r\n");
				html.append("\t\t\t\t<td>").append(o[5]).append("</td>\r\n");
				html.append("\t\t\t\t<td>").append(o[6]).append("</td>\r\n");
				html.append("\t\t\t\t<td>").append(o[1]).append("</td>\r\n");
				sql = o[7];
				sql = sql.substring(1, sql.length() - 1);
				html.append("\t\t\t\t<td>").append(sql).append("</td>\r\n");
				html.append("\t\t\t\t<td>").append(o[8]).append("</td>\r\n");
				html.append("\t\t\t</tr>");
				if(createNew){
					html.append("\r\n</table><body></html>");
				}
				return html.toString();
			default:
				return String.join(",", o);
		}
	}
	
	private void json(Object obj, StringBuilder xml){
		/*try {
			String str = JSONObject.toJSONString(obj);
			JSONObject json = JSONObject.parseObject(str);
			json.forEach((k, v) -> {
				xml.append("\t\t<").append(k).append(">");
				json(v, xml);
				xml.append("</").append(k).append(">\r\n");
			});
	    } catch (Exception e) {
	    	xml.append(obj);
	    }*/
		xml.append(obj);
	}
	
	public void setType(String type) {
		if(StringUtils.isBlank(type)){
			this.type = TYPE_NONE;
		}
		
		switch(type.toUpperCase()){
			case TYPE_CONSOLE:
				this.type = TYPE_CONSOLE;
				break;
			case TYPE_LOG:
				this.type = TYPE_LOG;
				break;
			default:
				Log.warn("Mybatis Sql Handler type is NONE, do not record sql");
				this.type = TYPE_NONE;	
				break;
		}
	}

	public void setLogDir(String logDir) {
		if(StringUtils.isBlank(logDir)){
			Log.warn("Mybatis Sql Handler Log directory is empty, use default dir path({})",
					LOG_DIR);
			this.logDir = LOG_DIR;
			return;
		}
		File file = new File(logDir);
		if(!file.exists() || !file.isDirectory()){
			Log.warn("Mybatis Sql Handler Log directory is not exists, use default dir path({})",
					LOG_DIR);
			this.logDir = LOG_DIR;
			return;
		}
		
		this.logDir = logDir;
	}

	public void setLogFileNamePrefix(String logFileNamePrefix) {
		if(StringUtils.isBlank(logFileNamePrefix)){
			Log.warn("Mybatis Sql Handler logFileNamePrefix is wrong, use default name({})",
					LOG_FILE_NAME_PREFIX);
			this.logFileNamePrefix = LOG_FILE_NAME_PREFIX;
			return;
		}
		this.logFileNamePrefix = logFileNamePrefix;
	}

	public void setLogFileRecordType(String logFileRecordType) {
		if(StringUtils.isBlank(logFileRecordType)){
			this.logFileRecordType = LFRTP_FORMAT_DATE;
		}

		this.logFileRecordType = logFileRecordType.toUpperCase();
		
		switch(this.logFileRecordType){
			case LOG_FILE_RECORD_TYPE_YEAR:
				this.lfrtpFormat = LFRTP_FORMAT_YEAR;
				break;
			case LOG_FILE_RECORD_TYPE_MONTH:
				this.lfrtpFormat = LFRTP_FORMAT_MONTH;
				break;
			case LOG_FILE_RECORD_TYPE_DATE:
				this.lfrtpFormat = LFRTP_FORMAT_DATE;
				break;
			case LOG_FILE_RECORD_TYPE_HOUR:
				this.lfrtpFormat = LFRTP_FORMAT_HOUR;
				break;
			case LOG_FILE_RECORD_TYPE_MINUTE:
				this.lfrtpFormat = LFRTP_FORMAT_MINUTE;
				break;
			case LOG_FILE_RECORD_TYPE_SECOND:
				this.lfrtpFormat = LFRTP_FORMAT_SECOND;
				break;
			default:
				Log.info("Mybatis Sql Handler logFileRecordType is DATE, use type({})",
						LFRTP_FORMAT_DATE);
				this.lfrtpFormat = LFRTP_FORMAT_DATE;
				break;
		}
	}
	
	public void setLogMaxSize(long logMaxSize) {
		if(logMaxSize < 0){
			this.logMaxSize = LOG_MAX_SIZE;
			Log.warn("Mybatis Sql Handler logMaxSize is wrong, use default({}{})",
					LOG_MAX_SIZE, "M");
			return;
		}
		this.logMaxSize = logMaxSize < 0 ? LOG_MAX_SIZE : logMaxSize;
	}
	
	public void setLogFileNameSuffix(String logFileNameSuffix) {
		if(StringUtils.isBlank(logFileNameSuffix)){
			this.logFileNameSuffix = LOG_FILE_NAME_SUFFIX_TXT;
		}
		
		this.logFileNameSuffix = logFileNameSuffix.toLowerCase();
		switch(this.logFileNameSuffix){
			case LOG_FILE_NAME_SUFFIX_XML: 
				this.logFileNameSuffix = LOG_FILE_NAME_SUFFIX_XML;
				break;
			case LOG_FILE_NAME_SUFFIX_HTML: 
				this.logFileNameSuffix = LOG_FILE_NAME_SUFFIX_HTML;
				break;
			default:
				Log.info("Mybatis Sql Handler logFileNameSuffix is TXT, use suffix({})",
						LOG_FILE_NAME_SUFFIX_TXT);
				this.logFileNameSuffix = LOG_FILE_NAME_SUFFIX_TXT;
				break;
		}
	}

	public void setCorePoolSize(int corePoolSize) {
		if(corePoolSize < 0){
			this.corePoolSize = CORE_POOL_SIZE;
			Log.warn("Mybatis Sql Handler corePoolSize is wrong, use default({})",
					CORE_POOL_SIZE);
			return;
		}
		this.corePoolSize = corePoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		if(maximumPoolSize < 0){
			this.maximumPoolSize = MAXIMUM_POOL_SIZE;
			Log.warn("Mybatis Sql Handler maximumPoolSize is wrong, use default({})",
					MAXIMUM_POOL_SIZE);
			return;
		}
		this.maximumPoolSize = maximumPoolSize;
	}
}

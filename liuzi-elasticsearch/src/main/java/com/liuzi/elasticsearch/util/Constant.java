package com.liuzi.elasticsearch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;


/**
 * @description: 参数类
 * @author: zsy
 * @create: 2019-01-22 09:52
 **/
public class Constant {
    /*//非分页，默认的查询条数
    public static int DEFALT_PAGE_SIZE = 200;
    //搜索建议默认条数
    public static int COMPLETION_SUGGESTION_SIZE = 10;
    //高亮字段默认tag
    public static String HIGHLIGHT_TAG = "";
    //创建索引mapping时，是否默认创建keyword
    public static boolean DEFAULT_KEYWORDS = true;
    */
	
	public static double[] DEFAULT_PERCSEGMENT = {50.0, 95.0, 99.0};
    /**
	 * 连接超时时间
	 */
	public static final int CONNECT_TIMEOUT = 1000;
	/**
	 * 通话超时时间
	 */
	public static final int SOCKET_TIMEOUT = 30000;
	/**
	 * 获取连接超时时间
	 */
	public static final int CONNECTION_REQUEST__TIMEOUT = 500;
	/**
	 * 最大连接数
	 */
	public static final int MAX_CONNECTION_NUM = 100;
	/**
	 * 最大路由连接数
	 */
	public static final int MAX_CONNECTION_PER_ROUTE = 100;
	/**
	 * 文档类型
	 */
	public static final String INDEX_TYPE = "_doc";
	/**
	 * 索引间隔符
	 */
	public static final String INDEX_SPACER = "-";
	
    /**
     * 查询缓存时间，30分钟
     */
	public static final long SEARCH_AIVE_TIME = 30;
    /**
     * 默认第一页
     */
	public static final int PAGE_NO = 1;
    /**
     * 每页默认条数
     */
	public static final int PAGE_SIZE = 50;
    /**
     * 高亮类型
     */
	public static final String HIGH_LIGHT_TYPE = "unified";
    /**
     * 索引日期格式
     */
	public static final String INDEX_PATTERN = "yyyyMMdd";
    /**
     * 关键字标志
     */
	public static final String KEYWORD = ".keyword";
    
    private static String _index;
    private static String _type = INDEX_TYPE;
    private static String _spacer = INDEX_SPACER;
    private static long _searchAliveTime = SEARCH_AIVE_TIME;
    
    public static void init(String index, String type, String spacer, long searchAliveTime){
    	_index = index;
    	if(searchAliveTime >= 0){
    		_searchAliveTime = searchAliveTime;
    	}
    	if(!StringUtils.isBlank(type)){
    		_type = type;
    	}
    	if(!StringUtils.isBlank(spacer)){
    		_spacer = spacer;
    	}
    }
    
    public static long _searchAliveTime(){
    	return _searchAliveTime;
    }
    
    /**
	 * 类型
	 */
	public static String _type(){
		return _type;
	}
	
	/**
	 * 索引
	 */
	public static String _index(String table){
		return _index + _spacer + table;
	}
	public static String _index(String table, Object date) throws Exception{
		return _index(table, date, null);
	}
	public static String _index(String table, Object date, String pattern) throws Exception{
		return _index + _spacer + table + _spacer + getDate(date, pattern);
	}
	
	private static String getDate(Object date, String pattern) throws Exception{
		pattern = StringUtils.isBlank(pattern) ? INDEX_PATTERN : pattern;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		
		String dateStr;
		if(org.springframework.util.StringUtils.isEmpty(date)){
			dateStr = sdf.format(new Date());
		}else{
			if(date instanceof String){
				Date d = null;
				try {
					d = sdf.parse(date.toString());
				} catch (ParseException e) {
					throw new Exception("Date pattern is wrong");
				}
				dateStr = sdf.format(d);
			}else if(date instanceof Date){
				dateStr = sdf.format((Date) date);
			}else{
				dateStr = sdf.format(new Date());
			}
		}
		
		return dateStr;
	}
 }

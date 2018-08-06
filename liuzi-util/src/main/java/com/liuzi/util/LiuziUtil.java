package com.liuzi.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class LiuziUtil {
	
	private static Logger logger = LoggerFactory.getLogger(LiuziUtil.class);
	
	private final static String DEFAULT_NUM = "0123456789";
	private final static String DEFAULT_ENG = "abcdefghijklmnopqrstuvwxyz";
	
	public static void tag(){
		tag(null);
	}
	
	public static void tag(String msg){
		StringBuilder sbf = new StringBuilder();
    	sbf.append("\n");
    	sbf.append("            _    _            _   _ _ _\n");
    	sbf.append("       /\\\\ | |  (_)_   _ ___ (_) |  _ _|\n");
    	sbf.append("      ( ( )| |  | | | | |_  /| | | |_ _\n");
    	sbf.append("       \\\\/ | |__| | |_| |/ /_| | |  _  |\n");
    	sbf.append("        '  |____| |\\__|_|_ _ | | | |_| |\n");
    	sbf.append("      ==========| |==============|_ _ _|=\n");
    	sbf.append("       || Liuzi |_| -----> v1.0\n");
    	if(msg != null) sbf.append("\n " + msg + "\n");
		logger.info(sbf.toString());
	}
	
	/**
	 * 随机数（默认0-9）
	 * @param len 长度
	 * @return
	 */
	public static String random(int len){
	    return RandomStringUtils.random(len, DEFAULT_NUM);
	}
	
	/**
	 * 随机数（默认a-z）
	 * @param len 长度
	 * @return
	 */
	public static String randomEng(int len){
	    return RandomStringUtils.random(len, DEFAULT_ENG);
	}
	
	/**
	 * 随机数（默认0-9）
	 * @param str 字符串
	 * @param len 长度
	 * @return
	 */
	public static String random(String str,int len){
		if(StringUtils.isEmpty(str)) str = DEFAULT_NUM;
	    return RandomStringUtils.random(len, str);
	}
	
	/**
	 * 获取地址路径（http://127.0.0.1）
	 * @param request
	 * @return
	 */
	public static String getDomain(HttpServletRequest request){
		return request.getScheme() + "://" + request.getServerName();
	}
	
	/**
	 * 获取地址路径（http://127.0.0.1:8080）
	 * @param request
	 * @return
	 */
	public static String getFullPathUrl(HttpServletRequest request){
		return request.getScheme() + "://" + request.getServerName() + ":" + 
					request.getServerPort(); 
	}
	
	/**
	 * 获取项目地址路径（http://127.0.0.1:8080/pro）
	 * @param request
	 * @return
	 */
	public static String getFullProjectUrl(HttpServletRequest request){
		return request.getScheme() + "://" + request.getServerName() + ":" + 
					request.getServerPort() + "/" + request.getContextPath(); 
	}
	
	public static boolean getResp(Object obj, HttpServletResponse response){
	    return getResp(obj, response, "json");
	}
	
	public static boolean getResp(Object obj, HttpServletResponse response, String type){
	    try {
	    	response.setContentType("text/"+type+";charset=UTF-8");
	    	
			response.getWriter().write(JSONObject.toJSONString(obj).toString());
			response.getWriter().flush();
		} catch (IOException e) {
			logger.error("response fail：" + e.getMessage());
			e.printStackTrace(); 
		}
	    return false;
	}
	
	public static List<Long> ids(String ids){
		List<Long> list = null;
		if(!StringUtils.isEmpty(ids)){
	        String[] idList = ids.split(",");
	        if(idList.length > 0){
	        	list = new ArrayList<Long>();
    	        for(String id : idList){
    	        	list.add(Long.parseLong(id));
    	        }
	        }
	    }
		return list;
	}
	
	public static Long[] ids2(String ids){
		Long[] list = null;
		if(StringUtils.isEmpty(ids)){
	        return null;
	    }
		
        String[] idList = ids.split(",");
        int length = idList.length;
        if(length > 0){
        	list = new Long[idList.length];
	        for(int i = 0; i < length; i ++){
	        	list[i] = Long.parseLong(idList[i]);
	        }
        }
		return list;
	}
	
	/**
	 * 乱序重排
	 * @param list<T>
	 * @return 
	 */
	public static <T> List<T> upset(List<T> list){
        int size = list.size();
        Random random = new Random();
        
        for(int i = 0; i < size; i++) {
            //获取随机位置
            int rp = random.nextInt(size);
            //当前元素与随机元素交换
            T obj = list.get(i);
            list.set(i, list.get(rp));
            list.set(rp, obj);
        }
       return list; 
	}
	
	public static <E> void forEach(Iterable<? extends E> elements, 
			BiConsumer<Integer, ? super E> action) {
        Objects.requireNonNull(elements);
        Objects.requireNonNull(action);

        int index = 0;
        for (E element : elements) {
            action.accept(index++, element);
        }
    }
	
	public static String getWebappPath(HttpServletRequest req){
		return req.getSession().getServletContext().getRealPath("/");
	}
	
	public static final <T> List<T> toList(Object obj, Class<T> c){
		return JSONArray.parseArray(JSONArray.toJSONString(obj), c);
	}
	
	public static final <T> List<T> toList(List<Object> obj, Class<T> c){
		return JSONArray.parseArray(JSONArray.toJSONString(obj), c);
	}
	
	public static Map<String, Object> object2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}

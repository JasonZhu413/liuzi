package com.liuzi.util.common;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Consts;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class LiuziUtil {
	
	public static void tag(String msg, String version){
		StringBuilder sbf = new StringBuilder();
    	sbf.append("\n");
    	sbf.append("            _    _            _   _ _ _\n");
    	sbf.append("       /\\\\ | |  (_)_   _ ___ (_) |  _ _|\n");
    	sbf.append("      ( ( )| |  | | | | |_  /| | | |_ _\n");
    	sbf.append("       \\\\/ | |__| | |_| |/ /_| | |  _  |\n");
    	sbf.append("        '  |____| |\\__|_|_ _ | | | |_| |\n");
    	sbf.append("      ==========| |==============|_ _ _|=\n");
    	sbf.append("       || Liuzi |_| -----> ");
    	sbf.append(version).append("\n\n");
    	sbf.append(msg).append("\n");
		Log.info(sbf.toString());
	}
	
	
	/**
	 * 获取协议
	 * @param request
	 * @return http
	 */
	public static String getProtocol(HttpServletRequest request){
		return request.getScheme();
	}
	
	/**
	 * 获取Host
	 * @param request
	 * @return 127.0.0.1
	 */
	public static String getHost(HttpServletRequest request){
		return request.getServerName();
	}
	
	/**
	 * 获取端口
	 * @param request
	 * @return 80
	 */
	public static int getPort(HttpServletRequest request){
		return request.getServerPort();
	}
	
	/**
	 * 获取地址
	 * @param request
	 * @return http://127.0.0.1:80
	 */
	public static String getAddress(HttpServletRequest request){
		return getProtocol(request) + "://" + getHost(request) + ":" + getPort(request);
	}
	
	/**
	 * 获取项目地址路径
	 * @param request
	 * @return http://127.0.0.1:8080/pro
	 */
	public static String getProjectUrl(HttpServletRequest request){
		return getAddress(request) + "/" + request.getContextPath(); 
	}
	
	public static boolean response(Object obj, HttpServletResponse response){
	    return response(obj, response, ContentType.APPLICATION_JSON);
	}
	
	public static boolean response(Object obj, HttpServletResponse response, ContentType contentType){
	    try {
	    	StringBuilder builder = new StringBuilder();
	    	builder.append(contentType == null ? ContentType.APPLICATION_FORM_URLENCODED : contentType);
	    	builder.append("; charset=");
	    	builder.append(Consts.UTF_8);
	    	response.setContentType(builder.toString());
			response.getWriter().write(JSONObject.toJSONString(obj).toString());
			response.getWriter().flush();
		} catch (IOException e) {
			Log.error(e, "response error");
		}
	    return false;
	}
	
	public static enum ContentType{
		APPLICATION_ATOM_XML("application/atom+xml"),
		APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
		APPLICATION_JSON("application/json"),
	    APPLICATION_OCTET_STREAM("application/octet-stream"),
	    APPLICATION_SVG_XML("application/svg+xml"),
	    APPLICATION_XHTML_XML("application/xhtml+xml"),
	    APPLICATION_XML("application/xml"),
	    MULTIPART_FORM_DATA("multipart/form-data"),
	    TEXT_HTML("text/html"),
	    TEXT_PLAIN("text/plain"),
	    TEXT_XML("text/xml"),
	    WILDCARD("*/*");
		
		private String type;
		private ContentType(String type){
			this.type = type;
		}
	    public String getType() {
			return type;
		}
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
	
	/**
	 * 索引循环 str为 T，index为索引
	 * LiuziUtil.forEach(list, (index, str) -> { });
	 * @param elements
	 * @param action
	 */
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
	
	public static void main(String[] args) {
		System.out.println(ContentType.APPLICATION_JSON.toString());
	}
}

package com.liuzi.util.wechat;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JavaIdentifierTransformer;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.liuzi.util.common.Log;
import com.thoughtworks.xstream.XStream;


/**
 * Xml处理类
 * @author zsy
 */
public class XmlUtil {
	
	/**
	 * Xml转Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMap(HttpServletRequest request){
		Map<String, String> map = null;
		SAXReader reader = new SAXReader();
		try(InputStream ins = request.getInputStream();){
			Document doc = reader.read(ins);
			Element root = doc.getRootElement();
			List<Element> list = root.elements();
			if(list == null || list.isEmpty()){
				return map;
			}
			
			map = new HashMap<>();
			for(Element e : list){
				map.put(e.getName(), e.getText());
			}
		}catch (Exception e) {
			Log.info(e, "[Xml to Map] Request Xml 转换 Map 错误");
		}
		return map;
	}
	
	/**
	 * Map转Bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T mapToBean(Map<String, String> map, Class<T> clazz){
        try {
        	JSONObject json = JSONObject.fromObject(map);
            JsonConfig config = new JsonConfig();
            config.setJavaIdentifierTransformer(new JavaIdentifierTransformer() {
                @Override
                public String transformToJavaIdentifier(String str) {
                    char[] chars = str.toCharArray();
                    chars[0] = Character.toLowerCase(chars[0]);
                    return new String(chars);
                }
            });
            config.setRootClass(clazz);
            return (T) JSONObject.toBean(json, config);
        } catch (Exception e) {
            Log.error(e, "[Map to Bean]转换失败");
        }
        return null;
	}
	
	/**
	 * Bean转Xml
	 */
	public static <T> String beanToXml(T t){
		if(t == null){
			return "";
		}
		
		XStream xstream = new XStream();
		xstream.alias("xml", t.getClass());
		return xstream.toXML(t);
	}
}

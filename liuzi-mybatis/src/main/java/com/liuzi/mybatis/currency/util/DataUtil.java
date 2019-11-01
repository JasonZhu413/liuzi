package com.liuzi.mybatis.currency.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import lombok.Data;

import com.liuzi.mybatis.currency.data.ColumnData;
import com.liuzi.mybatis.currency.data.TableData;
import com.liuzi.util.common.Log;


@Data
public class DataUtil {
	
	private static final char UNDER_LINE = '_';
	
	public static TableData getTable(Class<?> clazz){
		return new TableData(clazz);
	}
	
	public static TableData getTable(Class<?> clazz, String as){
		return new TableData(clazz, as);
	}
	
	public static ColumnData getColumn(Field field){
		return new ColumnData(field);
	}
	
	/**
	 * 获取所有字段
	 * @param clazz
	 * @return
	 */
	public static List<Field> getFields(Class<?> clazz){
		List<Field> fields = new ArrayList<>();
		List<String> fieldNames = new ArrayList<>();
		Class<?> c = clazz;
		while (c != null) {
			Field[] fs = c.getDeclaredFields();
			for(Field f : fs){
				//除静态字段
				if(Modifier.isStatic(f.getModifiers()) ||
						f.isAnnotationPresent(Transient.class) ||
	                    !BeanUtils.isSimpleValueType(f.getType())){
					continue;
				}
				String fName = f.getName();
				if(fieldNames.contains(fName)){
					continue;
				}
				fields.add(f);
				fieldNames.add(fName);
			}
			// 得到父类
			c = c.getSuperclass();
		}
		return fields;
	}
	
	/**
	 * 获取实体值,参数值
	 * @param t 实体
	 * @param name 参数名称
	 * @return
	 */
	public static <T> Object getMethodValue(T t, String name){
		Object result = null;
		PropertyDescriptor pro = null;
		try {
			pro = new PropertyDescriptor(name, t.getClass());
		} catch (IntrospectionException e) {
			Log.error(e, "GET METHOD VALUE ERROR");
		}
		
		if(pro == null){
			return result;
		}
		
		Method method = pro.getReadMethod();
		if(method == null){
			return null;
		}
		
		try {
			result = method.invoke(t);
		} catch (IllegalAccessException e) {
			Log.error(e, "GET METHOD VALUE ERROR");
		} catch (IllegalArgumentException e) {
			Log.error(e, "GET METHOD VALUE ERROR");
		} catch (InvocationTargetException e) {
			Log.error(e, "GET METHOD VALUE ERROR");
		}
		
		return result;
	}
	
	/**
	 * 驼峰转下划线
	 * @param param
	 * @return
	 */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            //下划线
            if(i > 0 && Character.isUpperCase(c)){
            	sb.append(UNDER_LINE);
            }
            //转小写
            sb.append(Character.toLowerCase(c)); 
        }
        return sb.toString();
    }
    
    /**
     * 下划线转驼峰
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        // "_" 后转大写标志,默认字符前面没有"_"
        Boolean flag = false; 
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDER_LINE) {
                flag = true;
                continue;   //标志设置为true,跳过
            } else {
                if (flag == true) {
                    //表示当前字符前面是"_" ,当前字符转大写
                    sb.append(Character.toUpperCase(param.charAt(i)));
                    //重置标识
                    flag = false;  
                } else {
                    sb.append(Character.toLowerCase(param.charAt(i)));
                }
            }
        }
        return sb.toString();
    }

}

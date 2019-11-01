package com.liuzi.util.excel;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;

import com.liuzi.util.common.Log;
import com.liuzi.util.date.DateUtil;

import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Created by zsy
 */
public class ExcelExportUtil {
	
	private static final String TYPE = ".xlsx";
	private static final SimpleDateFormat SDF_UK = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 导出
     * @param list 查询数据List<T(实体类)>
     * @param fileName 文件/sheet名
     * @param response
     */
	public static <T> void exportT(List<T> list, String fileName, HttpServletResponse response){
    	exportT(list, fileName, response);
    }
    
    /**
     * 导出
     * @param list 查询数据List<T(实体类)>
     * @param fileName 文件/sheet名
     * @param map {字段名1=字段1解释,字段2=字段2解释}
     * @param response
     */
	public static <T> void exportT(List<T> list, String fileName, LinkedHashMap<String, String> map, 
    		HttpServletResponse response){
    	if(list == null || list.isEmpty()){
    		Log.info("导出数据为空！");
    		return;
    	}
    	
    	fileName = StringUtils.isEmpty(fileName) ? DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS") : fileName;
    	
    	Workbook wb = getWbByT(list, fileName, map);
    	if(wb == null){
    		Log.warn("验证Workbook为空，返回");
    		return;
    	}
    	try{
    		Log.info("导出数据开始生成......");
    		
    		response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");  
            fileName = new String(fileName.replaceAll(" ", "").getBytes("utf-8"), "iso8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + TYPE);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
            
            Log.info("导出数据完成：{}", fileName);
    	}catch(Exception e){
    		Log.error(e, "导出数据错误：{}", fileName);
    	}
    }

    private static <T> Workbook getWbByT(List<T> list, String sheetName, LinkedHashMap<String, String> map){
    	Log.info("初始化Workbook......");
    	
        try (Workbook wb = new HSSFWorkbook();){
        	
        	Log.info("创建Sheet......");
        	
            Sheet sheet = wb.createSheet(sheetName);
            Row row;
            CellStyle style = wb.createCellStyle();
            //style.setAlignment(CellStyle.ALIGN_CENTER);//居中
            Cell cell;

            Class<?> classType = list.get(0).getClass();
            Log.info("获取数据类型class type: " + classType + "......");
            
            if(map == null || map.isEmpty() || map.size() == 0){
            	Log.info("导出全部字段......");
            	
                Field[] fields = classType.getDeclaredFields();

                for (int i = 0; i < list.size() + 1; i++){
                    row = sheet.createRow(i);
                    if(i == 0){
                        row.createCell(0).setCellValue("No");
                        for (int j = 0; j < fields.length; j++){
                            String fieldName = fields[j].getName();
                            cell = row.createCell(j + 1);
                            cell.setCellStyle(style);
                            cell.setCellValue(fieldName);
                        }
                    }else{
                        row.createCell(0).setCellValue(i);
                        for (int j = 0; j < fields.length; j++){
                            String fieldName = fields[j].getName();
                            Class<?> cla = fields[j].getType();
                            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                            Method getMethod = classType.getMethod(getMethodName, new Class[]{});
                            Object object = getMethod.invoke(list.get(i-1), new Object[]{});
                            if(object != null){
                                if("class java.util.Date".equals(cla.toString())){
                                    row.createCell(j + 1).setCellValue(getToDateUK(object.toString()));
                                }else{
                                    row.createCell(j + 1).setCellValue(object.toString());
                                }
                            }else{
                                row.createCell(j + 1).setCellValue("");
                            }
                        }
                    }
                }
            }else{
            	Log.info("导出自定义字段......");
            	
            	for (int i = 0; i < list.size() + 1; i++){
                    row = sheet.createRow(i);
                    if(i == 0){
                        row.createCell(0).setCellValue("No");
                        int j = 0;
                        Iterator<Entry<String, String>> it = map.entrySet().iterator();
                    	while(it.hasNext()){
                    		Entry<String, String> entry = it.next();
                    		cell = row.createCell(++j);
                            cell.setCellStyle(style);
                            cell.setCellValue(entry.getValue());
                    	}
                    }else{
                        row.createCell(0).setCellValue(i);
                        int j = 0;
                        Iterator<Entry<String, String>> it = map.entrySet().iterator();
                    	while(it.hasNext()){
                    		Entry<String, String> entry = it.next();
                    		String fieldName = entry.getKey();
                            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                            Method getMethod = classType.getMethod(getMethodName, new Class[]{});
                            Class<?> cla = getMethod.getReturnType();
                            Object object = getMethod.invoke(list.get(i - 1), new Object[]{});
                            ++j;
                            if(object!=null){
                                if("class java.util.Date".equals(cla.toString())){
                                    row.createCell(j).setCellValue(getToDateUK(object.toString()));
                                }else{
                                    row.createCell(j).setCellValue(object.toString());
                                }
                            }else{
                                row.createCell(j).setCellValue("");
                            }
                    	}
                    }
                }
            }
            Log.info("Workbook初始化完成，返回Workbook");
            return wb;
        }catch (Exception e){
            Log.error(e, "Workbook初始化错误");
        }
        
        return null;
    }
    
    /**
     * 导出
     * @param list 查询数据List<Map<String, Object>>
     * @param fileName 文件/sheet名
     * @param response
     */
    public static void exportMap(List<Map<String, Object>> list, String fileName, HttpServletResponse response){
    	exportMap(list, fileName, response);
    }
    
    /**
     * 导出
     * @param list 查询数据List<Map<String, Object>>
     * @param fileName 文件/sheet名
     * @param map {字段名1=字段1解释,字段2=字段2解释}
     * @param response
     */
    public static <T> void exportMap(List<Map<String, Object>> list, String fileName, LinkedHashMap<String, String> map, 
    		HttpServletResponse response){
    	if(list == null || list.size() == 0){
    		Log.info("导出数据为空！");
    		return;
    	}
    	
    	if(StringUtils.isEmpty(fileName)){
    		fileName = DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
    	}
    	
    	Workbook wb = getWbByMap(list, fileName, map);
    	if(wb == null){
    		Log.warn("验证Workbook为空，返回");
    		return;
    	}
    	try{
    		Log.info("导出数据开始生成......");
    		
    		response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");  
            fileName = new String(fileName.replaceAll(" ", "").getBytes("utf-8"), "iso8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + TYPE);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
            
            Log.info("导出数据完成......");
    	}catch(Exception e){
    		Log.error(e, "导出数据错误 {}", fileName);
    	}
    }
    
    private static Workbook getWbByMap(List<Map<String, Object>> list, String sheetName, 
    		LinkedHashMap<String, String> fileds){
    	Log.info("初始化Workbook......");
    	
        try (Workbook wb = new HSSFWorkbook();){
        	
        	Log.info("创建Sheet......");
        	
            Sheet sheet = wb.createSheet(sheetName);
            CellStyle style = wb.createCellStyle();
            Row row;
            Cell cell;
            
            if(fileds == null || fileds.isEmpty() || fileds.size() == 0){
            	Log.info("导出全部字段" + list.size() + "......");
            	
            	Map<String,Object> map = list.get(0);
            	row = sheet.createRow(0);
            	row.createCell(0).setCellValue("No");
                int c = 0;
                for (String key : map.keySet()){
                    cell = row.createCell(++c);
                    cell.setCellStyle(style);
                    cell.setCellValue(key);
                }
            	
                for (int i = 1; i < list.size() + 1; i++){
                    map = list.get(i - 1);
                    row = sheet.createRow(i);
                    row.createCell(0).setCellValue(i);
                    c = 0;
                    for (String key : map.keySet()) {
                        cell = row.createCell(++c);
                        cell.setCellStyle(style);
                        String val = StringUtils.isEmpty(map.get(key)) ? "" : map.get(key).toString();
                        cell.setCellValue(val);
                    }
                }
            }else{
            	Log.info("导出自定义字段......");
            	
            	row = sheet.createRow(0);
                row.createCell(0).setCellValue("No");
                int j = 0;
                Iterator<Entry<String, String>> it = fileds.entrySet().iterator();
            	while(it.hasNext()){
            		Entry<String, String> entry = it.next();
            		cell = row.createCell(++j);
            		cell.setCellStyle(style);
                    cell.setCellValue(entry.getValue());
            	}
            	
                Map<String,Object> map;
                for (int i = 1; i < list.size() + 1; i++){
                    map = list.get(i - 1);
                    row = sheet.createRow(i);
                    row.createCell(0).setCellValue(i);
                    j = 0;
                    it = fileds.entrySet().iterator();
                	while(it.hasNext()){
                		Entry<String, String> entry = it.next();
                		Object c = map.get(entry.getKey());
                        String val = StringUtils.isEmpty(c) ? "" : c.toString();
                        row.createCell(++j).setCellValue(val);
                	}
                }
            }
            
            Log.info("初始化Workbook完成......");
            
            return wb;
        }catch (Exception e){
        	Log.error(e, "初始化Workbook错误");
        }
        
        return null;
    }
    
    private static String getToDateUK(String str) throws ParseException{
		return SDF.format(SDF_UK.parse(str));
	}
}

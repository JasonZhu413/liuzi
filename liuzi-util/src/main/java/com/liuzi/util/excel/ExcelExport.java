package com.liuzi.util.excel;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by zhusy
 */
public class ExcelExport {
	
	private static Workbook wb;
	
	private static SimpleDateFormat sdfUk = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 导出
     * @param list 查询数据list
     * @param ex 导出条件ExcelExportModel
     * @param response
     */
    public static <T> void export(List<T> list,ExcelExportModel ex,HttpServletResponse response){
        try{
        	wb = new HSSFWorkbook();
            Class<?> classType = list.size()==0?Map.class:list.get(0).getClass();
            String sheetName = StringUtils.isEmpty(ex.getExName()) ? "export" : ex.getExName();
            Sheet sheet = wb.createSheet(sheetName);
            Row row;
            CellStyle style = wb.createCellStyle();
            //style.setAlignment(CellStyle.ALIGN_CENTER);//居中
            Cell cell;

            if(StringUtils.isEmpty(ex.getExPro())){
                Field[] fields = classType.getDeclaredFields();

                for (int i = 0; i < list.size() + 1; i++){
                    row = sheet.createRow(i);
                    if(i == 0){
                        row.createCell(0).setCellValue("序号");
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
                            Method getMethod = classType.getMethod(getMethodName,new Class[]{});
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
                String[] e = ex.getExPro().split(",");

                for (int i = 0; i < list.size() + 1; i++){
                    row = sheet.createRow(i);
                    if(i == 0){
                        row.createCell(0).setCellValue("序号");
                        for (int j = 0; j < e.length; j++){
                            String fieldTitle = ex.getExTitle().split(",")[j];
                            cell = row.createCell(j + 1);
                            cell.setCellStyle(style);
                            cell.setCellValue(fieldTitle);
                        }
                    }else{
                        row.createCell(0).setCellValue(i);
                        for (int j = 0; j < e.length; j++){
                            String fieldName = e[j];
                            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                            Method getMethod = classType.getMethod(getMethodName,new Class[]{});
                            Class<?> cla = getMethod.getReturnType();
                            Object object = getMethod.invoke(list.get(i - 1), new Object[]{});
                            if(object!=null){
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
            }


            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");  
            String fileName = new String(sheetName.replaceAll(" ", "").getBytes("utf-8"), "iso8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    /**
     * 导出 
     * @param list 查询数据list<map>
     * @param ex 导出条件ExcelExportModel
     * @param response
     */
    public static void exportMap(List<Map<String,Object>> list, ExcelExportModel ex, HttpServletResponse response){
		
        try{
        	wb = new HSSFWorkbook();
        	String sheetName = StringUtils.isEmpty(ex.getExName()) ? "export" : ex.getExName();
            Sheet sheet = wb.createSheet(sheetName);
            CellStyle style = wb.createCellStyle();
            Row row;
            Cell cell;

            if(StringUtils.isEmpty(ex.getExPro())){
                Map<String,Object> map;
                for (int i = 0; i < list.size() + 1; i++){
                    map = list.get(i);
                    row = sheet.createRow(i);
                    if(i == 0){
                        row.createCell(0).setCellValue("序号");
                        Integer c = 0;
                        for (String key : map.keySet()) {
                            cell = row.createCell(c++);
                            cell.setCellStyle(style);
                            cell.setCellValue(key);
                        }
                    }else{
                        row.createCell(0).setCellValue(i);
                        Integer c = 0;
                        for (String key : map.keySet()) {
                            cell = row.createCell(c++);
                            cell.setCellStyle(style);
                            String val = StringUtils.isEmpty(map.get(key)) ? "" : c.toString();
                            cell.setCellValue(val);
                        }
                    }
                }
            }else{
                String[] e = ex.getExPro().split(",");
                Map<String,Object> map;
                for (int i = 0; i < list.size() + 1; i++){
                    row = sheet.createRow(i);
                    if(i == 0){
                        row.createCell(0).setCellValue("序号");
                        for (int j = 0; j < e.length ; j++){
                            String fieldTitle = ex.getExTitle().split(",")[j];
                            cell = row.createCell(j+1);
                            cell.setCellStyle(style);
                            cell.setCellValue(fieldTitle);
                        }
                    }else{
                        map = list.get(i - 1);
                        row.createCell(0).setCellValue(i);
                        for (int j = 0; j < e.length; j++){
                            Object c = map.get(e[j]);
                            String val = StringUtils.isEmpty(c) ? "" : c.toString();
                            row.createCell(j + 1).setCellValue(val);
                        }
                    }
                }
            }

            response.setContentType("application/vnd.ms-excel");
            String fileName = new String(sheetName.replaceAll(" ", "").getBytes("UTF-8"), "ISO8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static String getToDateUK(String str) throws ParseException{
		return sdf.format(sdfUk.parse(str));
	}
}

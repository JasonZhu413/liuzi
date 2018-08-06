package com.liuzi.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import com.liuzi.util.ConfigUtil;
import com.liuzi.util.Result;
import com.liuzi.util.excel.FileUploadSample;

@Slf4j
public class ExcelImport{

    private static Workbook wb = null;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public Result importExcel(HttpServletRequest request, Map<String, Object> map) {
    	
    	String type = request.getParameter("type");
        if(type == null || Integer.parseInt(type) != 4){
            return new Result(0, "上传文件类型错误");
        }
        
    	Result res = FileUploadSample.uploadFile(request);//文件上传
    	if(res.getResult() == 0){
    		return res;
    	}
    	
    	String filePath = "";
    	res = importInfo(filePath, map);
    	
    	return res;
    }
    
    public Result importInfo(String filePath, Map<String, Object> map) {
    	filePath = ConfigUtil.getStringValue("cto.upload.file.path") + filePath;
    	File excelFile = new File(filePath); //创建文件对象
    	Workbook wb = null;
    	try(
    		InputStream is = new FileInputStream(excelFile);
        ){
    		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
            if("XLSX".equals(fileType.toUpperCase())){
            	wb = new XSSFWorkbook(is);
            }else if("XLS".equals(fileType.toUpperCase())){
                wb = new HSSFWorkbook(is);
            }else{
            	return new Result(0, "文件类型错误，导入失败");
            }
            
            List<List<Object>> list = readExcel(wb);
            
            if(excelFile.exists()) {
                log.debug("删除文件 " + filePath + " ...");
                excelFile.delete();
            }
            
            return excelImportDB(list, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(0, e.getMessage());
        }
    }
    
    public Result importExcelList(HttpServletRequest request) {
    	
    	String type = request.getParameter("type");
        if(type == null || Integer.parseInt(type) != 4){
            return new Result(0, "上传文件类型错误");
        }
        
    	Result res = FileUploadSample.uploadFile(request);//文件上传
    	if(res.getResult() == 0){
    		return res;
    	}
    	
    	String filePath = "";
    	filePath = ConfigUtil.getStringValue("cto.upload.file.path") + filePath;
    	File excelFile = new File(filePath); //创建文件对象
    	Workbook wb = null;
    	try(
    		InputStream is = new FileInputStream(excelFile);
        ){
    		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
            if("XLSX".equals(fileType.toUpperCase())){
            	wb = new XSSFWorkbook(is);
            }else if("XLS".equals(fileType.toUpperCase())){
                wb = new HSSFWorkbook(is);
            }else{
            	return new Result(0, "文件类型错误，导入失败");
            }
            
            List<List<Object>> list = readExcel(wb);
            
            if(excelFile.exists()) {
                log.debug("删除文件 " + filePath + " ...");
                excelFile.delete();
            }
            
            return new Result(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(0, e.getMessage());
        }
    }

    /**
     * 读取 excel
     */
	public List<List<Object>> readExcel() throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();
        Sheet sheet = wb.getSheetAt(0);
        Object value = null;
        Row row = null;
        Cell cell = null;
        int counter = 0;
        int cellNum = 0;
        for (int i = sheet.getFirstRowNum(),e = sheet.getPhysicalNumberOfRows(); counter < e; i++) {
            row = sheet.getRow(i);
            int cellNo = 0;
            if (row != null){
                cellNo = row.getPhysicalNumberOfCells();
            }
            if (row == null) {
                continue;
            } else if(cellNo <= 1){
                counter++;
                continue;
            }else {
                counter++;
            }
            if(i == 0){
            	cellNum = row.getLastCellNum();
        	}
            List<Object> linked = new LinkedList<Object>();
            for (int j = 0; j < cellNum; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    linked.add(null);
                    continue;
                }
                
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        /*if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            value = df.format(cell.getNumericCellValue());
                        } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                            value = nf.format(cell.getNumericCellValue());
                        } else {
                            value = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                        }*/
                        if(DateUtil.isCellDateFormatted(cell)){
        	        		value = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
        	        	}else{
        	        		double val=cell.getNumericCellValue();
        	        		long intVal=(long) val;
        	        		value=val-intVal==0?String.valueOf(intVal):String.valueOf(val);
        	        	}
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        value = "";
                        break;
                    default:
                        value = cell.toString();
                }
                linked.add(value);
            }
            list.add(linked);
        }
        if(list == null || list.size()<=0){
            return null;
        }
        return list;
    }
	
    /**
     * 读取 excel
     */
	public List<List<Object>> readExcel(Workbook wb) throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();
        Sheet sheet = wb.getSheetAt(0);
        Object value = null;
        Row row = null;
        Cell cell = null;
        int counter = 0;
        int cellNum = 0;
        for (int i = sheet.getFirstRowNum(),e = sheet.getPhysicalNumberOfRows(); counter < e; i++) {
            row = sheet.getRow(i);
            int cellNo = 0;
            if (row != null){
                cellNo = row.getPhysicalNumberOfCells();
            }
            if (row == null) {
                continue;
            } else if(cellNo <= 1){
                counter++;
                continue;
            }else {
                counter++;
            }
            if(i == 0){
            	cellNum = row.getLastCellNum();
        	}
            
            List<Object> linked = new LinkedList<Object>();
            for (int j = 0; j < cellNum; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    linked.add(null);
                    continue;
                }
                
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        /*if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            value = df.format(cell.getNumericCellValue());
                        } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                            value = nf.format(cell.getNumericCellValue());
                        } else {
                            value = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                        }*/
                    	if(DateUtil.isCellDateFormatted(cell)){
        	        		value = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
        	        	}else{
        	        		double val=cell.getNumericCellValue();
        	        		long intVal=(long) val;
        	        		value=val-intVal==0?String.valueOf(intVal):String.valueOf(val);
        	        	}
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        value = "";
                        break;
                    default:
                        value = cell.toString();
                }
                if (j==0&&StringUtils.isEmpty(value==null?null:value.toString())) {
                    break;
                }
                linked.add(value);
            }
            if(linked.size()==0) break;
            list.add(linked);
        }
        if(list == null || list.size()<=0){
            return null;
        }
        return list;
    }
	
	public <T> Result excelImportDB(List<List<Object>> list, Map<String, Object> map){
    	int count = 0;
        int size = list.size();
        List<T> listT = new ArrayList<T>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        StringBuffer sbf = new StringBuffer();
        Map<String, Object> returnMap;
        
        int reCod = 1;
        try {
        	
			for (int i = 1; i < size; i += 0) {
				returnMap = new HashMap<String, Object>();
                count++;
                //读取到空结束
                if(list.get(i) == null || list.get(i).size() == 0){
                    break;
                }

                Result res = initmodel(list.get(i), map);
                if(res.getResult() == 0){
                	returnMap.put("count", count);
                	returnMap.put("reason", res.getMsg());
                	//res.setMsg("第" + count + "条失败，失败原因：" + res.getMsg());
                	//return res;
                	reCod = 0;
                	sbf.append("第" + count + "条失败，失败原因：" + res.getMsg() + ";\n");
                	resultList.add(returnMap);
                }else{
                	listT.add((T)res.getModel());
                }
                
                list.remove(0);
                size--;
            }
			
			if(reCod == 0){
				Result r = new Result();
				r.setResult(0);
				r.setMsg(sbf.toString());
				r.setModel(resultList);
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("导入失败，系统错误" + e.getMessage());
			return new Result(0, "导入失败，系统错误");
		} 

        return new Result(listT);
    }
	
	public Result initmodel(List<Object> list, Map<String, Object> map){
        return new Result();
    }
}

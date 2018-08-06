package com.liuzi.util.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static List<List<String>> readExcel(String filePath){
		
		Workbook wb=null;
		InputStream is=null;
		List<List<String>> list=new ArrayList<List<String>>();
		try{
			is=new FileInputStream(filePath);
			String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
	        if("XLSX".equals(fileType.toUpperCase())){
	        	wb = new XSSFWorkbook(is);
	        }else if("XLS".equals(fileType.toUpperCase())){
	            wb = new HSSFWorkbook(is);
	        }else{
	        	//return new Result(0, "文件类型错误，导入失败");
	        }
	        
	        Sheet sheet=wb.getSheetAt(0);
	        int start=sheet.getFirstRowNum();
	        int last=sheet.getLastRowNum();

	        int rowStart=0;
	        int rowEnd=0;
	        for(int i=start;i<last;i++){
	        	Row row=sheet.getRow(i);
	        	if(i==start){
	        		rowStart=row.getFirstCellNum();
	        		rowEnd=row.getLastCellNum();
	        		continue;
	        	}
	        	List<String> rowList=new ArrayList<String>();
	        	int count=0;
	        	for(int j=rowStart;j<rowEnd;j++){
	        		Cell cell=row.getCell(j);
	        		String val=getStringVal(cell);
	        		if(val!=null&&!"".equals(val)) count++;
	        		rowList.add(val);
	        	}
	        	if(count==0) continue;
	        	list.add(rowList);
	        }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(is!=null){
					is.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static List<Map<String,Object>> readExcelMap(String filePath,ExcelExportModel model){
		Workbook wb = null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try(
			InputStream is = new FileInputStream(filePath);
			){
			
			String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
	        if("XLSX".equals(fileType.toUpperCase())){
	        	wb = new XSSFWorkbook(is);
	        }else if("XLS".equals(fileType.toUpperCase())){
	            wb = new HSSFWorkbook(is);
	        }else{
	        	//return new Result(0, "文件类型错误，导入失败");
	        }
	        
	        Sheet sheet = wb.getSheetAt(0);
	        int last = sheet.getLastRowNum();
	        int start = sheet.getFirstRowNum();
	        int rowStart = 0;
			int rowEnd = 0;
	        String[] key = model.getExPro().split(",");
	        for(int i = start; i<last; i++){
	        	Row row = sheet.getRow(i);
	        	if(i == start){
	        		rowStart = row.getFirstCellNum();
	        		rowEnd = row.getLastCellNum();
	        		continue;
	        	}
	        	int count = 0;
	        	Map<String,Object> rowMap = new HashMap<String,Object>();
	        	for(int j = 0; j < key.length; j++){
	        		Cell cell = row.getCell(j);
	        		String val = getStringVal(cell);
	        		if(val != null && !"".equals(val)) count++;
	        		rowMap.put(key[j - rowStart], val);
	        	}
	        	if(count == 0) continue;
	        	list.add(rowMap);
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("deprecation")
	public static String getStringVal(Cell cell){
		String value="";
		if(cell==null) return "";
		switch (cell.getCellType()) {
	        
	        case Cell.CELL_TYPE_NUMERIC:
	        	if(DateUtil.isCellDateFormatted(cell)){
	        		value = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
	        	}else{
	        		double val=cell.getNumericCellValue();
	        		long intVal=(long) val;
	        		value=val-intVal==0?String.valueOf(intVal):String.valueOf(val);
	        	}
	            break;
	        case Cell.CELL_TYPE_FORMULA:
	        	/*try{
	        		value=String.valueOf(cell.getNumericCellValue());
	        	}catch(IllegalStateException e){
	        		value=String.valueOf(cell.getRichStringCellValue());
	        	}*/
	        	switch (cell.getCachedFormulaResultType()) {
					case Cell.CELL_TYPE_NUMERIC:
						value=String.valueOf(cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
					case Cell.CELL_TYPE_BOOLEAN:
						value = String.valueOf(cell.getBooleanCellValue());
					case Cell.CELL_TYPE_ERROR:
						value ="";
				default:
					break;
				}
	        case Cell.CELL_TYPE_STRING:
	            value = cell.getStringCellValue();
	            break;
	        case Cell.CELL_TYPE_BOOLEAN:
	            value = String.valueOf(cell.getBooleanCellValue());
	            break;
	        case Cell.CELL_TYPE_BLANK:
	            value = "";
	            break;
	        case Cell.CELL_TYPE_ERROR:
	        	value = "";
	        	break;
	        default:
	            value = cell.toString().trim();
	    }
		return value;
	}
	
	@SuppressWarnings("deprecation")
	public static Object getObjectVal(Cell cell){
		Object value="";
		switch (cell.getCellType()) {
	        
	        case Cell.CELL_TYPE_NUMERIC:
	        	if(DateUtil.isCellDateFormatted(cell)){
	        		value = DateUtil.getJavaDate(cell.getNumericCellValue());
	        	}else{
	        		double val=cell.getNumericCellValue();
	        		int intVal=(int) val;
	        		value=val-intVal==0?intVal:val;
	        	}
	            break;
	        case Cell.CELL_TYPE_FORMULA:
	        	switch (cell.getCachedFormulaResultType()) {
					case Cell.CELL_TYPE_NUMERIC:
						value=cell.getNumericCellValue();
						break;
					case Cell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
					case Cell.CELL_TYPE_BOOLEAN:
						value = cell.getBooleanCellValue();
					case Cell.CELL_TYPE_ERROR:
						value ="";
				default:
					break;
				}
	        case Cell.CELL_TYPE_STRING:
	            value = cell.getStringCellValue();
	            break;
	        case Cell.CELL_TYPE_BOOLEAN:
	            value = cell.getBooleanCellValue();
	            break;
	        case Cell.CELL_TYPE_BLANK:
	            value = "";
	            break;
	        case Cell.CELL_TYPE_ERROR:
	        	value = "";
	        	break;
	        default:
	            value = cell.toString().trim();
	    }
		return value;
	}
}
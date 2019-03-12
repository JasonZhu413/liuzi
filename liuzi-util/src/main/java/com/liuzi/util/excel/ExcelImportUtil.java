package com.liuzi.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

import com.liuzi.util.common.Result;
import com.liuzi.util.upoload.FileUpload;

@Slf4j
public class ExcelImportUtil{

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 上传且导入(无第一行)
     * @param request
     * @param path 文件上传目录
     * @param excelCallBack 回调函数
     * @return
     */
    static Result uploadAndImport(HttpServletRequest request, String path, 
    		ExcelImportCallBack excelCallBack) {
    	return uploadAndImport(request, path, false, excelCallBack);
    }
    
    /**
     * 上传且导入
     * @param request
     * @param path 文件上传目录
     * @param type 是否需要excel第一行(默认false不需要)
     * @param excelCallBack 回调函数
     * @return
     */
    static Result uploadAndImport(HttpServletRequest request, String path, 
    		boolean type, ExcelImportCallBack excelCallBack) {
    	Result res = FileUpload.start(request, path);//文件上传
    	if(res.getResult() == -1){
    		return res;
    	}
    	
    	return importExcel(path, type, excelCallBack);
    }
    
    /**
     * 导入excel(无第一行)
     * @param path 文件地址
     * @param excelCallBack 回调函数
     * @return
     */
    static Result importExcel(String path, ExcelImportCallBack excelCallBack){
    	return importExcel(path, false, excelCallBack);
    }
    
    static Result importExcel(String path, boolean type, ExcelImportCallBack excelCallBack) {
    	File file = new File(path); //创建文件对象
    	Workbook wb = null;
    	
    	try(InputStream is = new FileInputStream(file);){
    		String fileName = file.getName();
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
			
            if("XLSX".equals(fileType)){
            	wb = new XSSFWorkbook(is);
            }else if("XLS".equals(fileType)){
                wb = new HSSFWorkbook(is);
            }else{
            	return Result.ERROR.error("文件类型错误，导入失败");
            }
            
            List<List<Object>> list = readExcel(wb);
            
            if(file.exists()) {
                log.debug("删除文件 " + path + " ...");
                file.delete();
            }
            
            return getData(list, type, excelCallBack);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR.error("文件导入错误" + e.getMessage());
        }
    }
    
    /**
     * 读取 excel
     */
	@SuppressWarnings("deprecation")
	static List<List<Object>> readExcel(Workbook wb) throws IOException {
        List<List<Object>> list = new LinkedList<>();
        Sheet sheet = wb.getSheetAt(0);
        Object value = null;
        Row row = null;
        Cell cell = null;
        int counter = 0;
        int cellNum = 0;
        for (int i = sheet.getFirstRowNum(), e = sheet.getPhysicalNumberOfRows(); counter < e; i++) {
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
            } else {
                counter++;
            }
            if(i == 0){
            	cellNum = row.getLastCellNum();
        	}
            
            List<Object> linked = new LinkedList<>();
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
        	        		double val = cell.getNumericCellValue();
        	        		long intVal = (long) val;
        	        		value = val - intVal == 0 ? String.valueOf(intVal) : String.valueOf(val);
        	        	}
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue() + "";
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        value = "";
                        break;
                    default:
                        value = cell.toString();
                }
                if (j == 0 && StringUtils.isEmpty(value == null ? null : value.toString())) {
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
	
	private static Result getData(List<List<Object>> list, boolean type, 
			ExcelImportCallBack excelCallBack){
    	int count = 0;
        int size = list.size();
        List<Object> listT = new ArrayList<>();
        StringBuffer sbf = new StringBuffer();
        int reCod = 1;
        try {
        	int typ = type ? 0 : 1;
			for (int i = typ; i < size; i += 0) {
                count++;
                //读取到空结束
                if(list.get(i) == null || list.get(i).size() == 0){
                    break;
                }

                Result res = Result.SUCCESS;
                if(excelCallBack != null){
                	res = excelCallBack.handler(list.get(i));
                }
                if(res.getResult() == -1){
                	reCod = 0;
                	sbf.append("第" + count + "条失败, 失败原因: " + res.getErrorMsg() + ", 内容: " + 
                			list.get(i) + "\n");
                	if("-1".equals(res.getErrorCode())){
                		break;
                	}
                }else{
                	listT.add(res.getData());
                }
                
                list.remove(0);
                size--;
            }
			
			if(reCod == 0){
				log.warn(sbf.toString());
				return Result.ERROR.data(sbf.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("导入失败，系统错误" + e.getMessage());
			return Result.ERROR.error("导入失败，系统错误");
		} 

        return Result.SUCCESS.data(listT);
    }
}

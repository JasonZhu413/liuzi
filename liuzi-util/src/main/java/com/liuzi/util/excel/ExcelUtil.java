package com.liuzi.util.excel;


import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.liuzi.util.common.Result;


public class ExcelUtil {
	
	/**
	 * 导入(无excel第一行数据)
	 * @param path
	 * @param excelImportCallBack 回调函数需要复写handler
	 * exp: Result result = ExcelImportUtil.importExcel(path, new ExcelImportCallBack() {
			@Override
			public Result handler(List<Object> list) throws Exception {
				Long id = this.getLong(list.get(0));
				if(id == null){
					return Result.error("id不能为空");
				}
				
				String name = this.getString(list.get(1));
				
				Test test = new Test();
				test.setId(id);
				test.setName(name);
				return Result.success(test);
			}
		});
	 * @return
	 */
    public static Result importExcel(String path, ExcelImportCallBack excelImportCallBack){
    	return ExcelImportUtil.importExcel(path, excelImportCallBack);
    }
    
    /**
	 * 导入
	 * @param path
	 * boolean type 是否需要excel第一行(true需要, false不需要)
	 * @param excelImportCallBack 回调函数需要复写handler
	 * exp: Result result = ExcelImportUtil.importExcel(path, new ExcelImportCallBack() {
			@Override
			public Result handler(List<Object> list) throws Exception {
				Long id = this.getLong(list.get(0));
				if(id == null){
					return Result.error("id不能为空");
				}
				
				String name = this.getString(list.get(1));
				
				Test test = new Test();
				test.setId(id);
				test.setName(name);
				return Result.success(test);
			}
		});
	 * @return
	 */
    public static Result importExcel(String path, boolean type, ExcelImportCallBack excelImportCallBack){
    	return ExcelImportUtil.importExcel(path, type, excelImportCallBack);
    }
    
    /**
     * 上传并导入(无excel第一行数据)
     * @param request
     * @param path 文件上传目录
     * @param excelImportCallBack 回调函数需要复写handler
	 * exp: Result result = ExcelImportUtil.importExcel(path, new ExcelImportCallBack() {
			@Override
			public Result handler(List<Object> list) throws Exception {
				Long id = this.getLong(list.get(0));
				if(id == null){
					return Result.error("id不能为空");
				}
				
				String name = this.getString(list.get(1));
				
				Test test = new Test();
				test.setId(id);
				test.setName(name);
				return Result.success(test);
			}
		});
     * @return
     */
    public static Result uploadAndImport(HttpServletRequest request, String path, 
    		ExcelImportCallBack excelImportCallBack) {
    	return uploadAndImport(request, path, false, excelImportCallBack);
    }
    
    /**
     * 上传且导入
     * @param request
     * @param path 文件上传目录
     * @param type 是否需要excel第一行(true需要, false不需要)
     * @param excelImportCallBack 回调函数需要复写handler
	 * exp: Result result = ExcelImportUtil.importExcel(path, new ExcelImportCallBack() {
			@Override
			public Result handler(List<Object> list) throws Exception {
				Long id = this.getLong(list.get(0));
				if(id == null){
					return Result.error("id不能为空");
				}
				
				String name = this.getString(list.get(1));
				
				Test test = new Test();
				test.setId(id);
				test.setName(name);
				return Result.success(test);
			}
		});
     * @return
     */
    public static Result uploadAndImport(HttpServletRequest request, String path, 
    		boolean type, ExcelImportCallBack excelImportCallBack) {
    	return ExcelImportUtil.uploadAndImport(request, path, type, excelImportCallBack);
    }
    
    
    /**
     * 导出（全部字段）
     * @param list 查询数据List<T(实体类)>
     * @param fileName 文件/sheet名
     * @param response
     */
    public static <T> void exportT(List<T> list, String fileName, HttpServletResponse response){
    	exportT(list, fileName, response);
    }
    
    /**
     * 导出(map为空：导出全部字段)
     * @param list 查询数据List<T(实体类)>
     * @param fileName 文件/sheet名
     * @param map {字段名1=字段1解释,字段2=字段2解释}
     * @param response
     */
    public static <T> void exportT(List<T> list, String fileName, LinkedHashMap<String, String> map, 
    		HttpServletResponse response){
    	ExcelExportUtil.exportT(list, fileName, map, response);
    }
    
    /**
     * 获取Workbook(map为空：导出全部字段)
     * @param list 查询数据List<T(实体类)>
     * @param sheetName 文件/sheet名
     * @param map {字段名1=字段1解释,字段2=字段2解释}
     * @return
     */
    public static <T> Workbook getWbByT(List<T> list, String sheetName, 
    		LinkedHashMap<String, String> map){
    	return ExcelExportUtil.getWbByT(list, sheetName, map);
    }
    
    /**
     * 导出
     * @param list 查询数据List<Map<String, Object>>
     * @param fileName 文件/sheet名
     * @param response
     */
    public static <T> void exportMap(List<Map<String, Object>> list, String fileName, 
    		HttpServletResponse response){
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
    	ExcelExportUtil.exportMap(list, fileName, map, response);
    }
    
    /**
     * 获取Workbook(map为空：导出全部字段)
     * @param list 查询数据List<Map<String, Object>>
     * @param sheetName 文件/sheet名
     * @param map {字段名1=字段1解释,字段2=字段2解释}
     * @return
     */
    public static <T> Workbook getWbByMap(List<Map<String, Object>> list, String sheetName, 
    		LinkedHashMap<String, String> map){
    	return ExcelExportUtil.getWbByMap(list, sheetName, map);
    }
    
    public static void main(String[] args) {
    	testImport();//导入
    	testExport();//导出
	}
    
    private static void testImport(){
    	String path = "D:\\File\\test\\接口文档.xlsx";
		
		Result result = ExcelUtil.importExcel(path, new ExcelImportCallBack() {
			@Override
			public Result handler(List<Object> list) throws Exception {
				Long id = this.getLong(list.get(0));
				if(id == 1){
					return Result.error("id不能等于1");
				}
				
				String name = this.getString(list.get(1));
				
				ExcelTestEntity test = new ExcelTestEntity();
				test.setId(id);
				test.setName(name);
				return Result.success(test);
			}
		});
		if(result.getResult() == 0){
			@SuppressWarnings("unchecked")
			List<ExcelTestEntity> list = (List<ExcelTestEntity>) result.getData();
			System.out.println(list);
		}
    }
    
    private static void testExport(){
    	/*List<ExcelTestEntity> list = new ArrayList<>();
    	ExcelTestEntity test = new ExcelTestEntity();
    	test.setId(11L);
    	test.setName("test1");
    	list.add(test);
    	test = new ExcelTestEntity();
    	test.setId(21L);
    	test.setName("test2");
    	list.add(test);*/
    	
    	List<Map<String, Object>> list = new ArrayList<>();
    	Map<String, Object> test = new HashMap<>();
    	test.put("id", 1L);
    	test.put("name", "test1");
    	test.put("phone", null);
    	list.add(test);
    	test = new HashMap<>();
    	test.put("id", 2L);
    	test.put("name", "test2");
    	test.put("phone", null);
    	list.add(test);
    	
    	LinkedHashMap<String, String> map = new LinkedHashMap<>();
    	map.put("id", "ID");
    	map.put("name", "名称");
    	
    	String fileName = "testExport";
    	
    	try (FileOutputStream ouputStream = new FileOutputStream("D:\\File\\test\\testExport1.xlsx");){
    		
    		Workbook wb = getWbByMap(list, fileName, map);
    		
            fileName = new String(fileName.replaceAll(" ", "").getBytes("UTF-8"), "ISO8859-1");
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
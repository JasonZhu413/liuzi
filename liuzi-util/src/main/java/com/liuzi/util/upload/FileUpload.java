package com.liuzi.util.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.liuzi.util.common.Log;
import com.liuzi.util.common.RandomUtil;
import com.liuzi.util.common.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * 文件上传
 * @author zsy
 */
public class FileUpload {
	
	private static final String[] PICTURE = {"", "PNG", "JPEG", "JPG", "GIF", "BMP"};
	private static final String[] VIDEO = {};
	
	private static final String sizeKey = "file_upload_size";
	private static final String progressKey = "file_upload_progress";
	
	private static NumberFormat numberFormat;
	
	/**
     * 文件上传
     * @param request
     * @param path 上传目录
     * @return
     */
    public static Result start(HttpServletRequest request, String path){
    	if(request == null || StringUtils.isEmpty(path)){
    		return Result.error("参数错误, request/path is null");
    	}
    	
    	MultipartHttpServletRequest multi = (MultipartHttpServletRequest) request;
		
    	MultipartFile file = multi.getFile(multi.getFileNames().next());
		if(file == null || file.isEmpty()){
        	return Result.error("文件不存在");
        }
		
		Map<String, Object> map = new HashMap<>();
		
		//原文件名
        String oldName = file.getOriginalFilename();
        //文件后缀
        String suffix = oldName.substring(oldName.lastIndexOf(".") + 1);
        //新文件名
        String newName = System.currentTimeMillis() + "&" + RandomUtil.all(8) + "." + suffix;
        //文件位置
        String filePath = path + newName;
        //文件大小K
        float size = getFloat((float) file.getSize() / 1024);
        
        map.put("oldName", oldName);
        map.put("suffix", suffix);
        map.put("newName", newName);
        map.put("temp", path);//目录
        map.put("path", filePath);
        map.put("size", size);
        
        File fileSave = new File(path);
        fileSave.setWritable(true, false);
        if (!fileSave.exists()){
            Log.info("文件夹不存在{}，创建文件夹...", path);
            fileSave.mkdirs();
        }
        
        //session保存文件大小
        //HttpSession session = request.getSession();
        //session.setAttribute(sizeKey + "_" + newName, size);
        
        try(InputStream in = file.getInputStream();
        	OutputStream out = new FileOutputStream(filePath);){
        	
            byte[] buffer = new byte[1024];//缓存
            int len = 0;
            float progress = 0;
            
            while ((len = in.read(buffer)) > 0) {
            	out.write(buffer, 0, len);
            	//session保存文件进度
            	//progress += len;
            	//session.setAttribute(progressKey + "_" + newName, getFloat(progress / 1024));
            }
            
            //压缩
            /*String comFileName = ImgCompress.compress(newName, path, suffix,100, 0);
            
            File cfile = new File(path + comFileName);
            size = getFloat((float) cfile.length() / 1024);
            
            map.put("newName", comFileName);
            map.put("path", path + comFileName);
            map.put("size", size);*/
        }catch (Exception e) {
            Log.error(e, "上传文件出错");
            return Result.error("文件上传错误");
        } finally{
        	//session.setAttribute(sizeKey + "_" + newName, null);
            //session.setAttribute(progressKey + "_" + newName, null);
        }
        
        return Result.success();
    }
    
    /**
     * 获取文件上传进度
     * @param request
     * @param fileName 文件名
     * @return
     */
    public static Result schedule(HttpServletRequest request, String newName){
    	if(request == null || StringUtils.isEmpty(newName)){
    		return Result.error("参数错误, newName/request is null");
    	}
    	
    	HttpSession session = request.getSession();
    	Object size = session.getAttribute(sizeKey + "_" + newName);
    	if(StringUtils.isEmpty(size)){
    		return Result.success();
    	}
    	
    	Object progress = session.getAttribute(progressKey + "_" + newName);
    	
    	float schedule = getFloat((float) progress / (float) size * 100);
    	
    	Map<String, Object> map = new HashMap<>();
    	map.put("size", size);
    	map.put("progress", progress);
    	map.put("schedule", schedule + "%");
        return Result.success(map);
    }
    
    private static float getFloat(float num){
    	if(num == 0){
    		return 0;
    	}
    	
    	if(numberFormat == null){
    		// 设置精确到小数点后2位
    		numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
    	}
    	return Float.parseFloat(numberFormat.format(num));
    }
    
    public static void main(String[] args) {
    	File file = new File("D:/File/test/CGWeb_20190221_1.log");
    	String path = "D:/File/test/CGWeb_20190221_2.log";
    	try(InputStream in = new FileInputStream(file);
        	OutputStream out = new FileOutputStream(path)){
    		
    		float size = getFloat((float) file.length() / 1024);
            
            byte[] buffer = new byte[1024];//缓存
            int len = 0;
            float progress = 0;
            while ((len = in.read(buffer)) > 0) {
            	out.write(buffer, 0, len);
            	
            	progress += len;
            	
            	float pg = getFloat(progress / 1024);
            	float schedule = getFloat(pg / size * 100);
            	System.out.println("大小：" + pg + "K/" + size + "K, 进度：" + schedule + "%");
            }
        }catch (Exception e) {
        	Log.error(e, "上传文件出错");
        }
	}
}


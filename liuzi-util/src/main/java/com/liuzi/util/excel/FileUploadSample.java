package com.liuzi.util.excel;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;





import com.liuzi.util.ConfigUtil;
import com.liuzi.util.Result;

import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.util.*;

import lombok.extern.slf4j.Slf4j;


/**
 * Created by zhusy
 */
@Slf4j
public class FileUploadSample {

	public static Result uploadFile(HttpServletRequest req){
		return uploadFile(req, "");
	}
    /**
     * 文件上传
     * @param req
     * @return
     */
    public static Result uploadFile(HttpServletRequest req, String url){
        MultipartHttpServletRequest multi = (MultipartHttpServletRequest) req;
        MultipartFile file = multi.getFile("file");
        
        if(file.isEmpty()){
        	return new Result(0, "上传错误");
        }

        String fileName = file.getOriginalFilename();

        if(StringUtils.isEmpty(fileName) || fileName.split("\\.").length < 2){
            return new Result(0, "文件错误");
        }
        Long maxSize = ConfigUtil.getLongValue(("cto.upload.file.maxSize"));
        if(file.getSize() > (maxSize * 1024 * 1024)){
            return new Result(0, "文件大小超出限制, 最大为" + maxSize + "M");
        }

    	//String ff = fileName.substring(0, fileName.lastIndexOf("."));//文件名
    	String fn = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀

        String ft = fn.toUpperCase();
        
        Integer type = Integer.parseInt(req.getParameter("type"));
        if(!checkType(type, ft)){
        	return new Result(0, "文件格式错误");
        }

		String newFileName = com.liuzi.util.StringUtil.randomString(6);//新文件名
		newFileName += "_" + UUID.randomUUID().toString() + "." + fn;

		url = (StringUtils.isEmpty(url) ? "" : url + "/");
		
        String uploadPath = ConfigUtil.getStringValue("cto.upload.file.path") + url;//文件上传位置
        
        //文件位置
        String filePath = uploadPath + newFileName;
        
        File fileSave = new File(uploadPath);
        fileSave.setWritable(true, false);
        if (!fileSave.exists()){
            log.info("文件夹不存在，创建文件夹...");
            fileSave.mkdirs();
        }
        
        try(
        		InputStream in = file.getInputStream();
                OutputStream out = new FileOutputStream(filePath);//输出
        	){
        	
            byte[] buffer = new byte[1024];//缓存
            int len = 0;

            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            
            //压缩
            //String comFileName = ImgCompress.compress(newFileName, 100, 0);
            //fileInfo.setUrl(comFileName);//文件地址
        }catch (Exception e) {
            log.error("上传文件出错：" + e.getMessage());
            return new Result(0, "上传文件出错");
        }

        return new Result();
    }
    
    private static boolean checkType(Integer type, String suffix){
    	switch (type) {
			case 1:
				return "PNG".equals(suffix)|| "JPEG".equals(suffix) || "JPG".equals(suffix) || "GIF".equals(suffix) || "BMP".equals(suffix);
			case 2:
				return "MP4".equals(suffix);
			case 3:
				return "MP3".equals(suffix);
			default:
				return true;
		}
    }
    
    
    

}


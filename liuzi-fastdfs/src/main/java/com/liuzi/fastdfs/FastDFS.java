package com.liuzi.fastdfs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.liuzi.fastdfs.FastDFSConfig;

public class FastDFS extends FastDFSConfig{
	
	public FastDFS(String confFile) {
		super(confFile);
	}

	private static Logger logger = LoggerFactory.getLogger(FastDFS.class);
	
	static{
		if (storageClient == null){
		    synchronized(FastDFS.class) {
		      if (storageClient == null)
		    	  init();
		    }
		}
	}
	
	/**
	 * 上传
	 * @param request
	 * @return Map
	 * 		   oldName 原文件名
	 * 		   newName 生成文件名
	 * 		   group 组名
	 * 		   path 地址
	 * 		   url 详细地址(group + path)
	 * 		   size 大小(M)
	 * 		   ext 扩展名
	 */
	public static Map<String, Object> upload(HttpServletRequest request){
		return uploadBatch(request).get(0);
	}
	
	/**
	 * 批量上传
	 * @param request
	 * @return List<Map>
	 * 		   oldName 原文件名
	 * 		   newName 生成文件名
	 * 		   group 组名
	 * 		   path 地址
	 * 		   url 详细地址(group + path)
	 * 		   size 大小(M)
	 * 		   ext 扩展名
	 */
	private static List<Map<String, Object>> uploadBatch(HttpServletRequest request){
		if(!fdfs_is_init()) return null;
		
		logger.info("FastDFS上传文件开始...");
	    MultipartHttpServletRequest multi = (MultipartHttpServletRequest) request;
        
	    List<MultipartFile> list = new ArrayList<MultipartFile>();
	    
        Map<String, MultipartFile> map = multi.getFileMap();
        
        for (Map.Entry<String, MultipartFile> entry : map.entrySet()) {  
        	list.add(entry.getValue());
        } 
        
        logger.info("上传文件数：" + list.size());
        
        List<Map<String, Object>> returnList = new ArrayList<>();
        Map<String, Object> returnMap; 
        int count = 0;
        for(MultipartFile mf : list){
        	count ++;
        	
        	String fileName = mf.getOriginalFilename();
            long fileSize = mf.getSize();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            
            logger.info("文件" + count + "：开始上传...\n文件名：" + fileName + "\n文件大小：" + fileSize + "b");
            
            int maxSize = ClientGlobal.g_file_upload_max_size;
            if(fileSize > (maxSize * 1024 * 1024)){
            	logger.warn("上传文件大小超过" + maxSize + "M，上传文件取消，上传结束...");
            	return null;
            }
            
            String[] uploadResults = upload_object(mf, fileName, fileSize, ext);
            String group = uploadResults[0];//组
            String path = uploadResults[1];//地址
            
            returnMap = new HashMap<String, Object>();
            returnMap.put("oldName", fileName);
            returnMap.put("newName", path.substring(path.lastIndexOf("\\") + 1));
            returnMap.put("group", group);
            returnMap.put("path", path);
            returnMap.put("url", group + "/" + path);
            returnMap.put("size", fileSize);
            returnMap.put("ext", ext);
            returnList.add(returnMap);
            
            logger.info("文件" + count + "：上传成功...\ngroup：" + group + "\npath：" + path + "");
        }
		
        logger.info("FastDFS上传文件结束...");
        
		return returnList;
	}
	
	private static String[] upload_object(MultipartFile attach, String fileName, 
			long fileSize, String ext) {
		
		 NameValuePair[] nvp = null;
		 
		 /*nvp = new NameValuePair[]{ 
             new NameValuePair("name", fileName), 
             new NameValuePair("size", String.valueOf(fileSize)),
             new NameValuePair("sex", ext) 
		 }; */
		
		 String[] uploadResults = null;
		 try {
			 uploadResults = storageClient.upload_file(attach.getBytes(), ext, nvp);
			 logger.info("FastDFS上传文件成功......");
		 } catch (Exception e) {
			 logger.error("FastDFS上传出错，上传文件失败：" + e.getMessage());
			 e.printStackTrace();
		 }
        
		 return uploadResults;
    }
	
    public static ResponseEntity<byte[]> download(String group, String path, String newName) {
    	if(!fdfs_is_init()) return null;
    	
    	byte[] content = null;
        HttpHeaders headers = new HttpHeaders();
        try {
            content = storageClient.download_file(group, newName);
            headers.setContentDispositionFormData("attachment",  
            		new String(newName.getBytes("UTF-8"), "iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        } catch (Exception e) {
        	logger.error("FastDFS下载出错，下载文件失败：" + e.getMessage());
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(content, headers, HttpStatus.CREATED);
    }

    public static Map<String, Object> getInfo(String group, String path){ 
    	if(!fdfs_is_init()) return null;
    	
    	Map<String, Object> map = null;
        try { 
            FileInfo fi = storageClient.get_file_info(group, path);
            
            map = new HashMap<>();
            map.put("group", group);
            map.put("path", path);
            map.put("name", path.substring(path.lastIndexOf("\\") + 1));
            map.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fi.getCreateTimestamp()));
            map.put("fileSize", fi.getFileSize());
            map.put("ip", fi.getSourceIpAddr());
            map.put("crc32", fi.getCrc32());
        } catch (Exception e) { 
        	logger.error("FastDFS获取文件信息出错，获取文件信息失败：" + e.getMessage());
            e.printStackTrace(); 
        } 
        return map;
    } 

    public static NameValuePair[] getMeta(String group, String path){ 
    	if(!fdfs_is_init()) return null;
    	
    	NameValuePair[] nvps = null;
        try { 
            nvps = storageClient.get_metadata(group, path); 
        } catch (Exception e) { 
        	logger.error("FastDFS获取元数据出错，获取元数据失败：" + e.getMessage());
            e.printStackTrace(); 
        } 
        return nvps;
    } 


    public static int delete(String group, String path){ 
    	if(!fdfs_is_init()) return 0;
    	
        try { 
        	int errorno = storageClient.delete_file("group1", "M00/00/00/wKgRcFV_08OAK_KCAAAA5fm_sy874.conf"); 
        	logger.error("FastDFS删除文件，errorNo：" + errorno + "（0-成功，其他-失败）");
        	if(errorno != 0) return 0;
        } catch (Exception e) { 
        	logger.error("FastDFS删除文件出错，删除文件失败：" + e.getMessage());
            e.printStackTrace(); 
        } 
        
        return 1;
    }
    
    private static boolean fdfs_is_init(){
    	if(storageClient == null){
			logger.warn("FastDFS调用失败，未初始化FastDFS配置，调用方法：FastDFSConfig.init");
			return false;
		}
    	return true;
    }
}

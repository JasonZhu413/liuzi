package com.liuzi.fastdfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class FastDFSPool extends FastDFSPoolConfig{
	
	private static Logger logger = LoggerFactory.getLogger(FastDFSPool.class);
	
	static{
		if (idleConnectionPool == null || idleConnectionPool.size() == 0){
		    synchronized(FastDFSPool.class) {
		      if (idleConnectionPool == null || idleConnectionPool.size() == 0)
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
		List<Map<String, Object>> list = uploadBatch(request);
		return list == null ? null : list.get(0);
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
		//if(!fdfs_is_init()) return null;
		
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
            returnMap.put("newName", path.substring(path.lastIndexOf("/") + 1));
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
    
    //上传缓存数据到storage服务器
    private static String[] upload_object(MultipartFile mf, String fileName, long fileSize, String ext){
    	
    	NameValuePair[] nvp = null;
		 
		 /*nvp = new NameValuePair[]{ 
            new NameValuePair("name", fileName), 
            new NameValuePair("size", String.valueOf(fileSize)),
            new NameValuePair("sex", ext) 
		 }; */
    	
        String[] upPath = null;
        StorageClient storageClient1 = checkOut(10);
        try {
            upPath = storageClient1.upload_file(mf.getBytes(), ext, nvp);
            checkIn(storageClient1);
        } catch (Exception e) {
        	drop(storageClient1);//异常销毁此连接
            logger.error("upload fail：" + e.getMessage());
            e.printStackTrace();
        }
        
        return upPath;
    }
    
    //将文件缓存到字节数组中
    /*private byte[] getFileBuff(File file){
        byte[] buff = null;
        try (FileInputStream inputStream = new FileInputStream(file)){
            buff = new byte[inputStream.available()];
            inputStream.read(buff);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buff;
    }*/
}

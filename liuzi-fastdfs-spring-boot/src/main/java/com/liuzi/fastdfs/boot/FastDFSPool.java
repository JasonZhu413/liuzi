package com.liuzi.fastdfs.boot;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csource.common.NameValuePair;

import com.liuzi.fastdfs.boot.base.ClientGlobal;
import com.liuzi.fastdfs.boot.base.StorageClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class FastDFSPool{
	
	private static Logger logger = LoggerFactory.getLogger(FastDFSPool.class);
	
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
	public static FastDFSFile upload(HttpServletRequest request){
		List<FastDFSFile> list = uploadBatch(request);
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
	private static List<FastDFSFile> uploadBatch(HttpServletRequest request){
		logger.info("FastDFS上传文件开始...");
	    MultipartHttpServletRequest multi = (MultipartHttpServletRequest) request;
        
	    List<MultipartFile> list = new ArrayList<MultipartFile>();
	    
        Map<String, MultipartFile> map = multi.getFileMap();
        
        for (Map.Entry<String, MultipartFile> entry : map.entrySet()) {  
        	list.add(entry.getValue());
        } 
        
        logger.info("上传文件数：" + list.size());
        
        List<FastDFSFile> returnList = new ArrayList<FastDFSFile>();
        FastDFSFile fdf;
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
            
            String[] uploadResults = upload_object(mf, ext);
            String group = uploadResults[0];//组
            String path = uploadResults[1];//地址
            
            fdf = new FastDFSFile();
            fdf.setOldName(fileName);
            fdf.setNewName(path.substring(path.lastIndexOf("/") + 1));
            fdf.setGroup(group);
            fdf.setPath(path);
            fdf.setUrl(group + "/" + path); 
            fdf.setSize(fileSize / 1024);
            fdf.setExt(ext);
            returnList.add(fdf);
            
            logger.info("文件" + count + "：上传成功...\ngroup：" + group + "\npath：" + path + "");
        }
		
        logger.info("FastDFS上传文件结束...");
        
		return returnList;
	}
	
	/**
     * 写文件
     * @param content
     * @param ext
     * @return
     */
    public static FastDFSFile write(String content, String ext){
    	if(StringUtils.isEmpty(content)){
			return null;
		}
    	
    	if(StringUtils.isEmpty(ext)){
    		ext = "txt";
    	}
		
    	FastDFSFile fdf = new FastDFSFile();
		
		byte[] b = null;
		try {
			b = content.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		fdf.setSize(b.length / 1024);
        fdf.setExt(ext);
		
		String[] data = upload_object(b, ext, null);
		String group = data[0];
		String path = data[1];
		String fileName = path.substring(path.lastIndexOf("/") + 1);
		fdf.setGroup(group);
        fdf.setPath(path);
        fdf.setUrl(group + "/" + path); 
		fdf.setOldName(fileName);
        fdf.setNewName(fileName);
        
		return fdf;
    }
    
    /**
     * 下载文件
     * @param response
     * @param url
     * @return
     */
    public static void download(HttpServletResponse response, String url){
    	if(StringUtils.isEmpty(url)){
			return;
		}
		
		int first = url.indexOf("/");
		String groupName = url.substring(0, first);
		String filePath = url.substring(first + 1);
		
		download(response, groupName, filePath);
    }
    
    /**
     * 下载文件
     * @param response
     * @param group
     * @param path
     * @return
     */
    public static void download(HttpServletResponse response,
    		String group, String path){
    	//response.setContentType("application/octet-stream");
    	OutputStream os = null;
    	try {
    		os = response.getOutputStream();
	    	byte[] b = download_object(group, path);
			if(b == null){
				return;
			}
		    os.write(b);
		    os.flush();
        }catch (Exception e){
        	logger.error("download fail：" + e.getMessage());
        	e.printStackTrace();
        }finally{
    		try {
    			if(os != null){
					os.close();
        		}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
    
    /**
     * 下载文件
     * @param response
     * @param url
     * @return
     */
    public static void downloadZip(HttpServletResponse response, String url){
    	if(StringUtils.isEmpty(url)){
			return;
		}
		
		int first = url.indexOf("/");
		String groupName = url.substring(0, first);
		String filePath = url.substring(first + 1);
		
		downloadZip(response, groupName, filePath);
    }
    
    /**
     * 下载文件
     * @param response
     * @param group
     * @param path
     * @return
     */
    public static void downloadZip(HttpServletResponse response,
    		String group, String path){
    	//response.setContentType("application/octet-stream");
    	OutputStream os = null;
    	GZIPOutputStream out = null;
    	try {
    		os = response.getOutputStream();
    		out = new GZIPOutputStream(os);
    		
	    	byte[] b = download_object(group, path);
	    	
			if(b == null){
				return;
			}
			out.write(b);
			out.flush();
        }catch (Exception e){
        	logger.error("download fail：" + e.getMessage());
        	e.printStackTrace();
        }finally{
    		try {
    			if(out != null){
    				out.close();
        		}
    			if(os != null){
					os.close();
        		}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
    
    /**
     * 读文件
     * @param url
     * @return
     */
    public static String read(String url){
    	if(StringUtils.isEmpty(url)){
			return null;
		}
		
		int first = url.indexOf("/");
		String groupName = url.substring(0, first);
		String filePath = url.substring(first + 1);
		
    	return read(groupName, filePath);
    }
    
    /**
     * 读文件
     * @param group
     * @param path
     * @return
     */
    public static String read(String group, String path){
    	byte[] b = download_object(group, path);
		if(b == null){
			return null;
		}
		
		String content = "";
		
		try {
			content = new String(b, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("file download fail：" + e.getMessage());
		}
		
		return content;
    }
    
    /**
     * 删除文件
     * @param url
     * @return
     */
    public static int delete(String url){
    	if(StringUtils.isEmpty(url)){
			return -1;
		}
		
		int first = url.indexOf("/");
		String groupName = url.substring(0, first);
		String filePath = url.substring(first + 1);
		
    	return delete(groupName, filePath);
    }
    
    /**
     * 删除文件
     * @param group
     * @param path
     * @return
     */
    public static int delete(String group, String path){
    	StorageClient storageClient1 = FastDFSPoolConfig.checkOut(10);
    	int i = -1;
    	try {
    		
    		i = storageClient1.delete_file(group, path);
    		FastDFSPoolConfig.checkIn(storageClient1);
    	} catch (Exception e) {
    		FastDFSPoolConfig.drop(storageClient1);
    		e.printStackTrace(); 
    		logger.error("file delete fail：" + e.getMessage());
    	}
    	return i;
    }
    
    private static String[] upload_object(MultipartFile mf, String ext){
    	String[] str = null;
		try {
			str = upload_object(mf.getBytes(), ext, null);
		} catch (IOException e) {
			logger.error("upload error：" + e.getMessage());
			e.printStackTrace();
		}
        return str;
    }
    
    private static String[] upload_object(byte[] b, String ext, NameValuePair[] list){
        String[] data = null;
        StorageClient storageClient1 = FastDFSPoolConfig.checkOut(10);
        try {
        	data = storageClient1.upload_file(b, ext, list);
            
        	FastDFSPoolConfig.checkIn(storageClient1);
        } catch (Exception e) {
        	FastDFSPoolConfig.drop(storageClient1);//异常销毁此连接
            logger.error("upload fail：" + e.getMessage());
            e.printStackTrace();
        }
        
        return data;
    }
    
    public static byte[] download_object(String group, String path){
    	if(StringUtils.isEmpty(group) || StringUtils.isEmpty(path)){
			return null;
		}
    	
    	byte[] b = null;
    	StorageClient storageClient1 = FastDFSPoolConfig.checkOut(10);
    	try{
        	b = storageClient1.download_file(group, path);
        	FastDFSPoolConfig.checkIn(storageClient1);
        } catch (Exception e) {
        	FastDFSPoolConfig.drop(storageClient1);//异常销毁此连接
        	e.printStackTrace();
        	logger.error("file download fail：" + e.getMessage());
        }
    	
		return b;
    }
}

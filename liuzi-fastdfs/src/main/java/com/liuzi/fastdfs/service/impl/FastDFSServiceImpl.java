package com.liuzi.fastdfs.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csource.common.NameValuePair;

import com.liuzi.fastdfs.FastDFSFile;
import com.liuzi.fastdfs.FastDFSPoolConfig;
import com.liuzi.fastdfs.base.ClientGlobal;
import com.liuzi.fastdfs.base.StorageClient;
import com.liuzi.fastdfs.service.FastDFSService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Service("fastDFSService")
public class FastDFSServiceImpl implements FastDFSService{
	
	private Logger logger = LoggerFactory.getLogger(FastDFSServiceImpl.class);
	
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
	@Override
	public FastDFSFile upload(HttpServletRequest request){
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
	@Override
	public List<FastDFSFile> uploadBatch(HttpServletRequest request){
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
            fdf.setFullUrl(ClientGlobal.fileServer + group + "/" + path);
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
	@Override
    public FastDFSFile write(String content, String ext){
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
        fdf.setFullUrl(ClientGlobal.fileServer + group + "/" + path);
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
	@Override
    public void download(HttpServletResponse response, String url, String specFileName){
    	if(StringUtils.isEmpty(url)){
			return;
		}
		
		int first = url.indexOf("/");
		String groupName = url.substring(0, first);
		String filePath = url.substring(first + 1);
		
		this.download(response, groupName, filePath, specFileName);
    }
    
    /**
     * 下载文件
     * @param response
     * @param group
     * @param path
     * @return
     */
	@Override
    public void download(HttpServletResponse response,
    		String group, String path, String specFileName){
    	OutputStream os = null;
    	try {
    		response.addHeader("Content-Disposition", "attachment;filename=" + new String(specFileName.replaceAll(" ", "").getBytes("UTF-8"), "ISO8859-1"));
            /*response.addHeader("Content-Length", "" + f.length());
            response.setContentType("application/octet-stream");*/
    		
    		os = response.getOutputStream();
	    	byte[] b = download_object(group, path, specFileName);
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
	@Override
    public void downloadZip(HttpServletResponse response, String url, String specFileName){
    	if(StringUtils.isEmpty(url)){
			return;
		}
		
		int first = url.indexOf("/");
		String groupName = url.substring(0, first);
		String filePath = url.substring(first + 1);
		
		this.downloadZip(response, groupName, filePath, specFileName);
    }
	
    /**
     * 下载文件
     * @param response
     * @param group
     * @param path
     * @return
     */
	@Override
    public void downloadZip(HttpServletResponse response,
    		String group, String path, String specFileName){
    	
    	OutputStream os = null;
    	ZipOutputStream out = null;
    	try {
    		String extName = specFileName.substring(specFileName.lastIndexOf(".") + 1);
    		specFileName = specFileName.substring(0, specFileName.lastIndexOf("."));
    		specFileName = new String(specFileName.replaceAll(" ", "").getBytes("UTF-8"), "ISO8859-1");
    		
    		String[] exts = {"RAR", "TAR", "ZIP", "GZIP", "CAB", "UUE",
					"ARJ", "BZ2", "LZH", "JAR", "ACE", "ISO", "7-ZIP", 
					"Z", "GZ"};
			int length = Arrays.binarySearch(exts, extName.toUpperCase());
			
			if(length <= 0){
				response.addHeader("Content-Disposition", "attachment;filename=" + specFileName + ".zip");
			}
			
    		os = response.getOutputStream();
    		out = new ZipOutputStream(os);
    		
	    	byte[] b = download_object(group, path, specFileName);
			if(b == null){
				return;
			}
			
			if(length <= 0){
				ZipEntry entry = new ZipEntry(specFileName + "." + extName);
				out.putNextEntry(entry);
				out.write(b);
				out.flush();
			}
        }catch (Exception e){
        	logger.error("download fail：" + e.getMessage());
        	e.printStackTrace();
        }finally{
    		try {
    			if(out != null){
    				out.closeEntry();
    				out.close();
        		}
    			if(os != null){
					os.close();
        		}
			} catch (IOException e) {
				e.printStackTrace();
				logger.info("fastdfs download error : " + e.getMessage());
			}
        }
    }
    
    /**
     * 读文件
     * @param url
     * @return
     */
	@Override
    public String read(String url){
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
	@Override
    public String read(String group, String path){
    	byte[] b = download_object(group, path, null);
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
	@Override
    public int delete(String url){
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
	@Override
    public int delete(String group, String path){
    	StorageClient storageClient = FastDFSPoolConfig.checkOut(10);
    	int i = -1;
    	try {
    		
    		i = storageClient.delete_file(group, path);
    		//FastDFSPoolConfig.checkIn(storageClient);
    	} catch (Exception e) {
    		//FastDFSPoolConfig.drop(storageClient);
    		e.printStackTrace(); 
    		logger.error("file delete fail：" + e.getMessage());
    	} finally{
        	FastDFSPoolConfig.drop(storageClient);
        }
    	return i;
    }
    
    private String[] upload_object(MultipartFile mf, String ext){
    	String[] str = null;
		try {
			str = upload_object(mf.getBytes(), ext, null);
		} catch (IOException e) {
			logger.error("upload error：" + e.getMessage());
			e.printStackTrace();
		}
        return str;
    }
    
    private String[] upload_object(byte[] b, String ext, NameValuePair[] list){
        String[] data = null;
        StorageClient storageClient = FastDFSPoolConfig.checkOut(10);
        try {
        	data = storageClient.upload_file(b, ext, list);
            
        	//FastDFSPoolConfig.checkIn(storageClient);
        } catch (Exception e) {
        	//FastDFSPoolConfig.drop(storageClient);//异常销毁此连接
            logger.error("upload fail：" + e.getMessage());
            e.printStackTrace();
        } finally{
        	FastDFSPoolConfig.drop(storageClient);
        }
        
        return data;
    }
    
    private byte[] download_object(String group, String path, String specFileName){
    	if(StringUtils.isEmpty(group) || StringUtils.isEmpty(path)){
			return null;
		}
    	
    	byte[] b = null;
    	StorageClient storageClient = FastDFSPoolConfig.checkOut(10);
    	try{
        	b = storageClient.download_file(group, path);
        	//FastDFSPoolConfig.checkIn(storageClient);
        } catch (Exception e) {
        	//FastDFSPoolConfig.drop(storageClient);//异常销毁此连接
        	e.printStackTrace();
        	logger.error("file download fail：" + e.getMessage());
        } finally{
        	FastDFSPoolConfig.drop(storageClient);
        }
    	return b;
    }
}

package com.liuzi.fastdfs;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.csource.common.NameValuePair;

import com.liuzi.fastdfs.base.ClientGlobal;
import com.liuzi.fastdfs.base.StorageClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
public class FdfsServer{
	
	@Autowired
	private FastDFSPoolConfig fastDFSPoolConfig;
	
	/**
	 * 上传
	 * @param request
	 * @return FdfsFile
	 */
	public FdfsFile upload(HttpServletRequest request){
		List<FdfsFile> list = this.uploadBatch(request);
		return list == null ? null : list.get(0);
	}
	
	/**
	 * 批量上传
	 * @param request
	 * @return List<FdfsFile>
	 */
	private List<FdfsFile> uploadBatch(HttpServletRequest request){
		log.info("FastDFS上传文件开始...");
	    MultipartHttpServletRequest multi = (MultipartHttpServletRequest) request;
        
	    List<MultipartFile> list = new ArrayList<MultipartFile>();
	    
        Map<String, MultipartFile> map = multi.getFileMap();
        
        for (Map.Entry<String, MultipartFile> entry : map.entrySet()) {  
        	list.add(entry.getValue());
        } 
        
        log.info("上传文件数：" + list.size());
        
        List<FdfsFile> returnList = new ArrayList<FdfsFile>();
        FdfsFile fdfsFile;
        int count = 0;
        for(MultipartFile mf : list){
        	count ++;
        	
        	String fileName = mf.getOriginalFilename();
            long fileSize = mf.getSize();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            
            log.info("文件" + count + "：开始上传...\n文件名：" + fileName + "\n文件大小：" + fileSize + "b");
            
            int maxSize = ClientGlobal.g_file_upload_max_size;
        	if(maxSize > 0 && fileSize > (maxSize * 1024 * 1024)){
        		log.warn("上传文件大小超过" + maxSize + "M，上传文件取消，上传结束...");
            	return null;
            }
            
            String[] uploadResults = this.upload_object(mf, suffix);
            String group = uploadResults[0];//组
            String path = uploadResults[1];//地址
            String wholePath = group + "/" + path;
            
            fdfsFile = new FdfsFile();
            fdfsFile.setOriginName(fileName);
            fdfsFile.setRealName(path.substring(path.lastIndexOf("/") + 1));
            fdfsFile.setGroup(group);
            fdfsFile.setPath(path);
            fdfsFile.setWholePath(wholePath);
            fdfsFile.setUrl(ClientGlobal.fileServer + wholePath);
            fdfsFile.setSize(fileSize / 1024);
            fdfsFile.setSuffix(suffix);
            
            //图片生成缩略图
            ImgReset.excute(fdfsFile, request);
            
            returnList.add(fdfsFile);
            
            log.info("文件" + count + "：上传成功...\ngroup：" + group + "\npath：" + path + "");
        }
		
        log.info("FastDFS上传文件结束...");
        
		return returnList;
	}
	
	/**
     * 写文件
     * @param content
     * @param suffix
     * @return
     */
    public FdfsFile write(String content, String suffix){
    	if(StringUtils.isEmpty(content)){
			return null;
		}
    	
    	if(StringUtils.isEmpty(suffix)){
    		suffix = "txt";
    	}
		
    	FdfsFile fdf = new FdfsFile();
		
		byte[] b = null;
		try {
			b = content.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		fdf.setSize(b.length / 1024);
        fdf.setSuffix(suffix);
		
		String[] data = this.upload_object(b, suffix, null);
		String group = data[0];
		String path = data[1];
		String fileName = path.substring(path.lastIndexOf("/") + 1);
		fdf.setGroup(group);
        fdf.setPath(path);
        fdf.setUrl(group + "/" + path); 
        fdf.setOriginName(fileName);
        fdf.setRealName(fileName);
        
		return fdf;
    }
    
    /**
     * 下载文件
     * @param response
     * @param url
     * @return
     */
    public void download(HttpServletResponse response, String url){
    	if(StringUtils.isEmpty(url)){
			return;
		}
		
		int first = url.indexOf("/");
		String groupName = url.substring(0, first);
		String filePath = url.substring(first + 1);
		
		this.download(response, groupName, filePath);
    }
    
    /**
     * 下载文件
     * @param response
     * @param group
     * @param path
     * @return
     */
    public void download(HttpServletResponse response,
    		String group, String path){
    	//response.setContentType("application/octet-stream");
    	OutputStream os = null;
    	try {
    		os = response.getOutputStream();
	    	byte[] b = this.download_object(group, path);
			if(b == null){
				return;
			}
		    os.write(b);
		    os.flush();
        }catch (Exception e){
        	log.error("download fail：" + e.getMessage());
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
    public void downloadZip(HttpServletResponse response, String url){
    	if(StringUtils.isEmpty(url)){
			return;
		}
		
		int first = url.indexOf("/");
		String groupName = url.substring(0, first);
		String filePath = url.substring(first + 1);
		
		this.downloadZip(response, groupName, filePath);
    }
    
    /**
     * 下载文件
     * @param response
     * @param group
     * @param path
     * @return
     */
    public void downloadZip(HttpServletResponse response, String group, String path){
    	//response.setContentType("application/octet-stream");
    	OutputStream os = null;
    	GZIPOutputStream out = null;
    	try {
    		os = response.getOutputStream();
    		out = new GZIPOutputStream(os);
    		
	    	byte[] b = this.download_object(group, path);
	    	
			if(b == null){
				return;
			}
			out.write(b);
			out.flush();
        }catch (Exception e){
        	log.error("download fail：" + e.getMessage());
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
    public String read(String url){
    	if(StringUtils.isEmpty(url)){
			return null;
		}
		
		int first = url.indexOf("/");
		String groupName = url.substring(0, first);
		String filePath = url.substring(first + 1);
		
    	return this.read(groupName, filePath);
    }
    
    /**
     * 读文件
     * @param group
     * @param path
     * @return
     */
    public String read(String group, String path){
    	byte[] b = this.download_object(group, path);
		if(b == null){
			return null;
		}
		
		String content = "";
		
		try {
			content = new String(b, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("file download fail：" + e.getMessage());
		}
		
		return content;
    }
    
    /**
     * 删除文件
     * @param url
     * @return
     */
    public int delete(String url){
    	if(StringUtils.isEmpty(url)){
			return -1;
		}
		
		int first = url.indexOf("/");
		String groupName = url.substring(0, first);
		String filePath = url.substring(first + 1);
		
    	return this.delete(groupName, filePath);
    }
    
    /**
     * 删除文件
     * @param group
     * @param path
     * @return
     */
    public int delete(String group, String path){
    	StorageClient storageClient1 = fastDFSPoolConfig.checkOut(10);
    	int i = -1;
    	try {
    		i = storageClient1.delete_file(group, path);
    		fastDFSPoolConfig.checkIn(storageClient1);
    	} catch (Exception e) {
    		fastDFSPoolConfig.drop(storageClient1);
    		e.printStackTrace(); 
    		log.error("file delete fail：" + e.getMessage());
    	}
    	return i;
    }
    
    private String[] upload_object(MultipartFile mf, String ext){
    	String[] str = null;
		try {
			str = this.upload_object(mf.getBytes(), ext, null);
		} catch (IOException e) {
			log.error("upload error：" + e.getMessage());
			e.printStackTrace();
		}
        return str;
    }
    
    private String[] upload_object(byte[] b, String ext, NameValuePair[] list){
        String[] data = null;
        StorageClient storageClient1 = fastDFSPoolConfig.checkOut(10);
        try {
        	data = storageClient1.upload_file(b, ext, list);
            
        	fastDFSPoolConfig.checkIn(storageClient1);
        } catch (Exception e) {
        	fastDFSPoolConfig.drop(storageClient1);//异常销毁此连接
        	log.error("upload fail：" + e.getMessage());
            e.printStackTrace();
        }
        
        return data;
    }
    
    public byte[] download_object(String group, String path){
    	if(StringUtils.isEmpty(group) || StringUtils.isEmpty(path)){
			return null;
		}
    	
    	byte[] b = null;
    	StorageClient storageClient1 = fastDFSPoolConfig.checkOut(10);
    	try{
        	b = storageClient1.download_file(group, path);
        	fastDFSPoolConfig.checkIn(storageClient1);
        } catch (Exception e) {
        	fastDFSPoolConfig.drop(storageClient1);//异常销毁此连接
        	e.printStackTrace();
        	log.error("file download fail：" + e.getMessage());
        }
    	
		return b;
    }
}

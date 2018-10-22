package com.liuzi.fastdfs.boot.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liuzi.fastdfs.boot.FastDFSFile;


public interface FastDFSService{
	
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
	public FastDFSFile upload(HttpServletRequest request);
	
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
	public List<FastDFSFile> uploadBatch(HttpServletRequest request);
	
	/**
     * 写文件
     * @param content
     * @param ext
     * @return
     */
    public FastDFSFile write(String content, String ext);
    
    /**
     * 下载文件
     * @param response
     * @param url
     * @return
     */
    public void download(HttpServletResponse response, String url, String specFileName);
    
    /**
     * 下载文件
     * @param response
     * @param group
     * @param path
     * @return
     */
    public void download(HttpServletResponse response,
    		String group, String path, String specFileName);
    
    /**
     * 下载文件
     * @param response
     * @param url
     * @return
     */
    public void downloadZip(HttpServletResponse response, String url, String specFileName);
    
    /**
     * 下载文件
     * @param response
     * @param group
     * @param path
     * @return
     */
    public void downloadZip(HttpServletResponse response,
    		String group, String path, String specFileName);
    
    /**
     * 读文件
     * @param url
     * @return
     */
    public String read(String url);
    
    /**
     * 读文件
     * @param group
     * @param path
     * @return
     */
    public String read(String group, String path);
    
    /**
     * 删除文件
     * @param url
     * @return
     */
    public int delete(String url);
    
    /**
     * 删除文件
     * @param group
     * @param path
     * @return
     */
    public int delete(String group, String path);
    
    
    
}

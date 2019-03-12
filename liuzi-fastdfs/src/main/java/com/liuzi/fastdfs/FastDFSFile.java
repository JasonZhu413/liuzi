package com.liuzi.fastdfs;

import java.io.Serializable;

import lombok.Data;



@Data
public class FastDFSFile implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 原始文件名
	 */
	private String oldName;
	/**
	 * 新文件名
	 */
	private String newName;
	
	/**
	 * group name
	 */
	private String group;
	
	/**
	 * path
	 */
	private String path;
	
	/**
	 * 地址
	 */
	private String url;
	
	/**
	 * 地址
	 */
	private String fullUrl;
	
	/**
	 * 文件大小（k）
	 */
    private long size;
    
    /**
     * 扩展名
     */
	private String suffix;
}


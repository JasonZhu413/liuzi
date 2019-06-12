package com.liuzi.fastdfs;

import java.io.Serializable;

import lombok.Data;



@Data
public class FdfsFile implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 原始文件名
	 */
	private String originName;
	
	/**
	 * 实际文件名
	 */
	private String realName;
	
	/**
	 * 文件组
	 */
	private String group;
	
	/**
	 * 文件目录
	 */
	private String path;
	
	/**
	 * 文件全目录（group+path）
	 */
	private String wholePath;
	
	/**
	 * 原图地址（域名+文件全目录）
	 */
	private String url;
	
	/**
	 * 大图地址（压缩后）
	 */
	private String large;
	
	/**
	 * 中图地址（压缩后）
	 */
	private String middle;
	
	/**
	 * 小图地址（压缩后）
	 */
	private String small;
	
	/**
	 * 文件大小（kb）
	 */
    private long size;
    
    /**
     * 扩展名
     */
	private String suffix;
}


package com.liuzi.fastdfs;

import java.io.Serializable;

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
	private String ext;
	
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	
	
}


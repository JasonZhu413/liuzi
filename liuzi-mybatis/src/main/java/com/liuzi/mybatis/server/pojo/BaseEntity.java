package com.liuzi.mybatis.server.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 基础实体
 * @author zsy
 */
@Data
public class BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 编号
	 */
	protected Long id;
	/**
	 * 是否删除 
	 * 0-否
	 * 1-是
	 */
	protected Integer isDelete;
	/**
	 * 创建时间
	 */
	protected Date createTime;
	/**
	 * 更新时间
	 */
	protected Date updateTime;
}

package com.cto.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ArticleType implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long pId;
	
	private String name;

	private Integer type;
	
	private String typeName;

	private String remark;
	
	private Integer isDelete = 0;
	
	private Date createTime;//
	
	private Date updateTime;//

}
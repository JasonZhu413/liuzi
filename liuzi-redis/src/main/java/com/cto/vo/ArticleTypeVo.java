package com.cto.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.cto.model.ArticleType;

import lombok.Data;

@Data
public class ArticleTypeVo implements Serializable{

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
	
	private List<ArticleType> child;
	
	public ArticleTypeVo(){
		
	}
	
	public ArticleTypeVo(ArticleType at){
		this.id = at.getId();
		this.pId = at.getPId();
		this.name = at.getName();
		this.type = at.getType();
		this.typeName = at.getTypeName();
		this.remark = at.getRemark();
	}
}
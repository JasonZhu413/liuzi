package com.liuzi.mybatis.pojo;

import java.io.Serializable;
import java.util.Date;

public class BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected Integer isDelete = 0;
	
	protected Date createTime;//
	
	protected Date updateTime;//


	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "BaseEntity [isDelete=" + isDelete + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}

}

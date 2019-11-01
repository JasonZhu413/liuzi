package com.liuzi.mybatis.server.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 分页实体
 * @author zsy
 * @param <T>
 */
@Data
public class Page<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 默认页码为1
	 */
	public static final int PAGE_NO = 1;
	/**
	 * 默认每页显示10条
	 */
	public static final int PAGE_SIZE = 10;
	
	
	/**
	 * 分页参数（第几条）
	 */
	private int limit;
	/**
	 * 分页参数（每页几条）
	 */
	private int offset;
	/**
	 * 查询第几页
	 */
	private int pageNo;
	/**
	 * 每页查询几条
	 */
	private int pageSize;
	/**
	 * 查询总数
	 */
	private long totalCount;
	/**
	 * 共多少页
	 */
	private int pageTotal;
	/**
	 * 当前页数量
	 */
	private int number;
	/**
	 * 内容
	 */
	private List<T> data = new ArrayList<>();
	
	public Page() {
		this.pageNo = PAGE_NO;
		this.pageSize = PAGE_SIZE;
		this.limit = (this.pageNo - 1) * this.pageSize;
		this.offset = this.pageSize;
	}
	
	/**
	 * @param pageNo 第几页
	 * @param pageSize 每页条数
	 */
	public Page(Integer pn, Integer ps) {
		this.pageNo = pn == null || pn <= 0 ? PAGE_NO : pn;
		this.pageSize = ps == null || ps <= 0 ? PAGE_SIZE : ps;
		this.limit = (this.pageNo - 1) * this.pageSize;
		this.offset = this.pageSize;
	}

	public Page(List<T> data) {
		this.data = data;
	}
}

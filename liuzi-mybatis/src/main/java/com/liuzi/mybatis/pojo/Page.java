package com.liuzi.mybatis.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title:        Page
 * 
 * @Description   分页实体
 * 
 * @author        ZhuShiyao
 * 
 * @Time          2016-11-23 11:02:29
 * 
 * @version       1.0
 * 
 */
public class Page<T> {
	/**
	 * 默认页码为1
	 */
	private static final int PAGE_NO = 1;
	
	/**
	 * 默认每页显示10条
	 */
	private static final int PAGE_SIZE = 10;
	
	/**
	 * 正序
	 */
	public static final String ASC = "asc";
	
	/**
	 * 倒序
	 */
	public static final String DESC = "desc";
	
	/**
	 * 页码
	 */
	private int pageNo = 0;
	
	/**
	 * 每页显示条数
	 */
	private int pageSize = 0;
	
	/**
	 * 总数
	 */
	private long totalCount = 0L;
	
	/**
	 * 第几条开始查询
	 */
	private int limit = 0;
	
	/**
	 * 每次查询几条
	 */
	private int offset = 10;
	
	/**
	 * 共多少页
	 */
	private int pageTotal = 0;
	
	/**
	 * 当前页数量
	 */
	private int number;
	
	/**
	 * 实体
	 */
	private List<T> data = new ArrayList<T>();
	
	public Page() {
		this.pageNo = Page.PAGE_NO;
		this.pageSize = Page.PAGE_SIZE;
	}
	
	public Page(int pageNo, int pageSize) {
		this.pageNo = pageNo <= 0 ? Page.PAGE_NO : pageNo;
		this.pageSize = pageSize <= 0 ? Page.PAGE_SIZE : pageSize;
		this.limit = (this.pageNo - 1) * this.pageSize;
		this.offset = this.pageSize;
	}

	public Page(List<T> data) {
		this.data = data;
	}
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Page [pageNo=" + pageNo + ", pageSize=" + pageSize + 
				", totalCount=" + totalCount + ", limit=" + limit + 
				", offset=" + offset + ", pageTotal=" + pageTotal + 
				", number=" + number + ", data=" + data + "]";
	}
}

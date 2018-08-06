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
	private Integer pageNo = 0;
	
	/**
	 * 每页显示条数
	 */
	private Integer pageSize = 0;
	
	/**
	 * 总数
	 */
	private Integer totalCount = 0;
	
	/**
	 * 第几条开始查询
	 */
	private Integer limit = 0;
	
	/**
	 * 每次查询几条
	 */
	private Integer offset = 10;
	
	/**
	 * 共多少页
	 */
	private Integer pageTotal = 0;
	
	/**
	 * 实体
	 */
	private List<T> result = new ArrayList<T>();
	
	public Page() {
		this.pageNo = Page.PAGE_NO;
		this.pageSize = Page.PAGE_SIZE;
	}
	
	public Page(Integer pageNo, Integer pageSize) {
		this.pageNo = pageNo == null || pageNo == 0 ? Page.PAGE_NO : pageNo;
		this.pageSize = pageSize == null || pageSize == 0 ? Page.PAGE_SIZE : pageSize;
		this.limit = (this.pageNo - 1) * this.pageSize;
		this.offset = this.pageSize;
	}

	public Page(List<T> result) {
		this.result = result;
	}
	
	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(Integer pageTotal) {
		this.pageTotal = pageTotal;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	/*private int getDefaultPageNo(Integer pn) {
		return (pn == null || pn == 0) ? PAGE_NO : pn;
	}

	private int getDefaultPageSize(Integer ps) {
		return (ps == null || ps == 0) ? PAGE_SIZE : ps;
	}*/

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "Page [pageNo=" + pageNo + ", pageSize=" + pageSize
				+ ", totalCount=" + totalCount + ", limit=" + limit
				+ ", offset=" + offset + ", pageTotal=" + pageTotal
				+ ", result=" + result + "]";
	}
	
}

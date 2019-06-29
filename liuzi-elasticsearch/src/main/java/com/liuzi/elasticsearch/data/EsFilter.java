package com.liuzi.elasticsearch.data;

import lombok.Data;

@Data
public class EsFilter {
	/**
	 * 查询字段
	 */
	private String[] includes;
	/**
	 * 不包含字段
	 */
	private String[] excludes;
}

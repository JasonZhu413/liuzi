package com.liuzi.util.baidu.result;

import java.io.Serializable;

import lombok.Data;

import org.json.JSONArray;

@Data
public class Spam implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 正确调用生成的唯一标识码，用于问题定位
	 */
	private Long logId;
	/**
	 * 包含审核结果详情
	 */
	private Result result;
	/**
	 * 错误码
	 */
	private int errorCode;
	/**
	 * 错误信息
	 */
	private String errorMsg;
	
	@Data
	public static class Result implements Serializable{
		private static final long serialVersionUID = 1L;
		/**
		 * 请求中是否包含违禁
		 * 0表示非违禁，1表示违禁，2表示建议人工复审
		 */
		private int spam;
		/**
		 * 审核未通过的类别列表与详情
		 */
		private JSONArray reject;
		/**
		 * 待人工复审的类别列表与详情
		 */
		private JSONArray review;
		/**
		 * 审核通过的类别列表与详情
		 */
		private JSONArray pass;
	}
}

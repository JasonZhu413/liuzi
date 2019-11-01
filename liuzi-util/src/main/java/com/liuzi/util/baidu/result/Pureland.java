package com.liuzi.util.baidu.result;

import java.io.Serializable;

import lombok.Data;


@Data
public class Pureland implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 正确调用生成的唯一标识码，用于问题定位
	 */
	private Long logId;
	/**
	 * 错误文本
	 */
	private String text;
	/**
	 * 审核结果
	 */
	private Items[] items;
	/**
	 * 错误码
	 */
	private int errorCode;
	/**
	 * 错误信息
	 */
	private String errorMsg;
	
	@Data
	public static class Items implements Serializable{
		private static final long serialVersionUID = 1L;
		/**
		 * 0：无风险；1：疑似；2：有风险
		 */
		private int grade;
		/**
		 * 命中的违规关键词
		 */
		private Object hits;
		/**
		 * 模型分数值[0-1]，分数越高则置信度越高
		 */
		private float score;
		/**
		 * 违禁类型
		 * 1：色情；2：涉政
		 */
		private int label;
	}
}

package com.liuzi.util.token;

import net.minidev.json.JSONObject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenInfo{
    
	/**
	 * 验证是否成功
	 */
	private Boolean valid;
	/**
	 * 验证成功/失败信息
	 */
	private String msg;
	/**
	 * token
	 */
	private String token;
	/**
	 * token生成时间
	 */
	private Long createTime;
	/**
	 * 超时时间
	 */
	private Long expTime;
	/**
	 * 数据
	 */
	private JSONObject data;
}

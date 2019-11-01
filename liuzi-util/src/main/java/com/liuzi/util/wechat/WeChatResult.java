package com.liuzi.util.wechat;

import java.io.Serializable;

import lombok.Data;

/**
 * 返回码
 * @author zsy
 */
@Data
public class WeChatResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 返回码
	 */
	private String errcode;
	
	/**
	 * 说明
	 */
    private String errmsg;
    
    /**
     * 消息id
     */
    private Long msgid;
}

package com.liuzi.util.common;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.ToString;

/**
 * @Title:        Result
 * 
 * @Description:  json返回内容
 * 
 * @author        ZhuShiyao
 * 
 * @Date          2017年3月30日 下午9:47:55
 * 
 * @version       1.0
 * 
 */
@Getter
@ToString
public enum Result{ 
	
	SUCCESS(0, "SUCCESS", "成功"), ERROR(-1, "FAIL", "失败");
	
	/**
	 * 标识 0(成功) -1(失败)
	 */
	private int result;
	
	/**
	 * 代码  SUCCESS(result=0) FAIL(result=-1)
	 */
	private String code;
	
	/**
	 * 提示信息 成功(result=0) 失败(result=-1)
	 */
	private String msg;
	
	/**
	 * 失败代码(数字6位)
	 */
	private String errorCode;
	
	/**
	 * 失败代码(英文)
	 */
	private String errorEngCode;
	
	/**
	 * 失败信息
	 */
	private String errorMsg;
	
	/**
	 * 数据内容
	 */
	private Object data;
	
	Result(int result, String code, String msg){
		this.result = result;
		this.code = code;
		this.msg = msg;
	}
	
	public Result msg(String msg){
		this.msg = msg;
		return this;
	}
	
	public Result data(Object data){
		this.data = data;
		return this;
	}
	
	public Result data(String msg, Object data){
		this.msg = msg;
		this.data = data;
		return this;
	}
	
	public Result error(String errorMsg){
		this.errorMsg = errorMsg;
		return this;
	}
	
	public Result error(String errorCode, String errorMsg){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		return this;
	}
	
	public Result error(String errorCode, String errorEngCode,
			String errorMsg){
		this.errorCode = errorCode;
		this.errorEngCode = errorEngCode;
		this.errorMsg = errorMsg;
		return this;
	}
	
	public Result error(String errorCode, String errorEngCode,
			String errorMsg, Object data){
		this.errorCode = errorCode;
		this.errorEngCode = errorEngCode;
		this.errorMsg = errorMsg;
		this.data = data;
		return this;
	}
	
	private void check(){
		if(judge()){
			return;
		}
		
		try {
			throw new IllegalArgumentException(" >>> WARN 返回信息异常: " + this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	private boolean judge(){
		if(this.result == -1 && StringUtils.isEmpty(this.errorCode)){
			return false;
		}
		if(this.result == -1 && this.errorCode.length() != 6){
			return false;
		}
		if(this.result == -1 && StringUtils.isEmpty(this.errorEngCode)){
			return false;
		}
		if(this.result == -1 && StringUtils.isEmpty(this.errorMsg)){
			return false;
		}
		return true;
	}
}

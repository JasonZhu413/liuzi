package com.liuzi.util;

import java.io.Serializable;

import net.sf.json.JSONObject;

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
public class Result implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 返回标识 1-成功 0-失败
	 */
	private int result = 1;
	
	/**
	 * 错误代码
	 */
	private int code = 0;
	
	/**
	 * 失败信息
	 */
	private String msg;
	
	/**
	 * 返回内容
	 */
	private Object model;
	

  	public Result(){}
  	
  	public Result(int result){
  		this.result = result;
  	}

  	public Result(String msg){
  		this.result = 0;
	    this.msg = msg;
  	}
  	
  	public Result(int result, String msg){
  		this.result = result;
	    this.msg = msg;
  	}
  	
  	public Result(Object model){
  		this.model = model;
  	}
  	
  	public Result(int result, Object model){
  		this.result = result;
  		this.model = model;
  	}
  	
  	public Result(int result, int code, String msg){
  		this.result = result;
  		this.code = code;
	    this.msg = msg;
  	}
  	
	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public String toString(){
	    return JSONObject.fromObject(this).toString();
	}
}

package com.liuzi.activemq.boot;

import java.io.Serializable;


public class ProducerModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int result = 1;
	
	private int code;
	
	private String msg;
	
	private Object model;

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

	@Override
	public String toString() {
		return "ProducerModel [result=" + result + ", code=" + code + ", msg="
				+ msg + ", model=" + model + "]";
	}
	
	
	
}

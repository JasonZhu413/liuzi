package com.liuzi.util.common;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import lombok.Data;

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
@Data
public class Result implements Serializable{ 
	
	private static final long serialVersionUID = 1L;
	
	private static final int[] RESULTS = new int[]{0, -1, -2};
	private static final String[] CODES = new String[]{"SUCCESS", "ERROR", "WARN"};
	private static final String[] MSGS = new String[]{"成功", "失败", "警告"};
	
	/**
	 * 标识 0(成功) -1(失败) -2(警告)
	 */
	private int result;
	
	/**
	 * 代码  SUCCESS(result=0) ERROR(result=-1) WARN(result=-2)
	 */
	private String code;
	
	/**
	 * 提示信息 成功(result=0) 失败(result=-1) 警告(result=-2)
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
	
	public static Result success(){
		return success(null, null);
	}
	
	public static Result success(String msg){
		return success(msg, null);
	}
	
	public static Result success(Object data){
		return success(null, data);
	}

	public static Result success(String msg, Object data){
		Result result = new Result();
		result.setResult(RESULTS[0]);
		result.setCode(CODES[0]);
		result.setMsg(StringUtils.isEmpty(msg) ? MSGS[0] : msg);
		result.setData(data);
		return result;
	}
	
	public static Result error(){
		return error(null, null, null, null);
	}
	
	public static Result error(String errorMsg){
		return error(null, null, errorMsg, null);
	}
	
	public static Result error(Object data){
		return error(null, null, null, data);
	}
	
	public static Result error(String errorCode, String errorMsg){
		return error(errorCode, null, errorMsg, null);
	}
	
	public static Result error(String errorCode, String errorMsg, Object data){
		return error(errorCode, null, errorMsg, data);
	}
	
	public static Result error(String errorCode, String errorEngCode,
			String errorMsg){
		return error(errorCode, errorEngCode, errorMsg, null);
	}
	
	public static Result error(String errorCode, String errorEngCode,
			String errorMsg, Object data){
		Result result = new Result();
		result.setResult(RESULTS[1]);
		result.setCode(CODES[1]);
		result.setMsg(MSGS[1]);
		result.setErrorCode(errorCode);
		result.setErrorEngCode(errorEngCode);
		result.setErrorMsg(errorMsg);
		result.setData(data);
		return result;
	}
	
	public static Result warn(){
		return warn(null, null);
	}
	
	public static Result warn(String msg){
		return warn(msg, null);
	}
	
	public static Result warn(Object data){
		return warn(null, data);
	}

	public static Result warn(String msg, Object data){
		Result result = new Result();
		result.setResult(RESULTS[2]);
		result.setCode(CODES[2]);
		result.setMsg(StringUtils.isEmpty(msg) ? MSGS[2] : msg);
		result.setData(data);
		return result;
	}
	
	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("Result{\n");
		sbf.append(" result: " + this.getResult() + ",\n");
		sbf.append(" code: " + this.getCode() + ",\n");
		sbf.append(" msg: " + this.getMsg() + ",\n");
		sbf.append(" errorCode: " + this.getErrorCode() + ",\n");
		sbf.append(" errorEngCode: " + this.getErrorEngCode() + ",\n");
		sbf.append(" errorMsg: " + this.getErrorMsg() + ",\n");
		sbf.append(" data: " + this.getData() + "\n");
		sbf.append("}");
        return sbf.toString();
    }
}

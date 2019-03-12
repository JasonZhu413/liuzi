package com.liuzi.util.excel;

import java.util.List;

import org.springframework.util.StringUtils;

import com.liuzi.util.common.Result;

public class ExcelImportCallBack{

	public Result handler(List<Object> list) throws Exception{
		try {
			throw new IllegalArgumentException(">>> WARN!!! Excel导入, new ExcelCallBack未覆盖handler函数");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.error("-1", "Excel导入, new ExcelCallBack未覆盖handler函数");
	}
	
	public String getString(Object obj) {
		return StringUtils.isEmpty(obj) ? null : obj.toString();
	}

	public Integer getInt(Object obj) {
		return StringUtils.isEmpty(obj) ? null : Integer.parseInt(obj.toString());
	}
	
	public Long getLong(Object obj) {
		return StringUtils.isEmpty(obj) ? null : Long.parseLong(obj.toString());
	}

	public Double getDouble(Object obj) {
		return StringUtils.isEmpty(obj) ? null : Double.parseDouble(obj.toString());
	}

	public Float getFloat(Object obj) {
		return StringUtils.isEmpty(obj) ? null : Float.parseFloat(obj.toString());
	}

	public Boolean getBoolean(Object obj) {
		return StringUtils.isEmpty(obj) ? null : Boolean.parseBoolean(obj.toString());
	}
}

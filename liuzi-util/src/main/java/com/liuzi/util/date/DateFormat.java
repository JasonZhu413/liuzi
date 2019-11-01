package com.liuzi.util.date;


public enum DateFormat {
	yy("yy"),
	yyyy("yyyy"),
	MM("MM"),
	dd("dd"),
	yyyyMMdd("yyyyMMdd"),
	yyyyMMdd_by_hl("yyyy-MM-dd"),
	yyyyMMdd_by_ol("yyyy/MM/dd"),
	HHmmss("HHmmss"),
	HHmmss_by_symbol("HH:mm:ss"),
	HHmmssSSS("HHmmssSSS"),
	HHmmssSSS_by_symbol("HH:mm:ss.SSS"),
	yyyyMMddHHmmss("yyyyMMddHHmmss"),
	yyyyMMddHHmmss_by_hl("yyyy-MM-dd HH:mm:ss"),
	yyyyMMddHHmmss_by_ol("yyyy/MM/dd HH:mm:ss"),
	yyyyMMddHHmmssSSS("yyyyMMddHHmmssSSS"),
	yyyyMMddHHmmssSSS_by_hl("yyyy-MM-dd HH:mm:ss.SSS"),
	yyyyMMddHHmmssSSS_by_ol("yyyy/MM/dd HH:mm:ss.SSS"),
	;
	
	private String format;
	
	private DateFormat(String format){
		this.format = format;
	}
	
	@Override
	public String toString() {
		return this.format;
	}
}

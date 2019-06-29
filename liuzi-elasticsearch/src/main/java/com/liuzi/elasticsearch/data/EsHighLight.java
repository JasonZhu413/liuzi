package com.liuzi.elasticsearch.data;

import lombok.Data;

@Data
public class EsHighLight {

	private String[] name;
	
	private String type;

	public EsHighLight() {}
	
	public EsHighLight(String... name) {
		this.name = name;
	}
	
	public EsHighLight(String type, String... name) {
		this.name = name;
		this.type = type;
	}
	
}

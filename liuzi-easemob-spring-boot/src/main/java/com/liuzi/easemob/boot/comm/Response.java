package com.liuzi.easemob.boot.comm;


import java.io.Serializable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Response implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String action;
	private String application;
	private JSONObject params;
	private String path;
	private String uri;
	private JSONArray entities;
	private Object data;
  	private Long timestamp;
  	private Integer duration;
  	private String organization;
  	private String applicationName;
  	private String cursor;
  	private Integer count;
  	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public JSONArray getEntities() {
		return entities;
	}
	public void setEntities(JSONArray entities) {
		this.entities = entities;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getCursor() {
		return cursor;
	}
	public void setCursor(String cursor) {
		this.cursor = cursor;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
	    sb.append("class Response {\n");
	    sb.append("    action: ").append(toIndentedString(action)).append("\n");
	    sb.append("    application: ").append(toIndentedString(application)).append("\n");
	    sb.append("    params: ").append(toIndentedString(params)).append("\n");
	    sb.append("    path: ").append(toIndentedString(path)).append("\n");
	    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
	    sb.append("    entities: ").append(toIndentedString(entities)).append("\n");
	    sb.append("    data: ").append(toIndentedString(data)).append("\n");
	    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
	    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
	    sb.append("    organization: ").append(toIndentedString(organization)).append("\n");
	    sb.append("    applicationName: ").append(toIndentedString(applicationName)).append("\n");
	    sb.append("    cursor: ").append(toIndentedString(cursor)).append("\n");
	    sb.append("    count: ").append(toIndentedString(count)).append("\n");
	    sb.append("}");
	    return sb.toString();
	}
	
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

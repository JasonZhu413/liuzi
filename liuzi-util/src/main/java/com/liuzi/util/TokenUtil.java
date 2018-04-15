package com.liuzi.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;


/**
 * @Title:        TokenUtil
 * 
 * @Description:  TODO
 * 
 * @author        ZhuShiyao
 * 
 * @Date          2017年3月30日 下午11:51:06
 * 
 * @version       1.0
 * 
 */
public class TokenUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final long timeout = ConfigUtil.getIntValue("api.token.timeout");

	  private Map<String, Object> attribute = new HashMap<String, Object>();
	  private String token;
	  private Integer uid;
	  private String tokenKey;
	  private Long lastLoginTime;

	  public TokenUtil(){}

	  public TokenUtil(Integer uid){
		  this.uid = uid;
		  this.token = MD5.crypt(System.currentTimeMillis() + 
	      StringUtil.randomString(8));
		  this.token = MD5.crypt(this.token + uid);
		  this.tokenKey = "token:" + this.token;
	  }

	  public Object getAttributeKey(String key){
		  return this.attribute.get(key);
	  }

	  public void setAttribute(String key, Object value) {
		  this.attribute.put(key, value);
	  }

	  public void removeAttribute(String key) {
		  this.attribute.remove(key);
	  }

	  public String getToken() {
		  return this.token;
	  }

	  public void setToken(String token) {
		  this.token = token;
	  }

	  public Integer getUid() {
		  return this.uid;
	  }

	  public void setUid(Integer uid) {
		  this.uid = uid;
	  }

	  public String getTokenKey() {
		  return this.tokenKey;
	  }

	  public void setTokenKey(String tokenKey) {
		  this.tokenKey = tokenKey;
	  }

	  public static long getTimeout() {
		  return timeout;
	  }

	  public Map<String, Object> getAttribute() {
		  return this.attribute;
	  }

	  public Long getLastLoginTime(){
		  return this.lastLoginTime;
	  }

	  public void setLastLoginTime(Long lastLoginTime) {
		  this.lastLoginTime = lastLoginTime;
	  }

	  public String toString(){
		  return JSONObject.fromObject(this).toString();
	  }

	  @SuppressWarnings("unchecked")
	public static TokenUtil getJsonToToken(Object obj) {
		  JSONObject json = JSONObject.fromObject(obj);
		  TokenUtil token = (TokenUtil) JSONObject.toBean(json, TokenUtil.class);
		  Map<String,Object> attribute = token.getAttribute();
		  JSONObject smsRandCode = (JSONObject) attribute.get("smsRandCode");
		  if (smsRandCode != null) {
			  Map<String,Object> map = new HashMap<String,Object>();
			  Iterator<Object> it = smsRandCode.keys();
			  while (it.hasNext()) {
				  String key = String.valueOf(it.next());
				  Object value = smsRandCode.get(key);
				  map.put(key, value);
			  }
			  token.setAttribute("smsRandCode", map);
		  }
		  return token; 
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
	    Map<String,Object> map = new HashMap<String,Object>();

	    map.put("phone", Long.valueOf(15810198225L));
	    map.put("randCode", "448868");
	    map.put("smsCodeId", "60");
	    map.put("type", Integer.valueOf(1));
	    map.put("time", Long.valueOf(System.currentTimeMillis()));
	    TokenUtil t = new TokenUtil();

	    t.setToken("62c61682c6fe67cb462a611e9e573d00");
	    t.setTokenKey("token:62c61682c6fe67cb462a611e9e573d00");
	    t.setUid(Integer.valueOf(-1));
	    JSONObject json = JSONObject.fromObject(t.toString());
	    TokenUtil token = (TokenUtil)JSONObject.toBean(json, TokenUtil.class);
	    JSONObject attribute = (JSONObject)json.get("attribute");
	    JSONObject smsRandCode = (JSONObject)attribute.get("smsRandCode");
	    if (smsRandCode != null) {
	    	Map<String,Object> map2 = new HashMap<String,Object>();
	    	Iterator<Object> it = smsRandCode.keys();
	    	while (it.hasNext()) {
	    		String key = String.valueOf(it.next());
	    		Object value = smsRandCode.get(key);
	    		map2.put(key, value);
	    	}
	    	token.setAttribute("smsRandCode", map2);
	    }
	    System.out.println(token.toString());
	}
}

package com.liuzi.util.baidu;


import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.http.AipRequest;
import com.baidu.aip.http.EBodyFormat;
import com.baidu.aip.http.Headers;
import com.baidu.aip.http.HttpContentType;
import com.liuzi.util.baidu.result.Pureland;
import com.liuzi.util.baidu.result.Spam;
import com.liuzi.util.common.Log;


/**
 * 微信公众号工具类
 * @author zsy
 */
public class BaiduClient extends AipContentCensor{
	
	public BaiduClient(String appId, String apiKey, String secretKey){
		super(appId, apiKey, secretKey);
		this.setConnectionTimeoutInMillis(BaiduConsts.DEFAULT_CTIM);
		this.setSocketTimeoutInMillis(BaiduConsts.DEFAULT_STIM);
	}
	
	public Spam spam(String content, HashMap<String, String> options){
		JSONObject json = super.antiSpam(content, options);
		
		Spam spam = new Spam();
		
    	if(json.has("error_code")){
    		spam.setErrorCode(json.getInt("error_code"));
    		spam.setErrorMsg(json.getString("error_msg"));
    		return spam;
    	}
    	
    	spam.setLogId(json.getLong("log_id"));
    	
    	JSONObject jsonResult = json.getJSONObject("result");
    	
    	Spam.Result result = new Spam.Result();
    	result.setSpam(jsonResult.getInt("spam"));
    	result.setReject(jsonResult.getJSONArray("reject"));
    	result.setReview(jsonResult.getJSONArray("review"));
    	result.setPass(jsonResult.getJSONArray("pass"));
    	
    	spam.setResult(result);
    	
        return spam;
    }
	

	public Pureland pureland(String text){
		Pureland pureland = new Pureland();
		if(StringUtils.isBlank(text)){
			pureland.setErrorCode(-1);
			pureland.setErrorMsg("内容不存在");
			return pureland;
		}
		
		if(text.length() > BaiduConsts.PURELAND_TEXT_MAX_LENGTH){
			pureland.setErrorCode(-2);
			pureland.setErrorMsg("文字字数超过限制");
			return pureland;
		}
		
		AipRequest request = new AipRequest();
		preOperation(request);
		
		request.addHeader(Headers.CONTENT_TYPE, HttpContentType.JSON_DATA);
	    request.setContentEncoding("UTF-8");
		try {
			//request.addBody("text", text);
			request.addBody("text", new String(text.getBytes("utf-8")));
		} catch (Exception e) {
			Log.error(e, "pureland param encode trans error");
		}
	    request.setBodyFormat(EBodyFormat.RAW_JSON);
        request.setUri(BaiduConsts.PURELAND_URL);
        
        postOperation(request);
        JSONObject json = requestServer(request);
        
    	if(json.has("error_code")){
    		pureland.setErrorCode(json.getInt("error_code"));
    		pureland.setErrorMsg(json.getString("error_msg"));
    		return pureland;
    	}
    	
    	pureland.setLogId(json.getLong("log_id"));
    	
    	JSONArray itemsJson = json.getJSONArray("items");
    	int length = itemsJson.length();
    	
    	Pureland.Items[] itemss = new Pureland.Items[length];
    	for(int i = 0; i < length; i ++){
    		JSONObject itemJson = new JSONObject(itemsJson.get(i));
    		Pureland.Items items = new Pureland.Items();
    		items.setGrade(itemJson.getInt("grade"));
    		items.setScore(Float.parseFloat(itemJson.getString("score")));
    		items.setLabel(itemJson.getInt("label"));
    		items.setHits(itemJson.getInt("hits"));
    		itemss[i] = items;
    	}
        return pureland;
    }
	
    public static void main(String[] args) throws Exception {
    	String appId = "17530710";
    	String apiKey = "a8uCKZBawEPlag3HFrZ9Vtzl";
    	String secretKey = "Ijh3VkiLwFsbOlYS2hRqiGFsmLVs6qYx";
    	BaiduClient client = new BaiduClient(appId, apiKey, secretKey);
    	String text = "过了一会，刘豪撸下一点白洁上身的裙口，看她露出软柔的部分，" +
    			"刘豪意乱神迷的吸了上去。再过了一会，白洁也呼吸有些加促，她的手忍不" +
    			"住轻轻握住了刘豪的那东西。随着轻抚的动作，白洁的身上裙子被褪了下来。";
    	System.out.println(client.pureland(text).toString());
	}
}

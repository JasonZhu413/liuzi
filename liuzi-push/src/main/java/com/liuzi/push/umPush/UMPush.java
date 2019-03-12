package com.liuzi.push.umPush;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import push.PushClient;
import push.android.AndroidBroadcast;
import push.android.AndroidCustomizedcast;
import push.ios.IOSBroadcast;
import push.ios.IOSCustomizedcast;


@Slf4j
@Service("uMPush")
public class UMPush{
	
	@Autowired
	private PushClient client;
	@Autowired
	private IOSBroadcast iOSBroadcast;
	@Autowired
	private IOSCustomizedcast iOSCustomizedcast;
	@Autowired
	private AndroidBroadcast androidBroadcast;
	@Autowired
	private AndroidCustomizedcast androidCustomizedcast;
	
	
	/**
	 * 向所有用户发送
	 * 
	 * @param title 消息标题
	 * @param description 消息描述
	 * @param badge 红点数
	 * @param skip_type 消息类型 1-自定义行为 2-打开URL 3-打开App
     * @param skip_link      消息链接
	 * @param map 自定义参数 
	 * @param type 0-所有 1-android 2-ios
	 */
	public void sendToAll(String title, String description, int badge, int skip_type, 
			String skip_link, Map<String, Object> map, int type){
		if(type != 1){
			iosToAll(title, description, badge, skip_type, skip_link, map);
		}
		if(type != 2){
			androidToAll(title, description, badge, skip_type, skip_link, map);
		}
	}
	
	/**
	 * 指定alias推送
	 * 
	 * @param title 消息标题
	 * @param description 消息描述
	 * @param badge 红点数
	 * @param alias aliasList 指定alias
	 * @param aliasType alias类型
	 * @param skip_type 消息类型 1-自定义行为 2-打开URL 3-打开App
     * @param skip_link      消息链接
	 * @param map 自定义参数
	 * @param type 0-所有 1-android 2-ios
	 */
	public void sendToAlias(String title, String description, int badge, List<String> alias,
			String aliasType, int skip_type, String skip_link, Map<String, Object> map, int type){
		if(type != 1){
			iosToAlias(title, description, badge, alias, aliasType, skip_type, skip_link, map);
		}
		if(type != 2){
			androidToAlias(title, description, badge, alias, aliasType, skip_type, skip_link, map);
		}
	}
	
	
	//ios
	public void iosToAll(String title, String description, int badge, int skip_type, 
			String skip_link, Map<String, Object> map){
		try {
			log.info("-----         UMeng push to all(IOS) start         -----");
			long start = System.currentTimeMillis();
			
			System.out.println("   IOSBroadcast create start ......");
			
			iOSBroadcast.setDescription(description);
			iOSBroadcast.setAlert(title);
			iOSBroadcast.setBadge(badge);
			iOSBroadcast.setSound("default");
			
			if(UMPushConfig.production){
				iOSBroadcast.setProductionMode();
			}else{
				iOSBroadcast.setTestMode();
			}
			
			Map<String, Object> thisMap = map;
	        if(map == null){
	        	thisMap = new HashMap<>();
	        }
	        thisMap.put("skip_type", skip_type);
	        thisMap.put("skip_link", skip_link);
	        iOSBroadcast.setCustomizedField("data", JSONObject.fromObject(thisMap).toString());
			
			System.out.println("   IOSBroadcast create end, push start ......");
			
			boolean result = client.send(iOSBroadcast);
			System.out.println("   !!!Result: " + result);
			
			long end = System.currentTimeMillis();
            System.out.println("  ----- UMeng push to all(IOS) end, use " + (end - start) + " millisecond, stop connection -----");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//ios
	public void iosToAlias(String title, String description, int badge, List<String> alias,
			String aliasType, int skip_type, String skip_link, Map<String, Object> map){
		try {
			log.info("-----         UMeng push to alias(IOS) start         -----");
			long start = System.currentTimeMillis();
			
			System.out.println("   IOSCustomizedcast create start ......");
			
			iOSCustomizedcast.setDescription(description);
			StringBuffer sbf = new StringBuffer();
			for(String als : alias){
				sbf.append(als + ",");
			}
			int length = sbf.length();
			if(length > 0){
				sbf.deleteCharAt(length - 1);
				iOSCustomizedcast.setAlias(sbf.toString(), aliasType);
			}
			iOSCustomizedcast.setAlert(title);
			iOSCustomizedcast.setBadge(badge);
			iOSCustomizedcast.setSound("default");
			
			if(UMPushConfig.production){
				iOSCustomizedcast.setProductionMode();
			}else{
				iOSCustomizedcast.setTestMode();
			}
			
			Map<String, Object> thisMap = map;
	        if(map == null){
	        	thisMap = new HashMap<>();
	        }
	        thisMap.put("skip_type", skip_type);
	        thisMap.put("skip_link", skip_link);
	        iOSCustomizedcast.setCustomizedField("data", JSONObject.fromObject(thisMap).toString());
	        
	        System.out.println("   IOSCustomizedcast create end, push start ......");
			
			boolean result = client.send(iOSCustomizedcast);
			System.out.println("   !!!Result: " + result);
			
			long end = System.currentTimeMillis();
            System.out.println("  ----- UMeng push to alias(IOS) end, use " + (end - start) + " millisecond, stop connection -----");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//android
	public void androidToAll(String title, String description, int badge, int skip_type, 
			String skip_link, Map<String, Object> map){
		try {
			log.info("-----         UMeng push to all(Android) start         -----");
			long start = System.currentTimeMillis();
			
			System.out.println("   AndroidBroadcast create start ......");
			
			androidBroadcast.setTitle(title);
			androidBroadcast.setDescription(description);
			
			if(UMPushConfig.production){
				androidBroadcast.setProductionMode();
			}else{
				androidBroadcast.setTestMode();
			}
			
			Map<String, Object> thisMap = map;
	        if(map == null){
	        	thisMap = new HashMap<>();
	        }
	        thisMap.put("skip_type", skip_type);
	        thisMap.put("skip_link", skip_link);
	        androidBroadcast.setExtraField("data", JSONObject.fromObject(thisMap).toString());
			
			//broadcast.setTicker("Android broadcast ticker");
			//broadcast.setText("Android broadcast text");
			//broadcast.setExpireTime("2018-03-18 21:17:58");
			//broadcast.goAppAfterOpen();
			//broadcast.setDisplayType(AndroidNotification.DisplayType.MESSAGE);
			System.out.println("   AndroidBroadcast create end, push start ......");
			
			boolean result = client.send(androidBroadcast);
			System.out.println("   !!!Result: " + result);
			
			long end = System.currentTimeMillis();
            System.out.println("  ----- UMeng push to all(Android) end, use " + (end - start) + " millisecond, stop connection -----");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void androidToAlias(String title, String description, int badge, List<String> alias,
			String aliasType, int skip_type, String skip_link, Map<String, Object> map){
		try{
			log.info("-----         UMeng push to alias(Android) start         -----");
			long start = System.currentTimeMillis();
			
			System.out.println("   AndroidCustomizedcast create start ......");
			
			androidCustomizedcast.setDescription(description);
			StringBuffer sbf = new StringBuffer();
			for(String als : alias){
				sbf.append(als + ",");
			}
			int length = sbf.length();
			if(length > 0){
				sbf.deleteCharAt(length - 1);
				androidCustomizedcast.setAlias(sbf.toString(), aliasType);
			}
			
			//customizedcast.setText("Android customizedcast text");
			//customizedcast.setTicker("Android customizedcast ticker");
			//customizedcast.setPlayVibrate(false);
			//customizedcast.setPlayLights(false);
			//customizedcast.setPlaySound(true);
			//customizedcast.goAppAfterOpen();
			//customizedcast.setTitle(title);
			//customizedcast.setBuilderId(222);
			//customizedcast.setCustomField("custom");
			//customizedcast.setExpireTime("2018-05-14 21:17:58");
			//customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
			
			if(UMPushConfig.production){
				androidCustomizedcast.setProductionMode();
			}else{
				androidCustomizedcast.setTestMode();
			}
			
			Map<String, Object> thisMap = map;
	        if(map == null){
	        	thisMap = new HashMap<>();
	        }
	        thisMap.put("skip_type", skip_type);
	        thisMap.put("skip_link", skip_link);
	        androidCustomizedcast.setExtraField("data", JSONObject.fromObject(thisMap).toString());
			
	        System.out.println("   AndroidCustomizedcast create end, push start ......");
			
			boolean result = client.send(androidCustomizedcast);
			System.out.println("   !!!Result: " + result);
			
			long end = System.currentTimeMillis();
            System.out.println("  ----- UMeng push to alias(Android) end, use " + (end - start) + " millisecond, stop connection -----");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

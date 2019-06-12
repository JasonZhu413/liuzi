package com.liuzi.push.jgPush;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.PushPayload.Builder;


@Slf4j
public class JGPush{
	
	@Autowired
	private JPushClient jpushClient;
	
	/**
	 * 向所有用户发送
	 * @param alert 消息
	 * @param title 消息标题
	 * @param content 消息描述
	 * @param badge 红点数
	 * @param skip_type 消息类型 1-自定义行为 2-打开URL 3-打开App
     * @param skip_link      消息链接
	 * @param map 自定义参数
	 * @param type 0-所有 1-android 2-ios
	 */
	public void sendToAll(String alert, String title, String content, int badge,
			int skip_type, String skip_link, Map<String, Object> map, int type) {
		push_object(alert, title, content, badge, null, null, skip_type, skip_link, map, type);
    }
	
	/**
	 * 指定alias推送
	 * @param alert 消息
	 * @param title 消息标题
	 * @param content 消息描述
	 * @param badge 红点数
	 * @param aliasList 指定alias
	 * @param skip_type 消息类型 1-自定义行为 2-打开URL 3-打开App
     * @param skip_link      消息链接
	 * @param map 自定义参数
	 * @param type 0-所有 1-android 2-ios
	 */
	public void sendToAlias(String alert, String title, String content, int badge,
			List<String> alias, int skip_type, String skip_link, Map<String, Object> map, 
			int type) {
		push_object(alert, title, content, badge, null, alias, skip_type, skip_link, map, type);
	}
	
	/**
	 * 指定tags推送
	 * @param alert 消息
	 * @param title 消息标题
	 * @param content 消息描述
	 * @param badge 红点数
	 * @param tagsList 指定tags
	 * @param skip_type 消息类型 1-自定义行为 2-打开URL 3-打开App
     * @param skip_link 消息链接
	 * @param map 自定义参数
	 * @param type 0-所有 1-android 2-ios
	 */
	public void sendToTags(String alert, String title, String content, int badge, 
			List<String> tags, int skip_type, String skip_link, Map<String, Object> map, 
			int type) {
		push_object(alert, title, content, badge, tags, null, skip_type, skip_link, map, type);
    }
	
	private void push_object(String alert, String title, String content, int badge, 
			List<String> tags, List<String> alias, int skip_type, String skip_link, 
			Map<String, Object> map, int type){
		
			log.info("-----         Jiguang push start         -----");
			long start = System.currentTimeMillis();
			
			int typ = type != 1 && type != 2 ? 0 : type;
			String typeName = typ == 1 ? "Android" : (typ == 2 ? "IOS" : "All");
			System.out.println("   Builder Install start, typeName: " + typeName + " ......");
			
			Builder builder = PushPayload.newBuilder();
			if(typ == 1){
				builder.setPlatform(Platform.android());
			}else if(typ == 2){
				builder.setPlatform(Platform.ios());
			}else{
				builder.setPlatform(Platform.all());
			}
			
			System.out.println("   Alias/Tag set start, alias.size: + " + (alias == null ? 0 : 
				alias.size()) + ", tag.size: " + (tags == null ? 0 : tags.size()) + "......");
			
	        if (alias != null && alias.size() > 0) {
	            builder = builder.setAudience(Audience.alias(alias));
	        } else {
	            if (tags == null || tags.size() == 0){
	            	builder = builder.setAudience(Audience.all());
	            }else {
	                builder = builder.setAudience(Audience.tag(tags));
	            }
	        }
	        
	        if(map == null){
	        	map = new HashMap<String, Object>();
	        }
	        map.put("skip_type", skip_type);
	        map.put("skip_link", skip_link);
	        
	        System.out.println("   Alias/Tag set success, Message set start ......");
	        
	        builder.setMessage(Message.newBuilder()
	        		.setTitle(title)
	                .setMsgContent(content)
	                .addExtra("data", JSONObject.fromObject(map).toString())
	                .build());
	        
	        System.out.println("   Message set success, Options set start ......");
	        
	        builder.setOptions(Options.newBuilder()
	        		.setApnsProduction(JGPushConfig.production)
		    		.setTimeToLive(JGPushConfig.aliveTime) 
		    		.build());
	        
	        System.out.println("   Options set success, PlatformNotification add start ......");
	
	        cn.jpush.api.push.model.notification.Notification.Builder buder = 
	        		Notification.newBuilder().setAlert(alert);
	        
	        if(type != 1){
	        	buder.addPlatformNotification(IosNotification.newBuilder()
	            		.incrBadge(badge).setSound("default").build());
			}
	        if(type != 2){
	        	buder.addPlatformNotification(AndroidNotification.newBuilder().build());
			}
	        
	        System.out.println("   PlatformNotification add success, " + typeName + " push start ......");
	        
	        PushPayload payload = builder.setNotification(buder.build()).build();
	        try {
		    	PushResult result = jpushClient.sendPush(payload);
		    	System.out.println("   !!!Result: " + result);
		    	
				long end = System.currentTimeMillis();
	            System.out.println("  ----- Jiguang push end, use " + (end - start) + " millisecond, stop connection -----");
			} catch (Exception e) {
				e.printStackTrace();
				log.info("  ----- Jiguang push system error: " + e.getMessage());
			} 
	}
	
	public Date addMinutes2Date(Date date, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, num);
        return new Date(cal.getTime().getTime());
    }
}

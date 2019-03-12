package com.liuzi.push.xiaomiPush;


import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import com.xiaomi.xmpush.server.Sender.BROADCAST_TOPIC_OP;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Slf4j
@Service("xiaomiPush")
public class XiaomiPush {
	
	@Autowired
	private Sender xiaoMiSenderIOS;
	@Autowired
	private Sender xiaoMiSenderAndroid;
	
    /**
     * 向所有设备发送推送
     *
     * @param messagePayload 消息
     * @param title          消息标题
     * @param description    消息描述
     * @param skip_type       消息类型 1-自定义行为 2-打开URL 3-打开App
     * @param skip_link      消息链接
     * @param map      自定义参数
     * @param type      	0-所有 1-android 2-ios
     */
    public void sendToAll(String messagePayload, String title, String description, 
    		int skip_type, String skip_link, Map<String, Object> map, int type){
        try {
        	log.info("-----         XiaoMi push start         -----");
			long start = System.currentTimeMillis();
			
        	Message message = getMessage(messagePayload, title, description, skip_type, 
        			skip_link, map);
        	
        	if(type != 1){
        		System.out.println("   IOS push start ......");
	            
	            // 根据topicList做并集运算, 发送消息到指定一组设备上
	            Result iosResult = xiaoMiSenderIOS.broadcastAll(message, 3);
	            
	            System.out.println("   IOS push end, result: " + iosResult + " ......");
			}
	        if(type != 2){
	        	System.out.println("   Android push start ......");
	        	
	            // 根据topicList做并集运算, 发送消息到指定一组设备上
	            Result androidResult = xiaoMiSenderAndroid.broadcastAll(message, 3);
	            
	            System.out.println("   Android push end, result: " + androidResult + " ......");
			}
            
            long end = System.currentTimeMillis();
            System.out.println("  ----- Xiaomi push end, use " + (end - start) + " millisecond, stop connection -----");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("  ----- Xiaomi push(sendAll) system error: " + e.getMessage());
		}
    }
 
    /**
     * 指定标签推送(推送指定类型用户)
     *
     * @param messagePayload 消息
     * @param title          消息标题
     * @param description    消息描述
     * @param skip_type       消息类型 1-自定义行为 2-打开URL 3-打开App
     * @param skip_link      消息链接
     * @param map      自定义参数
     * @param topicList      指定推送类型
     * @param type      	0-所有 1-android 2-ios
     */
    public void sendToTopic(String messagePayload, String title, String description, 
    		int skip_type, String skip_link, Map<String, Object> map, List<String> topicList,
    		int type){
        try {
        	log.info("-----         XiaoMi push start         -----");
			long start = System.currentTimeMillis();
			
        	Message message = getMessage(messagePayload, title, description, skip_type, 
        			skip_link, map);
        	
        	if(type != 1){
        		System.out.println("   IOS push start ......");
	            
	            // 根据topicList做并集运算, 发送消息到指定一组设备上
	            Result iosResult = xiaoMiSenderIOS.multiTopicBroadcast(message, topicList, 
	            		BROADCAST_TOPIC_OP.UNION, 3);
	            
	            System.out.println("   IOS push end, result: " + iosResult + " ......");
			}
	        if(type != 2){
	        	System.out.println("   Android push start ......");
	        	
	            // 根据topicList做并集运算, 发送消息到指定一组设备上
	            Result androidResult = xiaoMiSenderAndroid.multiTopicBroadcast(message, topicList, 
	            		BROADCAST_TOPIC_OP.UNION, 3);
	     
	            System.out.println("   Android push end, result: " + androidResult + " ......");
			}
            
            long end = System.currentTimeMillis();
            System.out.println("  ----- Xiaomi push end, use " + (end - start) + " millisecond, stop connection -----");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("  ----- Xiaomi push(send) system error: " + e.getMessage());
		}
    }
 
    /**
     * 指定alias推送
     *
     * @param messagePayload 消息
     * @param title          消息标题
     * @param description    消息描述
     * @param skip_type      消息类型 1-自定义行为 2-打开URL 3-打开App
     * @param skip_link      消息链接
     * @param map      		 自定义参数
     * @param aliasList      指定alias
     * @param type      	0-所有 1-android 2-ios
     */
    public void sendToAlias(String messagePayload, String title, String description, 
    		int skip_type, String skip_link, Map<String, Object> map, List<String> aliasList,
    		int type){
        try {
        	log.info("-----         XiaoMi push(sendToAliases) start         -----");
			long start = System.currentTimeMillis();
			
        	Message message = getMessage(messagePayload, title, description, skip_type, 
        			skip_link, map);
        	
        	if(type != 1){
        		System.out.println("   IOS push start ......");
        		
		        Result iosResult = xiaoMiSenderIOS.sendToAlias(message, aliasList, 3);
		        
		        System.out.println("   IOS push end, result: " + iosResult + " ......");
			}
	        if(type != 2){
	        	System.out.println("   Android push start ......");
	        	
				Result androidResult = xiaoMiSenderAndroid.sendToAlias(message, aliasList, 3);
				
				System.out.println("   Android push end, result: " + androidResult + " ......");
			}
        	
	        long end = System.currentTimeMillis();
            System.out.println("  ----- Xiaomi push end, use " + (end - start) + " millisecond, stop connection -----");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("  ----- Xiaomi push(sendToAliases) system error: " + e.getMessage());
		}
    }
    
    private static Message getMessage(String messagePayload, String title, 
    		String description, int skip_type, String skip_link, 
    		Map<String, Object> map){
    	System.out.println("   Message Install start ......");
    	
    	Constants.useOfficial();
    	
    	if(map == null){
    		map = new HashMap<>();
    	}
    	map.put("skip_type", skip_type);
    	map.put("skip_link", skip_link);
    	
        Message message = new Message.Builder()
        		.title(title)
        		.description(description)
        		.payload(messagePayload)
                .extra("data", JSONObject.fromObject(map).toString())
                .restrictedPackageName(XiaomiPushConfig.package_name)
                .notifyType(1) //默认提示音提示
                .passThrough(0) 
            .build();
        
        System.out.println("   Message Install end ......");
        return message;
    }
}

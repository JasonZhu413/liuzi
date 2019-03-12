package com.liuzi.push.iosPush;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Slf4j
@Service("iOSPush")
public class IOSPush {
	
	@Autowired
	private AppleNotificationServer appleNotificationServer;
	
	/**
	 * 群发
	 * @param tokens 手机标志(必传)
	 * @param message 消息(必传)
	 * @param badge 红点数(必传)
	 * @param skip_type      消息类型 1-自定义行为 2-打开URL 3-打开App
     * @param skip_link      消息链接
	 * @param map 自定义参数
	 */
	public void send(List<String> tokens, String title, int badge,  
			Integer skip_type, String skip_link, Map<String, String> map){
		if(tokens == null || tokens.size() == 0){
			System.out.println("  ----- Params error, tokens size is zero, IOS push end");
			return;
		}
		if(StringUtils.isEmpty(title)){
			System.out.println("  ----- Params error, title is empty, IOS push end");
			return;
		}
		
		try {
			log.info("-----         IOS push start         -----");
			long start = System.currentTimeMillis();
			
			System.out.println("   Create Apple connection...");
			
			PushNotificationManager pushManager = new PushNotificationManager();
			pushManager.initializeConnection(appleNotificationServer);
			
			System.out.println("   Connection success, device tokens...");
			
			//组装tokens
			List<Device> device = new ArrayList<Device>();
            for (String token : tokens) {
                device.add(new BasicDevice(token));
            }
            
            System.out.println("   Device(" + device.size() + ") tokens success, install Payload...");
            
            Map<String, String> thisMap = map;
            if(thisMap == null){
            	thisMap = new HashMap<>();
            }
            thisMap.put("skip_type", skip_type + "");
            thisMap.put("skip_link", skip_link);
            
            //组装payload
            PushNotificationPayload payload = new PushNotificationPayload();
			payload.addAlert(title);
			payload.addBadge(badge);
			payload.addSound("default"); //默认提示音
			payload.addCustomDictionary("data", JSONObject.fromObject(map).toString());
			
			System.out.println("   Payload install success, push start...");
            
            //发送
            List<PushedNotification> notifications = pushManager.sendNotifications(payload, device);
			List<PushedNotification> failedNotifications = PushedNotification
                    .findFailedNotifications(notifications);
            List<PushedNotification> successfulNotifications = PushedNotification
                    .findSuccessfulNotifications(notifications);
            int failed = failedNotifications.size();
            int successful = successfulNotifications.size();
            if (successful > 0 && failed == 0) {
            	System.out.println("   !!!Success, all msg push success, count:" + successful + " ...");
            } else if (successful == 0 && failed > 0) {
            	System.out.println("   !!!Failed, all msg push failed, count:" + failed + " ...");
            } else if (successful == 0 && failed == 0) {
            	System.out.println("   !!!Error, no notifications could be sent...");
            } else {
            	System.out.println("   !!!Success, success count:" + successful + ", failed count:" + failed + " ...");
            }
            
            long end = System.currentTimeMillis();
            System.out.println("  ----- IOS push end, use " + (end - start) + " millisecond, stop connection -----");
            //断开连接
            pushManager.stopConnection();
		} catch (Exception e) {
			e.printStackTrace();
			log.info("  ----- IOS push system error: " + e.getMessage());
		}
	}
}

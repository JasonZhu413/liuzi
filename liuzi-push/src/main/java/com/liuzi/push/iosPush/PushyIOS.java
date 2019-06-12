package com.liuzi.push.iosPush;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;


@Slf4j
public class PushyIOS{
	
	/**
	 * 限流
	 */
	private static final Semaphore semaphore = new Semaphore(10000);
	@Autowired
    private ApnsClient apnsClient;
	
	@SuppressWarnings("rawtypes")
	public void push(String title, String content, List<String> tokens, Map<String, String> extras) {
		log.info("-----         IOS推送开始         -----");
		
        int total = tokens.size();
        final CountDownLatch latch = new CountDownLatch(total);
        final AtomicLong successCnt = new AtomicLong(0);

        ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
        payloadBuilder.setSound("default");
        payloadBuilder.setBadgeNumber(1);
        payloadBuilder.setAlertBody(content);
        payloadBuilder.setAlertTitle(title);
        for(Entry<String, String> entry : extras.entrySet()){
        	payloadBuilder.addCustomProperty(entry.getKey(), entry.getValue());
		}
        String payload = payloadBuilder.buildWithDefaultMaximumLength();
        
        System.out.println("   组装参数成功，长度: " + payload.getBytes().length +
        		"，内容：" + payload + " ...");
        
        try{
        	System.out.println("   开始发送...");
        	for (String deviceToken : tokens) {
                final String token = TokenUtil.sanitizeTokenString(deviceToken);
                SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, 
                		PushyIOSConfig.pkage, payload);
                
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    log.error("ios push get semaphore failed, deviceToken:{}", deviceToken);
                    e.printStackTrace();
                }
                final Future<PushNotificationResponse<SimpleApnsPushNotification>> future = apnsClient.sendNotification(pushNotification);

                future.addListener(new GenericFutureListener<Future<PushNotificationResponse>>() {
                    @Override
                    public void operationComplete(Future<PushNotificationResponse> pushNotificationResponseFuture) throws Exception {
                        if (future.isSuccess()) {
                            final PushNotificationResponse<SimpleApnsPushNotification> response = future.getNow();
                            if (response.isAccepted()) {
                                successCnt.incrementAndGet();
                            } else {
                                Date invalidTime = response.getTokenInvalidationTimestamp();
                                log.error("Notification rejected by the APNs gateway: " + response.getRejectionReason());
                                if (invalidTime != null) {
                                    log.error("\t…and the token is invalid as of " + response.getTokenInvalidationTimestamp());
                                }
                            }
                        } else {
                            log.error("send notification device token={} is failed {} ", token, future.cause().getMessage());
                        }
                        latch.countDown();
                        semaphore.release();
                    }
                });
            }
        	
        	latch.await(20, TimeUnit.SECONDS);
        	
        	long success = successCnt.get();
        	log.info("IOS 推送成功，[共推送" + total + "个][成功" + (success) + "个]");
        } catch(Exception e){
        	log.error("发送失败：" + e.getMessage()); 
        }
    }
}

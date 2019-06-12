package com.liuzi.push.huaweiPush;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.geesanke.plugin.huawei.push.SendClient;
import com.geesanke.plugin.huawei.push.model.Ext;
import com.geesanke.plugin.huawei.push.model.Message;
import com.geesanke.plugin.huawei.push.model.Message.MessageBuilder;
import com.geesanke.plugin.huawei.push.model.Payload;
import com.geesanke.plugin.huawei.push.model.Result;
import com.geesanke.plugin.huawei.push.model.Ext.ExtBuilder;
import com.geesanke.plugin.huawei.push.model.enums.ActionType;
import com.geesanke.plugin.huawei.push.model.enums.MessageType;


@Slf4j
public class HuaweiPush {
	
	@Autowired
	private SendClient sendClient;
	
	/**
	 * 华为群发(android)
	 * @param tokens 手机标志(必传)
	 * @param title 消息(必传)
	 * @param skip_type      消息类型 1-自定义行为 2-打开URL 3-打开App
     * @param skip_link      消息链接
	 * @param map 自定义参数
	 */
	public void send(List<String> tokens, String title, String content, ActionType skip_type, 
			String skip_link, Map<String, String> map) {
		if(tokens == null || tokens.size() == 0){
			System.out.println("  ----- Params error, tokens size is zero, Huawei(android) push end");
			return;
		}
		if(StringUtils.isEmpty(title)){
			System.out.println("  ----- Params error, title is empty, Huawei(android) push end");
			return;
		}
		
		log.info("-----         Huawei(android) push start         -----");
		long start = System.currentTimeMillis();
		
		System.out.println("   Create MessageBuilder -> Message, skip_type: " + 
				skip_type + ", skip_link: " + skip_link + " ......");
		
		MessageBuilder messageBuilder = Message.newInstance()
				.addType(MessageType.PASSTHROUGH)
				.addBody(content, title);
				
		if(!StringUtils.isEmpty(skip_link)){
			ActionType type = StringUtils.isEmpty(skip_type) ? ActionType.INTENT : skip_type;
			messageBuilder = messageBuilder.addAction(type, skip_link);
		}
		
		Message msg = messageBuilder.build();
			
		System.out.println("   Create Message success, Create ExtBuilder -> Ext...");
		
		ExtBuilder extBuilder = Ext.newInstance();
		
		//组装自定义
		if(map != null && !map.isEmpty()){
			extBuilder = extBuilder.addCustomize(map);
		}
		
		Ext ext = extBuilder.build();
		
		System.out.println("   Create Ext success, install Payload...");
		
		Payload payload = Payload.newInstance().addMsg(msg).addExt(ext).build();
		
		System.out.println("   Payload install success, push start...");
		

		int length = tokens.size();
		String[] strs = new String[length];
		tokens.toArray(strs);
		
		try {	
			if(length <= 1000){
				push(strs, payload, 1);
			}else{
				int max = 1000;
				int times = length / max;
				
				System.out.println("   !!!token.length > 1000, need push times: " + 
						(times + 1) + "...");
				
				for(int i = 0, e = times + 1; i < e; i ++){
					System.out.println("   " + (i + 1) + " times push start ......");
					
					int index = i * max;
					int offset = (i + 1) * max;
					if(i == times){
						offset = length;
					}
					String[] xh = Arrays.copyOfRange(strs, index, offset);
					push(xh, payload, i + 1);
				}
			}
			
			long end = System.currentTimeMillis();
            System.out.println("  ----- Huawei(android) push end, use " + (end - start) + " millisecond, stop connection -----");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("  ----- Huawei(android) push system error: " + e.getMessage());
		}
    }
	
	//推送
	private void push(String[] tokens, Payload payload, int times){
		Result result = sendClient.push(tokens, payload);
		System.out.println("   !!!Result(" + times + "): " + result);
	}
}

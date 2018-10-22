package com.liuzi.jPush.boot.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.liuzi.jPush.boot.JPushConfig;
import com.liuzi.jPush.boot.JPushModel;
import com.liuzi.jPush.boot.service.JPushService;
import com.liuzi.util.DateUtil;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
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

@Service("jPushService")
public class JPushServiceImpl implements JPushService{
	
	private static Logger logger = LoggerFactory.getLogger(JPushServiceImpl.class);
	
	@Resource
	private JPushClient jpushClient;
	
	@Override
	public void push(JPushModel model) {
		push_object(model, "all");
    }
	
	@Override
	public void pushAndroid(JPushModel model) {
		push_object(model, "android");
	}
	
	@Override
	public void pushIOS(JPushModel model) {
		push_object(model, "ios");
    }
	
	private void push_object(JPushModel model, String typ){
		Builder builder = PushPayload.newBuilder();
		if("android".equals(typ)){
			builder.setPlatform(Platform.android());
		}else if("ios".equals(typ)){
			builder.setPlatform(Platform.ios());
		}else{
			builder.setPlatform(Platform.all());
		}

        if (model.getAlias() != null && !"".equals(model.getAlias())) {
            builder = builder.setAudience(Audience.alias(model.getAlias()));
        } else {
            if (model.getTag() == null || "all".equals(model.getTag())){
            	builder = builder.setAudience(Audience.all());
            }else {
            	String[] tagArr = model.getTag().split(",");
                List<String> tagList=new ArrayList<String>();
                for(int i=0;tagArr!=null&&i<tagArr.length;i++){
                	tagList.add(tagArr[i]);
                }
                builder = builder.setAudience(Audience.tag(tagList));
            }
        }
        
        builder.setMessage(Message.newBuilder()
                .setMsgContent(model.getMsg())
                .addExtra("extra", model.getExtra())
                .setTitle(model.getTitle())
                .build());

        builder.setOptions(Options.newBuilder()
        		.setApnsProduction(JPushConfig.g_apns) //APNS推送环境是否生产
	    		.setTimeToLive(JPushConfig.g_alive_time) //过期时间
	    		.build());

        cn.jpush.api.push.model.notification.Notification.Builder buder = 
        		Notification.newBuilder().setAlert(model.getAlert());
        
        if(typ == null || "android".equals(typ) || "all".equals(typ)){
        	buder.addPlatformNotification(AndroidNotification.newBuilder().build());
		}
        
        if(typ == null || "ios".equals(typ) || "all".equals(typ)){
        	buder.addPlatformNotification(IosNotification.newBuilder()
            		.incrBadge(1).setSound("default").build());
		}
        
        PushPayload payload = builder.setNotification(buder.build()).build();
        try {
	    	PushResult result = jpushClient.sendPush(payload);
			
			logger.info("jPush result：" + result);
			
			model.setPushed(1);
			model.setMsgId(result.msg_id + "");
		} catch (APIConnectionException e) {
			e.printStackTrace();
			
			logger.info("Connection error. Should retry later. ");
			model.setErrCode("connectERR");
			model.setNextDate(addMinutes2Date(new Date(), 5)); 
		} catch (APIRequestException e) {
			e.printStackTrace();
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Msg ID: " + e.getMsgId() + " Error Code: " + e.getErrorCode() + " Error Message: " + e.getErrorMessage());
			model.setErrCode(e.getErrorCode() + "：" + e.getErrorMessage());
			model.setMsgId(e.getMsgId() + "");
			model.setNextDate(addMinutes2Date(new Date(), 5));
			model.setPushed(1);
		} 
	}
	
	public static Date addMinutes2Date(Date date, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, num);
        return new Date(cal.getTime().getTime());
    }
	
	public static void main(String[]args){
    	JPushModel model = new JPushModel();
    	model.setAlert("收到一条新消息");
    	model.setAlias("15210050811");
        
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "消息正文");
        map.put("seq", "000001");
        map.put("type", "UC_USERMSG");
        map.put("time", DateUtil.date2Str(new Date()));
        model.setMsg(new Gson().toJson(map));
        
    	//push(model);
    }
}

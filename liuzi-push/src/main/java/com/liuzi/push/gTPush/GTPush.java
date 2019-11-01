package com.liuzi.push.gTPush;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.Message;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.NotyPopLoadTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.liuzi.util.common.Log;


public class GTPush {
	
	@Autowired
	private IGtPush iGtPush;
	
	/**
	 * 向所有用户发送
	 * 
	 * @param title 消息标题
	 * @param content 消息描述
	 * @param skip_type 消息类型 1-自定义行为 2-打开URL 3-打开App
	 * @param skip_link 消息链接
	 * @param tags 标志
	 * @param map 自定义参数
	 */
	public void sendToAll(String title, String content, int skip_type, String skip_link, 
			List<String> tags, Map<String, Object> map){
		//log.info("-----         Getui push start         -----");
		long start = System.currentTimeMillis();
		
		//System.out.println("   IGtPush Create ......");
		
		//push = new IGtPush(host, appKey, secret);
		
		//System.out.println("   IGtPush Create success, AppMessage install ......");
		
		AppMessage message = new AppMessage();
		//选择模板
		getMessage(message, title, content, skip_type, skip_link, map);
        
		//配置applist
        List<String> appIdList = new ArrayList<>();
        appIdList.add(GTPushConfig.appId);
        message.setAppIdList(appIdList);
        
        //条件
        AppConditions cdt = new AppConditions();
        cdt.addCondition(AppConditions.TAG, tags);
        message.setConditions(cdt);
        //cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
        //cdt.addCondition(AppConditions.REGION, provinceList);
        
        //System.out.println("   AppMessage install success, push start ......");
        
        IPushResult ret = null;
        try {
	        //IPushResult ret = push.pushMessageToApp(message, "任务别名_toApp");
	        ret = iGtPush.pushMessageToApp(message);
		} catch (Exception e) {
			Log.error(e, "  ----- Getui push system error");
		}
        String result = null;
        if (ret != null) {
        	result = ret.getResponse().toString();
        }
        //System.out.println("   !!!Result: " + result);
        
        long end = System.currentTimeMillis();
        //System.out.println("  ----- Getui push end, use " + (end - start) + " millisecond, stop connection -----");
	}  
	
	/**
	 * 单个用户
	 * @param title 消息标题
	 * @param content 消息描述
	 * @param skip_type 消息类型 1-自定义行为 2-打开URL 3-打开App
	 * @param skip_link 消息链接
	 * @param clientId
	 * @param alias alias
	 * @param map 自定义参数
	 */
	public void sendToSingle(String title, String content, int skip_type, String skip_link, 
			String clientId, List<String> alias, Map<String, Object> map){ 
		//log.info("-----         Getui push start         -----");
		long start = System.currentTimeMillis();
		
		//System.out.println("   IGtPush Create ......");
		
		//System.out.println("   IGtPush Create success, SingleMessage install ......");
		
		SingleMessage message = new SingleMessage();
        
		//选择模板
		getMessage(message, title, content, skip_type, skip_link, map);
		
        Target target = new Target();
        target.setAppId(GTPushConfig.appId);
        target.setClientId(clientId);
        if(alias != null && alias.size() > 0){
        	StringBuffer sbf = new StringBuffer();
        	for(String als : alias){
        		sbf.append(als + ",");
	        }
        	sbf.deleteCharAt(sbf.length() - 1);
	        target.setAlias(sbf.toString());
        }
        
        //System.out.println("   AppMessage install success, push start ......");
        
        IPushResult ret = null;
        try {
            ret = iGtPush.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            Log.error(e, "  ----- Getui push system error, Push again");
            ret = iGtPush.pushMessageToSingle(message, target, e.getRequestId());
        }

        String result = null;
        if (ret != null) {
        	result = ret.getResponse().toString();
        }
        //System.out.println("   !!!Result: " + result);
        
        long end = System.currentTimeMillis();
        //System.out.println("  ----- Getui push end, use " + (end - start) + " millisecond, stop connection -----");
    }  
	
	/******************************************
	 * 模板
	 ******************************************/
	
	//1.点击通知打开应用
	public NotificationTemplate notificationTemplateDemo(Map<String, Object> map) {
		NotificationTemplate template = new NotificationTemplate();
        template.setAppId(GTPushConfig.appId);
        template.setAppkey(GTPushConfig.appKey);
        
        Style0 style = new Style0();
        style.setTitle(map.get("title").toString());
        style.setText(map.get("content").toString());
        template.setStyle(style);
        
	    // 配置通知栏图标
	    //template.setLogo("");
	    // 配置通知栏网络图标
	    //template.setLogoUrl("");
	    // 设置通知是否响铃，震动，或者可清除
	    //template.setIsRing(true);
	    //template.setIsVibrate(true);
	    //template.setIsClearable(true);
	    //透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	    //template.setTransmissionType(1);
	    //template.setTransmissionContent("请输入您要透传的内容");
	    return template;
	}
		
	//2. 点击通知打开网页
	public LinkTemplate linkTemplateDemo(Map<String, Object> map) {
        LinkTemplate template = new LinkTemplate();
        template.setAppId(GTPushConfig.appId);
        template.setAppkey(GTPushConfig.appKey);
        template.setUrl(map.get("skip_link") + "");// 设置打开的网址地址
        
        Style0 style = new Style0();
        style.setTitle(map.get("title").toString());
        style.setText(map.get("content").toString());
        template.setStyle(style);
       
        return template;
    }
	
	//3. 点击通知弹窗下载
	public NotyPopLoadTemplate notyPopLoadTemplateDemo(Map<String, Object> map) {
	    NotyPopLoadTemplate template = new NotyPopLoadTemplate();
	    // 设置APPID与APPKEY
	    template.setAppId(GTPushConfig.appId);
	    template.setAppkey(GTPushConfig.appKey);
	    
	    // 弹框标题与内容
	    template.setPopTitle(map.get("title").toString());
	    template.setPopContent(map.get("content").toString());
	    // 弹框显示的图片
	    template.setPopImage(map.get("popImage") + "");
	    template.setPopButton1("下载");
	    template.setPopButton2("取消");
	    // 下载标题
	    template.setLoadTitle(map.get("title") + "");
	    template.setLoadIcon(map.get("loadIcon") + "");
	    // 下载地址    
	    template.setLoadUrl(map.get("skip_link") + ""); 
	    
	    Style0 style = new Style0();
        style.setTitle(map.get("title").toString());
        style.setText(map.get("content").toString());
        template.setStyle(style);
        
	    return template;
	}
	
	//4. 透传消息
	public TransmissionTemplate transmissionTemplateDemo(Map<String, Object> map) {
	    TransmissionTemplate template = new TransmissionTemplate();
	    template.setAppId(GTPushConfig.appId);
	    template.setAppkey(GTPushConfig.appKey);
	    // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	    template.setTransmissionType(2);
	    template.setTransmissionContent(JSONObject.fromObject(map).toString());
	    return template;
	}
	
	//5. iOS模版
	public TransmissionTemplate getTemplate(Map<String, Object> map) {
	    TransmissionTemplate template = new TransmissionTemplate();
	    template.setAppId(GTPushConfig.appId);
	    template.setAppkey(GTPushConfig.appKey);
	    template.setTransmissionContent(JSONObject.fromObject(map).toString());
	    template.setTransmissionType(2);
	    
	    APNPayload payload = new APNPayload();
	    payload.setContentAvailable(1);
	    payload.setSound("default");
	    //简单模式APNPayload.SimpleMsg 
	    //payload.setAlertMsg(new APNPayload.SimpleAlertMsg(""+map.get("content")));
	    //字典模式使用下者
	    payload.setAlertMsg(getDictionaryAlertMsg( map));
	    template.setAPNInfo(payload);
	    return template;
	}
	
	private APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(Map<String, Object> map){
	    APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
	    //alertMsg.setBody("");
	    //alertMsg.setActionLocKey("ActionLockey");
	    alertMsg.setLocKey("" + map.get("content"));
	    //alertMsg.addLocArg("");
	    //alertMsg.setLaunchImage("launch-image");
	    // IOS8.2以上版本支持
	    alertMsg.setTitle("" + map.get("title"));
	    alertMsg.setTitleLocKey("" + map.get("title"));
	    //alertMsg.addTitleLocArg("TitleLocArg");
	    return alertMsg;
	}
	
	private Message getMessage(Message message, String title, String content, 
			int skip_type, String skip_link, Map<String, Object> map){
		
		if(map == null){
			map = new HashMap<>();
		}
		map.put("title", title);
		map.put("content", content);
		map.put("skip_type", skip_type);
		map.put("skip_link", skip_link);
		
		//模板
		ITemplate template;
		switch (skip_type) {
			case 1: //透传
				template = transmissionTemplateDemo(map);
				break;
			case 2: //打开网页
				template = linkTemplateDemo(map);
				break;
			case 3: //打开应用
				template = notificationTemplateDemo(map);
		        break;
			/*case 4: //下载
				template = notyPopLoadTemplateDemo(map);
				break;*/
			/*case 5: //ios
				template = getTemplate(map);
				break;*/
			default:
				template = transmissionTemplateDemo(map);
				break;
		}
			
		message.setData(template);
        message.setOffline(GTPushConfig.offline);
        message.setOfflineExpireTime(GTPushConfig.offlineExpireTime);
        message.setPushNetWorkType(GTPushConfig.pushNetWorkType);
		return message;
	}
}

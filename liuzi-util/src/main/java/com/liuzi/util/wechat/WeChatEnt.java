package com.liuzi.util.wechat;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import net.sf.json.JSONObject;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 基本参数类
 * @author zsy
 */
public class WeChatEnt {
	
	/**
	 * AccessToken
	 */
	@Data
	public static class WeChatAccessToken implements Serializable {

		private static final long serialVersionUID = 1L;
		
		/**
		 * 凭证
		 * 此access_token与基础支持的access_token不同
		 */
		private String access_token;
		/**
		 * 接口调用凭证超时时间（秒）
		 */
	    private int expires_in;
	    /**
	     * 用户刷新access_token
	     */
	    private String refresh_token;
	    /**
	     * 用户唯一标识
	     * 在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	     */
	    private String openid;
	    /**
	     * 用户授权的作用域
	     * 使用逗号（,）分隔
	     */
	    private String scope;
	}
	
	/**
	 * 授权类型
	 */
	public enum OauthType {
		/**
		 * 不弹出授权页面
		 * 直接跳转，只能获取用户openid
		 */
		snsapi_base,
		
		/**
		 * 弹出授权页面
		 * 可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息
		 */
		snsapi_userinfo;
	}
	
	/**
	 * 微信用户信息
	 */
	@Data
	public static class WeChatUser implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
	    /**
	     * 用户是否订阅该公众号标识
		 * 值为0时，代表此用户没有关注该公众号，拉取不到其余信息
	     */
	    private int subscribe;
	    /**
	     * 用户的标识，对当前公众号唯一
	     */
	    private String openid;
	    /**
	     * 用户的昵称
	     */
	    private String nickname;
	    /**
	     * 用户的性别
	     * 值为1时是男性，值为2时是女性，值为0时是未知
	     */
	    private int sex;
	    /**
	     * 用户所在城市
	     */
	    private String city;
	    /**
	     * 用户所在国家
	     */
	    private String country;
	    /**
	     * 用户所在省份
	     */
	    private String province;
	    /**
	     * 用户的语言
	     * 简体中文为zh_CN
	     */
	    private String language;
	    /**
	     * 用户头像
	     * 1. 最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像）
	     * 2. 用户没有头像时该项为空。
	     * 3. 若用户更换头像，原有头像URL将失效。
	     */
	    private String headimgurl;
	    /**
	     * 用户关注时间（时间戳）
	     * 如果用户曾多次关注，则取最后关注时间
	     */
	    private long subscribe_time;
	    /**
	     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
	     */
	    private String unionid;
	    /**
	     * 备注
	     * 公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
	     */
	    private String remark;
	    /**
	     * 用户所在的分组ID
	     * （兼容旧的用户分组接口）
	     */
	    private long groupid;
	    /**
	     * 用户被打上的标签ID列表
	     */
	    private Long[] tagid_list;
	    /**
	     * 用户关注的渠道来源
	     * 1. ADD_SCENE_SEARCH 公众号搜索
	     * 2. ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移
	     * 3. ADD_SCENE_PROFILE_CARD 名片分享
	     * 4. ADD_SCENE_QR_CODE 扫描二维码
	     * 5. ADD_SCENE_PROFILE_ LINK 图文页内名称点击
	     * 6. ADD_SCENE_PROFILE_ITEM 图文页右上角菜单
	     * 7. ADD_SCENE_PAID 支付后关注
	     * 8. ADD_SCENE_OTHERS 其他
	     */
	    private String subscribe_scene;
	    /**
	     * 二维码扫码场景（开发者自定义）
	     */
	    private long qr_scene;	
	    /**
	     * 二维码扫码场景描述（开发者自定义）
	     */
	    private String qr_scene_str;
		  
	}  
	
	/**
	 * 签名
	 * @author zsy
	 */
	@Data
	public static class WeChatTicket implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		private String ticket;
		private Integer expires_in;

		public WeChatTicket(){
			
		}
		
		public WeChatTicket(String ticket, Integer expires_in){
			this.ticket = ticket;
			this.expires_in = expires_in;
		}
	}
	
	/**
	 * 消息类型
	 */
	public enum MsgType {
		/**
		 * 普通消息
		 * 文本、图片、语音、视频、小视频、地理位置、链接
		 */
		text, image, voice, video, shortvideo, location, link,
		/**
		 * 事件消息
		 * 
		 */
		event;
	}
	
	/**
	 * 事件消息类型
	 */
	public enum EventType {
		/**
		 * 订阅、扫描带参数二维码事件
		 */
		subscribe,
		/**
		 * 取消订阅
		 */
		unsubscribe,
		/**
		 * 用户已关注时的事件推送
		 */
		SCAN,
		/**
		 * 上报地理位置事件
		 */
		LOCATION,
		/**
		 * 自定义菜单事件
		 */
		CLICK,
		/**
		 * 点击菜单跳转链接时的事件推送
		 */
		VIEW;
	}
	
	/**
	 * 发送模板
	 * @author zsy
	 */
	@Getter
	public static class MessageTemplate implements Serializable{
		private static final long serialVersionUID = 1L;
		/**
		 * 接收者openid
		 */
    	private String touser;
		/**
		 * 模板ID
		 */
    	private String template_id;
		/**
		 * 模板数据
		 */
    	private JSONObject data;
    	/**
		 * 模板跳转链接（海外帐号没有跳转能力）
		 */
    	@Setter private String url;
		/**
		 * 跳小程序所需数据，不需跳小程序可不用传该数据
		 */
    	@Setter private Miniprogram miniprogram;
    	
		private MessageTemplate(String touser, String template_id, JSONObject data){
			this.touser = touser;
			this.template_id = template_id;
			this.data = data;
		}
		
		public static MessageTemplateBuilder builder(String touser, String template_id,
				TempData... data){
			return new MessageTemplateBuilder(touser, template_id, data);
		}
		
		public String toString(){
			JSONObject data = new JSONObject();
			data.put("touser", this.getTouser());
			data.put("template_id", this.getTemplate_id());
			data.put("data", this.getData());
			String url = this.getUrl();
			if(!StringUtils.isEmpty(url)){
				data.put("url", url);
			}
			Miniprogram miniprogram = this.getMiniprogram();
			if(miniprogram != null){
				data.put("miniprogram", JSONObject.fromObject(miniprogram));
			}
			return data.toString();
		}
		
		public static class MessageTemplateBuilder{
			private MessageTemplate messageTemplate;
			private MessageTemplateBuilder(String touser, String templateId, TempData... data){
				JSONObject out = null;
				JSONObject in;
				if(data != null && data.length > 0){
					out = new JSONObject();
					for(TempData d : data){
						in = new JSONObject();
						in.put("value", d.getValue());
						in.put("color", d.getColor());
						out.put(d.getKey(), in);
					}
				}
				messageTemplate = new MessageTemplate(touser, templateId, out);
			}
			
			public MessageTemplateBuilder url(String url){
				messageTemplate.setUrl(url);
				return this;
			}
			public MessageTemplateBuilder miniprogram(String appid, String pagepath){
				messageTemplate.setMiniprogram(new Miniprogram(appid, pagepath));
				return this;
			}
			public MessageTemplate build(){
				return messageTemplate;
			}
		}
		
		/**
		 * 模板数据
		 */
		@Getter
		public static class TempData implements Serializable{
			private static final long serialVersionUID = 1L;
			
			/**
			 * 公众平台配置参数名称.DATA
			 */
	    	private String key;
			/**
			 * 参数内容
			 */
	    	private String value;
	    	
			/**
			 * 模板内容字体颜色，不填默认为黑色
			 */
	    	private String color;
	    	
	    	public TempData(String key, String value){
	    		this.key = key;
	    		this.value = value;
	    		this.color = "#173177";
	    	}
	    	
	    	public TempData(String key, String value, String color){
	    		this.key = key;
	    		this.value = value;
	    		this.color = color;
	    	}
		}
		
		/**
		 * 点击跳转小程序
		 */
		@Data
		public static class Miniprogram implements Serializable{
			private static final long serialVersionUID = 1L;
			/**
			 * 所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
			 */
	    	private String appid;
			/**
			 * 所需跳转到小程序的具体页面路径，支持带参数,
			 * （示例index?foo=bar），要求该小程序已发布，暂不支持小游戏
			 */
	    	private String pagepath;
	    	
	    	private Miniprogram(String appid, String pagepath){
	    		this.appid = appid;
	    		this.pagepath = pagepath;
	    	}
		}
    }
}

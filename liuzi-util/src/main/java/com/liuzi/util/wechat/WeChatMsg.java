package com.liuzi.util.wechat;

import java.io.Serializable;

/*import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;*/

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 消息接收类
 * @author zsy
 */
@Data
public class WeChatMsg implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 开发者微信号
	 */
	protected String toUserName;
	/**
	 * 发送方帐号（一个OpenID）
	 */
	protected String fromUserName;
	/**
	 * 消息创建时间
	 */
	protected long createTime;
	/**
	 * 消息类型
	 */
	protected String msgType;
	/**
	 * 消息id
	 */
	protected long msgId;
	
	/**
	 * 普通消息
	 */
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class Simple extends WeChatMsg{
		private static final long serialVersionUID = 1L;

		/**
		 * 文本消息
		 */
		@Data
		@EqualsAndHashCode(callSuper = false)
		public static class Text extends Simple{
			private static final long serialVersionUID = 1L;
			/**
			 * 文本消息内容
			 */
			private String content;
		}
		
		/**
		 * 图片消息
		 */
		@Data
		@EqualsAndHashCode(callSuper = false)
		//@XmlRootElement(name = "xml")
		//@XmlAccessorType(XmlAccessType.FIELD)
		public static class Image extends Simple{
			private static final long serialVersionUID = 1L;
			/**
			 * 图片链接（由系统生成）
			 */
			private String picUrl;
			/**
			 * 图片消息媒体id，可以调用多媒体文件下载接口拉取数据
			 */
			private String mediaId;
		    //@XmlElementWrapper(name = "Image")
			//@XmlElementWrapper注解可以在原xml结点上再包装一层xml，但仅允许出现在数组或集合属性上。
		    //private String[] mediaId ;
		}
		
		/**
		 * 语音消息
		 */
		@Data
		@EqualsAndHashCode(callSuper = false)
		public static class Voice extends Simple{
			private static final long serialVersionUID = 1L;
			/**
			 * 语音消息媒体id，可以调用获取临时素材接口拉取数据。
			 */
		    private String mediaId ;
		    /**
			 * 语音格式，如amr，speex等
			 */
		    private String format;
	    	/**
	    	 * 语音识别结果，UTF8编码
	    	 * 开通语音识别后，用户每次发送语音给公众号时，微信会在推送的语音消息XML数据包中，增加一个Recognition字段
	    	 * （注：由于客户端缓存，开发者开启或者关闭语音识别功能，对新关注者立刻生效，对已关注用户需要24小时生效。
	    	 * 	开发者可以重新关注此帐号进行测试）。
	    	 */
		    private String recognition;
		}
		
		/**
		 * 视频消息
		 */
		@Data
		@EqualsAndHashCode(callSuper = false)
		public static class Video extends Simple{
			private static final long serialVersionUID = 1L;
			/**
			 * 视频消息媒体id，可以调用获取临时素材接口拉取数据。
			 */
		    private String mediaId ;
		    /**
			 * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
			 */
		    private String thumbMediaId;
		}
		
		/**
		 * 小视频消息
		 */
		@Data
		@EqualsAndHashCode(callSuper = false)
		public static class Shortvideo extends Simple{
			private static final long serialVersionUID = 1L;
			/**
			 * 视频消息媒体id，可以调用获取临时素材接口拉取数据。
			 */
		    private String mediaId ;
		    /**
			 * 视频消息缩略图的媒体id，可以调用获取临时素材接口拉取数据。
			 */
		    private String thumbMediaId;
		}
		
		/**
		 * 地理位置消息
		 */
		@Data
		@EqualsAndHashCode(callSuper = false)
		public static class Location extends Simple{
			private static final long serialVersionUID = 1L;
			/**
			 * 地理位置维度
			 */
		    private float location_X ;
		    /**
			 * 地理位置经度
			 */
		    private float location_Y;
		    /**
			 * 地图缩放大小
			 */
		    private int scale;
		    /**
			 * 地理位置信息
			 */
		    private String label;
		}
		
		/**
		 * 链接消息
		 */
		@Data
		@EqualsAndHashCode(callSuper = false)
		public static class Link extends Simple{
			private static final long serialVersionUID = 1L;
			/**
			 * 消息标题
			 */
		    private String title ;
		    /**
			 * 消息描述
			 */
		    private String description;
		    /**
			 * 消息链接
			 */
		    private String url;
		}
	}
	
	/**
	 * 事件消息
	 */
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class Event extends WeChatMsg{
		private static final long serialVersionUID = 1L;
		/**
		 * 事件类型
		 * 1. 订阅：subscribe
		 * 2. 取消订阅：unsubscribe
		 * 3. 二维码
		 * 	a. 用户未关注时，进行关注后的事件推送：subscribe
		 *  b. 用户已关注时的事件推送：SCAN
		 * 4. 上报地理位置：LOCATION
		 * 5. 自定义菜单事件
		 *  a. 点击菜单拉取消息时的事件推送：事件类型，CLICK
		 *	b. 点击菜单跳转链接时的事件推送：事件类型，VIEW
		 */
		protected String event;
		
		/**
		 * 关注/取消关注事件
		 */
		@Data
		@EqualsAndHashCode(callSuper = false)
		public static class Subscribe extends Event{
			private static final long serialVersionUID = 1L;
		}
		
		/**
		 * 扫描带参数二维码事件
		 */
		@Data
		@EqualsAndHashCode(callSuper = false)
		public static class QrCode extends Event{
			private static final long serialVersionUID = 1L;
			
		    /**
			 * 事件KEY值
			 * 1. 用户未关注时，进行关注后的事件推送：qrscene_为前缀，后面为二维码的参数值
			 * 2. 用户已关注时的事件推送：是一个32位无符号整数，即创建二维码时的二维码scene_id
			 */
		    private String eventKey;
		    /**
			 * 二维码的ticket，可用来换取二维码图片
			 */
		    private String ticket;
		}
		
		/**
		 * 上报地理位置事件
		 */
		@Data
		@EqualsAndHashCode(callSuper = false)
		public static class Location extends Event{
			private static final long serialVersionUID = 1L;
		    /**
			 * 地理位置纬度
			 */
		    private String latitude;
		    /**
			 * 地理位置经度
			 */
		    private String longitude;
		    /**
			 * 地理位置精度
			 */
		    private String precision;
		}
		
		/**
		 * 自定义菜单事件
		 */
		@Data
		@EqualsAndHashCode(callSuper = false)
		public static class Click extends Event{
			private static final long serialVersionUID = 1L;
			
		    /**
			 * 点击菜单拉取消息时的事件推送：事件KEY值，与自定义菜单接口中KEY值对应
			 * 点击菜单跳转链接时的事件推送：事件KEY值，设置的跳转URL
			 */
		    private String eventKey;
		}
	}
}

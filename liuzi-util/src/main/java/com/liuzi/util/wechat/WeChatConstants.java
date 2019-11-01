package com.liuzi.util.wechat;


/**
 * 共享变量
 * @author zsy
 */
public class WeChatConstants {
	
	/**
	 * 授权加密参数
	 */
	protected static char[] DIGIT = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	/**
	 * 授权url
	 */
    protected static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    /**
     * 获取accessToken url
     */
    protected static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    /**
     * 刷新accessToken url
     */
    protected static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    /**
     * 判断accessToken是否有效 url
     */
    protected static final String AUTH_URL = "https://api.weixin.qq.com/sns/auth";
    /**
     * 获取有时效性的token url
     */
    protected static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    /**
     * 获取签名算法 url
     */
    protected static final String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    /**
     * 用户信息url
     */
    protected static final String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info";
    /**
     * 获取微信服务器ip地址或ip段url
     */
    protected static final String WECHAT_IP = "https://api.weixin.qq.com/cgi-bin/getcallbackip";
	/**
	 * 模板消息发送地址
	 */
	protected static final String MSG_TEMP_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send";
	
}

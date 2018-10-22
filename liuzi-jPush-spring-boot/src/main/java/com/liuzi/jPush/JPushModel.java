package com.liuzi.jPush;

import java.io.Serializable;
import java.util.Date;

public class JPushModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    private String title;

    /**
     * 提示信息
     */
    private String alert;

    /**
     * 标签
     */
    private String tag;

    /**
     * 别名
     */
    private String alias;

    /**
     * 客户端类型
     
    private String type;*/

    /**
     * 错误代码
     */
    private String errCode;

    /**
     * 信息
     */
    private String msg;
    
    /**
     * 附加信息
     */
    private String extra;
    
    /**
     * 信息编号
     */
    private String msgId;

    /**
	 * 添加日期
	 * */
	private Date addDate;
	
    /**
     * 下次推送时间
     */
    private Date nextDate;

    /**
     * 操作系统
     
    private String os;*/

    /**
     * 已推送
     */
    private Integer pushed = 0;

    /**
     * 收到
     */
    private Integer received = 0;


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}

	public Integer getPushed() {
		return pushed;
	}

	public void setPushed(Integer pushed) {
		this.pushed = pushed;
	}

	public Integer getReceived() {
		return received;
	}

	public void setReceived(Integer received) {
		this.received = received;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
    
    
    
    
}

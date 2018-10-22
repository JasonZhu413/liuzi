package com.liuzi.easemob.boot.comm;

import java.io.Serializable;


public class EasemobUser extends io.swagger.client.model.User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String nickname;
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}

package com.lxy.beans;

import java.io.Serializable;


public class ChatEntityAu implements Serializable {
	private static final long serialVersionUID = 1L;
	private int userImage;
	private String smessage;
	
	public int getUserImage() {
		return userImage;
	}
	public ChatEntityAu(int userImage, String smessage, String chatTime,
			boolean isMyMsg) {
		super();
		this.userImage = userImage;
		this.smessage = smessage;
		this.chatTime = chatTime;
		this.isMyMsg = isMyMsg;
	}
	public void setUserImage(int userImage) {
		this.userImage = userImage;
	}
	public String getSmessage() {
		return smessage;
	}
	public void setSmessage(String smessage) {
		this.smessage = smessage;
	}
	public String getChatTime() {
		return chatTime;
	}
	public void setChatTime(String chatTime) {
		this.chatTime = chatTime;
	}
	public boolean isMyMsg() {
		return isMyMsg;
	}
	public void setMyMsg(boolean isMyMsg) {
		this.isMyMsg = isMyMsg;
	}
	private String chatTime;
	private boolean isMyMsg;
	
	

}

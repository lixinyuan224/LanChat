package com.lxy.util;

import java.io.Serializable;

import com.lxy.beans.ChatEntityAu;
import com.lxy.beans.UserInfo;

public class MyProtocol implements Serializable{
	private static final long serialVersionUID = 1L;

	private UserInfo sender;
	private UserInfo receiver;
//	private long timeStamp;
//	public long getTimeStamp() {
//		return timeStamp;
//	}
//	public void setTimeStamp(long timeStamp) {
//		this.timeStamp = timeStamp;
//	}
	private int cmd;
	private ChatEntityAu chatEntity;
	private String filename;
	private String fileLength;
	private String filePath;

	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public UserInfo getSender() {
		return sender;
	}
	public void setSender(UserInfo sender) {
		this.sender = sender;
	}
	public UserInfo getReceiver() {
		return receiver;
	}
	public void setReceiver(UserInfo receiver) {
		this.receiver = receiver;
	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public ChatEntityAu getChatEntity() {
		return chatEntity;
	}
	public void setChatEntity(ChatEntityAu chatEntityAu) {
		this.chatEntity = chatEntityAu;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFileLength() {
		return fileLength;
	}
	public void setFileLength(String fileLength) {
		this.fileLength = fileLength;
	}
	
}

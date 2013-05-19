package com.lxy.beans;

import java.io.Serializable;

/**
 * 个人信息，包括，姓名，性别，签名，自己当前ip，主机名字，头像的id
 */

public class UserInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;//phoneNumber
	private int groupId;
	private String nickname;
	private String truename;
	private String sex;
	private String signature;
	private String ip;
	private int headImgid;
	
	public UserInfo(){//无参构造方法
		id="123456";
		groupId=0;
		nickname="admin";
		sex="null";
		signature="1";
		headImgid=-1;
	}

	public UserInfo(String id, int groupId, String nickname, String truename, String sex,
			String signature, String ip, int headImg) {
		
		this.id = id;
		this.groupId = groupId;
		this.nickname = nickname;
		this.truename=truename;
		this.sex = sex;
		this.signature = signature;
		this.ip = ip;
		this.headImgid = headImg;
	}
	
	public UserInfo(String id, String nickname, String truename, String sex,String signature, String ip, int headImg) { 
		
		this.id = id;
		this.nickname = nickname;
		this.truename=truename;
		this.groupId=-1;
		this.sex = sex;
		this.signature = signature;
		this.ip = ip;
		this.headImgid = headImg;
	}
	
	public boolean isEmpty(){//信息是否完全
		if(truename!=null&&nickname!=null&&sex!=null&&signature!=null&&headImgid!=-1)
			return false;
		else
			return true;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getnickname() {
		return nickname;
	}
	public void setnickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getHeadImgId() {
		return headImgid;
	}
	public void setHeadImgId(int headImg) {
		this.headImgid = headImg;
	}
}

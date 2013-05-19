package com.lxy.layoutInfo;

import com.lxy.beans.UserInfo;

public class MyInfosetting {
	private String nickname;	//�ǳ�
	private String truename;	//����
	private String sex;			//�Ա�
	private String signature;	//ǩ��
	private String headIcon;		//ͷ��id
	private String id;
	private String ip;
	
	public UserInfo getMyinfo(){
		UserInfo myinfo = null;
		if(nickname!=null&&truename!=null&&sex!=null&&signature!=null&&headIcon!=null&&id!=null){
			myinfo=new UserInfo(id, nickname, truename, sex, signature, ip,Integer.parseInt(headIcon));
		}
		return myinfo;
	}
	
	public void setMyInfo(UserInfo myinfo){
		this.headIcon=Integer.toString(myinfo.getHeadImgId());
		this.id=myinfo.getId();
		this.ip=myinfo.getIp();
		this.nickname=myinfo.getnickname();
		this.sex=myinfo.getSex();
		this.signature=myinfo.getSignature();
		this.truename=myinfo.getTruename();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
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
	public String getHeadIcon() {
		return headIcon;
	}
	public void setHeadIcon(String headIcon) {
		this.headIcon = headIcon;
	}
	public boolean isNull(){
		if(nickname!=null&&truename!=null&&sex!=null&&
				signature!=null&&headIcon!=null&&id!=null){
			return true;
		}else{
			return false;
		}
	}
}

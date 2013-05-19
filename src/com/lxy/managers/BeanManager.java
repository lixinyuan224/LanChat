package com.lxy.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.lxy.beans.ChatEntity;
import com.lxy.beans.UserInfo;
/**
 * 
 * @author lixy
 *
 */
public class BeanManager {
	private UserInfo myInfo;//自己的信息
	private Map<String, ArrayList<ChatEntity>> myUserChatMap;// 所有聊天记录,以ip(phoneNum)为键
	private ArrayList<ArrayList<UserInfo>> children;//所有子组
	private boolean isInvisibility=true;
	private boolean isFileTransport=false;
	private boolean isVioceTransport=false;
	
	public synchronized boolean isInvisibility() {
		return isInvisibility;
	}

	public synchronized void setInvisibility(boolean isInvisibility) {
		this.isInvisibility = isInvisibility;
	}

	public synchronized boolean isFileTransport() {
		return isFileTransport;
	}

	public synchronized void setFileTransport(boolean isFileTransport) {
		this.isFileTransport = isFileTransport;
	}

	public synchronized boolean isVioceTransport() {
		return isVioceTransport;
	}

	public synchronized void setVioceTransport(boolean isVioceTransport) {
		this.isVioceTransport = isVioceTransport;
	}

	public BeanManager(){
		myInfo=new UserInfo();
		refresh();
	}
	
	public UserInfo getMyInfo() {
		return myInfo;
	}

	public void setUserinfo(UserInfo myInfo) {
		this.myInfo = myInfo;
	}

	public Map<String, ArrayList<ChatEntity>> getMyUserChatMap() {
		return myUserChatMap;
	}

	public void setMyUserChatMap(Map<String, ArrayList<ChatEntity>> myUserChatMap) {
		this.myUserChatMap = myUserChatMap;
	}

	public ArrayList<ArrayList<UserInfo>> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<ArrayList<UserInfo>> children) {
		this.children = children;
	}
	
	public void refresh(){
		myUserChatMap=new HashMap<String, ArrayList<ChatEntity>>();
		children=new ArrayList<ArrayList<UserInfo>>();
		for(int i=0;i<4;i++){
			ArrayList<UserInfo> a=new ArrayList<UserInfo>();
			children.add(a);
		}
	}

}

package com.lxy.layoutInfo;

import java.util.List;

import android.widget.ListView;

import com.lxy.beans.ChatEntity;
import com.lxy.beans.ChatEntityAu;
import com.lxy.beans.UserInfo;

public class MyBalloonSetting {
	
	private List<ChatEntity> chatList ;//�������(��Ҫ)����������߳�ͬ��
	private ListView chatListView;
	private ChatAdapter chatAdapter;
	private UserInfo currPerson;  //�ļ�����Ŀ�����
	private boolean ispop=false;
	private String filePath=null;//Ҫ�����ļ���·��
	private ChatEntityAu currChatEntity;
	private boolean isSend=false;
	
	public boolean isSend() {
		return isSend;
	}
	public void setSend(boolean isSend) {
		this.isSend = isSend;
	}
	public ChatEntityAu getCurrChatEntity() {
		return currChatEntity;
	}
	public void setCurrChatEntity(ChatEntityAu currChatEntity) {
		this.currChatEntity = currChatEntity;
	}
	public boolean isIspop() {
		return ispop;
	}
	public void setIspop(boolean ispop) {
		this.ispop = ispop;
	}
	public UserInfo getCurrPerson() {
		return currPerson;
	}
	public void setCurrPerson(UserInfo currPerson) {
		this.currPerson = currPerson;
	}
	public ListView getChatListView() {
		return chatListView;
	}
	public void setChatListView(ListView chatListView) {
		this.chatListView = chatListView;
	}
	public ChatAdapter getChatAdapter() {
		return chatAdapter;
	}
	public void setChatAdapter(ChatAdapter chatAdapter) {
		this.chatAdapter = chatAdapter;
	}
	public List<ChatEntity> getChatList() {
		return chatList;
	}
	public void setChatList(List<ChatEntity> chatList) {
		this.chatList = chatList;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}

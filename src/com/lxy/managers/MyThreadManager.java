package com.lxy.managers;

import com.lxy.net.MyNetSendMessage;
import com.lxy.net.MyOnlineOfflineTh;

public class MyThreadManager {
	private MyNetSendMessage mySendMessage;//发送数据包线程
	private MyOnlineOfflineTh myOnlineOfflineTh;
	
	public MyThreadManager(BeanManager beanmanager) {
		mySendMessage=new MyNetSendMessage();
		myOnlineOfflineTh=new MyOnlineOfflineTh(beanmanager.getMyInfo());
	}
	
	
	
	public MyNetSendMessage getMySendMessage() {
		return mySendMessage;
	}
	public void setMySendMessage(MyNetSendMessage mySendMessage) {
		this.mySendMessage = mySendMessage;
	}
	public MyOnlineOfflineTh getMyOnlineOfflineTh() {
		return myOnlineOfflineTh;
	}
	public void setMyOnlineOfflineTh(MyOnlineOfflineTh myOnlineOfflineTh) {
		this.myOnlineOfflineTh = myOnlineOfflineTh;
	}
	

}

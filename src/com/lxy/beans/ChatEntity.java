package com.lxy.beans;


import java.io.Serializable;

import com.lxy.util.ExpressionUtil;
import com.lxy.util.MycommandNum;

import android.content.Context;
import android.text.SpannableString;

public class ChatEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private int userImage;
	private SpannableString spannablecontent;
	private String sspanable;
	private String chatTime;
	
	private boolean isMyMsg;
	

	

	public ChatEntity(int userImage,  String sspanable, String chatTime,
			boolean isMyMsg) {
		this.userImage = userImage;
		this.sspanable = sspanable;
		this.chatTime = chatTime;
		this.isMyMsg = isMyMsg;

	}
	
	
	public void toSpannable(Context context){
		spannablecontent=ExpressionUtil.getExpressionString(context, sspanable, MycommandNum.zhengze);
	}
	
	private void tosspanable(){
		sspanable=spannablecontent.toString();
	}
	public ChatEntity(){
		
	}
	public ChatEntity(ChatEntityAu chat){
		this.chatTime=chat.getChatTime();
		this.isMyMsg=chat.isMyMsg();
		this.sspanable=chat.getSmessage();
		this.userImage=chat.getUserImage();
		
	}

	public int getUserImage() {
		return userImage;
	}

	public void setUserImage(int userImage) {
		this.userImage = userImage;
	}

	public SpannableString getContent() {
		return spannablecontent;
	}

	public void setSpannablecontent(SpannableString spannablecontent) {
		this.spannablecontent = spannablecontent;
		
	}

	public String getSspanable() {
		tosspanable();
		return sspanable;
	}
	public void setSspanable(String sspanable) {
		this.sspanable = sspanable;
	}
	public SpannableString getSpannablecontent() {
		return spannablecontent;
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

}

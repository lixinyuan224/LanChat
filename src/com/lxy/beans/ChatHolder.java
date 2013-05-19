package com.lxy.beans;

import android.widget.ImageView;
import android.widget.TextView;
/*
 * 生成每次对话
 * 
 * */
public class ChatHolder {
	private TextView timeTextView;  //时间，自动生成
	private ImageView userImageView;//头像
	private TextView contentTextView;//对话文本

	public TextView getTimeTextView() {
		return timeTextView;
	}

	public void setTimeTextView(TextView timeTextView) {
		this.timeTextView = timeTextView;
	}

	public ImageView getUserImageView() {
		return userImageView;
	}

	public void setUserImageView(ImageView userImageView) {
		this.userImageView = userImageView;
	}

	public TextView getContentTextView() {
		return contentTextView;
	}

	public void setContentTextView(TextView contentTextView) {
		this.contentTextView = contentTextView;
	}
}

package com.lxy.beans;

import android.widget.ImageView;
import android.widget.TextView;
/*
 * ����ÿ�ζԻ�
 * 
 * */
public class ChatHolder {
	private TextView timeTextView;  //ʱ�䣬�Զ�����
	private ImageView userImageView;//ͷ��
	private TextView contentTextView;//�Ի��ı�

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

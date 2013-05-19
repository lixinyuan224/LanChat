package com.lxy.managers;

import com.lxy.layoutInfo.MyBalloonSetting;
import com.lxy.layoutInfo.MyExpandablesetting;
import com.lxy.layoutInfo.MyFileChooseSetting;
import com.lxy.layoutInfo.MyInfosetting;

/*
 * �����洢�����������
 * 
 * */
public class LayoutManager {
	private MyInfosetting infosetting;		//������Ϣ����
	private MyExpandablesetting expandablesetting;  //  ���Ѷ����б����
	private MyBalloonSetting myballonsetting;  // �����������
	private MyFileChooseSetting myfilechoosesetting;  //�ļ�ѡ�����
	
	public LayoutManager(){
		infosetting=new MyInfosetting();
		expandablesetting=new MyExpandablesetting();
		myballonsetting=new MyBalloonSetting();
		myfilechoosesetting=new MyFileChooseSetting();
	}
	
	public MyFileChooseSetting getMyfilechoose() {
		return myfilechoosesetting;
	}

	public void setMyfilechoose(MyFileChooseSetting myfilechoosesetting) {
		this.myfilechoosesetting = myfilechoosesetting;
	}

	public MyBalloonSetting getMyballonsetting() {
		return myballonsetting;
	}
	public void setMyballonsetting(MyBalloonSetting myballonsetting) {
		this.myballonsetting = myballonsetting;
	}
	public MyExpandablesetting getExpandablesetting() {
		return expandablesetting;
	}
	public void setExpandablesetting(MyExpandablesetting expandablesetting) {
		this.expandablesetting = expandablesetting;
	}
	
	public MyInfosetting getInfosetting() {
		return infosetting;
	}

	public void setInfosetting(MyInfosetting infosetting) {
		this.infosetting = infosetting;
	}

}

package com.lxy.layoutInfo;

import java.util.ArrayList;
import java.util.List;

import android.widget.ExpandableListView;
import android.widget.TextView;

import com.lxy.beans.UserInfo;

public class MyExpandablesetting {
	
 	private List<String>  groupArray=new ArrayList<String>();//组名称
 	private TextView textview;         //我的状态
 	private ExpandableListView  expandableListView_one;//折叠列表
 	private ExpandableAdapter exlistadp=null; //折叠列表适配器
	private int groupPosition=0;
	private int childPosition=0;
	private UserInfo friendmove;
 	
 	
	public UserInfo getFriendmove() {
		return friendmove;
	}
	public void setFriendmove(UserInfo friendmove) {
		this.friendmove = friendmove;
	}
	public int getGroupPosition() {
		return groupPosition;
	}
	public void setGroupPosition(int groupPosition) {
		this.groupPosition = groupPosition;
	}
	public int getChildPosition() {
		return childPosition;
	}
	public void setChildPosition(int childPosition) {
		this.childPosition = childPosition;
	}
	public ExpandableListView getExpandableListView_one() {
		return expandableListView_one;
	}
	public void setExpandableListView_one(ExpandableListView expandableListView_one) {
		this.expandableListView_one = expandableListView_one;
	}
	public ExpandableAdapter getExlistadp() {
		return exlistadp;
	}
	public void setExlistadp(ExpandableAdapter exlistadp) {
		this.exlistadp = exlistadp;
	}
	public TextView getTextview() {
		return textview;
	}
	public void setTextview(TextView textview) {
		this.textview = textview;
	}
	
	public List<String> getGroupArray() {
		if(groupArray.isEmpty()){
			groupArray.add("   当前在线");
			groupArray.add("   正在聊天");
			groupArray.add("   陌生人");
			groupArray.add("   未读消息");
		}
		return groupArray;
	}
	
	public void setGroupArray(List<String> groupArray) {
		this.groupArray = groupArray;
	}
}

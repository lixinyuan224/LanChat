package com.lxy.layoutInfo;



import com.lxy.managers.BeanManager;
import com.lxy.managers.LayoutManager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableAdapter extends BaseExpandableListAdapter {//多级列表的适配器
	private Activity activity;
	private BeanManager beanManager;
	private LayoutManager layoutManager;
 	
	public  ExpandableAdapter(Activity a,BeanManager beanManager,LayoutManager layoutManager){  
        this.activity = a;  
        this.beanManager=beanManager;
        this.layoutManager=layoutManager;
    }  

	public Object getChild(int groupPosition, int childPosition) {
		return beanManager.getChildren().get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		String string=""; 
		if(beanManager.getChildren().get(groupPosition).size()!=0){
    	    string =beanManager.getChildren().get(groupPosition).get(childPosition).getnickname();
    	}
//    		Toast.makeText(MyExpandableActivity.this, 
//    				Integer.toString(childPosition)+Integer.toString(groupPosition),
//    				Toast.LENGTH_SHORT).show();
    		return getGenericView(string);
		
	}

	public int getChildrenCount(int groupPosition) {
		return beanManager.getChildren().get(groupPosition).size();
	}
   /* ----------------------------Group */
	public Object getGroup(int groupPosition) {
		 return layoutManager.getExpandablesetting().getGroupArray().get(groupPosition);    	}

	public int getGroupCount() {
		return layoutManager.getExpandablesetting().getGroupArray().size();
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}
	
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
       String   string=layoutManager.getExpandablesetting().getGroupArray().get(groupPosition);
       return getGenericView(string);
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {//childitem是否能被选中
		return true;
	}
	
/*
 * 树形列表设置
 * 
 * */
	private TextView  getGenericView(String string ) {
        AbsListView.LayoutParams  layoutParams =new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
        
        TextView  textView =new TextView(activity);
        textView.setLayoutParams(layoutParams);              
        textView.setGravity(Gravity.CENTER_VERTICAL |Gravity.LEFT);              
        textView.setPadding(40, 3, 0, 3);
        textView.setText(string);
        textView.setTextSize(30);
        textView.setTextColor(Color.BLACK);
        return textView;
   }

	
}


package com.lxy.layoutInfo;

import com.lxy.LanChat.R;
import com.lxy.beans.UserInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopCard {
	private PopupWindow pop;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private TextView tv5;
	private Button cancle;
	public PopCard(Context con,UserInfo user,View view){
		View contentView = LayoutInflater.from(con.getApplicationContext())  
                .inflate(R.layout.popup, null); 
		pop=new PopupWindow(contentView, 425, 440);
		pop.setFocusable(true); 
		pop.showAsDropDown(view);
		tv1=(TextView) contentView.findViewById(R.id.line1);
		tv2=(TextView) contentView.findViewById(R.id.line2);
		tv3=(TextView) contentView.findViewById(R.id.line3);
		tv4=(TextView) contentView.findViewById(R.id.line4);
		tv5=(TextView) contentView.findViewById(R.id.line5);
		cancle=(Button) contentView.findViewById(R.id.canclepop);
		tv1.setText("                 ��Ƭ\n������"+user.getnickname());
		tv2.setText("�Ա�"+user.getSex());
		tv3.setText("������"+user.getTruename());
		tv4.setText("ǩ����"+user.getSignature());
		tv5.setText("ip��ַ��"+user.getIp()+"\n");
		cancle.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				pop.dismiss();
			}
		});
		 
	}
	
}

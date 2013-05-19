package com.lxy.net.voice;

import java.lang.reflect.Field;

import com.lxy.LanChat.R;
import com.lxy.beans.UserInfo;
import com.lxy.managers.BeanManager;
import com.lxy.net.MyNetSendMessage;
import com.lxy.util.MyProtocol;
import com.lxy.util.MycommandNum;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author lixy
 *
 */
public class VoiceAlertDialog {
	private Dialog alertDialog;  
	private Context context;
    private Button cancel;
    private Button cancel2;
    private Button afirm;
    private TextView linkView;
    private TextView linkView2;
    private LinearLayout first;
    private LinearLayout second;
    private boolean ismyask=false;
    private UserInfo myinfo;
    private UserInfo curr;
    private MyProtocol mypro;
	private MyNetSendMessage msn;
	private ThreadMicTcp tmt;
	private ThreadReceiveTcp trt;
	private Handler handler;
	private BeanManager beanmanager;
	public VoiceAlertDialog(UserInfo curr ,Context con,BeanManager bean,Handler handler){
		this.handler=handler;
		this.context=con;
		this.curr=curr;
		this.beanmanager=bean;
		this.myinfo=beanmanager.getMyInfo();
	    
		trt=new ThreadReceiveTcp();
		tmt=new ThreadMicTcp(curr.getIp());
		Log.e("VoiceAlertDialog curr   myinfo",curr.getIp()+""+myinfo.getIp());
	 
	}
	
	public void creatDialog(boolean isAsk){
	   this.ismyask=isAsk;

	    if(ismyask){
	    	Log.i("true", "start");
	    	LayoutInflater layoutInflater = LayoutInflater.from(context); 
	        View myLoginView = layoutInflater.inflate(R.layout.sendstyle, null); 
	    	alertDialog=new  AlertDialog.Builder(context). 
	                setTitle("语音通话"). 
	                setView(myLoginView).
	                create(); 
	    
	    	Log.i("true", "alerDialog is creat");
	        first=(LinearLayout) myLoginView.findViewById(R.id.voicefirstsendview)	;
	        second=(LinearLayout) myLoginView.findViewById(R.id.voicesecondsendview);
	    	cancel=(Button)myLoginView.findViewById(R.id.cancleConnect);
	    	cancel2=(Button) myLoginView.findViewById(R.id.cancleConnect2);
	    	linkView=(TextView) myLoginView.findViewById(R.id.linkView);
	    	linkView.setText("连接中……");
	    	linkView2=(TextView) myLoginView.findViewById(R.id.linkView2);
	    	linkView2.setText("与"+curr.getnickname()+"通话中");
	    	cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					msn=new MyNetSendMessage();
					mypro=new MyProtocol();
					mypro.setCmd(MycommandNum.VOICENO);
					mypro.setReceiver(curr);
					mypro.setSender(myinfo);
	                msn.setData(mypro);
	                msn.start();
	              
	                alertDialog.dismiss();
				}
			});
	    	cancel2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					handler.sendEmptyMessage(MycommandNum.VOICENO);
					msn=new MyNetSendMessage();
					mypro=new MyProtocol();
					mypro.setReceiver(curr);
					mypro.setSender(myinfo);
					mypro.setCmd(MycommandNum.VOICENO);
		            msn.setData(mypro);
		            msn.start();
		            trt.over();
		            tmt.over();
		         
		            alertDialog.dismiss();
				}
			});
	    	Log.i("true", "end");
	    }else{
	    	LayoutInflater layoutInflater = LayoutInflater.from(context); 
	        View myLoginView = layoutInflater.inflate(R.layout.receivestyle, null); 
	    	alertDialog=new  AlertDialog.Builder(context). 
	                setTitle("语音通话"). 
	                setView(myLoginView).
	                create();
	    	first=(LinearLayout) myLoginView.findViewById(R.id.voicefirstreceiveview);
	    	second=(LinearLayout) myLoginView.findViewById(R.id.voicesecondreceiveview);
	    	afirm=(Button) myLoginView.findViewById(R.id.afirm);
	    	cancel=(Button) myLoginView.findViewById(R.id.notafirm);
	    	cancel2=(Button) myLoginView.findViewById(R.id.voicecancleConnect2);
			linkView=(TextView) myLoginView.findViewById(R.id.islinkView);
			linkView.setText(curr.getnickname()+"发来语音请求");
			linkView2=(TextView) myLoginView.findViewById(R.id.voicelinkView2);
			linkView2.setText("与"+curr.getnickname()+"通话中");
			afirm.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					msn=new MyNetSendMessage();
					mypro=new MyProtocol();
					mypro.setReceiver(curr);
					mypro.setSender(myinfo);
	                mypro.setCmd(MycommandNum.VOICEYES);
	                msn.setData(mypro);
	                msn.start();
	             	changeLayout();
	             	beanmanager.setVioceTransport(true);
				}
			});
			cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					msn=new MyNetSendMessage();
					mypro=new MyProtocol();
					mypro.setReceiver(curr);
					mypro.setSender(myinfo);
	                mypro.setCmd(MycommandNum.VOICENO);
	                msn.setData(mypro);
	                msn.start();
	             	alertDialog.dismiss();
	             	Toast.makeText(context, 
	             		"拒绝了"+curr.getnickname()+"通话请求", 
	             		Toast.LENGTH_SHORT).show();
				}
			});
			cancel2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					alertDialog.dismiss();
					handler.sendEmptyMessage(MycommandNum.VOICENO);
					msn=new MyNetSendMessage();
					mypro=new MyProtocol();
					mypro.setReceiver(curr);
					mypro.setSender(myinfo);
					mypro.setCmd(MycommandNum.VOICENO);
		            msn.setData(mypro);
		            msn.start();
		            handler.sendEmptyMessage(MycommandNum.VOICENO);
				}
			});
	    }//end of else 
	    alertDialog.setCanceledOnTouchOutside(false);
	}
	
	public void show(){
//		Log.i("show", "start");
		if(alertDialog!=null){
//			Log.i("show", "before show()");
			alertDialog.show();
//			Log.i("show", "after show");
		}
	}
	public void over(){
		if(trt!=null){
			if(trt.isAlive())
				trt.over();
		}
		if(tmt!=null){
			if(tmt.isAlive())
				tmt.over();
		}
		beanmanager.setVioceTransport(false);
	}
	
	public void dissmiss(){
		if(alertDialog!=null){
//			Log.i("show", "before show()");
			alertDialog.dismiss();
			beanmanager.setVioceTransport(false);
//			Log.i("show", "after show");
		}
	}
	
	public void changeLayout(){
		trt.start();
		tmt.start();
		first.setVisibility(View.GONE);
		second.setVisibility(View.VISIBLE);
	
	}
}

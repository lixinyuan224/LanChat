package com.lxy.net.file;


import com.lxy.LanChat.R;
import com.lxy.managers.BeanManager;
import com.lxy.net.MyNetSendMessage;
import com.lxy.util.MyProtocol;
import com.lxy.util.MycommandNum;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class FileTransAlertDialog {
	private AlertDialog alertDialog;  
	private Context context;
    private Button cancel;
    private Button confirm;
    private TextView titleView;
    private TextView titleView2;
    private TextView percent;
    private ProgressBar probar;
    private boolean ismyask=false;
    private LinearLayout first;
    private LinearLayout second;

    private MyProtocol mypro;
    private MyProtocol pro;
	private MyNetSendMessage msn;
	private FileControl fc;
	private BeanManager beanmanager;
    
	public FileTransAlertDialog(MyProtocol pro ,Context con,FileControl fc,BeanManager bean){
		this.context=con;
		this.pro=pro;
		this.fc=fc;
		this.beanmanager=bean;
		mypro=new MyProtocol();
	    mypro.setReceiver(pro.getSender());
	    mypro.setSender(pro.getReceiver());
	}
	
	public void creatDialog(boolean isAsk){
	   this.ismyask=isAsk;
	    if(ismyask){
	    	Log.i("true", "start");
	    	LayoutInflater layoutInflater = LayoutInflater.from(context); 
	        View myLoginView = layoutInflater.inflate(R.layout.filesendstyle, null); 
	    	alertDialog=new  AlertDialog.Builder(context). 
	                setTitle("文件传输"). 
	                setView(myLoginView).
	                create(); 
	    	Log.i("true", "alerDialog is creat");
	        		
	    	cancel=(Button)myLoginView.findViewById(R.id.cancle);
	    	titleView=(TextView) myLoginView.findViewById(R.id.textviewsend);
	    	titleView.setText("连接中……");
	    	first=(LinearLayout) myLoginView.findViewById(R.id.firstview);
	    	second=(LinearLayout) myLoginView.findViewById(R.id.secondview);
	    	probar=(ProgressBar) myLoginView.findViewById(R.id.progressBar);
			titleView2=(TextView) myLoginView.findViewById(R.id.textfilename);
			percent=(TextView) myLoginView.findViewById(R.id.percent);
//			percent.setVisibility(View.GONE);
			titleView2.setText("等待"+pro.getSender().getnickname()+"接收……");
			
	    	cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
	    	Log.i("true", "end");
//	    	msn=new MyNetSendMessage();
//	    	mypro.setCmd(MycommandNum.FILEREORNOT);
//	    	msn.setData(mypro);
//	    	msn.start();
	    }else{
	    	LayoutInflater layoutInflater = LayoutInflater.from(context); 
	        View myLoginView = layoutInflater.inflate(R.layout.filereceivestyle, null); 
	    	alertDialog=new  AlertDialog.Builder(context). 
	                setTitle("文件传输"). 
	                setView(myLoginView).
	                create();
	    	confirm=(Button) myLoginView.findViewById(R.id.confirm);
	    	cancel=(Button) myLoginView.findViewById(R.id.reject);
			titleView=(TextView) myLoginView.findViewById(R.id.textviewre);
			first=(LinearLayout) myLoginView.findViewById(R.id.firstview2);
	    	second=(LinearLayout) myLoginView.findViewById(R.id.secondview2);
	    	probar=(ProgressBar) myLoginView.findViewById(R.id.progressBar2);
			titleView2=(TextView) myLoginView.findViewById(R.id.textfilename2);
			percent=(TextView) myLoginView.findViewById(R.id.percent2);
			titleView.setText("文件名："+pro.getFilename()+"\n文件长度："+pro.getFileLength()+"B");
			titleView2.setText("文件名："+pro.getFilename()+"\n文件长度："+pro.getFileLength()+"B");
			confirm.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					fc.startFileRec(pro.getFilename(), pro.getFileLength());
					msn=new MyNetSendMessage();
					mypro.setCmd(MycommandNum.FILEYES);
					msn.setData(mypro);
					msn.start();
					Log.i("confirm.setOnClickListener(new","confirm ok");
	             	changeLayout();
				}
			});
			cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
	             	alertDialog.dismiss();
	             	mypro.setCmd(MycommandNum.FILENO);
	             	msn=new MyNetSendMessage();
					msn.setData(mypro);
					msn.start();
					
             	    Toast.makeText(context, "拒绝了文件传输请求",Toast.LENGTH_SHORT).show();
				}
			});
	    }
	   alertDialog.setCanceledOnTouchOutside(false);
		
	}
	
	public void show(){
		if(alertDialog!=null){
			
			alertDialog.show();
		}
	
	}
	
	public void dissmiss(){
		beanmanager.setFileTransport(false);
		if(alertDialog!=null){
			alertDialog.dismiss();
		}
	}
	
	public void plus(int per){
	 
	   if(per>=100){
		   Toast.makeText(context, 
        			"文件传输完成", 
        			Toast.LENGTH_SHORT).show();
		   alertDialog.dismiss();
	   }
	   if(probar!=null&&percent!=null){
		   probar.setProgress(per);
  	       percent.setText(Integer.toString(per)+"%");
		 Log.i("percent",Integer.toString(per));
	   }
	   
	}
	
	public void changeLayout(){
		beanmanager.setFileTransport(true);
		first.setVisibility(View.GONE);
		second.setVisibility(View.VISIBLE);
	}
}

package com.lxy.managers;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lxy.beans.ChatEntity;
import com.lxy.beans.UserInfo;
import com.lxy.net.MyNetSendMessage;
import com.lxy.net.file.FileControl;
import com.lxy.util.MyProtocol;
import com.lxy.util.MycommandNum;
import com.lxy.util.SoundPlayThread;

public class ProtocolManager extends Thread{


	private LayoutManager layoutManager;
	private BeanManager   beanManager;
	private FileControl fc;
	private Handler handler;
	private Context context;
	private MyProtocol protocol;
	private SoundPlayThread backsound;

	public ProtocolManager(BeanManager beanManager,LayoutManager layoutManager,Handler hd,
			Context context, FileControl fc){
		this.beanManager=beanManager;
		this.layoutManager=layoutManager;
		handler=hd;
		this.context=context;
	   	this.fc=fc;
	   
	   
	}
	
	@Override
	public void run() {
		dealWithPro();
	}
	
	public void setPreProtocol(MyProtocol protoco){
		this.protocol=protoco;
	}
	
	public synchronized void dealWithPro(){
//		//自己上线处理
		if(protocol.getSender().getIp().equals(beanManager.getMyInfo().getIp())){
			return;
		}
		//陌生人过滤
		ArrayList<UserInfo> usin=beanManager.getChildren().get(2);
		for(UserInfo u:usin){		
			if(u.getIp().equals(protocol.getSender().getIp()))	
				return;
		}
		//timeStamp处理
//		long time=protocol.getTimeStamp();
		
		int cmd=protocol.getCmd();
		switch(cmd){
			case MycommandNum.onLinere:{
				addPerson(protocol.getSender());
				if(layoutManager.getExpandablesetting().getExlistadp()!=null)
					handler.sendEmptyMessage(MycommandNum.onLinere);//ui更新
			}break;
			case MycommandNum.onLine:{
				
				backsound=new SoundPlayThread(context,4);
				backsound.start();
				addPerson(protocol.getSender());
		
				if(beanManager.isInvisibility()){ 	//隐身处理
					MyProtocol mp=new MyProtocol();
					mp.setCmd(MycommandNum.onLinere);
					mp.setReceiver(protocol.getSender());
					mp.setSender(beanManager.getMyInfo());
					MyNetSendMessage mns=new MyNetSendMessage();
					mns.setData(mp);
					mns.start();
				}
				
//				if(layoutManager.getExpandablesetting().getExlistadp()!=null)
//				  layoutManager.getExpandablesetting().getExlistadp().notifyDataSetChanged();//ui更新
			}break;
			case MycommandNum.offLine:{
				Message msg=new Message();
				msg.what=MycommandNum.offLinere;
				msg.obj=protocol.getSender();
				handler.sendMessage(msg);
				backsound=new SoundPlayThread(context,4);
				backsound.start();
				deletePerson(protocol.getSender());
//				layoutManager.getExpandablesetting().getExlistadp().notifyDataSetChanged();
			}break;
			case MycommandNum.Message:{
				backsound=new SoundPlayThread(context,3);
				backsound.start();
				addMessage(protocol);
//				layoutManager.getMyballonsetting().getChatAdapter().notifyDataSetChanged();
				handler.sendEmptyMessage(MycommandNum.Message);
				
				
			}break;
			//语音通话处理
			case MycommandNum.voice:	{	//
				if(beanManager.isVioceTransport()){ //语音通话中
					MyNetSendMessage message=new MyNetSendMessage();
					MyProtocol mp=new MyProtocol();
					mp.setCmd(MycommandNum.VOICEING);
					mp.setSender(beanManager.getMyInfo());
					mp.setReceiver(protocol.getSender());
					message.setData(mp);
					message.start();
				}else{
					backsound=new SoundPlayThread(context,5);
					backsound.start();
					Message msg=new Message();
					msg.what=MycommandNum.VOICEREORNOT;
					msg.obj=protocol;
					handler.sendMessage(msg);
				}
				
			}break;	
			case MycommandNum.VOICEING:{
				Log.i("voiceing","voicing");
				handler.sendEmptyMessage(MycommandNum.VOICEING);
			}break;
			case MycommandNum.VOICEYES:	{	//
				backsound=new SoundPlayThread(context,5);
				backsound.start();
			
				handler.sendEmptyMessage(MycommandNum.VOICEYES);
				
			}break;	
			case MycommandNum.VOICENO:	{	//
				backsound=new SoundPlayThread(context,5);
				backsound.start();
			
				handler.sendEmptyMessage(MycommandNum.VOICENO);
				
			}break;	
			//文件传输处理
			case MycommandNum.FILE:	{//文件发送请求
				
				if(beanManager.isFileTransport()){ //文件传输中
					MyNetSendMessage message=new MyNetSendMessage();
					MyProtocol mp=new MyProtocol();
					mp.setCmd(MycommandNum.FILEING);
					mp.setSender(beanManager.getMyInfo());
					mp.setReceiver(protocol.getSender());
					message.setData(mp);
					message.start();
				}else{
					backsound=new SoundPlayThread(context,2);
					backsound.start();
					
					Message msg=new Message();
					msg.what=MycommandNum.FILEREORNOT;
					msg.obj=protocol;
					handler.sendMessage(msg);
				}
			}break;
			case  MycommandNum.FILEING:{
				handler.sendEmptyMessage(MycommandNum.FILEING);
			}break;
			case MycommandNum.FILENO:{//不接受文件    toast
				backsound=new SoundPlayThread(context,2);
				backsound.start();
				handler.sendEmptyMessage(MycommandNum.FILENO);
				
			}break;
			case MycommandNum.FILEYES:{
				backsound=new SoundPlayThread(context,2);
				backsound.start();
				fc.startFileSend(layoutManager.getMyballonsetting().getFilePath(),
								protocol.getSender().getIp());
				
				handler.sendEmptyMessage(MycommandNum.FILEYES);
			}break;
			
		
		
		}
	}

	private boolean addPerson(UserInfo user){
		ArrayList<ArrayList<UserInfo>> children=beanManager.getChildren();
		
		boolean flag=false;
		Log.i("ProtocolManagerc addPerson(", Integer.toString(user.getHeadImgId()));
		if(children.get(0).isEmpty()){
			flag=true;
			beanManager.getChildren().get(0).add(user);
			beanManager.getMyUserChatMap().put(user.getIp(), new ArrayList<ChatEntity>());
			Log.i("ProtocolManagerchildren.get(0).isEmpty()"," addPerson==="+beanManager.getChildren().get(0).get(0).getIp());
			return false;
		}
		ArrayList<UserInfo> list0=children.get(0);//全部好友
		Iterator<UserInfo> it=list0.iterator();
		while(it.hasNext()){
			UserInfo ui=it.next();
			if(ui.getIp().equals(user.getIp())){
				Log.i("ProtocolManager"," addPerson===cunzai");
				if(ui.getnickname().equals(user.getnickname())&&ui.getHeadImgId()==user.getHeadImgId()
						&&ui.getSex().equals(user.getSex())){
					flag=true;
					
					break;
				}else{
					deletePerson(user);
					Log.i("ui.getIp()==user.getIp()",Boolean.toString(ui.getIp()==user.getIp()));
					Log.i("ui.getnickname()==user.getnickname()", Boolean.toString(ui.getnickname()==user.getnickname()));
					Log.i("ui.getHeadImgId()==user.getHeadImgId()", Boolean.toString(ui.getHeadImgId()==user.getHeadImgId()));
					Log.i("ui.getSex()==user.getSex()", Boolean.toString(ui.getSex()==user.getSex()));
					Log.i("ui.getSex()==user.getSex()", Boolean.toString(ui.getSex()==user.getSex()));
				}
			}
		}
		if(!flag){
			beanManager.getChildren().get(0).add(user);
			beanManager.getMyUserChatMap().put(user.getIp(), new ArrayList<ChatEntity>());
			Log.i("ProtocolManagerit"," addPerson==="+user.getIp());
		}
		
		
		if(layoutManager.getMyballonsetting().getCurrPerson()==null){
			if(!beanManager.getChildren().get(3).contains(user)){//判断是否为当前聊天
				beanManager.getChildren().get(3).add(user);
			}
		}else{
			if(!layoutManager.getMyballonsetting().getCurrPerson().getIp().equals(user.getIp()))
				if(!beanManager.getChildren().get(3).contains(user)){//判断是否为当前聊天
					beanManager.getChildren().get(3).add(user);
				}
		}
		return flag;
	
	}

	private void deletePerson(UserInfo user){
		ArrayList<ArrayList<UserInfo>> children=beanManager.getChildren();
		for(ArrayList<UserInfo> group:children){
			Iterator< UserInfo> ui=group.iterator();
			while(ui.hasNext()){
				UserInfo us=ui.next();
				if(us.getIp().equals(user.getIp())){
					
					Log.i("deletePerson(UserInfo user)",user.getIp());
					beanManager.getChildren().get(0).remove(user);
					beanManager.getChildren().get(1).remove(user);
					beanManager.getChildren().get(2).remove(user);
					beanManager.getChildren().get(3).remove(user);
					beanManager.getMyUserChatMap().remove(user.getIp());
				}
			}
			
		}
	}

	private void addMessage(MyProtocol protocol){
		addPerson(protocol.getSender());
		ChatEntity chaten=new ChatEntity(protocol.getChatEntity());
		chaten.setMyMsg(false);
		chaten.toSpannable(context);
		Iterator<UserInfo> ui=beanManager.getChildren().get(1).iterator();
		if(!ui.hasNext()){
			beanManager.getChildren().get(1).add(protocol.getSender());
		}
		while(ui.hasNext()){
			UserInfo u=ui.next();
			if(u.getHeadImgId()==protocol.getSender().getHeadImgId()&&u.getnickname()==protocol.getSender().getnickname()&&
					u.getIp()==protocol.getSender().getIp()){
				beanManager.getChildren().get(1).add(protocol.getSender());
			}
		}
		beanManager.getMyUserChatMap().get(
				protocol.getSender().getIp()).add(chaten);
		Log.i("protocolManager__addMessage", "getUserImage:"+protocol.getChatEntity().getUserImage());
		
	}

}

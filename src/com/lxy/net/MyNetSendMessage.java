package com.lxy.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.crypto.SecretKey;


import android.util.Log;

import com.lxy.util.MyProtocol;
import com.lxy.util.MycommandNum;
import com.lxy.util.ProtocolEncrypt;

/**
 * 
 * @author lixy
 *
 */

public class MyNetSendMessage extends Thread{
	private DatagramPacket udpSendPacket = null;	//用于接收的udp数据包
	private byte[] sendBuffer = null;
	private ProtocolEncrypt protocolencrypt; //
	private DatagramSocket udpSocket = null;	//用于接收和发送udp数据的socket
	private MyProtocol myProtocol;
	private InetAddress desIP;
	private SecretKey key;                     //
	private ByteArrayOutputStream bout;
	private ObjectOutputStream objectOut;
	public MyNetSendMessage(){
		try {
			udpSocket = new DatagramSocket();//绑定端口
		} catch (SocketException e) {
			e.printStackTrace();
		}	
		protocolencrypt=new ProtocolEncrypt();
	    key=protocolencrypt.getSecretKey(MycommandNum.key);
		bout = new ByteArrayOutputStream();
		try {
			objectOut = new ObjectOutputStream(bout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		if(myProtocol!=null&&desIP!=null){
			sendUdpData();
		}
	}
	
	public void setData(MyProtocol myProtocol){
		 this.myProtocol=myProtocol;
		  desIP=null;
			try {
				desIP = InetAddress.getByName(this.myProtocol.getReceiver().getIp());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
	}
	
	private  void sendUdpData(){	//发送UDP数据包的方法
		try {
			objectOut.reset();
		//	objectOut.writeByte( MSG_STR_HEAD); // 写入一个标志，便于接收端判断对象类型，MSG_STR_HEAD是一个自定义常量
			objectOut.writeObject(myProtocol);//msg是一个对象
			sendBuffer = bout.toByteArray();
			//加密操作
			byte []send;
			send=protocolencrypt.encryptData(sendBuffer,key);
			// 构造发送的UDP数据包
			byte []send1=protocolencrypt.baseEncode(send);
			Log.i("mynetsendmessage","send____"+send1.length);
			udpSendPacket = new DatagramPacket(send1, send1.length, desIP, MycommandNum.MSGport);
//			if(udpSocket==null) Log.i("udpsocket","null");
			udpSocket.send(udpSendPacket);	//发送udp数据包
			udpSendPacket = null;
		//	Log.i("insendudpdata", "aftersend");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {	//发送UDP数据包出错
			e.printStackTrace();
			udpSendPacket = null;
		}
	}
	
	
}

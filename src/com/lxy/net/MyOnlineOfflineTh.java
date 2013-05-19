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

import com.lxy.beans.UserInfo;
import com.lxy.util.MyProtocol;
import com.lxy.util.MycommandNum;
import com.lxy.util.ProtocolEncrypt;

public class MyOnlineOfflineTh extends Thread {
	private DatagramPacket udpSendPacket = null;	//用于接收的udp数据包
	private byte[] sendBuffer = null;
	private ProtocolEncrypt protocolencrypt;
	private DatagramSocket udpSocket = null;	//用于接收和发送udp数据的socket
	private MyProtocol myProtocol;
    private String s="192.168.0.";
    private ByteArrayOutputStream bout;
    private ObjectOutputStream objectOut ;
	private SecretKey key;
	public MyOnlineOfflineTh(UserInfo myinfo){
		protocolencrypt=new ProtocolEncrypt();
		myProtocol=new MyProtocol();
		myProtocol.setSender(myinfo);
		try {
			udpSocket=new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		 bout = new ByteArrayOutputStream();
		 try {
			objectOut = new ObjectOutputStream(bout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 key=protocolencrypt.getSecretKey(MycommandNum.key);
	}
	@Override
	public void run() {

		for(int i=1;i<255;i++){
			try {
				sendUdpData(InetAddress.getByName(s+Integer.toString(i)));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}					
		}
	}

	public void noticeOnline( ){	// 发送上线广播
		 myProtocol.setCmd(MycommandNum.onLine);
	}
	
	public void noticeOffline(){	//发送下线广播
		myProtocol.setCmd(MycommandNum.offLine);
	}
	
	private synchronized void sendUdpData(InetAddress desIP){
      
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
			udpSendPacket = new DatagramPacket(send1, send1.length, desIP, MycommandNum.MSGport);
//			if(udpSocket==null) Log.i("udpsocket","null");
			udpSocket.send(udpSendPacket);	//发送udp数据包
			udpSendPacket = null;
			Log.i(" MyOnlineOfflineTh", "aftersend"+desIP.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {	//发送UDP数据包出错
			e.printStackTrace();
			udpSendPacket = null;
		}
	}
}

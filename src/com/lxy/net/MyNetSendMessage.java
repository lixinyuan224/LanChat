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
	private DatagramPacket udpSendPacket = null;	//���ڽ��յ�udp���ݰ�
	private byte[] sendBuffer = null;
	private ProtocolEncrypt protocolencrypt; //
	private DatagramSocket udpSocket = null;	//���ڽ��պͷ���udp���ݵ�socket
	private MyProtocol myProtocol;
	private InetAddress desIP;
	private SecretKey key;                     //
	private ByteArrayOutputStream bout;
	private ObjectOutputStream objectOut;
	public MyNetSendMessage(){
		try {
			udpSocket = new DatagramSocket();//�󶨶˿�
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
	
	private  void sendUdpData(){	//����UDP���ݰ��ķ���
		try {
			objectOut.reset();
		//	objectOut.writeByte( MSG_STR_HEAD); // д��һ����־�����ڽ��ն��ж϶������ͣ�MSG_STR_HEAD��һ���Զ��峣��
			objectOut.writeObject(myProtocol);//msg��һ������
			sendBuffer = bout.toByteArray();
			//���ܲ���
			byte []send;
			send=protocolencrypt.encryptData(sendBuffer,key);
			// ���췢�͵�UDP���ݰ�
			byte []send1=protocolencrypt.baseEncode(send);
			Log.i("mynetsendmessage","send____"+send1.length);
			udpSendPacket = new DatagramPacket(send1, send1.length, desIP, MycommandNum.MSGport);
//			if(udpSocket==null) Log.i("udpsocket","null");
			udpSocket.send(udpSendPacket);	//����udp���ݰ�
			udpSendPacket = null;
		//	Log.i("insendudpdata", "aftersend");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {	//����UDP���ݰ�����
			e.printStackTrace();
			udpSendPacket = null;
		}
	}
	
	
}

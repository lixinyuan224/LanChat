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
	private DatagramPacket udpSendPacket = null;	//���ڽ��յ�udp���ݰ�
	private byte[] sendBuffer = null;
	private ProtocolEncrypt protocolencrypt;
	private DatagramSocket udpSocket = null;	//���ڽ��պͷ���udp���ݵ�socket
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

	public void noticeOnline( ){	// �������߹㲥
		 myProtocol.setCmd(MycommandNum.onLine);
	}
	
	public void noticeOffline(){	//�������߹㲥
		myProtocol.setCmd(MycommandNum.offLine);
	}
	
	private synchronized void sendUdpData(InetAddress desIP){
      
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
			udpSendPacket = new DatagramPacket(send1, send1.length, desIP, MycommandNum.MSGport);
//			if(udpSocket==null) Log.i("udpsocket","null");
			udpSocket.send(udpSendPacket);	//����udp���ݰ�
			udpSendPacket = null;
			Log.i(" MyOnlineOfflineTh", "aftersend"+desIP.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {	//����UDP���ݰ�����
			e.printStackTrace();
			udpSendPacket = null;
		}
	}
}

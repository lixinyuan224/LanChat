package com.lxy.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.crypto.SecretKey;


import com.lxy.managers.BeanManager;
import com.lxy.util.MyProtocol;
import com.lxy.util.MycommandNum;
import com.lxy.util.ProtocolEncrypt;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
/**
 * 
 * @author lixy
 *
 */

public class MynetListener extends Thread{
	
	private DatagramSocket udpSocket = null;	//���ڽ��պͷ���udp���ݵ�socket
	private DatagramPacket udpResPacket = null;	//���ڽ��յ�udp���ݰ�
	private byte[] resBuffer ;	//�������ݵĻ���

	private boolean onWork=true;
	private SecretKey key;
	private MyOnlineOfflineTh onlineoffline;
	private BeanManager beanmanager;
//	private LayoutManager layoutManager;

	private Handler mHandler=null;//�����߳�ͨ�ž��
	private ProtocolEncrypt protocolencrypt;  //����

	private ByteArrayInputStream in;
	private ObjectInputStream objectIn;
	
	
	public MynetListener(Handler handler,BeanManager beanmanager){
		mHandler=handler;
		protocolencrypt=new ProtocolEncrypt();
		this.beanmanager=beanmanager;
		
		resBuffer=new byte[5120];
		udpResPacket=new DatagramPacket(resBuffer, 5120);
		try {
			udpSocket=new DatagramSocket(MycommandNum.MSGport);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		 key=protocolencrypt.getSecretKey(MycommandNum.key);
	}
	
	public void run() {
		while(onWork){
			try {
				udpSocket.receive(udpResPacket);
			} catch (IOException e) {
				onWork = false;
				if(udpResPacket != null){
					udpResPacket = null;
				}
				if(udpSocket != null){
					udpSocket.close();
					udpSocket = null;
				}
		
				break;
			} //end of catch
			
			if(udpResPacket.getLength() == 0){
				continue;
			}
			byte []buffer = new byte[udpResPacket.getLength()];
			System.arraycopy(resBuffer, 0, buffer, 0, udpResPacket.getLength());
			
			Log.i("MynetListener--buffer",Integer.toString(buffer.length));
			buffer=protocolencrypt.decryptData(protocolencrypt.baseDecode(buffer),key);
			if(buffer==null)
					continue;
				
			
			
			
			 in = new ByteArrayInputStream(
					buffer, 0, buffer.length);//buf �Ǵ�������յ�������
			
			MyProtocol pro = null;
			try {
				objectIn = new ObjectInputStream(in);
				pro = (MyProtocol) objectIn.readObject();//��������
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Message msg=new Message();
			msg.obj=pro;
			msg.what=MycommandNum.receive;
			mHandler.sendMessage(msg);
			
			if(udpResPacket != null){	//ÿ�ν�����UDP���ݺ����ó��ȡ�������ܻᵼ���´��յ����ݰ����ضϡ�
				resBuffer=new byte[5120];
				udpResPacket.setData(resBuffer);
				udpResPacket.setLength(5120);
			}
		}//end  of while
		
		if(udpResPacket != null){
			udpResPacket = null;
		}
		if(udpSocket != null){
			udpSocket.close();
			udpSocket = null;
		}
	
	}

	
	public void disconnectSocket(){	// ֹͣ����UDP����
		if(onWork==true){
			onWork = false;	// �����߳����б�ʶΪ������
			onlineoffline=new MyOnlineOfflineTh(beanmanager.getMyInfo());
			onlineoffline.noticeOffline();
			onlineoffline.start();
		}
	}
}

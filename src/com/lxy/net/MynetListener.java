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
	
	private DatagramSocket udpSocket = null;	//用于接收和发送udp数据的socket
	private DatagramPacket udpResPacket = null;	//用于接收的udp数据包
	private byte[] resBuffer ;	//接收数据的缓存

	private boolean onWork=true;
	private SecretKey key;
	private MyOnlineOfflineTh onlineoffline;
	private BeanManager beanmanager;
//	private LayoutManager layoutManager;

	private Handler mHandler=null;//与主线程通信句柄
	private ProtocolEncrypt protocolencrypt;  //加密

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
					buffer, 0, buffer.length);//buf 是从网络接收到的数据
			
			MyProtocol pro = null;
			try {
				objectIn = new ObjectInputStream(in);
				pro = (MyProtocol) objectIn.readObject();//读出对象
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
			
			if(udpResPacket != null){	//每次接收完UDP数据后，重置长度。否则可能会导致下次收到数据包被截断。
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

	
	public void disconnectSocket(){	// 停止监听UDP数据
		if(onWork==true){
			onWork = false;	// 设置线程运行标识为不运行
			onlineoffline=new MyOnlineOfflineTh(beanmanager.getMyInfo());
			onlineoffline.noticeOffline();
			onlineoffline.start();
		}
	}
}

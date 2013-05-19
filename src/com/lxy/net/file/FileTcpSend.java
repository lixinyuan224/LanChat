package com.lxy.net.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.crypto.SecretKey;

import com.lxy.util.BytePacking;
import com.lxy.util.MycommandNum;
import com.lxy.util.ProtocolEncrypt;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class FileTcpSend extends Thread {
	private String filePath;	//保存发送文件路径的数组
	private Socket socket;
	private BufferedInputStream bis;	
	private BufferedOutputStream bos;
	BufferedInputStream fbis;
	private byte[] readBuffer = new byte[1020];
	private String senderip;
	private ProtocolEncrypt protocolEncrypt;
	private SecretKey key;
	private Handler mHandler;
	
	public FileTcpSend(String filePath,Handler mHandler,String ip){
		this.filePath= filePath;
		this.mHandler=mHandler;
		this.senderip=ip;
		this.protocolEncrypt=new ProtocolEncrypt();
		this.key=protocolEncrypt.getSecretKey(MycommandNum.key);

	}
	public void run() {
		try {
			socket = new Socket(senderip, 5678);
			Log.i("tcpRec", "已连接上发送端---->"+filePath);
			//发送文件
			File sendFile = new File(filePath);
			fbis = new BufferedInputStream(new FileInputStream(sendFile));
			Log.i("tcpSend", "准备开始发送文件....");
			bos = new BufferedOutputStream(socket.getOutputStream());
			
			int len = 0;
			long sended = 0;	//已发送文件大小
			Long fileLength=sendFile.length();
			int temp = 0;
			
			byte desEncrypt[] = null;
			byte base64Encrypt[] = null;
	
			
			while((len = fbis.read(readBuffer)) != -1){
				Log.i("len-->",Integer.toString(len));
				byte []bynow=readBuffer;
				System.arraycopy(readBuffer, 0, bynow, 0, len);
				desEncrypt=protocolEncrypt.encryptData(bynow, key);
				Log.i("desEncrypt-->",Integer.toString(desEncrypt.length));
				base64Encrypt=protocolEncrypt.baseEncode(desEncrypt);
				Log.i("base64Encrypt-->",Integer.toString(base64Encrypt.length));
			
				bos.write(base64Encrypt, 0, base64Encrypt.length);
			
				sended += len;	//已发送文件大小
				int sendedPer = (int) (sended * 100 / fileLength);	//接收百分比
				if(temp != sendedPer){	//每增加一个百分比，发送一个message
					Message msg = new Message();
					msg.what = MycommandNum.FILEPLUS;
					msg.obj = sendedPer;
					mHandler.sendMessage(msg);
					temp = sendedPer;
				}
				bos.flush();

			}//end of while
			Log.i("tcpsend", "文件发送成功");
			
			mHandler.sendEmptyMessage(MycommandNum.FILESUCCESS);
			
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Log.e("tcpRec", "....系统不支持GBK编码");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Log.e("tcpRec", "远程IP地址错误");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("tcpRec", "文件创建失败");
		}catch (IOException e) {
			e.printStackTrace();
			Log.e("tcpRec", "发生IO错误");
		}finally{	//处理
			if(bos != null){	
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bos = null;
			}
			
			if(fbis != null){
				try {
					fbis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fbis = null;
			}
			
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bis = null;
			}
			
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				socket = null;
			}
		}
	}
}

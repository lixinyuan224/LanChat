package com.lxy.net.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.crypto.SecretKey;

import com.lxy.util.BytePacking;
import com.lxy.util.MycommandNum;
import com.lxy.util.ProtocolEncrypt;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * ɽ����ѧ��ʮ�ڿ���ѵ��
 * @author lixy
 *
 */


public class FileTcpRec extends Thread {

	private String fileName;	//�ļ���
//	private String senderIp;    //���ͷ�ip
	private String savePath;	//�ļ�����·��
	private String fileLength;
	public static ServerSocket server;	
	private Socket socket;	
//	private byte[] readBuffer = new byte[6840];
	private byte[] readBuffer = new byte[1368];
	private Handler mHandler;
	
	private BufferedInputStream bis;
	private BufferedOutputStream fbos;
	private BytePacking bytepacking;
	
	public FileTcpRec(Handler mHandler, String fileName,String fileLength){
		this.mHandler=mHandler;
		this.fileName = fileName;
//		this.senderIp = senderIp;
		this.fileLength=fileLength;
	
		this.savePath=Environment.getExternalStorageDirectory().toString() + "/"+"LanChat"+ "/"
				+ "Trasport" ;
		//�жϽ����ļ����ļ����Ƿ���ڣ��������ڣ��򴴽�
		File fileDir = new File(savePath);
		if( !fileDir.exists()){	//��������
			fileDir.mkdir();
		}
		
		try {
			server = new ServerSocket(5678);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("tcpsend", "����tcp�˿�ʧ��");
		}
		
	}
	

	public void run() {
		
		try {
			socket = server.accept();
			Log.i("tcpsend", "��IPΪ" + socket.getInetAddress().getHostAddress() + "���û�����TCP����");
			bis = new BufferedInputStream(socket.getInputStream());
			
			File receiveFile = new File(savePath,fileName);	//Ҫ���յ��ļ�
			if(receiveFile.exists()){//������ ��.���ļ���׺���ж�
				int t1=fileName.lastIndexOf(".");
				if(t1>=0){
					String s[]=new String[2];
					s[0]=fileName.substring(0, t1-1);
					s[1]=fileName.substring(t1,fileName.length());
					Log.i("fileName.split(.);", s[0]+"||"+s[1]);
					receiveFile=new File(savePath,s[0]+"1."+s[1]);
				}else{
					receiveFile=new File(savePath,fileName+"1");
				}
			}
			else {
				Log.i("receiveFile.exists()", "receiveFile.createNewFile();");
				receiveFile.createNewFile();
			}
			
			fbos = new BufferedOutputStream(new FileOutputStream(receiveFile));
			int rlen = 0;
			long sended = 0;	//�ѷ����ļ���С
			long total = (((Integer.parseInt(fileLength)/1020)*8+
					(Integer.parseInt(fileLength))))/3*4;	//�ļ��ܴ�С
			int temp = 0;
			
			bytepacking=new BytePacking(fbos);
	
			while((rlen = bis.read(readBuffer)) != -1){
				Log.i("rlen-->",Integer.toString(rlen));
				
				sended +=bytepacking.append(readBuffer,rlen);//�ѽ����ļ���С
				
//				base64Decode=protocolEncrypt.baseDecode(readBuffer);
//				Log.i("base64Decode-->",Integer.toString(base64Decode.length));
//				desDecrypt=protocolEncrypt.decryptData(base64Decode, key);
//				Log.i("desDecrypt-->",Integer.toString(desDecrypt.length));
//				fbos.write(desDecrypt, 0, desDecrypt.length);
	//			fbos.write(readBuffer, 0, rlen);
				
//				sended += rlen;	
				int sendedPer = (int) (sended * 100 / total);	//���հٷֱ�
				if(temp != sendedPer){	//ÿ����һ���ٷֱȣ�����һ��message
					Message msg = new Message();
					msg.what = MycommandNum.FILEPLUS;
					msg.obj = sendedPer;
					mHandler.sendMessage(msg);
					temp = sendedPer;
				}
//				fbos.flush();
			}//end of while
			
			bytepacking.deal();
			
			Log.i("tcpsend", "�ļ����ͳɹ�");
			mHandler.sendEmptyMessage(MycommandNum.FILESUCCESS);
			
			
			
			
			}catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				Log.e("tcpsend", "��������ʱ��ϵͳ��֧��GBK����");
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("tcpsend", "����IO����");
			} finally{
				if(fbos != null){
					try {
						fbos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fbos = null;
				}
				if(bis != null){
					try {
						bis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
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
		
		if(server != null){
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			server = null;
		}
			
	}
	
}
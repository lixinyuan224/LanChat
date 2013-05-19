package com.lxy.net.voice;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.crypto.SecretKey;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.lxy.util.MycommandNum;
import com.lxy.util.ProtocolEncrypt;

public class ThreadMicTcp extends Thread {
	private final  int Sample_Rate = 8000;
	private final  int Channel_In_Configuration = AudioFormat.CHANNEL_IN_MONO;
	private final  int AudioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	private int recBufSize;
	private AudioRecord phoneMIC;

	private Socket socket;
	private boolean stoped=true;

	private InetAddress desAdd;
	private BufferedOutputStream bos;
	
	private ProtocolEncrypt protocolEncrypt;
	private SecretKey key;
	
	public ThreadMicTcp(String host){
	
		try {
			this.desAdd = InetAddress.getByName(host);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		this.protocolEncrypt=new ProtocolEncrypt();
		this.key=protocolEncrypt.getSecretKey(MycommandNum.key);
		
		
		recBufSize = AudioRecord.getMinBufferSize(Sample_Rate,
				Channel_In_Configuration, AudioEncoding);
//		recBufSize*=16;    
//		recBufSize=640;
		Log.i("ThreadMicTcp recBufSize-->",Integer.toString(recBufSize));
		phoneMIC = new AudioRecord(MediaRecorder.AudioSource.MIC, Sample_Rate,
				Channel_In_Configuration, AudioEncoding, recBufSize);
		Log.i("mic","rconstruct ok");
	}
	
	public void run(){
		try {
			this.socket=new Socket(desAdd,MycommandNum.VOICEport);
			bos=new BufferedOutputStream(socket.getOutputStream());
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("mic","run");
		byte[] com = new byte[recBufSize];
		int length;
		
		byte[] dst;
//		byte desEncrypt[];
//		byte base64Encrypt[];
		
		phoneMIC.startRecording();
		try{
			while (stoped) {
				
				length=phoneMIC.read(com, 0, recBufSize);
				dst=new byte[length];
				System.arraycopy(com, 0, dst, 0, length);
				
//				Log.i("com.length-->",Integer.toString(com.length));
//				desEncrypt=protocolEncrypt.encryptData(dst, key);
//				Log.i("desEncrypt.length-->",Integer.toString(desEncrypt.length));
//				base64Encrypt=protocolEncrypt.baseEncode(desEncrypt);
//				Log.i("base64Encrypt.length-->",Integer.toString(base64Encrypt.length));
//				bos.write(base64Encrypt,0,base64Encrypt.length);
//				
				Log.i("ThreadMicTcp length-->",Integer.toString(length));
				
				bos.write(dst,0,length);
				bos.flush();
			}
			phoneMIC.stop();
		}catch(IOException e){
			e.printStackTrace();
		}
		Log.i("mic","run over");
		if(socket!=null){
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			socket=null;
		}
	}
	

	public void over(){
		stoped=false;
		Log.i("mic","over!");
	}
}

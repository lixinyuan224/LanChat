package com.lxy.net.voice;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.crypto.SecretKey;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.lxy.util.MycommandNum;
import com.lxy.util.ProtocolEncrypt;
import com.lxy.util.VoiceByteDeal;

public class ThreadReceiveTcp extends Thread {
	private final  int Sample_Rate = 8000;
	private final  int Channel_Out_Configuration = AudioFormat.CHANNEL_OUT_MONO;
	private final  int AudioEncoding = AudioFormat.ENCODING_PCM_16BIT;

	private AudioTrack phoneSPK;

	private boolean stoped = true;
	private int playBufSize;
	
	private byte[] resBuffer;	//接收数据的缓存
	private ServerSocket server;
	private Socket socket;
	private BufferedInputStream bis;
	
	private ProtocolEncrypt protocolEncrypt;
	private SecretKey key;
	private VoiceByteDeal vbd;
	public ThreadReceiveTcp(){
		try {
			server=new ServerSocket(MycommandNum.VOICEport);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.protocolEncrypt=new ProtocolEncrypt();
		this.key=protocolEncrypt.getSecretKey(MycommandNum.key);
		
		playBufSize=AudioTrack.getMinBufferSize(Sample_Rate,
				Channel_Out_Configuration, AudioEncoding);
		
//***		playBufSize=864; 2228
		
//		playBufSize=640;
		resBuffer = new byte[playBufSize];

		Log.i("playBufSize-->",Integer.toString(playBufSize));
		phoneSPK = new AudioTrack(AudioManager.STREAM_MUSIC, Sample_Rate,
				Channel_Out_Configuration, AudioEncoding, playBufSize,
				AudioTrack.MODE_STREAM);
		
		phoneSPK.setStereoVolume(0.8f, 0.8f);
		Log.i("receiver","construct ok");
	}
	
	public void run(){
		try {
			socket=server.accept();
			bis=new BufferedInputStream(socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Log.i("receiver","run start");
		phoneSPK.play();
		if(socket==null)
		Log.i("socket","null");
		int length = 0;
//		vbd=new VoiceByteDeal(phoneSPK, 864);
//		vbd=new VoiceByteDeal(phoneSPK, 640);
		while(stoped){
			try {
				length=bis.read(resBuffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
//			vbd.append(resBuffer, length);
			
			Log.i("ThreadReceiveTcp while(stoped) length-->",Integer.toString(length));
			phoneSPK.write(resBuffer, 0, length);
			
		}//end of while
		phoneSPK.stop();
		Log.i("receiver","run over");
		
		if(socket != null){
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			socket = null;
		}
		if(server!=null){
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			server=null;
		}
	}
	
	public void over(){
		stoped=false;
	}
}

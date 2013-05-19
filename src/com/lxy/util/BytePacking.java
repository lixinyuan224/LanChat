package com.lxy.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.crypto.SecretKey;

import android.util.Log;

public class BytePacking implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private byte []data=null;
	private ProtocolEncrypt protocolEncrypt;
	private SecretKey key;
	private BufferedOutputStream fbos;
	public BytePacking(BufferedOutputStream fboss){
		this.protocolEncrypt=new ProtocolEncrypt();
		this.key=protocolEncrypt.getSecretKey(MycommandNum.key);
		this.fbos=fboss;
	}
	public int append(byte[] p,int length){
		if(data==null){
			data=new byte[length];
			System.arraycopy(p, 0, data, 0, length);
		}else{
			byte []b=new byte[data.length+length];
			System.arraycopy(data, 0, b, 0, data.length);
			System.arraycopy(p, 0, b, data.length, length);
			data=b;
		}
		return p.length;
	}
	 
	public void deal(){
		byte desDecrypt[];
		byte base64Decode[];
		Log.i("data.length%1368---->", Integer.toString(data.length%1368));
		
		for(int i=0;i<data.length;i+=1368){
			byte[] readBuffer=new byte[1368];
			System.arraycopy(data,i, readBuffer,0,1368);
			
			base64Decode=protocolEncrypt.baseDecode(readBuffer);
			Log.i("base64Decode-->",Integer.toString(base64Decode.length));
			desDecrypt=protocolEncrypt.decryptData(base64Decode, key);
			Log.i("desDecrypt-->",Integer.toString(desDecrypt.length));
			try {
				fbos.write(desDecrypt, 0, desDecrypt.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	public byte [] getData() {
		return data;
	}

	public void setData(byte [] data) {
		this.data = data;
	}
}

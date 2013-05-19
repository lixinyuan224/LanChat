package com.lxy.net.file;

import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.crypto.SecretKey;

import android.util.Log;

import com.lxy.util.PacketBytes;
import com.lxy.util.ProtocolEncrypt;


/**
 * 山西大学第十期科研训练
 * @author lixy
 *
 */

public class FileByteDeal implements PacketBytes {
	private byte []stack;
	private ProtocolEncrypt protocolEncrypt;
	private SecretKey key;
	private BufferedOutputStream fbos;
	private int dealLen;
	public FileByteDeal(BufferedOutputStream fbos,int dealLen){
		this.fbos=fbos;
		this.dealLen=dealLen;
	}
	
	public boolean append(byte[] ab, int len) {
		if(stack==null&&len<dealLen){
			stack=new byte[len];
			System.arraycopy(ab, 0, stack, 0, len);
			return false;
		}else if(stack==null&&len==dealLen){
			dealWith(ab);
		}else if(stack.length+len<dealLen){
			byte[] re = new byte[stack.length+len];
			System.arraycopy(stack, 0, re, 0, stack.length);
			System.arraycopy(ab, 0, re, stack.length, len);
			stack=re;
			return false;
		}else if(stack.length+len==dealLen){
			byte[] re = new byte[dealLen];
			System.arraycopy(stack, 0, re, 0, stack.length);
			System.arraycopy(ab, 0, re, stack.length, len);
			dealWith(re);
			return true;
		}else if(stack.length+len>dealLen){
			byte[] re = new byte[dealLen];
			int length=stack.length+len;
			System.arraycopy(stack, 0, re, 0, length);
			System.arraycopy(ab, 0, re, length, dealLen);
			stack=new byte[length-dealLen];
			System.arraycopy(ab, dealLen, stack, 0, length-dealLen);
			dealWith(re);
			return true;
		};
		return false;
		
	}

	public void dealWith(byte[] all) {
		byte desDecrypt[];
		byte base64Decode[];
		base64Decode=protocolEncrypt.baseDecode(all);
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

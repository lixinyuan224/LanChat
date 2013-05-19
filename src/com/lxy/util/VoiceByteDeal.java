package com.lxy.util;

import javax.crypto.SecretKey;

import android.media.AudioTrack;
import android.util.Log;

/**
 * 山西大学第十期科研训练
 * @author lixy
 *
 */

public class VoiceByteDeal implements PacketBytes{
	
	private ProtocolEncrypt protocolEncrypt;
	private SecretKey key;
	private AudioTrack phoneSPK;
	private byte []stack;
	private int dealLen;
	
	public VoiceByteDeal(AudioTrack spk,int dealLen){
		this.protocolEncrypt=new ProtocolEncrypt();
		this.key=protocolEncrypt.getSecretKey(MycommandNum.key);
		this.phoneSPK=spk;
		this.dealLen=dealLen;
	}

	public boolean append(byte[] ab,int len) {
		Log.i("append(byte[] ab,int len) -->",Integer.toString(len));
		if(stack==null){
			if(len<dealLen){
				Log.i("append(byte[] ab,int len) -->","new stack");
				stack=new byte[len];
				System.arraycopy(ab, 0, stack, 0, len);
				return false;
			}
			if(len==dealLen){
				Log.i("stack==null&&len==dealLen -->","dealWith(ab)");
				dealWith(ab);
			}
		}else if(stack!=null){
			 if(stack.length+len<dealLen){
				byte[] re = new byte[stack.length+len];
				Log.i("stack.length+len<dealLen-->","new byte[stack.length+len];");
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
				int sl=dealLen-stack.length;
				System.arraycopy(stack, 0, re, 0, stack.length);
				System.arraycopy(ab, 0, re, stack.length, sl);
				stack=new byte[len-sl];
				System.arraycopy(ab, sl, stack, 0, len-sl);
				dealWith(re);
				return true;
			}
		}
		return false;
	}

	public void dealWith(byte[] all) {
//		byte desDecrypt[];
//		byte base64Decode[];
//			Log.i("all.length",Integer.toString(all.length));
//		base64Decode=protocolEncrypt.baseDecode(all);
//			Log.i("base64Decode-->",Integer.toString(base64Decode.length));
//		desDecrypt=protocolEncrypt.decryptData(base64Decode, key);
//			Log.i("desDecrypt-->",Integer.toString(desDecrypt.length));
//		
//		phoneSPK.write(desDecrypt, 0, desDecrypt.length);
		
		phoneSPK.write(all, 0, all.length);
	}
}

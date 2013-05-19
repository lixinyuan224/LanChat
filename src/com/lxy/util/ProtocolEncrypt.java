package com.lxy.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
	/**
	 * 
	 * @author lixy
	 *
	 */

public class ProtocolEncrypt{
    private    String  Algorithm ="DES";
    //定义 加密算法,可用 DES,DESede,Blowfish
    private Base64 base64=new Base64();
    private byte []iv=new byte[]{1,2,3,4,5,6,7,8};
    public byte[] baseEncode(byte[] s){
    	return base64.encode(s);
    }
    
    public byte[] baseDecode(byte[] s){
    	return base64.decode(s);
    }
  
     //生成密钥
    public SecretKey getSecretKey (String seed) 
    {
        DESKeySpec dks;
        SecretKeyFactory keyFactory;
        SecretKey secretKey = null;
		try {
			 dks = new DESKeySpec(seed.getBytes());
			 keyFactory = SecretKeyFactory.getInstance(Algorithm);
			 secretKey = keyFactory.generateSecret(dks);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
     
        return  secretKey;
    }    
    //加密 源数据  密钥
    public    byte [] encryptData (byte [] input ,SecretKey  deskey)
    {
         Cipher c1;
         byte []cipherByte = null;
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(iv);   
			c1 = Cipher.getInstance ("DES/CBC/PKCS5Padding");
			c1.init (Cipher.ENCRYPT_MODE ,deskey ,zeroIv);
			cipherByte =c1.doFinal(input);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
         
         return  cipherByte;
    }
    
    //解密
    public  byte [] decryptData (byte [] input ,SecretKey  deskey) 
    {
        Cipher c1;
        byte [] clearByte = null;
		try {
			c1 = Cipher.getInstance ("DES/CBC/PKCS5Padding");//AES/CBC/PKCS5Padding
			IvParameterSpec zeroIv = new IvParameterSpec(iv);   
			c1.init (Cipher.DECRYPT_MODE ,deskey,zeroIv);
			clearByte =c1.doFinal (input);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
        return  clearByte;
    }
}
package com.lxy.net.file;


import android.os.Handler;

public class FileControl {
	private FileTcpSend fileTcpSend;
	private FileTcpRec fileTcpRec;
	
	private Handler mHandler;
	public FileControl(Handler mHandler){
		this.mHandler=mHandler;
	}
	public void startFileRec(String fileName,String fileLength){//接收文件线程
		fileTcpRec=new FileTcpRec(mHandler, fileName,fileLength);
		fileTcpRec.start();
	}
	
	public void startFileSend(String filePath,String ip){//发送文件线程
		fileTcpSend=new FileTcpSend(filePath,mHandler,ip);
		fileTcpSend.start();
	}
	
	
}
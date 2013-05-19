package com.lxy.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import com.lxy.beans.UserInfo;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
//import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


/*
 * 想用xml存储个人信息数据
 */
public class FindMyinfo {
	private UserInfo user=null;
	private File userFile=null;
	private String myDir=null;
	private Scanner scanner;
	private FileReader fr;
	private FileWriter fw;
	private Context context;
	public  FindMyinfo(Context context) {
		this.context=context;
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//有无sd卡
			myDir = Environment.getExternalStorageDirectory().toString() + "/"
					+ "LanChat"+ "/" + "userInfo" + "/";
			File f=new File(myDir);
			if(!f.exists())
				f.mkdirs();	
			userFile = new File(myDir,"user.txt");
		}else{
			Toast.makeText(context, "无sd卡", Toast.LENGTH_SHORT).show();
			
		}
		
	}

	public boolean findUser() {
		
		if (userFile!=null&&userFile.exists()) {
			try {
				fr=new FileReader(userFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			scanner=new Scanner(fr);
			String line=scanner.nextLine();
			String []tokens=line.split("\\|");
			//Log.i("before if in findmyuerinfo", "before");
			if (tokens.length == 5) {
				Log.i("if in findmyuerinfo", "in");
				String name = tokens[0];
				String sex = tokens[1];
				String autograph = tokens[2];
				String headImg=tokens[3];
				String truename=tokens[4];
					//Log.i("if in findmyuerinfo", name+"  "+sex+"  "+autograph+"  "+headImg);
				String ip = myIp();
					//Log.i("my ip", ip);
//				String id=myId();
					//Log.i("my id", id);
				String id="0";
				
				user = new UserInfo(id,-1,name, truename, sex, autograph, ip,Integer.parseInt(headImg));
			}
		}
		if(user==null){
			Log.i("in if in findmyuerinfo", "user is null");
			return false;
		}
		else return true;
			//Log.i("before if in findmyuerinfo", user.getIp());
	}

	public void creatUser(String name,String truename,//写入文件新信息
			String sex,String autograph,int headId){
		try {
			fw=new FileWriter(userFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PrintWriter pw=new PrintWriter(fw);//this is a erro
		pw.println(name+"|"+sex+"|"+autograph+"|"+headId+"|"+truename);
		pw.flush();
		pw.close();
		findUser();
	}
	
	public void creatUser(UserInfo u){
		try {
			fw=new FileWriter(userFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PrintWriter pw=new PrintWriter(fw);//this is a erro
		pw.println(u.getnickname()+"|"+u.getSex()+"|"+u.getSignature()+"|"+u.getHeadImgId()+"|"+u.getTruename());
		pw.flush();
		pw.close();
		findUser();
	}
	
	public UserInfo getUserinfo(){
		return user;
	}
	
	public String myIp(){
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifi.getConnectionInfo();  
		int longIp = wifiInfo.getIpAddress();  
		StringBuffer sb = new StringBuffer("");
		sb.append(String.valueOf((longIp & 0x000000FF)));// 将高24位置0
		sb.append(".");
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));// 将高16位置0，然后右移8位
		sb.append(".");
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));// 将高8位置0，然后右移16位
		sb.append(".");
		sb.append(String.valueOf((longIp >>> 24)));// 直接右移24位
		Log.i("ip",sb.toString());
		return sb.toString();
	}

	public  String myId(){
//		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//		String phoneId = tm.getLine1Number();
//		return phoneId;
		return "0";
	}
	
}

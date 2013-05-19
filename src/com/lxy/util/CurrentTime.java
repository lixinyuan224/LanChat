package com.lxy.util;

import java.util.Calendar;
	/**
	 * 
	 * @author lixy
	 *
	 */
public class CurrentTime {
	public static String getTime(){
		 Calendar c=Calendar.getInstance();
		  
		  String time=c.get(Calendar.YEAR)+"-"+                               //得到年
		  formatTime(c.get(Calendar.MONTH)+1)+"-"+//month加一    //月
		  formatTime(c.get(Calendar.DAY_OF_MONTH))+" "+           //日
		  formatTime(c.get(Calendar.HOUR_OF_DAY))+":"+              //时
		  formatTime(c.get(Calendar.MINUTE))+":"+                           //分
		  formatTime(c.get(Calendar.SECOND));  
		 
		  return time;
	}
	public static long getTim(){
		return System.currentTimeMillis();
	}
	private static String formatTime(int t){
		  return t>=10? ""+t:"0"+t;//三元运算符 t>10时取 ""+t
		 }
}

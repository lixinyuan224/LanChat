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
		  
		  String time=c.get(Calendar.YEAR)+"-"+                               //�õ���
		  formatTime(c.get(Calendar.MONTH)+1)+"-"+//month��һ    //��
		  formatTime(c.get(Calendar.DAY_OF_MONTH))+" "+           //��
		  formatTime(c.get(Calendar.HOUR_OF_DAY))+":"+              //ʱ
		  formatTime(c.get(Calendar.MINUTE))+":"+                           //��
		  formatTime(c.get(Calendar.SECOND));  
		 
		  return time;
	}
	public static long getTim(){
		return System.currentTimeMillis();
	}
	private static String formatTime(int t){
		  return t>=10? ""+t:"0"+t;//��Ԫ����� t>10ʱȡ ""+t
		 }
}

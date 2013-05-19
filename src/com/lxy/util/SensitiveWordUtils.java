package com.lxy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.content.Context;
//import android.util.Log;

public class SensitiveWordUtils  {

	private ArrayList<String> first = new ArrayList<String>();
	private String[] sortFirst;
	private char[] charFirst;
	private HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
	private HashMap<String, String[]> sortMap = new HashMap<String, String[]>();
	private HashMap<String, char[]> charMap = new HashMap<String, char[]>();

	private ArrayList<String> temp;
	private String key, value;
	private int length;
	
	public SensitiveWordUtils(final Context c) {
		new Thread(new Runnable() {
			public void run() {
				init(c);
			}
		}).start();
	}
	
	public void init(Context c){
		InputStreamReader in = null;
		BufferedReader bufReader = null;
		try {
			in =new InputStreamReader(c.getResources().getAssets().open("SensitiveWord1.txt"),"GBK");
		    bufReader = new BufferedReader(in);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<String> keys=new ArrayList<String>();
	
		Scanner scanner=new Scanner(bufReader);
		int ii=0;
		while(scanner.hasNext()){
			keys.add(scanner.nextLine());
			ii+=1;
			
		}
		System.out.println("ok"+ii);
		
//		Log.i(" sensitive=new SensitiveWordUtils(in);", Integer.toString(ii));
		for (String k : keys) {
			if (!first.contains(k.substring(0, 1))) {
				first.add(k.substring(0, 1));
			}
			length = k.length();
			for (int i = 1; i < length; i++) {
				key = k.substring(0, i);
				value = k.substring(i, i + 1);
				if (i == 1 && !first.contains(key)) {
					first.add(key);
				}

				// 有，添加
				if (map.containsKey(key)) {
					if (!map.get(key).contains(value)) {
						map.get(key).add(value);
					}
				}else {
					temp = new ArrayList<String>();
					temp.add(value);
					map.put(key, temp);
				}
			}
		}
		sortFirst = first.toArray(new String[first.size()]);
		Arrays.sort(sortFirst); // 排序

		charFirst = new char[first.size()];
		for (int i = 0; i < charFirst.length; i++) {
			charFirst[i] = first.get(i).charAt(0);
		}
		Arrays.sort(charFirst); // 排序

		String[] sortValue;
		ArrayList<String> v;
		Map.Entry<String, ArrayList<String>> entry;
		Iterator<Entry<String, ArrayList<String>>> iter = map.entrySet()
				.iterator();
		while (iter.hasNext()) {
			entry = (Map.Entry<String, ArrayList<String>>) iter.next();
			v = (ArrayList<String>) entry.getValue();
			sortValue = v.toArray(new String[v.size()]);
			Arrays.sort(sortValue); // 排序
			sortMap.put(entry.getKey(), sortValue);
		}

		char[] charValue;
		iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			entry = (Map.Entry<String, ArrayList<String>>) iter.next();
			v = (ArrayList<String>) entry.getValue();
			charValue = new char[v.size()];
			for (int i = 0; i < charValue.length; i++) {
				charValue[i] = v.get(i).charAt(0);
			}
			Arrays.sort(charValue); // 排序
			charMap.put(entry.getKey(), charValue);
		}
		scanner.close();
	}
	
	public String replace(final String content) {
		ExecutorService es = Executors.newFixedThreadPool(1);     
		Callable<String> call=new Callable<String>() {
			public String call() throws Exception {
				String r = null, f, c = content;
				String replacedword = content;
				char g;
				char[] temps;
				int length = c.length();
				if(length==1){
					if(first.contains(c)){
					String str = "*";
					replacedword = c.replace(c, str);
					return replacedword;
					}
				}
				for (int i = 0; i < length - 1; i++) {
					g = c.charAt(i);
					// 二分查找
					if (Arrays.binarySearch(charFirst, g) > -1) {
						 for (int j = i + 1; j < length; j++) {
							f = c.substring(i, j);
							g = c.charAt(j);
							temps = charMap.get(f);
							if (temps == null) { // 找到了
//								System.out.println("ok");
								r = f;
								String str = "";
								for (int m = 1; m <= r.length(); m++) {
									str = str + "*";
								}
								replacedword = c.replace(r, str);
								c = replacedword;
								break ;
							}
							// 二分查找
							if (Arrays.binarySearch(temps, g) > -1) {
								if (j == length - 1) {
									// print("find!");
//									System.out.println("find!");
									r = c.substring(i, j + 1);
									String str = "";
									for (int m = 1; m <= r.length(); m++) {
										str = str + "*";
									}
									replacedword = c.replace(r, str);
									c = replacedword;
									break ;
								}
							} else { // 没有找到了
								break;
							}
						}
					}
				}
				return replacedword;
			}
		};
		
		Future<String> fu=es.submit(call);
		
		String s = null;
		try {
			s = fu.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return s;
	}
	
	public String re(String content){
		String r = null, f, c = content;
		String replacedword = content;
		char g;
		char[] temps;
		int length = c.length();
		if(length==1){
			if(first.contains(c)){
			String str = "*";
			replacedword = c.replace(c, str);
			return replacedword;
			}
		}
		for (int i = 0; i < length - 1; i++) {
			g = c.charAt(i);
			// 二分查找
			if (Arrays.binarySearch(charFirst, g) > -1) {
				 for (int j = i + 1; j < length; j++) {
					f = c.substring(i, j);
					g = c.charAt(j);
					temps = charMap.get(f);
					if (temps == null) { // 找到了
//						System.out.println("ok");
						r = f;
						String str = "";
						for (int m = 1; m <= r.length(); m++) {
							str = str + "*";
						}
						replacedword = c.replace(r, str);
						c = replacedword;
						break ;
					}
					// 二分查找
					if (Arrays.binarySearch(temps, g) > -1) {
						if (j == length - 1) {
							// print("find!");
//							System.out.println("find!");
							r = c.substring(i, j + 1);
							String str = "";
							for (int m = 1; m <= r.length(); m++) {
								str = str + "*";
							}
							replacedword = c.replace(r, str);
							c = replacedword;
							break ;
						}
					} else { // 没有找到了
						break;
					}
				}
			}
		}
		return replacedword;
	}

	
}

package com.lxy.layoutInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.widget.ListView;


public class MyFileChooseSetting {
	private List<File> allFileitems ;
	private List<Map<String, Object>> allFilenames;
	private File_list_Thread filelistth;
	private ListView list;
	
	
	public MyFileChooseSetting(){
		allFileitems = new ArrayList<File>();
		allFilenames = new ArrayList<Map<String, Object>>();
	}
	public File_list_Thread getFilelistth() {
		return filelistth;
	}
	public void setFilelistth(File_list_Thread filelistth) {
		this.filelistth = filelistth;
	}
	public void setAllFileitems(List<File> allFileitems) {
		this.allFileitems = allFileitems;
	}
	public void setAllFilenames(List<Map<String, Object>> allFilenames) {
		this.allFilenames = allFilenames;
	}
	
	public List<File> getAllFileitems() {
		return allFileitems;
	}
	public List<Map<String, Object>> getAllFilenames() {
		return allFilenames;
	}
	public ListView getList() {
		return list;
	}
	public void setList(ListView list) {
		this.list = list;
		
	}
	

	
	
}
		
		



	
package com.lxy.layoutInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.SimpleAdapter;

import com.lxy.LanChat.R;
import com.lxy.managers.LayoutManager;

public class File_list_Thread extends AsyncTask<File, File, String> {
	private  LayoutManager layoutManager;
	private Context context;
	public File_list_Thread(LayoutManager layoutmanager,Context context){
		 this.layoutManager=layoutmanager;
		 this.context=context;
	 }
	@Override
	protected String doInBackground(File... params) {
		if (!params[0].getPath().equals(java.io.File.separator)) {
			Map<String, Object> fileItem = new HashMap<String, Object>();
		
			fileItem.put("foldimg", R.drawable.png_1638);
			fileItem.put("filename", getName(params[0].getParentFile()));
			layoutManager.getMyfilechoose().getAllFilenames().add(fileItem);
			layoutManager.getMyfilechoose().getAllFileitems().add(params[0].getParentFile());
		}
		if (params[0].isDirectory()) {
			File temfile[] = getSort(params[0].listFiles());
			if (temfile != null) {
				for (int x = 0; x < temfile.length; ++x) {
					this.publishProgress(temfile[x]);
				}
			}
		}
		return null;
	}

	protected void onProgressUpdate(File... values) {
		Map<String, Object> fileItem = new HashMap<String, Object>();
		if (values[0].isDirectory()) {
			fileItem.put("foldimg", R.drawable.fold);
		} else {
			fileItem.put("foldimg", R.drawable.text);
		}
		fileItem.put("filename", getName(values[0]));
		layoutManager.getMyfilechoose().getAllFileitems().add(values[0]);
		layoutManager.getMyfilechoose().getAllFilenames().add(fileItem);
		
		SimpleAdapter simple  = new SimpleAdapter(context,
				layoutManager.getMyfilechoose().getAllFilenames(), R.layout.layout_filelistview,
				new String[] { "foldimg", "filename" }, new int[] { R.id.foldimg,
						R.id.filename,});
		layoutManager.getMyfilechoose().getList().setAdapter(simple);
	}


    private String getName(File path){
    	String pathString=path.toString();
    	String name=pathString.substring(pathString.lastIndexOf("/")+1,pathString.length());
    	if(name.equals("")) name="/";
    	return name;
    }
    private File[] getSort(File[] file){
    	ArrayList<File> tem1=new ArrayList<File>();
    	ArrayList<File> tem2=new ArrayList<File>();
    	File []ff=new File[file.length];
    	for(int j=0;j<file.length;j++){
    		if(file[j].isDirectory())
    			tem1.add(file[j]);
    		else
    			tem2.add(file[j]);
    	}
    	Collections.sort(tem1);
    	Collections.sort(tem2);
    	tem1.addAll(tem2);
    	tem1.toArray(ff);
    	return ff;
    }
}

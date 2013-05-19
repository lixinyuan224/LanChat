package com.lxy.util;

import com.lxy.LanChat.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
//import android.util.Log;
	/**
	 * 
	 * @author lixy
	 *
	 */
public class SoundPlayThread extends Thread {
	private SoundPool soundPool;
	private int  soundlist;
	private AudioManager mgr;
	private float volume;
	
	public SoundPlayThread(Context context,int id){
		soundPool=new SoundPool(5, AudioManager.STREAM_MUSIC, 50);
		mgr=(AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		float svc=mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		float svm=mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volume=svc/svm;
		soundlist=id;
		
		switch(soundlist){
			case 1:{
				soundlist=soundPool.load(context, R.raw.askfile, 1);
			}break;
			case 2:{
				soundlist=soundPool.load(context, R.raw.filesended, 1);	
			}break;
			case 3:{
				soundlist=soundPool.load(context, R.raw.msg,1);
			}break;
			case 4:{
				soundlist=soundPool.load(context, R.raw.online,1);
			}break;
			case 5:{
				soundlist=soundPool.load(context, R.raw.voicesend,1);
			}break;
		}
		
//		Log.i("soundlist0",Integer.toString(soundlist[0]));

	}
	

	@Override
	public void run() {
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				soundPool.play(soundlist, volume,volume, 1, 0,1.0f);
			}
		});
	
	}

}

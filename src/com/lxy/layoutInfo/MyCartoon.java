package com.lxy.layoutInfo;

import com.lxy.LanChat.MainActivity;
import com.lxy.LanChat.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyCartoon extends SurfaceView implements SurfaceHolder.Callback{
	MainActivity activity;
	Paint paint;
	int currentAlpha=0;
	int screenWidth=480;
	int screenHeight=800;
	int sleepSpan=50;
	Bitmap[] logos=new Bitmap[2];
	Bitmap currentLogo;
	int currentX;
	int currentY;
	Handler handler;
	
	public MyCartoon(MainActivity activity,Handler handler) {
		super(activity);
		this.activity=activity;
		DisplayMetrics metric = new DisplayMetrics();
		this.activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth= metric.widthPixels;     // ÆÁÄ»¿í¶È£¨ÏñËØ£©
		screenHeight= metric.heightPixels;   // ÆÁÄ»¸ß¶È£¨ÏñËØ£©
		this.handler=handler;
		this.getHolder().addCallback(this);
		paint=new Paint();
		paint.setAntiAlias(true);
		//ÒªÐÞ¸ÄÆô¶¯»­Ãæ
		logos[0]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.android);
		logos[1]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.wel_1);
		
	}
	public void onDraw(Canvas canvas){
		paint.setColor(Color.BLACK);
		paint.setAlpha(255);
		canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
		
		if(currentLogo==null) return;
		paint.setAlpha(currentAlpha);
	
		canvas.drawBitmap(currentLogo, currentX, currentY,paint);
	
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(){
			public void run(){
				currentLogo=logos[0];
					for(int i=255;i>-10;i-=10){
						currentAlpha=i;
						if(currentAlpha<0){
							currentAlpha=0;
						}
						if(i%25==0){
						       Matrix matrix=new Matrix();
					           matrix.postScale(0.9f, 0.9f);
					           currentLogo=Bitmap.createBitmap(currentLogo,0,0,currentLogo.getWidth(),
					            		currentLogo.getHeight(),matrix,true);
					           
						}
						currentX=screenWidth/2-currentLogo.getWidth()/2;
						currentY=screenHeight/2-currentLogo.getHeight()/2;
						
						SurfaceHolder myholder=MyCartoon.this.getHolder();
						Canvas canvas=myholder.lockCanvas();
						try{
							synchronized(myholder){
								onDraw(canvas);
							}
						}catch(Exception e){
							
						}finally{
							if(canvas!=null)
								myholder.unlockCanvasAndPost(canvas);
						}
						
						try {
							if(i==255){
								Thread.sleep(1000);
							}
							Thread.sleep(sleepSpan);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					
					}
					SurfaceHolder myholder=MyCartoon.this.getHolder();
					Canvas canvas=myholder.lockCanvas();
					currentLogo=logos[1];
					currentX=screenWidth/2-currentLogo.getWidth()/2;
					currentY=screenHeight/2-currentLogo.getHeight()/2;
					currentAlpha=255;
					onDraw(canvas);
					try{
						synchronized(myholder){
							onDraw(canvas);
						}
					}catch(Exception e){
						
					}finally{
						if(canvas!=null)
							myholder.unlockCanvasAndPost(canvas);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(1);
				}
		}.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}

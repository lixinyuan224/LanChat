package com.lxy.layoutInfo;


import com.lxy.LanChat.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ExpressionAdapter  extends BaseAdapter{

	private Context		mContext;
	// ������������ ��ͼƬԴ
	private Integer[]	mImageIds	= 
	{ 
			R.drawable.png_001,R.drawable.png_002,
			R.drawable.png_003,R.drawable.png_004,R.drawable.png_005,
			R.drawable.png_006,R.drawable.png_007,R.drawable.png_008,
			R.drawable.png_009,R.drawable.png_010,R.drawable.png_011,
			R.drawable.png_009,R.drawable.png_015,R.drawable.png_010 ,
			R.drawable.png_011 ,R.drawable.png_012 ,
			R.drawable.png_013 ,R.drawable.png_014 ,
			R.drawable.png_016 ,R.drawable.png_017 ,R.drawable.png_018 ,
			R.drawable.png_019 ,R.drawable.png_020 ,R.drawable.png_021 ,
			R.drawable.png_022 ,R.drawable.png_023 ,R.drawable.png_024 ,
			R.drawable.png_025 ,R.drawable.png_026 ,R.drawable.png_027
	};
	public int getImageIds(int i){
		return mImageIds[i];
	}

	public ExpressionAdapter(Context c)
	{
		mContext = c;
	}

	// ��ȡͼƬ�ĸ���
	public int getCount()
	{
		return mImageIds.length;
	}

	// ��ȡͼƬ�ڿ��е�λ��
	public Object getItem(int position)
	{
		return position;
	}


	// ��ȡͼƬID
	public long getItemId(int position)
	{
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null)
		{
			// ��ImageView������Դ
			imageView = new ImageView(mContext);
			// ���ò��� ͼƬ20��20��ʾ
			imageView.setLayoutParams(new GridView.LayoutParams(60, 60));
			// ������ʾ��������
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		}
		else
		{
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(mImageIds[position]);
		return imageView;
	}

}

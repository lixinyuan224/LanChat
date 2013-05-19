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
	// 定义整型数组 即图片源
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

	// 获取图片的个数
	public int getCount()
	{
		return mImageIds.length;
	}

	// 获取图片在库中的位置
	public Object getItem(int position)
	{
		return position;
	}


	// 获取图片ID
	public long getItemId(int position)
	{
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null)
		{
			// 给ImageView设置资源
			imageView = new ImageView(mContext);
			// 设置布局 图片20×20显示
			imageView.setLayoutParams(new GridView.LayoutParams(60, 60));
			// 设置显示比例类型
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

package com.lxy.layoutInfo;

import com.lxy.LanChat.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter
{
	// 定义Context
	private Context		mContext;
	// 定义整型数组 即图片源
	private Integer[]	mImageIds	= 
	{ 
			R.drawable.head_01 ,R.drawable.head_02 ,R.drawable.head_03 ,
			R.drawable.head_04 ,R.drawable.head_05 ,R.drawable.head_06 ,
			R.drawable.head_07 ,R.drawable.head_08 ,R.drawable.head_09 ,
			R.drawable.head_10 ,R.drawable.head_11 ,R.drawable.head_12 
	};
	public int getImageIds(int i){
		return mImageIds[i];
	}

	public ImageAdapter(Context c)
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
			// 设置布局 图片90×90显示
			imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
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


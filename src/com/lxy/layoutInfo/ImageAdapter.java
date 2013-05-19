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
	// ����Context
	private Context		mContext;
	// ������������ ��ͼƬԴ
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
			// ���ò��� ͼƬ90��90��ʾ
			imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
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


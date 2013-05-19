package com.lxy.layoutInfo;

import java.util.List;

import com.lxy.LanChat.R;
import com.lxy.beans.ChatEntity;
import com.lxy.beans.ChatHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter  {
	private Context context = null;
	private List<ChatEntity> chatList = null;
	private LayoutInflater inflater = null;
	private int COME_MSG = 0;
	private int TO_MSG = 1;

	public ChatAdapter(Context context, List<ChatEntity> chatList) {
		this.context = context;
		this.chatList = chatList;
	}
	
	public void setchatAdapter(Context context, List<ChatEntity> chatList) {
		this.context = context;
		this.chatList = chatList;
	}

	public int getCount() {
		return chatList.size();
	}

	public Object getItem(int position) {
		return chatList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		
		ChatEntity entity = chatList.get(position);
		if (entity.isMyMsg()) {
			return COME_MSG;
		} else {
			entity.toSpannable(context);
			return TO_MSG;
		}
	}

	@Override
	public int getViewTypeCount() {
		
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ChatHolder chatHolder = null;
		inflater = LayoutInflater.from(this.context);
		if (convertView == null) {
			chatHolder = new ChatHolder();
			if (chatList.get(position).isMyMsg()) {
				convertView = inflater.inflate(R.layout.message_come, null);
			} else {
				convertView = inflater.inflate(R.layout.message_send, null);
			}
			chatHolder.setTimeTextView((TextView) convertView.findViewById(R.id.tv_time));
			chatHolder.setContentTextView((TextView) convertView.findViewById(R.id.tv_content));
			chatHolder.setUserImageView((ImageView) convertView.findViewById(R.id.iv_user_image));
			convertView.setTag(chatHolder);
		} else {
			chatHolder = (ChatHolder) convertView.getTag();
		}
		
		chatHolder.getTimeTextView().setText(
				chatList.get(position).getChatTime());
		chatHolder.getContentTextView().setText(
				chatList.get(position).getContent());
		Log.i("getView", Integer.toString(chatList.get(position).getUserImage()));
		chatHolder.getUserImageView().setImageResource(
				chatList.get(position).getUserImage());

		return convertView;
	}

	

}
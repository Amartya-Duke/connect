package com.nect.connect.emoji;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class EmojiAdapter extends ArrayAdapter<Integer>{
	

	private int resource;
	private Context context;
	private Integer[] emojiItems;
	
	
	public EmojiAdapter(Context context, int resource, Integer[] emojiItem) {
		super(context, resource, emojiItem);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.resource=resource;
		this.emojiItems=emojiItem;
	}

	public View getView(int position,View convertView,ViewGroup parent)
	{
		ImageView iView;
		if(convertView==null)
		{
			iView=new ImageView(context);
			iView.setLayoutParams(new GridView.LayoutParams(100,100));
			iView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			iView.setPadding(10, 10, 10, 10);
			iView.setTag(emojiItems[position]);
		}
		else
			iView=(ImageView)convertView;
	
		iView.setImageResource(emojiItems[position]);
		
		return iView;
		
	}

}

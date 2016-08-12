package com.nect.connect.emoji;



import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.nect.connect.R;

public class EmojiFragment extends Fragment {
	
	private OnEmojiSelectedListner emojiSelectedListner;
	
	
	public static EmojiFragment  newInstance()
	{
		return new EmojiFragment();
	}

	public EmojiFragment(){
		emojiSelectedListner=(OnEmojiSelectedListner)getActivity();
	}
	
	
	
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{
	View rootView=	inflater.inflate(R.layout.emoji_grid, null, false);
	GridView gView=(GridView)rootView.findViewById(R.id.emoji_gridView);
	gView.setOnItemClickListener(new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) { 
			// TODO Auto-generated method stub
			Log.e("position",position+"");
			emojiSelectedListner.onEmojiSelected(position+1);
		Log.e("char",	EmojiItem.getEmojiChar(position)+"");
		}
		
	});
	gView.setAdapter(new EmojiAdapter(getActivity(), 0, EmojiItem.getEmpjiIds()));
		return rootView;
		
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try{
			emojiSelectedListner=(OnEmojiSelectedListner)activity;
		}
		catch(ClassCastException ex)
		{
			throw new ClassCastException(activity.toString()+"must implement this interface");
		}
	}
	
	public interface OnEmojiSelectedListner {
public  void onEmojiSelected(int position);
	}
}

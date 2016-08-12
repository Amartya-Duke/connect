package com.nect.connect.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nect.connect.R;
import com.nect.connect.convorows;
import com.nect.connect.group;

public class GroupsAdapter extends ArrayAdapter<group>{

	
	
	
	private int resource;
	private ArrayList<group> list=new ArrayList<group>();;
	private Context context;

	public GroupsAdapter(Context context, int resource,ArrayList<group> list) {
		super(context, resource);
		this.context=context;
		this.resource=resource;
		this.list=list;
		Log.e("lsiy:",list.size()+"");
		// TODO Auto-generated constructor stub
	}

	@Override 
	public View getView(int position,View convertView,ViewGroup parent)
	{
		
		View view=convertView;
		
		if(view!=null)
		{
			LayoutInflater inflater=LayoutInflater.from(getContext());
			view=inflater.inflate(resource, null);
		}
		else
			Log.e("SD","NUKK");
		group g=getItem(position);
		Log.e("position",position+"");
		if(g!=null)
		{
			TextView tv=(TextView)view.findViewById(R.id.groupname);
			tv.setText(g.getName());
				
		}
		
		
		return view;
		
	}
}

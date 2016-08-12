package com.nect.connect.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nect.connect.R;
import com.nect.connect.convorows;

public class ConversationAdapter extends ArrayAdapter<convorows> {

	ArrayList<convorows> convorow = new ArrayList<convorows>(); // array for all
	private Context context;
																// the rows(each
																// row include
																// all data)

	public ConversationAdapter(Context context, int resource,
			ArrayList<convorows> convorow) {
		super(context, resource, convorow);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.convorow = convorow;
	}

	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView; // making view for each row
		if (view == null) {
			LayoutInflater inflater;
			inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(R.layout.layoutconversation, null);
		}

		convorows cr = getItem(position);
		if (cr != null) {
			
			TextView recieve = (TextView) view.findViewById(R.id.recieve);
			TextView send = (TextView) view.findViewById(R.id.send);
           
			if (cr.getRecieve() == null) {
				
				recieve.setVisibility(View.INVISIBLE);
				recieve.setVisibility(View.GONE);
			} else {
				
				
				recieve.setText(cr.getRecieve());
			}
			if (cr.getSend() == null) {
				
				send.setVisibility(View.INVISIBLE);
				send.setVisibility(View.GONE);
			} else {
				send.setText(cr.getSend());
			}
			Log.e("R:",cr.getR()+"");
//			if(cr.getR()==-1)
//				sentRead.setBackgroundColor(Color.WHITE);
//			 if(cr.getR()==1){
//					send.setBackgroundColor(Color.RED);
////					sentRead.setText("! !");
//			}
//			else if(cr.getR()==2)
//			{
//				sentRead.setBackgroundColor(Color.GREEN);
//				sentRead.setText("! !");
//			}
		}
		return view;
	}
}

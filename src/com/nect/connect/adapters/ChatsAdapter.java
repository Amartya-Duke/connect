package com.nect.connect.adapters;

import java.util.ArrayList;

import com.nect.connect.R;
import com.nect.connect.rows;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatsAdapter extends ArrayAdapter<rows> {

	ArrayList<rows> row = new ArrayList<rows>(); // array for all the rows(each
	private int resource;
 													// row include all data)

	public ChatsAdapter(Context context, int resource, ArrayList<rows> row) {
		super(context, resource, row);
		// TODO Auto-generated constructor stub
		this.row = row;
		this.resource=resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView; // making view for each row
		if (view == null) {
			LayoutInflater inflater;
			inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(resource, null);
		}

		rows r = getItem(position);
		if (r != null) {
			TextView name = (TextView) view.findViewById(R.id.chatName);
			//TextView status = (TextView) view.findViewById(R.id.status);
			TextView lastMessage = (TextView) view.findViewById(R.id.lastMessage);
			TextView unreadCnt=(TextView)view.findViewById(R.id.unreadCnt);
			ImageView picture = (ImageView) view.findViewById(R.id.picture);

			
			if (r.getName() != null) {
				name.setText(r.getName());
			}
			if (r.getUnreadCnt()>0) {
				unreadCnt.setText(r.getUnreadCnt()+"");
				unreadCnt.setTextColor(Color.DKGRAY);
			}
			if (r.getMessage()!= null) {
				
				
				lastMessage.setText(r.getMessage());
				if(r.getUnreadCnt()==0)
				lastMessage.setTextColor(Color.GRAY);
				else
					lastMessage.setTextColor(Color.BLUE);
			}
			if (r.getPicture() != null) {
				picture.setImageBitmap(r.getPicture());
			}else{
			picture.setImageResource(R.drawable.ic_launcher2);
			}
		}
		return view;

	}

}

package com.nect.connect.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nect.connect.R;
import com.nect.connect.contactrows;

public class ContactsAdapter extends ArrayAdapter<contactrows> {
	ArrayList<contactrows> contactrow = new ArrayList<contactrows>();

	public ContactsAdapter(Context context, int resource,
			ArrayList<contactrows> contactrow) {
		super(context, resource, contactrow);
		// TODO Auto-generated constructor stub
		this.contactrow = contactrow;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView; // making view for each row
		if (view == null) {
			LayoutInflater inflater;
			inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(R.layout.layoutcontacts, null);

		}
		contactrows contactr = getItem(position);
		if (contactr != null) {
			TextView name = (TextView) view.findViewById(R.id.contactName);
			TextView phone = (TextView) view.findViewById(R.id.contactNumber);
            ImageView im=(ImageView) view.findViewById(R.id.profilePicture);
			if (contactr.getName() != null) {
				name.setText(contactr.getName());
			}
			if (contactr.getPhone() != null) {
				phone.setText(contactr.getPhone());
			}
			if(contactr.getImage()!=null){
				//set profilePic here
			}
		}
		return view;

	}

}

package com.nect.connect.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.nect.connect.Conversation;
import com.nect.connect.R;
import com.nect.connect.group;
import com.nect.connect.rows;
import com.nect.connect.adapters.GroupsAdapter;
import com.nect.connect.database.ContactsDB;

public class GroupsFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static GroupsFragment newInstance() {
		GroupsFragment fragment = new GroupsFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, 1);
		fragment.setArguments(args);
		return fragment;
	}

	private ListView listView;
	private Context context;
	private String myPhone;

	public GroupsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.group_fragment, container,
				false);
		context=getActivity();
		SharedPreferences preferences = context.getSharedPreferences(
				"Logindata", context.MODE_PRIVATE);
		listView=(ListView)rootView.findViewById(R.id.groupList);
		
		myPhone=preferences.getString("phone", null);
		ArrayList<group> groupData = loadGroups();
		Log.e("size:",groupData.size()+"");
		GroupsAdapter groupAdapter = new GroupsAdapter(context,
				R.layout.layout_groups,groupData);
		initializeGroupList();
		listView.setAdapter(groupAdapter);
		return rootView;
	}

	private void initializeGroupList() {
		
listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				rows data=(rows) arg0.getItemAtPosition(arg2);
				if(myPhone!=null){
				Intent intent = new Intent(context,Conversation.class);
				intent.putExtra("phone",data.getPhone());
				intent.putExtra("MyPhone", myPhone);
				startActivity(intent);
				}
				else
				{
					 Toast.makeText(context, "Error.To prevent this error go to app settings and delete app data of this app",Toast.LENGTH_LONG).show();
					Toast.makeText(context, "Or Reinstall the app.",Toast.LENGTH_LONG).show();
				}
				
			}
			
		});
	}

	private ArrayList<group> loadGroups() {
		ContactsDB db=new ContactsDB(context);
		return db.retrieveGroups();
		
	}

}

package com.nect.connect.fragments;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nect.connect.Conversation;
import com.nect.connect.R;
import com.nect.connect.contactrows;
import com.nect.connect.rows;
import com.nect.connect.adapters.ChatsAdapter;
import com.nect.connect.database.ChatsDB;
import com.nect.connect.database.ContactsDB;
import com.nect.connect.database.ConvDB;
import com.nect.connect.emoji.EmojiAdapter;
import com.nect.connect.emoji.EmojiItem;





/**
 * A placeholder fragment containing a simple view.
 */
public class ChatsFragment extends Fragment {
	private String myPhone;
	private ArrayList<rows> row;
	private ChatsAdapter chats;
	private ListView listView;
	Context context;
	private View view;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ChatsFragment newInstance() {
		ChatsFragment fragment = new ChatsFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, 2);
		fragment.setArguments(args);
		return fragment;
	}

	public ChatsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.conversation_fragment, container,
				false);
		view=rootView;
		context=getActivity();
		SharedPreferences preferences = context.getSharedPreferences(
				"Logindata", context.MODE_PRIVATE);
		myPhone=preferences.getString("phone", null);
		
		
		IntentFilter  i=new IntentFilter("com.nect.connect.BROADCAST");
		LocalBroadcastManager.getInstance(context).registerReceiver(new LocalBroadcastReceiver(),i);
		
		
	    
		row=new ArrayList<rows>();
	    row=loadChats();
		
	chats=new ChatsAdapter(context, R.layout.layoutchat, row);
	listView=(ListView)rootView.findViewById(R.id.chatListView);
		initializeChatList();
		listView.setAdapter(chats);
		return rootView;
	}
	
private boolean isNetworkAvailable() {
		
		ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo active=cm.getActiveNetworkInfo();
		return active!=null && active.isConnected();
		
		
	}
	public class LocalBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			updateChatList(intent.getStringExtra("Sender"),intent.getStringExtra("Message"),intent.getStringExtra("SenderName"));
		}

		

	}
	private void updateChatList(String sender, String lastMessage,
			String name) {
		ChatsDB db=new ChatsDB(context);
		rows  cr=new rows( name,"stauts",lastMessage,null,sender,db.getUnreadCnt(sender)+1);

	    int index=0;
		for(int i=0;i<row.size();i++)
		{
			if(row.get(i).getPhone().equals(sender))
			index=i;
		}
		
		row.remove(index);
		
			row.add(0, cr);
			
			chats.notifyDataSetChanged();
			
	}	
	
	
	
	public ArrayList<contactrows> loadContacts()
	{
		ContactsDB contactsDB = new ContactsDB(context);
		Cursor cursor = contactsDB.retrieveContacts();
		ArrayList<contactrows> contactrows = new ArrayList<contactrows>();
		String name, phone;
		
		if(cursor!=null){
			if(cursor.moveToFirst()){
			do {
			
			name = cursor.getString(cursor.getColumnIndex("name"));
			phone = cursor.getString(cursor.getColumnIndex("phone"));
			contactrows contactr = new contactrows(name, phone,null);
			contactrows.add(contactr);
		} while (cursor.moveToNext());
		}}
		return contactrows;
	}
	
	
	
	
	public ArrayList<rows> loadChats()
	{
		ChatsDB db=new ChatsDB(context);
		ConvDB db0=new ConvDB(context);
		Cursor c=db.retrieveChats();

		c.moveToFirst();

		if(c!=null){
			if (c.moveToFirst()){
		do
		{
		String name=c.getString(c.getColumnIndex(db.getName()));
		String phone=c.getString(c.getColumnIndex(db.getPhone()));

		String lastMsg=db0.lastMessage(phone);
			
			rows cr=new rows(name,"status",lastMsg+"",null,phone,c.getInt(c.getColumnIndex(db.getUnread())));
			row.add(cr);
		
		}while(c.moveToNext());
		}
			}
		return row;
	}
	
	
	
	
	public void initializeChatList()
	{
		boolean internet=isNetworkAvailable();
		TextView offline=(TextView)view.findViewById(R.id.offline);
		if(internet==false){
			
			offline.setVisibility(View.VISIBLE);
		}
		else{
			offline.setVisibility(View.INVISIBLE);
			offline.setVisibility(View.GONE);
		}
		
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

	 
	
	
}

package com.nect.connect;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nect.connect.adapters.ChatsAdapter;
import com.nect.connect.adapters.GroupsAdapter;
import com.nect.connect.database.ChatsDB;
import com.nect.connect.database.ContactsDB;
import com.nect.connect.database.ConvDB;


public class Chats extends ActionBarActivity {

	private String myPhone;
	private String x=null;
	private ArrayList<rows> row;
	private ChatsAdapter chats;
	//private MainLayout mainLayout;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ListView listView;
	private ActionBarDrawerToggle mDrawerToggle;
	private RelativeLayout mDrawerRelative;
	private ViewPager mViewPager;
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sliding_drawer_chat);
		
	    
	    
		SharedPreferences preferences = getSharedPreferences(
				"Logindata", MODE_PRIVATE);
		myPhone=preferences.getString("phone", null);
		
		
		//Setting up the drawer 
		mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawerRelative=(RelativeLayout)findViewById(R.id.drawer_relative);
		setUpDrawerToggle();
		
		mDrawerList=(ListView)findViewById(R.id.left_drawer_listView);
		ArrayList<group> groupData = loadGroups();
		GroupsAdapter contactsAdapter = new GroupsAdapter(this,
				R.layout.layout_groups,groupData);
		initializeDrawerList();
		mDrawerList.setAdapter(contactsAdapter);
		
		
		IntentFilter  i=new IntentFilter("com.nect.connect.BROADCAST");
		LocalBroadcastManager.getInstance(this).registerReceiver(new LocalBroadcastReceiver(),i);
		
		
	     setTitle("Connect"); 
		row=new ArrayList<rows>();
	    row=loadChats();
		
	chats=new ChatsAdapter(this, R.layout.layoutchat, row);
	listView=(ListView)findViewById(R.id.chatListView);
		initializeChatList();
		listView.setAdapter(chats);
			
	
	}

	 private ArrayList<group> loadGroups() {
		ContactsDB db=new ContactsDB(getApplicationContext());
		
		 
		return db.retrieveGroups();
	}

	@Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	        mDrawerToggle.syncState();
	    }

	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }

	    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chats, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if(id==R.id.search)
		{
			startActivity(new Intent(Chats.this,SeachActivity.class));
			return true;
		}
		if(id==R.id.contacts){
			startActivity(new Intent(Chats.this,Contacts.class));
			return true;
		}
		if(mDrawerToggle.onOptionsItemSelected(item))
			return true;
		return super.onOptionsItemSelected(item);
	}
	
	
	
	private boolean isNetworkAvailable() {
		
		ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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
		ChatsDB db=new ChatsDB(this);
		rows  cr=new rows( name,"stauts",lastMessage,null,sender,db.getUnreadCnt(sender)+1);

	    int index=0;
		for(int i=0;i<row.size();i++)
		{
			if(row.get(i).phone.equals(sender))
			index=i;
		}
		
		row.remove(index);
		
			row.add(0, cr);
			
			chats.notifyDataSetChanged();
			
	}	
	
	
	
	public ArrayList<contactrows> loadContacts()
	{
		ContactsDB contactsDB = new ContactsDB(this);
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
		ChatsDB db=new ChatsDB(this);
		ConvDB db0=new ConvDB(this);
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
		TextView offline=(TextView)findViewById(R.id.offline);
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
				Intent intent = new Intent(Chats.this,Conversation.class);
				intent.putExtra("phone",data.phone);
				intent.putExtra("MyPhone", myPhone);
				startActivity(intent);
				}
				else
				{
					 Toast.makeText(Chats.this, "Error.To prevent this error go to app settings and delete app data of this app",Toast.LENGTH_LONG).show();
					Toast.makeText(Chats.this, "Or Reinstall the app.",Toast.LENGTH_LONG).show();
				}
				
			}
			
		});
	}

	 
	
	
	
	private void initializeDrawerList() {
		// Insert the fragment by replacing any existing fragment at the end of drawer
		Fragment fragment = new DrawerFragment();
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    fragmentManager.beginTransaction()
	                   .replace(R.id.frame_below_drawer, fragment)
	                   .commit();

		mDrawerList.setOnItemClickListener(new drawerListItemClickListener());
	}
	
	
	
	private class drawerListItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	        selectItem(position);
	    }
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
	    

	    mDrawerList.setItemChecked(position, true);
	    setTitle(getConversationName(position));
	    mDrawerLayout.closeDrawer(mDrawerRelative );
	}
 
	private CharSequence getConversationName(int position) {
		// TODO Auto-generated method stub
		return null;
	}
 


	@Override
	public void setTitle(CharSequence title) {
	    CharSequence mTitle = title;
	    if( getSupportActionBar()!=null)
	    getSupportActionBar().setTitle(mTitle);
	}

	
	
	public void setUpDrawerToggle()
	{
		 mDrawerToggle = new ActionBarDrawerToggle(
	                this,                  /* host Activity */
	                mDrawerLayout,         /* DrawerLayout object */
	                  /* nav drawer icon to replace 'Up' caret */
	                R.string.navigation_drawer_open,  /* "open drawer" description */
	                R.string.navigation_drawer_close  /* "close drawer" description */
	                ) {

	            /** Called when a drawer has settled in a completely closed state. */
	            public void onDrawerClosed(View view) {
	                super.onDrawerClosed(view);
	                if(getSupportActionBar()!=null)
	                getSupportActionBar().setTitle("Connect");
	            }

	            /** Called when a drawer has settled in a completely open state. */
	            public void onDrawerOpened(View drawerView) {
	                super.onDrawerOpened(drawerView);
	                if(getSupportActionBar()!=null)
	                getSupportActionBar().setTitle("Your Groups");
	            }
	        };
	        mDrawerLayout.setDrawerListener(mDrawerToggle);
	        if(getSupportActionBar()!=null){
	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	        getSupportActionBar().setHomeButtonEnabled(true);
	        }
	        
	       
	}
	
	
	 public void createGroup(View view)
     {
		 Intent i = new Intent(this,CreateGroupActivity.class);
		 
     	startActivity(i);
     }
	 public void add_contact(View view)
	 {
		 startActivity(new Intent(this,AddContact.class));
	 }
}
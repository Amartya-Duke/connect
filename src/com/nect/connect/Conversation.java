package com.nect.connect;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import java.util.Enumeration;
import java.util.Hashtable;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nect.connect.adapters.ConversationAdapter;
import com.nect.connect.database.ChatsDB;
import com.nect.connect.database.ContactsDB;
import com.nect.connect.database.ConvDB;
import com.nect.connect.emoji.EmojiFragment;
import com.nect.connect.emoji.EmojiFragment.OnEmojiSelectedListner;
import com.nect.connect.emoji.EmojiItem;

public class Conversation extends ActionBarActivity implements OnEmojiSelectedListner {

	ArrayList<convorows> convorow = new ArrayList<convorows>();
	
	//private ListView listView;
	private ConversationAdapter conversation;
	private GoogleCloudMessaging gcm;
	private String myPhone;
	private String reciepent_phone;

	private String name;

	private String SENDER_ID="980582253272";

	private EditText typeMess;
	private DrawerLayout mDrawerLayout;
	private RelativeLayout mDrawerRelative;

	private GridView mDrawerGrid;
 

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation);
		
		IntentFilter  i=new IntentFilter("com.nect.connect.BROADCAST");
		LocalBroadcastManager.getInstance(this).registerReceiver(new LocalBroadcastReceiver(),i);
		
		Log.e("Local Broadcast", "Local BroadcastReciever registered");
		
		myPhone=getIntent().getStringExtra("MyPhone");
	    gcm=GoogleCloudMessaging.getInstance(this);
		reciepent_phone = getIntent().getStringExtra("phone");
		
		SharedPreferences.Editor edit=getSharedPreferences("Convo Receiver",0).edit();
		edit.putString("receiver",reciepent_phone );
		edit.commit();
		ConvDB convoDB = new ConvDB(this);
		ContactsDB contactsDB = new ContactsDB(this);
		name=contactsDB.retrieveName(reciepent_phone);
		setTitle(name);   //to show the name of the person in action bar
		
		typeMess=(EditText) findViewById(R.id.typeMessage);
		
		String recieve;
		String send;
		int MID;
		Cursor cursor = convoDB.retrieveConvo(reciepent_phone);
		new ChatsDB(this).resetUnreadCount(reciepent_phone);
		cursor.moveToFirst();
		if (cursor != null) {
			
			if (cursor.moveToFirst()) {
				do {recieve=cursor.getString(cursor.getColumnIndex("recieve"));
					send=cursor.getString(cursor.getColumnIndex("send"));
					MID=cursor.getInt(cursor.getColumnIndex("mID"));
					int s=cursor.getInt(cursor.getColumnIndex("R"));
					if (recieve != null){
					convorows cr=new convorows(MID,recieve, null,s);
					
					convorow.add(cr);
					}else if (send!= null){
						convorows cr=new convorows(MID,null,send,s);
						convorow.add(cr);
					}
					
				} while (cursor.moveToNext());
			}
			conversation = new ConversationAdapter(this,
					R.layout.layoutconversation, convorow);
			ListView listView = (ListView) findViewById(R.id.convoListView);
		
			listView.setAdapter(conversation);
			listView.setSelection(conversation.getCount());
			//listView.notify();
		}

		
		//Setting up the drawer 
				mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
				mDrawerRelative=(RelativeLayout)findViewById(R.id.drawer_relative);
				mDrawerGrid=(GridView)findViewById(R.id.emoji_gridView);
				 if(getSupportActionBar()!=null){
			        	Log.e("actoinna","its there");
			        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			        getSupportActionBar().setHomeButtonEnabled(true);
			        }else
			        	Log.e("actoinna","its null");
				setUpDrawerToggle();
				initialiseGridView();
				
	}
	
	
	private void initialiseGridView() {
		Fragment fragment=EmojiFragment.newInstance();
	    FragmentManager fragmentManager = getSupportFragmentManager();
	  FragmentTransaction fTransaction = fragmentManager.beginTransaction();
	    if(fTransaction.isEmpty()){
	                   fTransaction.replace(R.id.emoji_container,fragment, "Emojis")
	                   .commit();
	    }
	    else{
	    	fTransaction.remove(fragment);
	    	fTransaction.commit();
	    }
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
//		if(mDrawerToggle.onOptionsItemSelected(item))
//			return true;
		return super.onOptionsItemSelected(item);
	}

	public void sendButton(View view) {
		 
		String typeMessage = typeMess.getText().toString();
		ChatsDB c=new ChatsDB(this);
		c.ifExist(reciepent_phone);
		convorows cr = new convorows(0,null, typeMessage,1);
		convorow.add(cr);
		conversation.notifyDataSetChanged();
		ListView listView = (ListView) findViewById(R.id.convoListView);
		listView.setAdapter(conversation);
		ConvDB convoDB = new ConvDB(this);
		int mID=convoDB.insertConvo(reciepent_phone, typeMessage, null, null,1);
		new getGCMKey().execute(reciepent_phone,typeMessage,mID+"");
//		new asyncSendMessage().execute(typeMessage,mID+"");   //asynchronously  send message to GCM
		ChatsDB chatsDb=new ChatsDB(this);
		chatsDb.updateChats(reciepent_phone);
		typeMess.setText("");
		listView.setSelection(conversation.getCount());
		
	}
	
	
	public class getGCMKey extends AsyncTask<String,String,String>
	{
		String result="";
		String msg="",mID="";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.e("result:KEY","here");
			StringBuffer response = new StringBuffer(result);
			Hashtable<String,String> ht=new Hashtable<String,String>();
			ht.put("phone", reciepent_phone);
			String urlParameters=getUrlParams(ht);
			msg=params[1];
			mID=params[2];
			URL url;
			  HttpURLConnection connection = null;  
			  try {
			    //Create connection
			    url = new URL(getBaseContext().getString(R.string.website)+"/connect/get_key.php");
			    connection = (HttpURLConnection)url.openConnection();
			    connection.setRequestMethod("POST");
			    connection.setRequestProperty("Content-Type", 
			         "application/x-www-form-urlencoded");
						
			    connection.setRequestProperty("Content-Length", "" + 
			             Integer.toString(urlParameters.getBytes().length));
			    connection.setRequestProperty("Content-Language", "en-US");  
						
			    connection.setUseCaches (false);
			    connection.setDoInput(true);
			    connection.setDoOutput(true);
			    
			    //Send request
			    DataOutputStream wr = new DataOutputStream (
			                connection.getOutputStream ());
			    wr.writeBytes (urlParameters);
			    wr.flush ();
			    wr.close ();
		       
			    //Get Response	
			    InputStream is = connection.getInputStream();
			    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			    String line;
			     
			    Log.e("ResponseBuff",rd.readLine()+"");
			    while((line = rd.readLine()) != null) {
			      response.append(line);
			      response.append('\r');
			    }
			    rd.close();
			    result=response.toString();
			    Log.e("result:KEY",result);
			  } catch (Exception e) {

			    e.printStackTrace();
			    //result= e.toString();

			  } finally {

			    if(connection != null) {
			      connection.disconnect(); 
			    }
			  }
			
			
			return result;
		}
		@Override
		protected void onPostExecute(String result)
		{
			
			if(result.equals("")||result.equals("-1")||result.equals("0")){
				
			}
			else
				new asyncSendMessage().execute(result,msg,mID+""); 
			
		}
		
	}
	public String getUrlParams(Hashtable<String,String> params){
		 if(params.size() == 0)
		        return "";

		    StringBuffer buf = new StringBuffer();
		    Enumeration<String> keys = params.keys();
		    while(keys.hasMoreElements()) {
		        buf.append(buf.length() == 0 ? "" : "&");
		        String key = keys.nextElement();
		        buf.append(key).append("=").append(params.get(key));
		    }
		    return buf.toString();
	}
	public String getSender(){
		return reciepent_phone;
	}
	@Override
	public void onBackPressed()
	{
	    super.onBackPressed(); 
	    startActivity(new Intent(Conversation.this, Chats.class));
	    finish();

	}
	
	public class asyncSendMessage extends AsyncTask<String,String,String[]>
	{

		@Override
		protected String[] doInBackground(String... payload) {
			// TODO Auto-generated method stub
			String reply=sendMessageToGCM(payload[0],payload[1],payload[2]);
			Log.e("Resutl","do in background");
			
			
			return new String[]{reply,payload[1],payload[2]};
		}
        
		@Override
		protected void onPostExecute(String result[])
		{
			Log.e("Check success",checkSuccess(result[0])+"");
			if(checkSuccess(result[0])){
				new ConvDB(Conversation.this).updateConversation(Integer.parseInt(result[2]),2);
				try{
					updateDynamicallyConversation(Integer.parseInt(result[2]),result[1]);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	
		
	}
	private String sendMessageToGCM(String key, String msg, String ID) {
		// TODO Auto-generated method stub
	
		
		
		SharedPreferences pref=getSharedPreferences("Message Id", MODE_PRIVATE);       //Getting Message Id
	int mId=	pref.getInt("Message_Id", 0);
		String id= myPhone+":"+(mId);                                                //Concatenate with MyPhone
		SharedPreferences.Editor editor=getSharedPreferences("Message Id", MODE_PRIVATE).edit();    //Increasing Id by 1 and storing.
		editor.putInt("Message_Id", mId+1);
		editor.commit();
		try {
		Bundle data=new Bundle();
		data.putString("MESSAGE_ID", ID);
		data.putString("CLIENT_MESSAGE",msg);
		data.putString("SENDER_PHONE", myPhone);
		data.putString("RECIPIENT_KEY",key);
	    data.putString("ACTION", "MESSAGE");
	    
			gcm.send(SENDER_ID+"@gcm.googleapis.com", id, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
		
//ArrayList<NameValuePair> nameValue = new ArrayList<NameValuePair>();
//nameValue.add(new BasicNameValuePair("receiver_phone", reciepent_phone));         //as per now we are sending message to ourself
//nameValue.add(new BasicNameValuePair("sender_phone", myPhone));            //check send2gc.php not test.php
//nameValue.add(new BasicNameValuePair("message", typeMessage));  
//nameValue.add(new BasicNameValuePair("Message_Id", id));     //sending message to send2gcm.php...
//HttpPost post = new HttpPost(
//"http://myappserver.netau.net/connect/send2gcm.php"); 
//String result="s";
//	try {
//		post.setEntity(new UrlEncodedFormEntity(nameValue)); // set the
//																// values
//																// and
//																// tags
//		HttpResponse httpResponse = hClient.execute(post); // executing
//															// post
//															// using
//															// client
//		result = EntityUtils.toString(httpResponse.getEntity()); // Storing
//												 					// the
//																	// result
//
//	} catch (ClientProtocolException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}	 
//	Log.e("Conversation", result);
//	return result;
	}



	public void updateDynamicallyConversation(int mID,String msg) {
		for(int i=0;i<convorow.size();i++)
		{
			if(convorow.get(i).getId()==mID){
				
				Log.e("index",i+"");
			   convorow.set(i, new convorows(2, msg,null,-1));
			   conversation.notifyDataSetChanged();
			   
			}
		}
		
	}


	private void updateList(String sender, String message,String senderName) {
		// TODO Auto-generated method stub
		if(sender.equals(reciepent_phone)){
		convorows cr = new convorows(0, message,null,-1);
		convorow.add(cr);
		conversation.notifyDataSetChanged();
		ListView listView = (ListView) findViewById(R.id.convoListView);
		listView.setAdapter(conversation);
		
		listView.setSelection(conversation.getCount());
		}
		else
		{
			
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); // using
																		// notification
			SharedPreferences preference= getSharedPreferences(
					"Logindata", MODE_PRIVATE);
		preference.getString("phone", null);
			Intent i = new Intent(this, Chats.class);i.putExtra("phone",sender);
			
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this);
			// builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
			mBuilder.setContentTitle("New Message from " + senderName);
			mBuilder.setContentText(message + "");
			mBuilder.setSmallIcon(R.drawable.ic_launcher2);
			mBuilder.setContentIntent(pIntent);
			mBuilder.setAutoCancel(true);
			mNotificationManager.notify(0,mBuilder.build());

		}
		
	}

	public boolean checkSuccess(String result)
	{
		if(result.contains("\"success\":1"))
			return true;
		else
			return false;
	}
	
	public class LocalBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.e("Local","Intent Recieved");
		
			updateList(intent.getStringExtra("Sender"),intent.getStringExtra("Message"),intent.getStringExtra("SenderName"));
		}
    
	}
	
	
	public void show_emoji(View view)
	{
		
		Fragment fragment=EmojiFragment.newInstance();
	    FragmentManager fragmentManager = getSupportFragmentManager();
	  FragmentTransaction fTransaction = fragmentManager.beginTransaction();
	    if(fTransaction.isEmpty()){
	                   fTransaction.replace(R.id.fragment_container,fragment, "Emojis")
	                   .commit();
	    }
	    else{
	    	fTransaction.remove(fragment);
	    	fTransaction.commit();
	    }
	}


//	@Override
//	    protected void onPostCreate(Bundle savedInstanceState) {
//	        super.onPostCreate(savedInstanceState);
//	        // Sync the toggle state after onRestoreInstanceState has occurred.
//	        mDrawerToggle.syncState();
//	    }
//
//	    @Override
//	    public void onConfigurationChanged(Configuration newConfig) {
//	        super.onConfigurationChanged(newConfig);
//	        mDrawerToggle.onConfigurationChanged(newConfig);
//	    }
	
	public void setUpDrawerToggle()
	{
//		 mDrawerToggle = new ActionBarDrawerToggle(
//	                this,                  /* host Activity */
//	                mDrawerLayout,         /* DrawerLayout object */
//	                  /* nav drawer icon to replace 'Up' caret */
//	                R.string.navigation_drawer_open,  /* "open drawer" description */
//	                R.string.navigation_drawer_close  /* "close drawer" description */
//	                ) {
//			 m
//	            /** Called when a drawer has settled in a completely closed state. */
//	            public void onDrawerClosed(View view) {
//	                super.onDrawerClosed(view);
//	                if(getSupportActionBar()!=null)
//	                getSupportActionBar().setTitle("Connect");
//	            }
//
//	            /** Called when a drawer has settled in a completely open state. */
//	            public void onDrawerOpened(View drawerView) {
//	                super.onDrawerOpened(drawerView);
//	                if(getSupportActionBar()!=null)
//	                getSupportActionBar().setTitle("Your Groups");
//	            }
//	        };
//	      
//	        mDrawerLayout.setDrawerListener(mDrawerToggle);
//	       
//	        
	       
	}
	
	@Override
	public void onEmojiSelected(int position) {
		// TODO Auto-generated method stub
		typeMess.append(EmojiItem.getEmojiChar(position)+"");
	}
	
}
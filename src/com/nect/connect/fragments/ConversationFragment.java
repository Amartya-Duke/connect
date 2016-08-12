package com.nect.connect.fragments;

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

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nect.connect.Chats;
import com.nect.connect.Conversation;
import com.nect.connect.R;
import com.nect.connect.convorows;
import com.nect.connect.Conversation.LocalBroadcastReceiver;
import com.nect.connect.Conversation.asyncSendMessage;
import com.nect.connect.Conversation.getGCMKey;
import com.nect.connect.adapters.ConversationAdapter;
import com.nect.connect.database.ChatsDB;
import com.nect.connect.database.ContactsDB;
import com.nect.connect.database.ConvDB;
import com.nect.connect.emoji.EmojiFragment;
import com.nect.connect.emoji.EmojiItem;
import com.nect.connect.emoji.EmojiFragment.OnEmojiSelectedListner;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

public class ConversationFragment extends Fragment  implements OnEmojiSelectedListner{

	
ArrayList<convorows> convorow = new ArrayList<convorows>();
	
	//private ListView listView;
	private ConversationAdapter conversation;
	private GoogleCloudMessaging gcm;
	private String myPhone;
	private String reciepent_phone;

	private String name;

	private SQLiteDatabase db;

	private String SENDER_ID="980582253272";

	private EditText typeMess;

	private Context context;

	private ListView listView;

	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		 rootView=inflater.inflate(R.layout.activity_conversation,container,false);
		
		context=getActivity();
				
				
		
		
		IntentFilter  i=new IntentFilter("com.nect.connect.BROADCAST");
		LocalBroadcastManager.getInstance(context).registerReceiver(new LocalBroadcastReceiver(),i);
		
		Log.e("Local Broadcast", "Local BroadcastReciever registered");
		
		myPhone=getActivity().getIntent().getStringExtra("MyPhone");
	    gcm=GoogleCloudMessaging.getInstance(context);
		reciepent_phone = getActivity().getIntent().getStringExtra("phone");
		
		SharedPreferences.Editor edit=context.getSharedPreferences("Convo Receiver",0).edit();
		edit.putString("receiver",reciepent_phone );
		edit.commit();
		ConvDB convoDB = new ConvDB(context);
		ContactsDB contactsDB = new ContactsDB(context);
		name=contactsDB.retrieveName(reciepent_phone);
		getActivity().setTitle(name);   //to show the name of the person in action bar
		
		typeMess=(EditText) rootView.findViewById(R.id.typeMessage);
		 listView = (ListView) rootView.findViewById(R.id.convoListView);
		String recieve;
		String send;
		int MID;
		Cursor cursor = convoDB.retrieveConvo(reciepent_phone);
		new ChatsDB(context).resetUnreadCount(reciepent_phone);
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
			conversation = new ConversationAdapter(context,
					R.layout.layoutconversation, convorow);
			ListView listView = (ListView) rootView.findViewById(R.id.convoListView);
		
			listView.setAdapter(conversation);
			listView.setSelection(conversation.getCount());
			//listView.notify();
		}
		
				return rootView;
	}


	public void sendButton(View view) {
		 
		String typeMessage = typeMess.getText().toString();
		ChatsDB c=new ChatsDB(context);
		c.ifExist(reciepent_phone);
		convorows cr = new convorows(0,null, typeMessage,1);
		convorow.add(cr);
		conversation.notifyDataSetChanged();
		ListView listView = (ListView) rootView.findViewById(R.id.convoListView);
		listView.setAdapter(conversation);
		ConvDB convoDB = new ConvDB(context);
		int mID=convoDB.insertConvo(reciepent_phone, typeMessage, null, null,1);
		
		
		new getGCMKey().execute(reciepent_phone,typeMessage,mID+"");
//		new asyncSendMessage().execute(typeMessage,mID+"");   //asynchronously  send message to GCM
		ChatsDB chatsDb=new ChatsDB(context);
		chatsDb.updateChats(reciepent_phone);
		typeMess.setText("");
		listView.setSelection(conversation.getCount());
		
	}
	
	
	public class getGCMKey extends AsyncTask<String,String,String>
	{
		String result;
		String msg="",mID="";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.e("result:KEY","here");
			msg=params[1];
			mID=params[2];
			
			Hashtable<String,String> ht=new Hashtable<String,String>();
			ht.put("phone", reciepent_phone);
			String urlParameters=getUrlParams(ht);
			URL url;
			  HttpURLConnection connection = null;  
			  try {
			    //Create connection
			    url = new URL(context.getString(R.string.website)+"/connect/get_key.php");
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
			    StringBuffer response = new StringBuffer(); 
			    Log.e("ResponseBuff",rd.readLine()+"");
			    while((line = rd.readLine()) != null) {
			      response.append(line);
			      response.append('\r');
			    }
			    rd.close();
			    return response.toString();

			  } catch (Exception e) {

			    e.printStackTrace();
			    return e.toString();

			  } finally {

			    if(connection != null) {
			      connection.disconnect(); 
			    }
			  }
			
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
	
	public String getSender(){
		return reciepent_phone;
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
				new ConvDB(context).updateConversation(Integer.parseInt(result[2]),2);
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

	
	
	private String sendMessageToGCM(String key, String msg, String ID) {
		// TODO Auto-generated method stub
	
		
		SharedPreferences pref=context.getSharedPreferences("Message Id", context.MODE_PRIVATE);       //Getting Message Id
	int mId=	pref.getInt("Message_Id", 0);
		String id= myPhone+":"+(mId);                                                //Concatenate with MyPhone
		SharedPreferences.Editor editor=context.getSharedPreferences("Message Id", context.MODE_PRIVATE).edit();    //Increasing Id by 1 and storing.
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
//getBaseContext().getString(R.string.website)+"connect/send2gcm.php"); 
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
		
		listView.setAdapter(conversation);
		
		listView.setSelection(conversation.getCount());
		}
		else
		{
			
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE); // using
																		// notification
			SharedPreferences preference= context.getSharedPreferences(
					"Logindata", context.MODE_PRIVATE);
		String myPhone=preference.getString("phone", null);
			Intent i = new Intent(context, Chats.class);i.putExtra("phone",sender);
			
			PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, 0);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context);
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
	    FragmentManager fragmentManager = getChildFragmentManager();
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


	@Override
	public void onEmojiSelected(int position) {
		// TODO Auto-generated method stub
		typeMess.append(EmojiItem.getEmojiChar(position)+"");
	}
	
	
	
}

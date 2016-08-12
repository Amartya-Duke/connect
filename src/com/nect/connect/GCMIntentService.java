package com.nect.connect;

import com.nect.connect.database.*;

import java.util.List;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;


public class GCMIntentService extends IntentService {

	private String sender_ph;
	private String msg;
	private String sender_name;

	public GCMIntentService(String Iservice) {
		super(Iservice);
		
		// TODO Auto-generated constructor stub
	}

	public GCMIntentService() {
		super("MyIntentService");

	}


	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Bundle b = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String mType = gcm.getMessageType(intent);
		Log.e("Message type",mType);
		if(mType.equals("reciept"))
		{
		String status=	intent.getStringExtra("message_status");
		String mid=	intent.getStringExtra("original_message_id");
			if(status.equals("MESSAGE_SENT_TO_DIVICE"))
			{
				Log.e("Delivery reciepnt...mID:",mid+"");
				Toast.makeText(getApplicationContext(), "Delivered", Toast.LENGTH_SHORT).show();
			}
		}
		else{
		 msg = intent.getStringExtra("MESSAGE");
		Log.e("Message recieved", msg + " "); // Receiving msg from GCm

		// Receiving sender no....see send2gcm.php
		sender_ph = intent.getStringExtra("SENDER");
		Log.e("Sender :", sender_ph + " ");
				ChatsDB chatsDB = new ChatsDB(this);
				ConvDB convoDB = new ConvDB(this);
				ContactsDB contactsDB=new ContactsDB(this);
				
if(contactsDB.ifExists(sender_ph)){
		sender_name = contactsDB.retrieveName(sender_ph);

		Intent localIntent=new Intent("com.nect.connect.BROADCAST");
		localIntent.putExtra("Sender", sender_ph);
		localIntent.putExtra("Message", msg);
		localIntent.putExtra("SenderName", sender_name);
		LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
			this.sendOrderedBroadcast(localIntent, null);
		
       Log.e("Local Broadcast", "Local Broadcast sent");
	
       chatsDB.updateChats(sender_ph);
    	
        // Inserting new message into database.
 		convoDB.insertConvo(sender_ph, null, msg, null,-1);
 		Log.e("Database:", "Conversation Inserted");
        chatsDB.updateRead(sender_ph);
		 if(isActivityRunning())
		 {
sendNotification(msg, sender_name);
		 }  
	}
		}
	
	}
	protected Boolean isActivityRunning() {
	
		Class<Conversation> convoClass = Conversation.class;
		Class<Chats> chatsClass = Chats.class;
	
		ActivityManager activityManager = (ActivityManager) getBaseContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = activityManager
				.getRunningTasks(Integer.MAX_VALUE);
		boolean m=true;
		for (ActivityManager.RunningTaskInfo task : tasks) {
			Log.e("Running Activviy", task.topActivity.getClassName());
			if (convoClass.getCanonicalName().equalsIgnoreCase(
					task.topActivity.getClassName()) || chatsClass.getCanonicalName().equalsIgnoreCase(
						 	task.topActivity.getClassName())) {
				m=false;
			}
			
		}
		
		
		return m;
			
		
		

	}
	private void sendNotification(String message,String senderName)
	{

		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); // using
																	// notification
		Intent i = new Intent(this, Chats.class);
		// flag added coz. we re going to start a new activity
		// i.putExtra("phone", sender_ph); //outside of the context
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this);
		// builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
		mBuilder.setContentTitle("New Message from " + senderName);
		mBuilder.setContentText(message + "");
		mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher2));
		mBuilder.setContentIntent(pIntent);
		mBuilder.setAutoCancel(true);
		mNotificationManager.notify(0, mBuilder.build());
		
	}
}
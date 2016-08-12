package com.nect.connect;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

public class GCMBroadcastReciever extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		String intentAction=intent.getAction();
		
		
		Log.e("GCMBroadcastReciever", "GCMIntent Recieved");
		// used for identifying the app.Broadcast receiver doesnt know which ap
		// to open when it receives the message
		ComponentName cName = new ComponentName(context.getPackageName(),
				GCMIntentService.class.getName()); // Identifier for a specific
													// application component

		startWakefulService(context, (intent.setComponent(cName)));
		setResultCode(Activity.RESULT_OK);
		
	}
		
	
}

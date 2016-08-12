
package com.nect.connect;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nect.connect.database.ContactsDB;

public class AddContact extends ActionBarActivity {



	private ProgressBar bar;
	private String SENDER_ID="980582253272";
	public GoogleCloudMessaging gcm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		 bar=(ProgressBar) findViewById(R.id.progressBarAddContact);
		 bar.setVisibility(View.INVISIBLE);
	}



	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contact, menu);
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
		return super.onOptionsItemSelected(item);
	}
	public void Add(View view){
		EditText ph=(EditText)findViewById(R.id.editText1);
		String phone =ph.getText().toString();
		ContactsDB contactsDB = new ContactsDB(this);
		if(!contactsDB.ifExists(phone))
		new Add().execute(phone);
		else{
			Toast.makeText(this, "You have already added this contact", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this,Contacts.class));
			finish();
		}
	}
	
	public class Add extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute()
		{
			
			bar.setVisibility(View.VISIBLE);
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String url1=getBaseContext().getString(R.string.website)+"/connect/search_user.php";
			
			Hashtable<String,String> ht=new Hashtable<String,String>();
			ht.put("phone",arg0[0]);
			String urlParameters=getUrlParams(ht);
			String result="-1";
			StringBuffer bf=new StringBuffer(result);
			URL url;
			  HttpURLConnection connection = null;  
			  try {
			    //Create connection
			    url = new URL(url1);
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
			    result=response.toString();
			    Log.e("result:",result);
			  } catch (Exception e) {

			    e.printStackTrace();
			    

			  } finally {

			    if(connection != null) {
			      connection.disconnect(); 
			    }
			  }
		
				
			String key_contact="";
			
			Log.e("OriginalResult",result+"");
			
			if(result.contains("GCM"))
			key_contact=result.substring(result.indexOf("GCM")+3);
			
			if(result.length()>1)
			result=result.substring(0,result.indexOf("GCM"));

			Log.e("KEY",key_contact+"");
			Log.e("Result",result+"");
			if(!result.equals("0")&&!result.equals("-1"))
			{
				ContactsDB insert = new ContactsDB(AddContact.this);
				insert.insertContact(arg0[0],result );
			}
			
			
			if(result!="-1"&&result!="0")
			{

				SharedPreferences prefs=getSharedPreferences("Message Id", MODE_PRIVATE);       //Getting Message Id
			int mId=	prefs.getInt("Message_Id", 0);
				        
				SharedPreferences.Editor editor=getSharedPreferences("Message Id", MODE_PRIVATE).edit();    //Increasing Id by 1 and storing.
				editor.putInt("Message_Id", mId+1);
				editor.commit();
				gcm=GoogleCloudMessaging.getInstance(getApplicationContext());
				SharedPreferences pref = getSharedPreferences(
						"Logindata", MODE_PRIVATE);
				String id= pref.getString("phone", "0000")+":"+(mId);  
				Bundle bundle=new Bundle();
				bundle.putString("RECIPIENT_KEY",key_contact);
				bundle.putString("PHONE",pref.getString("phone", "0000") );
				bundle.putString("NAME", pref.getString("name", "anonymous"));
				bundle.putString("ACTION","SEND_ADD_CONTACT_NOTIFICATION");
				try {
					gcm.send(SENDER_ID+"@gcm.googleapis.com", id, bundle);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result;
		}
		
		protected void onPostExecute(String msg){
			bar.setVisibility(View.INVISIBLE);
			if(msg.equals("-1")){
				
				Toast.makeText(AddContact.this, "Network Problem", Toast.LENGTH_LONG).show();
				
			}
			else if(msg.equals("0")){
				Toast.makeText(AddContact.this, "Contact not found!", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(AddContact.this, "Contact added", Toast.LENGTH_LONG).show();
			   startActivity(new Intent(AddContact.this,Contacts.class));
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

	}
	
	
	
	@Override
	public void onBackPressed()
	{
	    super.onBackPressed(); 
	  finish();
	    

	}
}

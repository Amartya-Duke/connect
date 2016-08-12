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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;



public class Login extends ActionBarActivity {
	EditText phon;
	EditText pass;
String key="980582253272";
private GoogleCloudMessaging gcm;
private String regid;
private SQLiteDatabase db;
private ProgressBar mProgressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_login);
		setTitle("Connect");
		
		new asyncCheckLoginData().execute();
		
		mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
		mProgressBar.setIndeterminate(true);
	    mProgressBar.setVisibility(View.INVISIBLE);
	   
	  
		phon = (EditText) findViewById(R.id.Phone_no);
		pass = (EditText) findViewById(R.id.Password);
		
	
	}

	
	

	public void login(View view) {
		String phone = phon.getText().toString();
		String password = pass.getText().toString();
		new loginAsync().execute(phone, password);
	

	}

	public void signUp(View view) {
		Intent intent = new Intent(this, Signup.class);
		startActivity(intent);

	}

//	public Hashtable<String,String> getValues(final String value, final String tag) {
//
//		NameValuePair nvp = new NameValuePair() {
//
//			@Override
//			public String getValue() {
//				// TODO Auto-generated method stub
//				return value;
//			}
//
//			@Override
//			public String getName() {
//				// TODO Auto-generated method stub
//				return tag;
//			}
//		};
//		return nvp;
//	}

	public class loginAsync extends AsyncTask<String, String, String> {
		String phone;
		String password;
		String result = "";
 
		@Override
		public void onPreExecute()
		{
			mProgressBar.setVisibility(View.VISIBLE);
		
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			phone = arg0[0];
			password = arg0[1];
			gcm = GoogleCloudMessaging.getInstance(Login.this);
			
			
			try {
				regid = gcm.register(key);
				Log.e("Register Id",regid);
			//	Toast.makeText(Login.this, "Your key:"+regid,Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//Toast.makeText(Login.this, "Error"+e,Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			
			if(regid==null)
				return "-1";
			
			Hashtable<String,String> ht=new Hashtable<String,String>();
			ht.put("phone",phone+"");
			ht.put("password",password);
			ht.put("key", regid);
			
			 URL url;
			  HttpURLConnection connection = null;  
			  String urlParameters=getUrlParams(ht);
			  StringBuffer response = new StringBuffer(result); 
			  try {
			    //Create connection
			    url = new URL(getBaseContext().getString(R.string.website)+"/connect/Login.php");
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
			    
			  
			    while((line = rd.readLine()) != null) {
			      response.append(line);
			      response.append('\r');
			    }
			    rd.close();
			    result = response.toString();

			  } catch (Exception e) {

			    e.printStackTrace();
			   
			  } finally {

			    if(connection != null) {
			      connection.disconnect(); 
			    }
			  }
			
			
				
			return result;

		}

		protected void onPostExecute(String msg) {
			mProgressBar.setVisibility(View.INVISIBLE);
			  Log.e("ResponseBuff",msg);
			try {
				JSONObject jObject = new JSONObject(msg);
				
				Log.e("Json response", jObject.toString());
	            JSONArray jArray = jObject.getJSONArray("result");

	            JSONObject jtemp = jArray.getJSONObject(0);
	            
	            if(jtemp.getString("server").equals("true")){
	            	if(jtemp.getString("exists").equals("true")){
	            		SharedPreferences.Editor editor = getSharedPreferences(
	    						"Logindata", MODE_PRIVATE).edit();
	    				editor.putString("phone", phone);
	    				editor.putString("password", password);
	    				editor.putString("Id", regid);
	    				editor.putString("name", jtemp.getString("name"));
	    				editor.commit();
	    				  Log.e("Key stored in shared Preference:",regid);
	    				Intent intent = new Intent(Login.this, MainActivity.class);
	    				intent.putExtra("phone", phone);
	    				intent.putExtra("password", password);
	    				intent.putExtra("Id", regid);
	    				
	    				startActivity(intent);
	            	}
	            	else{
	            		Toast.makeText(Login.this, "Incorrect Username/Password combination",
	    						Toast.LENGTH_LONG).show();
	            	}
	            }
	            else{
	            	Toast.makeText(Login.this, "Server Error...Allowing for Debugging", Toast.LENGTH_LONG)
					.show();
		SharedPreferences.Editor editor = getSharedPreferences(
				"Logindata", MODE_PRIVATE).edit();
		editor.putString("phone", "123456");
		editor.putString("password", "123456");
		editor.putString("Id", regid);
		editor.putString("name", "test");
		editor.commit();
		Intent intent = new Intent(Login.this, MainActivity.class);
		intent.putExtra("phone", "123456");
		intent.putExtra("password", "123456");
		intent.putExtra("Id", regid);
		
		startActivity(intent);
	            }
	            
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(Login.this, "JSON format exception", Toast.LENGTH_LONG)
				.show();
				e.printStackTrace();
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
	
	
	class asyncCheckLoginData extends AsyncTask<String,String,String>{
		Intent intent;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// getting the user's details stored from phone if exists
			
						SharedPreferences pref = getSharedPreferences("Logindata", MODE_PRIVATE);
						
						String phone_no = pref.getString("phone", null);
						String pass_word = pref.getString("password", null); // Second
						String reg_id=pref.getString("Id", null);														// argument-default
																				// value
						if (phone_no != null && pass_word != null && reg_id!=null ) { // Checking if data is
																		// stored
						
							intent = new Intent(getApplicationContext(), MainActivity.class);
						
							intent.putExtra("phone", phone_no);
							intent.putExtra("password", pass_word);
							intent.putExtra("Id", reg_id);
							startActivity(intent);
						}
						else
							intent=new Intent(getApplicationContext(),Login.class);
						
			return null;
		}
	}
}
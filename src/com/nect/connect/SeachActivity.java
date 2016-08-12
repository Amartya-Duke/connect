package com.nect.connect;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.nect.connect.adapters.ContactsAdapter;

public class SeachActivity extends ActionBarActivity {
	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seach);
		//new UploadFile().execute();
		lv=(ListView)findViewById(R.id.search_result);
		
		
		final EditText ed=(EditText)findViewById(R.id.searchText);
		ed.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String search=ed.getText().toString();
				Log.e("sss",search+"");
				
			new asyncSearch().execute(search);
			}
			
		});
	
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.seach, menu);
		
		View v=(View)menu.findItem(R.id.searchuser).getActionView();
		
		
	
		
//		final EditText ed=(EditText)v.findViewById(R.id.txt_search);
//		ed.addTextChangedListener(new TextWatcher(){
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				String search=ed.getText().toString();
//				Log.e("sss",search+"");
//				
//			new asyncSearch().execute(search);
//			}
//			
//		});
//		
		 return super.onCreateOptionsMenu(menu);
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
	
	class asyncSearch extends AsyncTask<String,String,String>
	{
		ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar);
		@Override
		protected void onPreExecute()
		{
		
		pb.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			Hashtable<String,String> ht=new Hashtable<String,String>();
			ht.put("name",params[0]);
			 URL url;
			  HttpURLConnection connection = null;  
			  String urlParameters=getUrlParams(ht);
			  String result="";
			  StringBuffer response = new StringBuffer(result); 
			  try {
			    //Create connection
			    url = new URL(getBaseContext().getString(R.string.website)+"/connect/search_userasname.php");
			    connection = (HttpURLConnection)url.openConnection();
			    connection.setRequestMethod("POST");
			    connection.setRequestProperty("Content-Type", 
			         "application/json");
						
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
		
		@Override
		protected void onPostExecute(String jsonResult)
		{
			Log.e("jresult",jsonResult+"");
			pb.setVisibility(View.INVISIBLE);
			pb.setVisibility(View.GONE);
			try {
				JSONObject jObject=new JSONObject(jsonResult);
				JSONArray jArray = jObject.getJSONArray("users");
				        
				for(int i=0;i<jArray.length();i++)
				{
					JSONObject jTemp = jArray.getJSONObject(i);
					String name=jTemp.getString("name");
					String phone=jTemp.getString("phone");
					ArrayList<contactrows> contactrows = new ArrayList<contactrows>();
					contactrows contactr = new contactrows(name, phone,null);
					contactrows.add(contactr);
					ContactsAdapter searchAdapter = new ContactsAdapter(getApplicationContext(),
							R.layout.layoutcontacts, contactrows);
					lv.setAdapter(searchAdapter);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
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
}

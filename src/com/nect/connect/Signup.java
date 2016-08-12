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






import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Signup extends ActionBarActivity {

	private ProgressBar mProgressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_signup);
		
		setTitle("Connect");
		mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
		mProgressBar.setIndeterminate(true);
	    mProgressBar.setVisibility(View.INVISIBLE);
	}

	

	public void signup(View view) {
		TextView name = (TextView) findViewById(R.id.Name);
		TextView email = (TextView) findViewById(R.id.Email);
		TextView phone = (TextView) findViewById(R.id.Phone);
		TextView password = (TextView) findViewById(R.id.Password);
		TextView repassword = (TextView) findViewById(R.id.RePassword);
		
		if(name.getText().toString().startsWith(" "))
			Toast.makeText(getApplicationContext(), "Name cannot start with space !",
				Toast.LENGTH_LONG).show();
		else if(name.getText().toString().length()<3)
			Toast.makeText(getApplicationContext(), "Name must be atleast of 3 characters ",
					Toast.LENGTH_LONG).show();
	
		else	if (!(password.getText().toString().equals(repassword.getText()
				.toString()))) {
			Toast.makeText(getApplicationContext(), "Password doesnt match",
					Toast.LENGTH_LONG).show();
			
		} else {
			Log.e("phodssss", "  " + phone.getText().toString());
			new asyncRegister().execute(name.getText().toString(), email
					.getText().toString(), phone.getText().toString(), password
					.getText().toString(),
					getBaseContext().getString(R.string.website)+"/connect/register.php");
		}
	}

	public class asyncRegister extends AsyncTask<String, String, String>

	{
		String result = "";
		
		@Override
		public void onPreExecute()
		{
			mProgressBar.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			
			Hashtable<String,String> ht=new Hashtable<String,String>();
			ht.put("name",args[0]);
			ht.put("phone",args[2]);
			ht.put("email",args[1]);
			ht.put("password", args[3]);
			

			 URL url;
			  HttpURLConnection connection = null;  
			  String urlParameters=getUrlParams(ht);
			  StringBuffer response = new StringBuffer(result); 
			  try {
			    //Create connection
			    url = new URL(getBaseContext().getString(R.string.website)+"/connect/register.php");
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
			    result = response.toString();

			  } catch (Exception e) {

			    e.printStackTrace();
			   
			  } finally {

			    if(connection != null) {
			      connection.disconnect(); 
			    }
			  }
			
			
			

			// startActivity(new Intent(Signup.this, Login.class));
			if (result.equals("1"))
				return "1";

			else if (result.equals("0"))
				return "0";
			else
				return "-1";
		}

		@Override
		protected void onPostExecute(String msg) {
			mProgressBar.setVisibility(View.INVISIBLE);
			setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
			if (msg.equals("1")) {
				Toast.makeText(Signup.this, "Registered succesfully! ",
						Toast.LENGTH_LONG).show();
				startActivity(new Intent(Signup.this, Login.class));
			} else if (msg.equals("0"))
				Toast.makeText(Signup.this,
						"This phone number is already registered ",
						Toast.LENGTH_LONG).show();

			else
				Toast.makeText(Signup.this, "Server Error", Toast.LENGTH_LONG)
						.show();
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
	    startActivity(new Intent(this, Login.class));
	    finish();

	}
	
}

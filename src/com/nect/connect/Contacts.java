package com.nect.connect;

import com.nect.connect.database.*;
import com.nect.connect.adapters.ContactsAdapter;
import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Contacts extends ActionBarActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
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
		ContactsAdapter contactsAdapter = new ContactsAdapter(Contacts.this,
				R.layout.layoutcontacts, contactrows);
		ListView listView = (ListView) findViewById(R.id.contactsListView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				contactrows cr = (contactrows) arg0.getItemAtPosition(arg2);
				Intent intent = new Intent(Contacts.this, Conversation.class);
				SharedPreferences preference= getSharedPreferences(
						"Logindata", MODE_PRIVATE);
			String myPhone=preference.getString("phone", null);
				intent.putExtra("phone", cr.getPhone());
				intent.putExtra("MyPhone", myPhone);
				startActivity(intent);

			}
		});
		listView.setAdapter(contactsAdapter);
	}


	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_contacts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed()
	{
	    super.onBackPressed(); 
	    startActivity(new Intent(this, Chats.class));
	    finish();

	}
}

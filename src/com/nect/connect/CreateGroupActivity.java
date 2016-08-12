package com.nect.connect;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nect.connect.adapters.DialogViewAdapter;
import com.nect.connect.adapters.dialogview_rows;
import com.nect.connect.database.ContactsDB;

public class CreateGroupActivity extends ActionBarActivity {
	 ArrayList<String> phone=new ArrayList<String>();
	private ArrayList<String> mArrayListPhone;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		 mArrayListPhone=new ArrayList<String>();
	
	}
 
	
	public void add_participants(View view)
	{
		new asyncLoadList().execute();
	}
	
	public class asyncLoadList extends AsyncTask<Void,Void,Void>
	{
		ArrayList<dialogview_rows> ar;
		ProgressBar pb= (ProgressBar) findViewById(R.id.progressBar);
		@Override
		protected void onPreExecute(){
			pb.setVisibility(View.VISIBLE);
			ar=new ArrayList<dialogview_rows>();
			
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			 ar = getAraryList();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void d){
			pb.setVisibility(View.INVISIBLE);
			ListView lv=(ListView)findViewById(R.id.participants);
			lv.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int position,
						long arg3) {
					// TODO Auto-generated method stub
					CheckBox gb=(CheckBox)v.findViewById(R.id.checkBoxSelect);
					if(gb.isChecked()){
						gb.setChecked(false);
						v.setAlpha((float) 0.8);
				Log.e("majsdn",""+	mArrayListPhone.remove(ar.get(position).getPhone()));
					Log.e("removed","position:"+position);}
					else{
						gb.setChecked(true);
						mArrayListPhone.add(ar.get(position).getPhone());
						v.setAlpha(1);
						Log.e("added","position:"+position);}
				}
				
			});
           lv.setAdapter(new DialogViewAdapter(getBaseContext(), R.layout.layoutcontacts_withcheckbox,ar));
           
		}
	}
	
	
	private ArrayList<dialogview_rows> getAraryList() {
		ArrayList<dialogview_rows> ar=new ArrayList<dialogview_rows>();
		ContactsDB db=new ContactsDB(this);
		Cursor c=db.retrieveContacts();
		if(c.moveToFirst()&&c!=null){
	
	do{
		ar.add(new dialogview_rows(null, c.getString(c.getColumnIndex("name")),c.getString(c.getColumnIndex("phone"))));
		
	}while(c.moveToNext());
	}
		return ar;	
	}
	
	public void create(View view)
	{
		EditText ed=(EditText)findViewById(R.id.editGrpName);
		if(mArrayListPhone.size()==0)
			Toast.makeText(this, "You must add atleast one participants", Toast.LENGTH_SHORT).show();
		else
		{
			String name=ed.getText().toString();
			if(name.startsWith(" "))
				Toast.makeText(getApplicationContext(), "Group Name cannot start with space !",
					Toast.LENGTH_LONG).show();
			else if(name.length()<3)
				Toast.makeText(getApplicationContext(), "Group Name must be atleast of 3 characters ",
						Toast.LENGTH_LONG).show();
			else
			new asyncCreateGroup().execute(name);
		}
	}
	
	public class asyncCreateGroup extends AsyncTask<String,Void,Void>
	{
		ProgressBar pb= (ProgressBar) findViewById(R.id.progressBar);
		String gName,gID;
		@Override
		protected void onPreExecute()
		{
			pb.setVisibility(View.VISIBLE);
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			gName=params[0];
			return null;
		}
		
		@Override
		protected void onPostExecute(Void params)
		{
			pb.setVisibility(View.INVISIBLE);
			
			
			ContactsDB db=new ContactsDB(CreateGroupActivity.this);
			db.createGroup(gName+"h", gName, mArrayListPhone);
			Toast.makeText(getApplicationContext(), "Group "+gName+" created", Toast.LENGTH_LONG).show();
			startActivity(new Intent(getApplicationContext(),MainActivity.class));
		}
	}
	
	
}

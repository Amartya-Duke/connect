package com.nect.connect.database;

import java.util.ArrayList;

import com.nect.connect.group;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactsDB extends SQLiteOpenHelper{

	private static String DATABASE_NAME="ContactsDatabase";
	private static int DATABASE_VERSION=1;
		
	public String tContacts="contacts_table";
	private String tGroups="groups_list";
	private String tGroupParticipants="group_participant";
	
	private String CREATE_TABLEcontacts="create table " + tContacts+ "( name varchar(20), phone varchar(10) primary key);";
    private String CREATE_TABLEgroups_list="create table " + tGroups+"( groupID varchar primary key,groupname varchar(20) );";
    private String CREATE_TABLEgroups_participants="create table "+tGroupParticipants+"( groupID varchar , phone varchar(10) );";

	public String name="name";
	public String phone="phone";
	public String groupID="groupID";
	public String groupname="groupname";
	
	
	public ContactsDB(Context context) {
		super(context, DATABASE_NAME,null,DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLEcontacts);
		Log.e("ContactsDB.java", "Contacts table created");

		db.execSQL(CREATE_TABLEgroups_list);
		Log.e("ContactsDB.java", "Group list table created");
		
		db.execSQL(CREATE_TABLEgroups_participants);
		Log.e("ContactsDB.java", "Group participants table created");
	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	
	public boolean ifExists(String Phone) {
		SQLiteDatabase writableDb = this.getWritableDatabase();
		String selection=phone+"='"+Phone+"'";
	Cursor	cursor=writableDb.query(tContacts, null, selection, null, null, null, null);
		
		cursor.moveToFirst();
		if(cursor.getCount()>0)
			return true;
		else
			return false;
	}
	
	
	
	
	public void insertContact(String Phone,String Name){
		SQLiteDatabase writableDb = this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(phone,Phone);
		values.put(name,Name);
		writableDb.insert(tContacts, null, values);
		Log.e("Contactsdb","Contact inserted");
		
	}
	
	
	
	public String retrieveName(String Phone){
		SQLiteDatabase writableDb = this.getWritableDatabase();
		String columns[]={name};
		String where=phone+"='"+Phone+"'";
		Cursor c=writableDb.query(tContacts, columns,where, null, null, null, null, null);	
		if(c!=null){c.moveToFirst();
		if(c.getString(c.getColumnIndex(name))!=null)
	return	c.getString(c.getColumnIndex(name));
		else 
			return null;
		}
		else
			return null;
	}
	
	
	
	public Cursor retrieveContacts(){
		SQLiteDatabase writableDb = this.getWritableDatabase();
		String columns[]={name,phone};
		Cursor cursor=null;
		cursor=writableDb.query(tContacts, columns,null, null, null, null, null, null);	
		
		cursor.moveToFirst();
		return cursor;
		
	}

  public void createGroup(String groupID,String groupName,ArrayList<String> list)
  {
	  SQLiteDatabase writableDb = this.getWritableDatabase();
	 ContentValues values=new ContentValues();
	 values.put(this.groupID,groupID);
	 values.put(this.groupname	,groupName);
	 writableDb.insert(tGroups, null, values);
	 
	 values=new ContentValues();
	 for(int i=0;i<list.size();i++){
	 values.put(this.groupID, groupID);
	 values.put(phone, list.get(i));
	 writableDb.insert(tGroupParticipants, null, values);
	 }
  }


public ArrayList<group> retrieveGroups() {

	 SQLiteDatabase writableDb = this.getWritableDatabase();
	 Cursor cursor=writableDb.query(tGroups, null, null, null, null, null, null, null);
	 ArrayList<group> list=new ArrayList<group>();
	 if(cursor!=null){
		if(cursor.moveToFirst()){
	 group g;
	 Log.e("sf",cursor.getCount()+"");
		do{
			String gName=cursor.getString(cursor.getColumnIndex(groupname));
			String gID=cursor.getString(cursor.getColumnIndex(groupID));
			Log.e("sfnameID",gName+" "+gID);
			g=new group(gID,gName);
				list.add(g)	;
		}while(cursor.moveToNext());
			
		}
	 }
	return list;
}


	
	
	
}

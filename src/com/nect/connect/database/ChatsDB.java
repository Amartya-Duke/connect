package com.nect.connect.database;

import java.util.ArrayList;

import com.nect.connect.rows;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatsDB extends SQLiteOpenHelper{
	private String name="name";
	private String phone="phone";
	private String unreadCount="unreadCount";
	
	
	private static String DATABASE_NAME="ChatsDatabase";
	private static int DATABASE_VERSION=1;
	
	public static String tChat="chats_table";
	public static String tGroupChat="group_chat";
	
	private String CREATE_DBchats="create table " + tChat+ "( phone varchar(10) primary key,name varchar(20),unreadCount integer);";
	private Context context;
	
	public ChatsDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context=context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_DBchats);
		Log.e("ChatsDB.java", "Chats database created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public String getName(){
		return name;
	}
	
	public String getPhone(){
		return phone;
	}
	
	public String getUnread(){
		return unreadCount;
	}
	
	
	public Cursor retrieveChats() {
		SQLiteDatabase writableDb = this.getWritableDatabase();
		Cursor cursor=writableDb.query(tChat, null, null, null, null, null, null);
		return cursor;
	}

	
	
	public int getUnreadCnt(String Phone) {
		Log.e("ChatsDB", Phone+"");
		SQLiteDatabase writableDb = this.getWritableDatabase();
		String selection=phone+"='"+Phone+"'";
		String columns[]={unreadCount};
	Cursor c=writableDb.query(tChat, columns, selection, null, null, null, null);
	c.moveToFirst();
		return	c.getInt(c.getColumnIndex(unreadCount));
		
		
	}

	
	
	public void updateChats(String Phone){
		
		
		SQLiteDatabase writableDb = this.getWritableDatabase();
			ArrayList<rows> row = new ArrayList<rows>();
			String columns[]={name,phone,unreadCount};
			Cursor cursor=null;
			String where="phone"+"='"+Phone+"'";
			cursor=writableDb.query(tChat, columns,where, null, null, null, null, null);	
			if(cursor.getCount()==0){
				insertChat(Phone, new ContactsDB(context).retrieveName(Phone), 0);
				cursor=writableDb.query(tChat, columns,where, null, null, null, null, null);	
				Log.e("fnb", "new");
			}
			Cursor c=null;
			
			c=writableDb.query(tChat, columns,null, null, null, null, null, null);	
			
			rows cr;
			
			
		if(c!=null && c.moveToFirst()){
			;
			do
			{
			String nm=c.getString(c.getColumnIndex("name"));
			String ph=c.getString(c.getColumnIndex("phone"));
				
		if(!ph.equals(Phone)){
	     cr=new rows( nm,"stauts",null,null,ph,c.getInt(c.getColumnIndex(unreadCount)));
	     row.add(cr);  
		}
			}while(c.moveToNext());
	    cursor.moveToFirst();		
	int	unreadCnt=cursor.getInt(cursor.getColumnIndex(unreadCount));
		cr=new rows(cursor.getString(cursor.getColumnIndex("name")),"stauts",null,null,Phone,unreadCnt);
		
		row.add(0, cr);
		writableDb.delete(tChat, null, null);
		for(int i=0;i<c.getCount();i++)
		{
			ContentValues values = new ContentValues();
			values.put("phone",row.get(i).getPhone());
			values.put("name",row.get(i).getName());
			values.put(unreadCount, row.get(i).getUnreadCnt());
			writableDb.insert(tChat, null, values);
			
		}
		
		}
		Log.e("ChatsDB.java",Phone+" .Chats Updated");
	}


	public void insertChat(String Phone, String Name, int unreadCnt) {
		SQLiteDatabase writableDb = this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(phone,Phone);
		values.put(name,Name);
		values.put(this.unreadCount, unreadCnt);
		writableDb.insert(tChat, null, values);
		Log.e("Chatsdb","Chat inserted");
		
		
	}
public void updateRead(String Phone)
{
	int	unreadCnt;
	SQLiteDatabase writableDb = this.getWritableDatabase();
	String where="phone"+"='"+Phone+"'";
	String[] columns={this.unreadCount};
	Cursor cursor = writableDb.query(tChat, columns,where, null, null, null, null, null);
	cursor.moveToFirst();
	unreadCnt=cursor.getInt(cursor.getColumnIndex(unreadCount));
	Log.e("ChatsDB.java",Phone+" .Unread Count from Db:"+unreadCnt);
	ContentValues values=new ContentValues();
    values.put(this.unreadCount,unreadCnt+1);
    writableDb.update(tChat, values, where, null);
	
	Log.e("ChatsDB.java",Phone+" .Chats Updated.Unread Count:"+unreadCnt+1);
}
	public void resetUnreadCount(String Phone)
	 {
		SQLiteDatabase writableDb = this.getWritableDatabase();
		String where="phone"+"='"+Phone+"'";
		ContentValues values=new ContentValues();
		values.put(unreadCount,00);
		writableDb.update(tChat, values, where, null);
		Log.e("ChatsDB", "Chats unread Count reset");
	 }

	public boolean ifExist(String Phone) {
		SQLiteDatabase writableDb = this.getWritableDatabase();
		String where="phone"+"='"+Phone+"'";
		Cursor cursor=null;
		cursor=writableDb.query(tChat,null,where, null, null, null, null, null);	
		if(cursor.getCount()==0){
			insertChat(Phone, new ContactsDB(context).retrieveName(Phone), 0);
			return false;
		}
		else
			return true;
		
	}

	
}

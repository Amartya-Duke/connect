package com.nect.connect.database;


import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ConvDB extends SQLiteOpenHelper{
public String tConvo="convo_table";
public String tContacts="contacts_table";

	public String phone="phone";
	public String Last="Last Seen";
	public String send="send";
	public String recieve="recieve";
	public String time="time";
    private String mID="mID";
    private String unreadCount="unreadCount";
	private String R="R";
    private static String DATABASE_NAME="ConversationDatabase";
	private static int DATABASE_VERSION=1;
    private String Create_DBconvo="create table " + tConvo+ "(mID integer primary key autoincrement, phone  varchar(10),"
    	                            	+ "  send  varchar, recieve varchar,time varchar,R integer);";
	private Context context;

    
	public ConvDB(Context context) {
		super(context,DATABASE_NAME,null, DATABASE_VERSION);
		this.context=context;
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(Create_DBconvo);
		Log.e("ConvDB.java.java", "Conversation database created");
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	public String getMessageFromID(int mID){
		SQLiteDatabase writableDb = this.getWritableDatabase();
		
		String msg="";
		String columns[]={send,recieve};
		String selection=this.mID+"='"+mID+"'";
		Cursor c=writableDb.query(tConvo, columns, selection, null, null, null, null);
		if(c.moveToFirst()){
			String Send=c.getString(c.getColumnIndex(send));
			String Recieve=c.getString(c.getColumnIndex(recieve));
		if(Send!=null) msg=Send;
		if(Recieve!=null) msg=Recieve;
		}
		return msg;
	}
	 
	
	public int insertConvo(String Phone,String Send,String Recieve,String Time,int i){
		SQLiteDatabase writableDb = this.getWritableDatabase();

		ContentValues values=new ContentValues();
		values.put(phone,Phone);
		values.put(send,Send);
		values.put(recieve,Recieve);
		values.put(time, Time);
		values.put(R, i);
		writableDb.insert(tConvo, null, values);
		Log.e("ConvoDB","conversation inserted with sentRead:"+i);
	  return lastMessageID(Phone);
		
	}
	
	
	public Cursor retrieveConvo(String Phone){
		SQLiteDatabase writableDb = this.getWritableDatabase();
		String columns[]={mID,send,recieve,time,mID,R};
		String where="phone"+"='"+Phone+"'";
	    ChatsDB db=new ChatsDB(context);
		Cursor c=writableDb.query(tConvo, columns,"phone="+Phone, null, null, null, null, null);
		c.moveToFirst();
		
		return c;
	}
	
	
	public String lastMessage(String Phone)
	{
		SQLiteDatabase writableDb = this.getWritableDatabase();
		String colums[]={send,recieve};
		Cursor cursor=null;
		String selection=phone+"='"+Phone+"'";
		cursor=writableDb.query(tConvo, colums, selection, null, null, null, null);
		cursor.moveToFirst();
		String msg="";
		if(cursor.moveToLast()){
			String Send=cursor.getString(cursor.getColumnIndex(send));
			String Recieve=cursor.getString(cursor.getColumnIndex(recieve));
		if(Send!=null) msg=Send;
		if(Recieve!=null) msg=Recieve;
		}
		Log.e("ConvDB",Phone+" .Last Msg:"+msg);
		return msg;	
	}

	public int lastMessageID(String Phone)
	{
		SQLiteDatabase writableDb = this.getWritableDatabase();
		String colums[]={mID};
		Cursor cursor=null;
		String selection=phone+"='"+Phone+"'";
		cursor=writableDb.query(tConvo, colums, selection, null, null, null, null);
		cursor.moveToFirst();
		int MID = 0;
		if(cursor.moveToLast()){
			MID=cursor.getInt(cursor.getColumnIndex(mID));
		
		}
		Log.e("ConvDB",Phone+" .Last Msg ID:"+MID);
		return MID;	
	}


	public void updateConversation(int mID, int i) {
		SQLiteDatabase writableDb = this.getWritableDatabase();
		String selection=this.mID+"='"+mID+"'";
		ContentValues values=new ContentValues();
		values.put(R,i);
		writableDb.update(tConvo, values, selection, null);
		Log.e("ConvoDB","Conversation updated.mID="+mID+".with:"+i);
	}
	
}
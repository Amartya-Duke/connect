package com.nect.connect;

import android.graphics.Bitmap;

public class rows {
	String name;
	String status;
	String lastMessage;
	Bitmap picture;
	String phone;
    int unreadCnt;
	public rows(String name,String status,String lastMessage,Bitmap picture,String phone,int unreadCnt){
	
		this.name=name;
		this.status=status;
		this.lastMessage=lastMessage;
		this.picture=picture;
		this.phone=phone;
		this.unreadCnt=unreadCnt;
	}
	
	public String getName(){
		return name;
		
	}
	public String getStatus(){
			return status;
			
		}
	public String getMessage(){
			return lastMessage;
			
		}
	 public Bitmap getPicture(){
		 return picture;
	 }
	public String getPhone(){
		 return phone;
		 
	 }
	public int getUnreadCnt()
	 {
		 return unreadCnt;
	 }
}

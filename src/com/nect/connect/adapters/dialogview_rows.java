package com.nect.connect.adapters;

import android.graphics.Bitmap;

public class dialogview_rows {

	
	private Bitmap bitmap;
	private String name;
	private String phone;

	public dialogview_rows(Bitmap bitmap,String name,String Phone)
	{
		this.bitmap=bitmap;
		this.name=name;
		this.phone=Phone;
	}
	
	public String getName(){ return name; }
	public String getPhone(){return phone; }
	public Bitmap getBitmap(){return bitmap;}
	
}

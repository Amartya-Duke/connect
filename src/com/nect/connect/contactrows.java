package com.nect.connect;

import android.graphics.Bitmap;

public class contactrows {
String name;
String phone;
private Bitmap bitmap;

public contactrows(String name,String phone,Bitmap bitmap){
	this.name=name;
	this.phone=phone;
	this.bitmap=bitmap;
}

public String getName(){
	return name;
}
public String getPhone(){
	return phone;
}

public Bitmap getImage() {
	
	return bitmap;
}
}
  
package com.nect.connect;

public class convorows {

	private String recieve;
	private String send;
	private int R;
	private int Id;
	public convorows(int Id,String recieve,String send, int R){
		this.recieve=recieve;
		this.send=send;
		this.R=R;
		this.Id=Id;
	}
	
	public String getRecieve(){
		return recieve;
	}
	public String getSend(){
		return send;
	}
	public int getR()
	{
		return R;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return Id;
	}
}

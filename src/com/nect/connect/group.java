package com.nect.connect;

public class group {
	
	
	
	private String groupID;
	private String groupName;

	public group(String groupID,String groupName)
	{
		this.groupID=groupID;
		this.groupName=groupName;
	}

	public String getID(){
	return groupID;
	}
	
	public String getName()
	{
		return groupName;
	}
	
}

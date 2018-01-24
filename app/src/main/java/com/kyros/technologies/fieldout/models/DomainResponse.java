package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.SerializedName;

public class DomainResponse{

	@SerializedName("user")
	private User user;

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	@Override
 	public String toString(){
		return 
			"DomainResponse{" + 
			"user = '" + user + '\'' + 
			"}";
		}
}
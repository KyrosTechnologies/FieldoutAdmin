package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.SerializedName;

public class DomainResponse{

	@SerializedName("domain")
	private User domain;

	public void setUser(User user){
		this.domain = user;
	}

	public User getUser(){
		return domain;
	}

	@Override
 	public String toString(){
		return 
			"DomainResponse{" + 
			"user = '" + domain + '\'' +
			"}";
		}
}
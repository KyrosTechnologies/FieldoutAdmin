package com.kyros.technologies.fieldout.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UsersResponse{

	@SerializedName("users")
	private List<UsersItem> users;

	public void setUsers(List<UsersItem> users){
		this.users = users;
	}

	public List<UsersItem> getUsers(){
		return users;
	}

	@Override
 	public String toString(){
		return 
			"UsersResponse{" + 
			"users = " + users + '\'' +
			"}";
		}
}
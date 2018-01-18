package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.SerializedName;

public class AddUserResponse {

	@SerializedName("user")
	private User user;

	@SerializedName("isSuccess")
	private boolean isSuccess;

	@SerializedName("result")
    private Result result;

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setIsSuccess(boolean isSuccess){
		this.isSuccess = isSuccess;
	}

	public boolean isIsSuccess(){
		return isSuccess;
	}

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "AddUserResponse{" +
                "user=" + user +
                ", isSuccess=" + isSuccess +
                ", result=" + result +
                '}';
    }
}
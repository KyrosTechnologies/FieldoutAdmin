package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kyros on 16-12-2017.
 */

public class GetSingleUserResponse {
    @SerializedName("user")
    private UserInfo user;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "GetSingleUserResponse{" +
                "user=" + user +
                '}';
    }
}

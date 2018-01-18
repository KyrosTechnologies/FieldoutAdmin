package com.example.rohin.fieldoutadmin.models;

/**
 * Created by kyros on 14-12-2017.
 */


import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class UserUpdateResponse {

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("result")
    @Expose
    private Result result;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UserUpdateResponse{" +
                "isSuccess=" + isSuccess +
                ", result=" + result +
                '}';
    }
}
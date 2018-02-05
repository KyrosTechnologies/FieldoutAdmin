package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kyros on 05-02-2018.
 */
@ToString
@AllArgsConstructor
@Getter
@Setter
public class ChangePasswordResponse {
    @SerializedName("isSuccess")
    private boolean isSuccess;
    @SerializedName("result")
    private String result;
    @SerializedName("userDetails")
    private User userDetails;

    public ChangePasswordResponse(){

    }
}

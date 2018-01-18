package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kyros on 23-12-2017.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class UpdateToolsAndResourceResponse {
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("result")
    @Expose
    private Resource resource;
    public UpdateToolsAndResourceResponse() {
    }
}

package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kyros on 28-12-2017.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class AddSchedulingResponse {
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("scheduling_window")
    @Expose
    private SchedulingWindow schedulingWindow;
    @SerializedName("result")
    @Expose
    private SchedulingWindow result;

    public AddSchedulingResponse() {
    }
}

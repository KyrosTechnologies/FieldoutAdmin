package com.kyros.technologies.fieldout.models;

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
public class ActivityTypeUpdateResponse {

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("result")
    @Expose
    private ActivityType activityType;

    public ActivityTypeUpdateResponse() {
    }
}

package com.kyros.technologies.fieldout.models;

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
public class DeleteScheduleWindowsResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private Boolean result;

    public DeleteScheduleWindowsResponse() {
    }
}

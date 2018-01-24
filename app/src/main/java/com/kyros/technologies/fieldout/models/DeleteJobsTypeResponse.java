package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kyros on 27-12-2017.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class DeleteJobsTypeResponse {
//    {
//        "message": "Job Type deleted successfully ",
//            "result": true
//    }
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private boolean result;

    public DeleteJobsTypeResponse() {
    }
}

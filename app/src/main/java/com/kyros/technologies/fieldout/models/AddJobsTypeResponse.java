package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kyros on 26-12-2017.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class AddJobsTypeResponse {
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("job_type")
    @Expose
    private JobType jobType;
    @SerializedName("result")
    @Expose
    private JobType result;
    public AddJobsTypeResponse() {
    }
}

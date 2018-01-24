package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kyros on 26-12-2017.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class JobsTypeResponse {
    @SerializedName("job_types")
    @Expose
    private List<JobType> jobTypes = null;

    public JobsTypeResponse() {
    }
}

package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
public class ProjectType {

    @SerializedName("JobTypeInfo")
    @Expose
    private List<JobTypeInfo> jobTypeInfo = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idDomain")
    @Expose
    private String idDomain;
    @SerializedName("job_sequences")
    @Expose
    private List<String> jobSequences = null;
    @SerializedName("type_name")
    @Expose
    private String typeName;

    public ProjectType() {
    }
}

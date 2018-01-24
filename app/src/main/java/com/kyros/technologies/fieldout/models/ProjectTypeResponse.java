package com.kyros.technologies.fieldout.models;

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
public class ProjectTypeResponse {

    @SerializedName("project_types")
    @Expose
    private List<ProjectType> projectTypes = null;

    public ProjectTypeResponse() {
    }
}

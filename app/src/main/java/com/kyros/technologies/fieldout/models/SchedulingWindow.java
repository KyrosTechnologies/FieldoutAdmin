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
public class SchedulingWindow {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idDomain")
    @Expose
    private String idDomain;
    @SerializedName("isPublic")
    @Expose
    private Integer isPublic;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("timeEnd")
    @Expose
    private String timeEnd;
    @SerializedName("timeStart")
    @Expose
    private String timeStart;

    public SchedulingWindow() {
    }
}

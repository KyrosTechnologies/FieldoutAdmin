package com.example.rohin.fieldoutadmin.models;

/**
 * Created by kyros on 22-12-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ActivityType {

    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("hasConflit")
    @Expose
    private String hasConflit;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idDomain")
    @Expose
    private String idDomain;
    @SerializedName("name")
    @Expose
    private String name;

    public ActivityType() {
    }
}
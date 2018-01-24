package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 21-12-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idDomain")
    @Expose
    private String idDomain;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdDomain() {
        return idDomain;
    }

    public void setIdDomain(String idDomain) {
        this.idDomain = idDomain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id='" + id + '\'' +
                ", idDomain='" + idDomain + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

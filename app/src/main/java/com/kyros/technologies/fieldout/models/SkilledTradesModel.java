package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kyros on 16-12-2017.
 */

public class SkilledTradesModel {
    @SerializedName("name")
    private List<String> name;

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SkilledTradesModel{" +
                "name=" + name +
                '}';
    }
}

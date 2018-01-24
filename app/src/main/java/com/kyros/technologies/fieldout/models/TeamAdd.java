package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kyros on 16-12-2017.
 */

public class TeamAdd {
    @SerializedName("id")
    private List<String> id;

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TeamAdd{" +
                "id=" + id +
                '}';
    }
}

package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 20-12-2017.
 */

import java.util.List;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class ManagersResponse {

    @SerializedName("managers")
    @Expose
    private List<Manager> managers = null;

    public List<Manager> getManagers() {
        return managers;
    }

    public void setManagers(List<Manager> managers) {
        this.managers = managers;
    }

    @Override
    public String toString() {
        return "ManagersResponse{" +
                "managers=" + managers +
                '}';
    }
}
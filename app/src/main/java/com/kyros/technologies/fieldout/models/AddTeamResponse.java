package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 21-12-2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddTeamResponse {

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("team")
    @Expose
    private TeamsItem team;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public TeamsItem getTeam() {
        return team;
    }

    public void setTeam(TeamsItem team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "AddTeamResponse{" +
                "isSuccess=" + isSuccess +
                ", team=" + team +
                '}';
    }
}
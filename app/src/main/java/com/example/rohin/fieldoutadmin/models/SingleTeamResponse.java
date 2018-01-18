package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kyros on 21-12-2017.
 */

public class SingleTeamResponse {
    @SerializedName("team")
    private TeamsItem teams;

    public void setTeams(TeamsItem teams){
        this.teams = teams;
    }

    public TeamsItem getTeams(){
        return teams;
    }

    @Override
    public String toString(){
        return
                "TeamsResponse{" +
                        "teams = '" + teams + '\'' +
                        "}";
    }
}

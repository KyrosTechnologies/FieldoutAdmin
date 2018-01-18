package com.example.rohin.fieldoutadmin.models;

/**
 * Created by kyros on 20-12-2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TechniciansResponse {

    @SerializedName("technicians")
    @Expose
    private List<Technician> technicians = null;

    public List<Technician> getTechnicians() {
        return technicians;
    }

    public void setTechnicians(List<Technician> technicians) {
        this.technicians = technicians;
    }

    @Override
    public String toString() {
        return "TechniciansResponse{" +
                "technicians=" + technicians +
                '}';
    }
}
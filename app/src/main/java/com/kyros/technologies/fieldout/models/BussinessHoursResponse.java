package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 13-12-2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BussinessHoursResponse {

    @SerializedName("business_hour")
    @Expose
    private BussinessHoursModel businessHour;

    public BussinessHoursModel getBusinessHour() {
        return businessHour;
    }

    public void setBusinessHour(BussinessHoursModel businessHour) {
        this.businessHour = businessHour;
    }

    @Override
    public String toString() {
        return "BussinessHoursResponse{" +
                "businessHour=" + businessHour +
                '}';
    }
}
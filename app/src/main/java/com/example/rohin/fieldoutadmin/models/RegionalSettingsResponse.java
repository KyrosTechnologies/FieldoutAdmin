package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kyros on 03-01-2018.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class RegionalSettingsResponse {

    @SerializedName("regional_settings")
    @Expose
    private RegionalSettings regionalSettings;
    @SerializedName("isSuccess")
    @Expose
    private boolean isSuccess;

    public RegionalSettingsResponse() {
    }
}

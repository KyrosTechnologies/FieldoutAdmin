package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    private List<RegionalSettings> regionalSettings;
    @SerializedName("isSuccess")
    @Expose
    private boolean isSuccess;

    public RegionalSettingsResponse() {
    }
}

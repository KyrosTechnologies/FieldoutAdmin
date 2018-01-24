package com.kyros.technologies.fieldout.models;

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
public class RegionalSettings {

    @SerializedName("addressFormat")
    @Expose
    private String addressFormat;
    @SerializedName("dateTimeFormat")
    @Expose
    private DateTimeFormat dateTimeFormat;
    @SerializedName("decimalAndCurrencyFormat")
    @Expose
    private DecimalAndCurrencyFormat decimalAndCurrencyFormat;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idDomain")
    @Expose
    private String idDomain;

    public RegionalSettings() {
    }
}

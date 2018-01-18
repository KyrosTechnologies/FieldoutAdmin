package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import retrofit2.http.GET;

/**
 * Created by kyros on 03-01-2018.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class DecimalAndCurrencyFormat {

    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;
    @SerializedName("decimalSeperator")
    @Expose
    private String decimalSeperator;
    @SerializedName("distanceUnit")
    @Expose
    private String distanceUnit;
    @SerializedName("groupSeperator")
    @Expose
    private String groupSeperator;

    public DecimalAndCurrencyFormat() {
    }
}

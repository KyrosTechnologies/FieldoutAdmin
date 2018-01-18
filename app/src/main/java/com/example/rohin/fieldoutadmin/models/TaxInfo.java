package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import retrofit2.http.GET;

/**
 * Created by kyros on 04-01-2018.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class TaxInfo {

    @SerializedName("_id")
    @Expose
    private Id id;
    @SerializedName("idDomain")
    @Expose
    private String idDomain;
    @SerializedName("isDefaulttax")
    @Expose
    private Boolean isDefaulttax;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rate")
    @Expose
    private Double rate;

    public TaxInfo() {
    }
}

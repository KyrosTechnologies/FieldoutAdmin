package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kyros on 04-01-2018.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class StockPart {
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idDomain")
    @Expose
    private String idDomain;
    @SerializedName("idTax")
    @Expose
    private String idTax;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("taxInfo")
    @Expose
    private TaxInfo taxInfo;

    public StockPart() {
    }
}

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
public class TaxResponse {
//isSuccess,tax
    //  "message": "Tax deleted successfully ",
 //   "result": true
    @SerializedName("taxes")
    @Expose
    private List<Tax> taxes = null;

    @SerializedName("isSuccess")
    @Expose
    private boolean isSuccess ;

    @SerializedName("tax")
    @Expose
    private Tax tax ;

    @SerializedName("result")
    @Expose
    private Tax result ;
    public TaxResponse() {
    }
}

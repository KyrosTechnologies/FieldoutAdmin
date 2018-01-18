package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
public class PartsAndServicesResponse {
    @SerializedName("stock_parts")
    @Expose
    private List<StockPart> stockParts = null;

    //isSuccess
    @SerializedName("isSuccess")
    @Expose
    private boolean isSuccess;

    //stock_part
    @SerializedName("stock_part")
    @Expose
    private StockPart stock_part;

    //result
    @SerializedName("result")
    @Expose
    private StockPart result;

    //message
    @SerializedName("message")
    @Expose
    private String message;
    public PartsAndServicesResponse() {
    }
}

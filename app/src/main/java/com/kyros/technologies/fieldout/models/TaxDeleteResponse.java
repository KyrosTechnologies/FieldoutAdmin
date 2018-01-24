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
public class TaxDeleteResponse {
    @SerializedName("message")
    @Expose
    private String message = null;

    @SerializedName("result")
    @Expose
    private boolean result ;

    public TaxDeleteResponse() {
    }
}

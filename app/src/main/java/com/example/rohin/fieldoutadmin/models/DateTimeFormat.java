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
public class DateTimeFormat {

    @SerializedName("longDateFormat")
    @Expose
    private String longDateFormat;
    @SerializedName("shortDateFormat")
    @Expose
    private String shortDateFormat;
    @SerializedName("shortTimeFormat")
    @Expose
    private String shortTimeFormat;
    @SerializedName("timeZone")
    @Expose
    private String timeZone;

    public DateTimeFormat() {
    }
}

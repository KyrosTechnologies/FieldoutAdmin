package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kyros on 05-01-2018.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomFieldResponse {
    //isSuccess

    @SerializedName("custom_fields")
    @Expose
    private List<CustomField> customFields = null;

    @SerializedName("custom_field")
    @Expose
    private CustomField customField;

    @SerializedName("isSuccess")
    @Expose
    private boolean isSuccess;

    //result

    @SerializedName("result")
    @Expose
    private CustomField result;
//    {
//        "message": "Custom Field deleted successfully ",
//            "result": true
//    }

    public CustomFieldResponse() {
    }
}

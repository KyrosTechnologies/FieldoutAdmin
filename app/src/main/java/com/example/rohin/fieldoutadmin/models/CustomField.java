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
@Getter
@Setter
@AllArgsConstructor
@ToString
public class CustomField {
    @SerializedName("choices")
    @Expose
    private List<String> choices = null;
    @SerializedName("form_type")
    @Expose
    private String formType;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idDomain")
    @Expose
    private String idDomain;
    @SerializedName("isPrivate")
    @Expose
    private Boolean isPrivate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("typeOfField")
    @Expose
    private String typeOfField;

    @SerializedName("textValue")
    @Expose
    private String textValue;

    public CustomField() {
    }
}

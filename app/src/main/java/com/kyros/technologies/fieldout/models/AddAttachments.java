package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kyros on 02-02-2018.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class AddAttachments {
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("fileData")
    @Expose
    private InputStream fileData;


    public AddAttachments() {
    }
}

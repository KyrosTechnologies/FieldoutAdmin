package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 22-12-2017.
 */


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TagResponse {

    @SerializedName("tags")
    @Expose
    private List<Tag> tags = null;

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    //result
    @SerializedName("result")
    @Expose
    private Tag result = null;

    public TagResponse() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("tags", tags).toString();
    }

}

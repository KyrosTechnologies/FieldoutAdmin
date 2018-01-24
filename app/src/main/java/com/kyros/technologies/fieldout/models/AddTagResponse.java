package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 21-12-2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddTagResponse {

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("tag")
    @Expose
    private Tag tag;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "AddTagResponse{" +
                "isSuccess=" + isSuccess +
                ", tag=" + tag +
                '}';
    }
}

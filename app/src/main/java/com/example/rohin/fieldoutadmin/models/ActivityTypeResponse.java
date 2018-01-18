package com.example.rohin.fieldoutadmin.models;

/**
 * Created by kyros on 22-12-2017.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ActivityTypeResponse {

    @SerializedName("activity_types")
    @Expose
    private List<ActivityType> activityTypes = null;

    public ActivityTypeResponse() {
    }
}


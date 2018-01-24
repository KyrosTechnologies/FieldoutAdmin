package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 22-12-2017.
 */

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
    public class UpdateTeamResponse {

        @SerializedName("isSuccess")
        @Expose
        private Boolean isSuccess;
        @SerializedName("result")
        @Expose
        private Result result;
        public UpdateTeamResponse(){}
    }


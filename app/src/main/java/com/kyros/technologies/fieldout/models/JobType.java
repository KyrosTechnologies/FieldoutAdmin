package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kyros on 26-12-2017.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class JobType  {
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idDomain")
    @Expose
    private String idDomain;
    @SerializedName("isDefault")
    @Expose
    private Boolean isDefault;
    @SerializedName("job_report_template")
    @Expose
    private String jobReportTemplate;
    @SerializedName("job_type_name")
    @Expose
    private String jobTypeName;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("skilled_trades")
    @Expose
    private SkilledTradesModel skilledTrades;

    public JobType() {
    }
}

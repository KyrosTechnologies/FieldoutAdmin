package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 13-12-2017.
 */

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class BussinessHoursModel {

    @SerializedName("friEndTime")
    @Expose
    private String friEndTime;
    @SerializedName("friStartTime")
    @Expose
    private String friStartTime;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idDomain")
    @Expose
    private String idDomain;
    @SerializedName("isFriClosed")
    @Expose
    private Integer isFriClosed;
    @SerializedName("isMonClosed")
    @Expose
    private Integer isMonClosed;
    @SerializedName("isSatClosed")
    @Expose
    private Integer isSatClosed;
    @SerializedName("isSunClosed")
    @Expose
    private Integer isSunClosed;
    @SerializedName("isThuClosed")
    @Expose
    private Integer isThuClosed;
    @SerializedName("isTueClosed")
    @Expose
    private Integer isTueClosed;
    @SerializedName("isWedClosed")
    @Expose
    private Integer isWedClosed;
    @SerializedName("monEndTime")
    @Expose
    private String monEndTime;
    @SerializedName("monStartTime")
    @Expose
    private String monStartTime;
    @SerializedName("satEndTime")
    @Expose
    private String satEndTime;
    @SerializedName("satStartTime")
    @Expose
    private String satStartTime;
    @SerializedName("sunEndTime")
    @Expose
    private String sunEndTime;
    @SerializedName("sunStartTime")
    @Expose
    private String sunStartTime;
    @SerializedName("thuEndTime")
    @Expose
    private String thuEndTime;
    @SerializedName("thuStartTime")
    @Expose
    private String thuStartTime;
    @SerializedName("tueEndTime")
    @Expose
    private String tueEndTime;
    @SerializedName("tueStartTime")
    @Expose
    private String tueStartTime;
    @SerializedName("wedEndTime")
    @Expose
    private String wedEndTime;
    @SerializedName("wedStartTime")
    @Expose
    private String wedStartTime;

    public String getFriEndTime() {
        return friEndTime;
    }

    public void setFriEndTime(String friEndTime) {
        this.friEndTime = friEndTime;
    }

    public String getFriStartTime() {
        return friStartTime;
    }

    public void setFriStartTime(String friStartTime) {
        this.friStartTime = friStartTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdDomain() {
        return idDomain;
    }

    public void setIdDomain(String idDomain) {
        this.idDomain = idDomain;
    }

    public Integer getIsFriClosed() {
        return isFriClosed;
    }

    public void setIsFriClosed(Integer isFriClosed) {
        this.isFriClosed = isFriClosed;
    }

    public Integer getIsMonClosed() {
        return isMonClosed;
    }

    public void setIsMonClosed(Integer isMonClosed) {
        this.isMonClosed = isMonClosed;
    }

    public Integer getIsSatClosed() {
        return isSatClosed;
    }

    public void setIsSatClosed(Integer isSatClosed) {
        this.isSatClosed = isSatClosed;
    }

    public Integer getIsSunClosed() {
        return isSunClosed;
    }

    public void setIsSunClosed(Integer isSunClosed) {
        this.isSunClosed = isSunClosed;
    }

    public Integer getIsThuClosed() {
        return isThuClosed;
    }

    public void setIsThuClosed(Integer isThuClosed) {
        this.isThuClosed = isThuClosed;
    }

    public Integer getIsTueClosed() {
        return isTueClosed;
    }

    public void setIsTueClosed(Integer isTueClosed) {
        this.isTueClosed = isTueClosed;
    }

    public Integer getIsWedClosed() {
        return isWedClosed;
    }

    public void setIsWedClosed(Integer isWedClosed) {
        this.isWedClosed = isWedClosed;
    }

    public String getMonEndTime() {
        return monEndTime;
    }

    public void setMonEndTime(String monEndTime) {
        this.monEndTime = monEndTime;
    }

    public String getMonStartTime() {
        return monStartTime;
    }

    public void setMonStartTime(String monStartTime) {
        this.monStartTime = monStartTime;
    }

    public String getSatEndTime() {
        return satEndTime;
    }

    public void setSatEndTime(String satEndTime) {
        this.satEndTime = satEndTime;
    }

    public String getSatStartTime() {
        return satStartTime;
    }

    public void setSatStartTime(String satStartTime) {
        this.satStartTime = satStartTime;
    }

    public String getSunEndTime() {
        return sunEndTime;
    }

    public void setSunEndTime(String sunEndTime) {
        this.sunEndTime = sunEndTime;
    }

    public String getSunStartTime() {
        return sunStartTime;
    }

    public void setSunStartTime(String sunStartTime) {
        this.sunStartTime = sunStartTime;
    }

    public String getThuEndTime() {
        return thuEndTime;
    }

    public void setThuEndTime(String thuEndTime) {
        this.thuEndTime = thuEndTime;
    }

    public String getThuStartTime() {
        return thuStartTime;
    }

    public void setThuStartTime(String thuStartTime) {
        this.thuStartTime = thuStartTime;
    }

    public String getTueEndTime() {
        return tueEndTime;
    }

    public void setTueEndTime(String tueEndTime) {
        this.tueEndTime = tueEndTime;
    }

    public String getTueStartTime() {
        return tueStartTime;
    }

    public void setTueStartTime(String tueStartTime) {
        this.tueStartTime = tueStartTime;
    }

    public String getWedEndTime() {
        return wedEndTime;
    }

    public void setWedEndTime(String wedEndTime) {
        this.wedEndTime = wedEndTime;
    }

    public String getWedStartTime() {
        return wedStartTime;
    }

    public void setWedStartTime(String wedStartTime) {
        this.wedStartTime = wedStartTime;
    }

    @Override
    public String toString() {
        return "BussinessHoursModel{" +
                "friEndTime='" + friEndTime + '\'' +
                ", friStartTime='" + friStartTime + '\'' +
                ", id='" + id + '\'' +
                ", idDomain='" + idDomain + '\'' +
                ", isFriClosed=" + isFriClosed +
                ", isMonClosed=" + isMonClosed +
                ", isSatClosed=" + isSatClosed +
                ", isSunClosed=" + isSunClosed +
                ", isThuClosed=" + isThuClosed +
                ", isTueClosed=" + isTueClosed +
                ", isWedClosed=" + isWedClosed +
                ", monEndTime='" + monEndTime + '\'' +
                ", monStartTime='" + monStartTime + '\'' +
                ", satEndTime='" + satEndTime + '\'' +
                ", satStartTime='" + satStartTime + '\'' +
                ", sunEndTime='" + sunEndTime + '\'' +
                ", sunStartTime='" + sunStartTime + '\'' +
                ", thuEndTime='" + thuEndTime + '\'' +
                ", thuStartTime='" + thuStartTime + '\'' +
                ", tueEndTime='" + tueEndTime + '\'' +
                ", tueStartTime='" + tueStartTime + '\'' +
                ", wedEndTime='" + wedEndTime + '\'' +
                ", wedStartTime='" + wedStartTime + '\'' +
                '}';
    }
}
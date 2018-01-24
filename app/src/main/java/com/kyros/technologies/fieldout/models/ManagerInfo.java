package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 20-12-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ManagerInfo {

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idDomain")
    @Expose
    private String idDomain;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("skilledTrades")
    @Expose
    private SkilledTradesModel skilledTrades;
    @SerializedName("teams")
    @Expose
    private TeamAdd teams;
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("isVerified")
    @Expose
    private Boolean isVerified;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public SkilledTradesModel getSkilledTrades() {
        return skilledTrades;
    }

    public void setSkilledTrades(SkilledTradesModel skilledTrades) {
        this.skilledTrades = skilledTrades;
    }

    public TeamAdd getTeams() {
        return teams;
    }

    public void setTeams(TeamAdd teams) {
        this.teams = teams;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ManagerInfo{" +
                "createdAt='" + createdAt + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", hash='" + hash + '\'' +
                ", id='" + id + '\'' +
                ", idDomain='" + idDomain + '\'' +
                ", language='" + language + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", profile='" + profile + '\'' +
                ", skilledTrades=" + skilledTrades +
                ", teams=" + teams +
                ", username='" + username + '\'' +
                '}';
    }
}

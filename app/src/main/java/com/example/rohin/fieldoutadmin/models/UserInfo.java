package com.example.rohin.fieldoutadmin.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfo {

	@SerializedName("isVerified")
	private boolean isVerified;

	@SerializedName("profile")
	private String profile;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("hash")
	private String hash;

	@SerializedName("username")
	private String  username;

	@SerializedName("password")
	private String  password;

	@SerializedName("firstName")
	private String  firstName;

	@SerializedName("lastName")
	private String  lastName;

	@SerializedName("language")
	private String  language;

	@SerializedName("email")
	private String  email;

	@SerializedName("phone")
	private String  phone;

    @SerializedName("idDomain")
    private String  idDomain;
//TeamAdd class used here
    @SerializedName("teams")
    private TeamAdd  teams;

    @SerializedName("CustomFieldValues")
    private List<CustomField> CustomFieldValues;
	//SkilledTradesModel class used here

	@SerializedName("skilledTrades")
	private SkilledTradesModel skilledTrades;
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setIsVerified(boolean isVerified){
		this.isVerified = isVerified;
	}

	public boolean isIsVerified(){
		return isVerified;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setProfile(String profile){
		this.profile = profile;
	}

	public String getProfile(){
		return profile;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setHash(String hash){
		this.hash = hash;
	}

	public String getHash(){
		return hash;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

    public String getIdDomain() {
        return idDomain;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setIdDomain(String idDomain) {
        this.idDomain = idDomain;
    }

    public TeamAdd getTeams() {
        return teams;
    }

    public void setTeams(TeamAdd teams) {
        this.teams = teams;
    }

    public List<CustomField>  getCustomFieldValues() {
        return CustomFieldValues;
    }

    public void setCustomFieldValues(List<CustomField>  customFieldValues) {
        CustomFieldValues = customFieldValues;
    }
		//SkilledTradesModel class used here
	public SkilledTradesModel getSkilledTrades() {
		return skilledTrades;
	}

	public void setSkilledTrades(SkilledTradesModel skilledTrades) {
		this.skilledTrades = skilledTrades;
	}

	@Override
	public String toString() {
		return "UserInfo{" +
				"isVerified=" + isVerified +
				", profile='" + profile + '\'' +
				", createdAt='" + createdAt + '\'' +
				", hash='" + hash + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", language='" + language + '\'' +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", idDomain='" + idDomain + '\'' +
				", teams=" + teams +
				", CustomFieldValues=" + CustomFieldValues +
				", skilledTrades=" + skilledTrades +
				'}';
	}
}
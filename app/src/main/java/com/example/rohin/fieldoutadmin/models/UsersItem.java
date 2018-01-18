package com.example.rohin.fieldoutadmin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UsersItem{

	@SerializedName("lastName")
	private String lastName;

	@SerializedName("deviceIDs")
	private List<String> deviceIDs;

	@SerializedName("teams")
	private TeamAdd teams;

	@SerializedName("profile")
	private String profile;

	@SerializedName("CustomFieldValues")
	private List<CustomField> customFieldValues;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("language")
	private String language;

	@SerializedName("skilledTrades")
	private SkilledTradesModel skilledTrades;

	@SerializedName("firstName")
	private String firstName;

	@SerializedName("password")
	private String password;

	@SerializedName("phone")
	private String phone;

	@SerializedName("id")
	private String id;

	@SerializedName("idDomain")
	private String idDomain;

	@SerializedName("email")
	private String email;

	@SerializedName("hash")
	private String hash;

	@SerializedName("username")
	private String username;

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setDeviceIDs(List<String> deviceIDs){
		this.deviceIDs = deviceIDs;
	}

	public List<String> getDeviceIDs(){
		return deviceIDs;
	}

	public void setTeams(TeamAdd teams){
		this.teams = teams;
	}

	public TeamAdd getTeams(){
		return teams;
	}

	public void setProfile(String profile){
		this.profile = profile;
	}

	public String getProfile(){
		return profile;
	}

	public void setCustomFieldValues(List<CustomField> customFieldValues){
		this.customFieldValues = customFieldValues;
	}

	public List<CustomField>  getCustomFieldValues(){
		return customFieldValues;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	public String getLanguage(){
		return language;
	}

	public void setSkilledTrades(SkilledTradesModel skilledTrades){
		this.skilledTrades = skilledTrades;
	}

	public SkilledTradesModel getSkilledTrades(){
		return skilledTrades;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setIdDomain(String idDomain){
		this.idDomain = idDomain;
	}

	public String getIdDomain(){
		return idDomain;
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

	@Override
 	public String toString(){
		return 
			"UsersItem{" + 
			"lastName = '" + lastName + '\'' + 
			",deviceIDs = '" + deviceIDs + '\'' + 
			",teams = '" + teams + '\'' + 
			",profile = '" + profile + '\'' + 
			",customFieldValues = '" + customFieldValues + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",language = '" + language + '\'' + 
			",skilledTrades = '" + skilledTrades + '\'' + 
			",firstName = '" + firstName + '\'' + 
			",password = '" + password + '\'' + 
			",phone = '" + phone + '\'' + 
			",id = '" + id + '\'' + 
			",idDomain = '" + idDomain + '\'' + 
			",email = '" + email + '\'' + 
			",hash = '" + hash + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}
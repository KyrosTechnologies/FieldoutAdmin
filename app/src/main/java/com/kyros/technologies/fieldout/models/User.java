package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("lastName")
	private String lastName;

    @SerializedName("company")
    private String company;

	@SerializedName("profile")
	private String profile;

	@SerializedName("CustomFieldValues")
	private Object customFieldValues;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("language")
	private String language;

	@SerializedName("skilledTrades")
	private Object skilledTrades;

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

	@SerializedName("domain")
	private String domain;

    @SerializedName("site")
    private String site;

    @SerializedName("idUser")
    private String idUser;

    @SerializedName("userinfo")
    private UserInfo userinfo;

    public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}


	public void setProfile(String profile){
		this.profile = profile;
	}

	public String getProfile(){
		return profile;
	}

	public void setCustomFieldValues(Object customFieldValues){
		this.customFieldValues = customFieldValues;
	}

	public  Object getCustomFieldValues(){
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

	public void setSkilledTrades(Object skilledTrades){
		this.skilledTrades = skilledTrades;
	}

	public Object getSkilledTrades(){
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    @Override
    public String toString() {
        return "User{" +
                "lastName='" + lastName + '\'' +
                ", company='" + company + '\'' +
                ", profile='" + profile + '\'' +
                ", customFieldValues=" + customFieldValues +
                ", createdAt='" + createdAt + '\'' +
                ", language='" + language + '\'' +
                ", skilledTrades=" + skilledTrades +
                ", firstName='" + firstName + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", id='" + id + '\'' +
                ", idDomain='" + idDomain + '\'' +
                ", email='" + email + '\'' +
                ", hash='" + hash + '\'' +
                ", username='" + username + '\'' +
                ", domain='" + domain + '\'' +
                ", site='" + site + '\'' +
                ", idUser='" + idUser + '\'' +
                ", userinfo=" + userinfo +
                '}';
    }
}
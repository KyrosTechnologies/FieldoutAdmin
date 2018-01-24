package com.kyros.technologies.fieldout.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

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

	@SerializedName("Managers")
	private List<String> Managers;

    @SerializedName("Technicians")
    private List<String> Technicians;

    @SerializedName("descriptions")
    @Expose
    private String descriptions;


    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("selected")
    @Expose
    private boolean selected;

    @SerializedName("managerInfo")
    @Expose
    private List<ManagerInfo> managerInfo;

     @SerializedName("technicianInfo")
     @Expose
     private List<TechnicianInfo> technicianInfo;

     @SerializedName("tags")
     @Expose
     private List<String> tags;




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

	public List<CustomField> getCustomFieldValues(){
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
    public String toString() {
        return "Result{" +
                "lastName='" + lastName + '\'' +
                ", deviceIDs=" + deviceIDs +
                ", teams=" + teams +
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
                ", Managers=" + Managers +
                ", Technicians=" + Technicians +
                ", descriptions='" + descriptions + '\'' +
                ", name='" + name + '\'' +
                ", selected=" + selected +
                ", managerInfo=" + managerInfo +
                ", technicianInfo=" + technicianInfo +
                ", tags=" + tags +
                '}';
    }

    public List<String> getManagers() {
        return Managers;
    }

    public void setManagers(List<String> managers) {
        Managers = managers;
    }

    public List<String> getTechnicians() {
        return Technicians;
    }

    public void setTechnicians(List<String> technicians) {
        Technicians = technicians;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<ManagerInfo> getManagerInfo() {
        return managerInfo;
    }

    public void setManagerInfo(List<ManagerInfo> managerInfo) {
        this.managerInfo = managerInfo;
    }

    public List<TechnicianInfo> getTechnicianInfo() {
        return technicianInfo;
    }

    public void setTechnicianInfo(List<TechnicianInfo> technicianInfo) {
        this.technicianInfo = technicianInfo;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
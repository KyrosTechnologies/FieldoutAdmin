package com.example.rohin.fieldoutadmin.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamsItem{

  	@SerializedName("Managers")
	private List<String> managers;

	@SerializedName("name")
	private String name;

	@SerializedName("Technicians")
	private List<String> technicians;

	@SerializedName("id")
	private String id;

	@SerializedName("idDomain")
	private String idDomain;

	@SerializedName("descriptions")
	private String descriptions;

	@SerializedName("tags")
	private List<String> tags;

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

    @SerializedName("hash")
    @Expose
    private String hash;

	@SerializedName("selected")
	private boolean  selected=false;

	@SerializedName("tagInfo")
	@Expose
	private List<TagInfo> tagInfo = null;

    @SerializedName("managerInfo")
    @Expose
    private List<ManagerInfo> managerInfo = null;

    @SerializedName("technicianInfo")
    @Expose
    private List<TechnicianInfo> technicianInfo = null;



	public void setManagers(List<String> managers){
		this.managers = managers;
	}

	public List<String> getManagers(){
		return managers;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setTechnicians(List<String> technicians){
		this.technicians = technicians;
	}

	public List<String> getTechnicians(){
		return technicians;
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

	public void setDescriptions(String descriptions){
		this.descriptions = descriptions;
	}

	public String getDescriptions(){
		return descriptions;
	}

	public void setTags(List<String> tags){
		this.tags = tags;
	}

	public List<String> getTags(){
		return tags;
	}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSelected() {
        return selected;
    }

    public List<TagInfo> getTagInfo() {
        return tagInfo;
    }

    public void setTagInfo(List<TagInfo> tagInfo) {
        this.tagInfo = tagInfo;
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

    @Override
    public String toString() {
        return "TeamsItem{" +
                "managers=" + managers +
                ", name='" + name + '\'' +
                ", technicians=" + technicians +
                ", id='" + id + '\'' +
                ", idDomain='" + idDomain + '\'' +
                ", descriptions='" + descriptions + '\'' +
                ", tags=" + tags +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", language='" + language + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", selected=" + selected +
                ", tagInfo=" + tagInfo +
                ", managerInfo=" + managerInfo +
                ", technicianInfo=" + technicianInfo +
                '}';
    }
}
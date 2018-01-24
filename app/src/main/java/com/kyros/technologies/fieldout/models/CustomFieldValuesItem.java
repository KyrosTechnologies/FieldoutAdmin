package com.kyros.technologies.fieldout.models;

import com.google.gson.annotations.SerializedName;

public class CustomFieldValuesItem{

	@SerializedName("type")
	private String type;

	@SerializedName("value")
	private String value;

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}

	@Override
 	public String toString(){
		return 
			"CustomFieldValuesItem{" + 
			"type = '" + type + '\'' + 
			",value = '" + value + '\'' + 
			"}";
		}
}
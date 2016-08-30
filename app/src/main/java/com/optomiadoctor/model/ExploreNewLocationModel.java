package com.optomiadoctor.model;

public class ExploreNewLocationModel {
	String strCityName;
	String strLongitude;
	String strLattitude;
	String strGoogleDesc;
	String strGoogleId;

	public ExploreNewLocationModel() {
	}

	public ExploreNewLocationModel(String cityname, String Longitude,
			String Lattitude) {
		this.strCityName = cityname;
		this.strLongitude = Longitude;
		this.strLattitude = Lattitude;
	}

	public String getStrCityName() {
		return strCityName;
	}

	public void setStrCityName(String strCityName) {
		this.strCityName = strCityName;
	}

	public String getStrLongitude() {
		return strLongitude;
	}

	public void setStrLongitude(String strLongitude) {
		this.strLongitude = strLongitude;
	}

	public String getStrLattitude() {
		return strLattitude;
	}

	public void setStrLattitude(String strLattitude) {
		this.strLattitude = strLattitude;
	}

	public String getStrGoogleDesc() {
		return strGoogleDesc;
	}

	public void setStrGoogleDesc(String strGoogleDesc) {
		this.strGoogleDesc = strGoogleDesc;
	}

	public String getStrGoogleId() {
		return strGoogleId;
	}

	public void setStrGoogleId(String strGoogleId) {
		this.strGoogleId = strGoogleId;
	}
}

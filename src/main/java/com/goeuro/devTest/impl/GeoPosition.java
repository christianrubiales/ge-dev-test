package com.goeuro.devTest.impl;

/**
 * An position containing a latitude and a longitude.
 */
public class GeoPosition {

	private String latitude;
	private String longitude;
	
	// Getters/Setters
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}

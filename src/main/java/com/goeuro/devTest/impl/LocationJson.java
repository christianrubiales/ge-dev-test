package com.goeuro.devTest.impl;

/**
 * A representation of a Location object that reflects the source JSON structure.
 */
public class LocationJson {

	private int _id;
	private String name;
	private String type;
	private GeoPosition geo_position;
	
	// Getters/Setters
	
	public int get_id() {
		return _id;
	}
	
	public void set_id(int _id) {
		this._id = _id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public GeoPosition getGeoPosition() {
		return geo_position;
	}
	
	public void setGeoPosition(GeoPosition geoPosition) {
		this.geo_position = geoPosition;
	}
	
}

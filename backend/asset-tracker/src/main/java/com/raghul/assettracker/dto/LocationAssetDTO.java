package com.raghul.assettracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class LocationAssetDTO {
	
	private String assetId;
	private Double lat;
	private Double lon;
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	@Override
	public String toString() {
		return "LocationAssetDTO [assetId=" + assetId + ", lat=" + lat + ", lon=" + lon + "]";
	}
	
	
	
	
	

}

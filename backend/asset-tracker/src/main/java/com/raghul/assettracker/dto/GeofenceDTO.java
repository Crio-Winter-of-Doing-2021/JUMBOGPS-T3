package com.raghul.assettracker.dto;

import java.util.List;

public class GeofenceDTO {
	
	private String assetId;
	private List<CoordinateDTO> coordinates;
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public List<CoordinateDTO> getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(List<CoordinateDTO> coordinates) {
		this.coordinates = coordinates;
	}
	

}

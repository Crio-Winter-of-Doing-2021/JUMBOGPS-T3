package com.raghul.assettracker.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class AssetGeofenceDTO {
	
	private GeofenceDTO geofenceDTO;
	private Boolean isWithinFence;
	private String message;
	private AssetDTO asset;
	public GeofenceDTO getGeofenceDTO() {
		return geofenceDTO;
	}
	public void setGeofenceDTO(GeofenceDTO geofenceDTO) {
		this.geofenceDTO = geofenceDTO;
	}
	public Boolean getIsWithinFence() {
		return isWithinFence;
	}
	public void setIsWithinFence(Boolean isWithinFence) {
		this.isWithinFence = isWithinFence;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public AssetDTO getAsset() {
		return asset;
	}
	public void setAsset(AssetDTO asset) {
		this.asset = asset;
	}
	
	
	
}

package com.raghul.assettracker.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Polygon;

@Entity
@Table(name="asset_geofence")
public class AssetGeofenceModel {
	
	@Id
	@Type(type="uuid-char")
	@Column(name="asset_geofence_id")
	private UUID assetGeofenceId;
	@Column(name="asset_id")
	@Type(type="uuid-char")
	private UUID assetId;
	@Column(name="created_by")
	private String createdBy;
	@Column(name="geofence",columnDefinition = "Geometry")
	private Polygon geoFence;
	public UUID getAssetGeofenceId() {
		return assetGeofenceId;
	}
	public void setAssetGeofenceId(UUID assetGeofenceId) {
		this.assetGeofenceId = assetGeofenceId;
	}
	public UUID getAssetId() {
		return assetId;
	}
	public void setAssetId(UUID assetId) {
		this.assetId = assetId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Polygon getGeoFence() {
		return geoFence;
	}
	public void setGeoFence(Polygon geoFence) {
		this.geoFence = geoFence;
	}
	

}

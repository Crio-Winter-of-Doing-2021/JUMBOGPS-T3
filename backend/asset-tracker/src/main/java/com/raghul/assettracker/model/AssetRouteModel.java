package com.raghul.assettracker.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name="asset_route")
public class AssetRouteModel {

	@Id
	@Column(name="asset_route_id")
	@Type(type="uuid-char")
	private UUID assetRouteId;
	@Column(name="asset_id")
	@Type(type="uuid-char")
	private UUID assetId;
	@Column(name="location_src",columnDefinition = "POINT")
	private Point locationSrc;
	@Column(name="location_dest",columnDefinition = "POINT")
	private Point locationDest;
	@Column(name="is_destination_reached")
	private Boolean isDestinationReached;
	@Column(name="created_by")
	private String createdBy;
	public UUID getAssetRouteId() {
		return assetRouteId;
	}
	public void setAssetRouteId(UUID assetRouteId) {
		this.assetRouteId = assetRouteId;
	}
	public UUID getAssetId() {
		return assetId;
	}
	public void setAssetId(UUID assetId) {
		this.assetId = assetId;
	}
	public Point getLocationSrc() {
		return locationSrc;
	}
	public void setLocationSrc(Point locationSrc) {
		this.locationSrc = locationSrc;
	}
	public Point getLocationDest() {
		return locationDest;
	}
	public void setLocationDest(Point locationDest) {
		this.locationDest = locationDest;
	}
	public Boolean getIsDestinationReached() {
		return isDestinationReached;
	}
	public void setIsDestinationReached(Boolean isDestinationReached) {
		this.isDestinationReached = isDestinationReached;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	
}

package com.raghul.assettracker.model;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;



@Entity
@Table(name="asset_location")
public class AssetLocation {
	
	
	@Id
	@Column(name="asset_location_id")
	@Type(type="uuid-char")
	private UUID assetLocationId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="asset_id")
	private Asset asset;
	@Column(name="location",columnDefinition = "POINT")
	private Point location;
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@Column(name="created_by")
	private String createdBy;
	public UUID getAssetLocationId() {
		return assetLocationId;
	}
	public void setAssetLocationId(UUID assetLocationId) {
		this.assetLocationId = assetLocationId;
	}
	public Asset getAsset() {
		return asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	public Timestamp getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	
	
	
	
	
	

}

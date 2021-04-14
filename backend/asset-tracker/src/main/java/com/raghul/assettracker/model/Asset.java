package com.raghul.assettracker.model;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="asset")
public class Asset {
	
	
	@Id
	@Column(name="asset_id")
	@Type( type = "uuid-char")
	private UUID assetId;
	@Column(name="asset_name")
	private String assetName;
	@Column(name="asset_type")
	@Type(type="uuid-char")
	private UUID assetType;
	
	@Column(name="created_on")
	private Timestamp createdOn;
	@Column(name="created_by")
	private String createdBy;
	public UUID getAssetId() {
		return assetId;
	}
	public void setAssetId(UUID assetId) {
		this.assetId = assetId;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public UUID getAssetType() {
		return assetType;
	}
	public void setAssetType(UUID assetType) {
		this.assetType = assetType;
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

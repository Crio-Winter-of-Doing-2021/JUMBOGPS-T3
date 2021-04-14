package com.raghul.assettracker.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name="asset_route_path")
public class AssetRoutePathModel {

	@Id
	@Column(name="asset_route_path_id")
	@Type(type="uuid-char")
	private UUID assetRoutePathId;
	
	@Column(name="asset_route_id")
	@Type(type="uuid-char")
	private UUID assetRouteId;
	
	@Column(name="location",columnDefinition = "POINT")
	private Point location;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="sort_order")
	private Integer sortOrder;

	public UUID getAssetRoutePathId() {
		return assetRoutePathId;
	}

	public void setAssetRoutePathId(UUID assetRoutePathId) {
		this.assetRoutePathId = assetRoutePathId;
	}

	public UUID getAssetRouteId() {
		return assetRouteId;
	}

	public void setAssetRouteId(UUID assetRouteId) {
		this.assetRouteId = assetRouteId;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	
	
	
}

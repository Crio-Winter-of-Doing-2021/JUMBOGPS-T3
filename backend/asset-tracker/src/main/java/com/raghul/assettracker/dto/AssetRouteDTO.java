package com.raghul.assettracker.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class AssetRouteDTO {
	
	private String assetId;
	private CoordinateDTO src;
	private CoordinateDTO dest;
	private List<OrderedCoordinateDTO> routes;
	private Boolean isDestinationReached;
	private Boolean isWithinRoute;
	private String message;
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public CoordinateDTO getSrc() {
		return src;
	}
	public void setSrc(CoordinateDTO src) {
		this.src = src;
	}
	public CoordinateDTO getDest() {
		return dest;
	}
	public void setDest(CoordinateDTO dest) {
		this.dest = dest;
	}
	public List<OrderedCoordinateDTO> getRoutes() {
		return routes;
	}
	public void setRoutes(List<OrderedCoordinateDTO> routes) {
		this.routes = routes;
	}
	public Boolean getIsDestinationReached() {
		return isDestinationReached;
	}
	public void setIsDestinationReached(Boolean isDestinationReached) {
		this.isDestinationReached = isDestinationReached;
	}
	public Boolean getIsWithinRoute() {
		return isWithinRoute;
	}
	public void setIsWithinRoute(Boolean isWithinRoute) {
		this.isWithinRoute = isWithinRoute;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	

}

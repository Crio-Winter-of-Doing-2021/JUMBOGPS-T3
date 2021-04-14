package com.raghul.assettracker.dto;

public class OrderedCoordinateDTO {

	private CoordinateDTO coordinate;
	private Integer order;
	public CoordinateDTO getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(CoordinateDTO coordinate) {
		this.coordinate = coordinate;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
}

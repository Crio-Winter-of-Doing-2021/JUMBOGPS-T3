package com.raghul.assettracker.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="notification")
public class NotificationModel {
	
	@Column(name="notification_id")
	@Id
	@Type(type="uuid-char")
	private UUID notificationId;
	@Column(name="asset_id")
	@Type(type="uuid-char")
	private UUID assetId;
	@Column(name="message")
	private String message;
	@Column(name="created_by")
	private String createdBy;
	public UUID getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(UUID notificationId) {
		this.notificationId = notificationId;
	}
	public UUID getAssetId() {
		return assetId;
	}
	public void setAssetId(UUID assetId) {
		this.assetId = assetId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	

}

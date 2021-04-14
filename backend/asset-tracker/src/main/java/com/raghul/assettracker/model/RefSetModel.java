package com.raghul.assettracker.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="ref_set")
public class RefSetModel {
	
	@Id
	@Column(name="ref_set_id")
	@Type(type="uuid-char")
	private UUID refSetId;
	@Column(name="ref_set_value")
	private String refSetKey;
	@Column(name="created_by")
	@Type(type="uuid-char")
	private UUID createdBy;
	public UUID getRefSetId() {
		return refSetId;
	}
	public void setRefSetId(UUID refSetId) {
		this.refSetId = refSetId;
	}
	public String getRefSetKey() {
		return refSetKey;
	}
	public void setRefSetKey(String refSetKey) {
		this.refSetKey = refSetKey;
	}
	public UUID getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UUID createdBy) {
		this.createdBy = createdBy;
	}
	

}

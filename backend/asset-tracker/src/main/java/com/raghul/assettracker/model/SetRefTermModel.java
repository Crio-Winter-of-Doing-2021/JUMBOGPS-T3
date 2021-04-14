package com.raghul.assettracker.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="set_ref_term")
public class SetRefTermModel {
	
	@Id
	@Column(name="set_ref_term_id")
	@Type(type="uuid-char")
	private UUID setRefTermId;
	@Column(name="ref_set_id")
	@Type(type="uuid-char")
	private UUID refSetId;
	@Column(name="ref_term_id")
	@Type(type="uuid-char")
	private UUID refTermId;
	public UUID getSetRefTermId() {
		return setRefTermId;
	}
	public void setSetRefTermId(UUID setRefTermId) {
		this.setRefTermId = setRefTermId;
	}
	public UUID getRefSetId() {
		return refSetId;
	}
	public void setRefSetId(UUID refSetId) {
		this.refSetId = refSetId;
	}
	public UUID getRefTermId() {
		return refTermId;
	}
	public void setRefTermId(UUID refTermId) {
		this.refTermId = refTermId;
	}
	
	

}

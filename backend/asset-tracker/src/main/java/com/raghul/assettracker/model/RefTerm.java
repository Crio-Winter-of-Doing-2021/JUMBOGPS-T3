package com.raghul.assettracker.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="ref_term")
public class RefTerm {
	
	@Id
	@Column(name="ref_term_id")
	@Type(type="uuid-char")
	private UUID refTermId;
	@Column(name="ref_term_value")
	private String refTermValue;
	public UUID getRefTermId() {
		return refTermId;
	}
	public void setRefTermId(UUID refTermId) {
		this.refTermId = refTermId;
	}
	public String getRefTermValue() {
		return refTermValue;
	}
	public void setRefTermValue(String refTermValue) {
		this.refTermValue = refTermValue;
	}
	
	
	

}

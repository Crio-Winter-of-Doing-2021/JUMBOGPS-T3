package com.raghul.assettracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class AuthenticateResponseDTO {
	
	private String jsonToken;
	private Long expiresIn;
	private String userRole;
	public String getJsonToken() {
		return jsonToken;
	}
	public void setJsonToken(String jsonToken) {
		this.jsonToken = jsonToken;
	}
	public Long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	

}

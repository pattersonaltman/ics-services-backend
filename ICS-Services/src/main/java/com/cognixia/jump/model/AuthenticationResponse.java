package com.cognixia.jump.model;

import java.io.Serializable;

// response object for when we authenticate and return the JWT
public class AuthenticationResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String jwt;
	
	public AuthenticationResponse(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	
}

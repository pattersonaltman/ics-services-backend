package com.cognixia.jump.model;

import java.io.Serializable;

// model object used to send the username & password when a user first authenticates and needs to create a JWT
public class AuthenticationRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	
	public AuthenticationRequest() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

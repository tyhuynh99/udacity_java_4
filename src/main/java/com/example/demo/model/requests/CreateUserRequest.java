package com.example.demo.model.requests;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest {

	@JsonProperty
	private String username;

	@JsonProperty
	@Size(min = 8, max = 16, message = "Password between 8 and 16 characters")
	private String password;

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

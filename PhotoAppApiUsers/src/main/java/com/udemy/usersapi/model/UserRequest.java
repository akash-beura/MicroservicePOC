package com.udemy.usersapi.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequest {

	@NotNull(message = "First name cannot be null")
	@Size(min = 2, message = "Minimum size is 2")
	private String firstName;
	@NotNull(message = "Last name cannot be null")
	@Size(min = 2, message = "Minimum size is 2")
	private String lastName;
	@NotNull(message = "Password name cannot be null")
	@Size(min = 8, max = 16, message = "Password should be >=8 and <=16 characters")
	private String encryptedPassword;
	@NotNull(message = "Password name cannot be empty")
	@Email
	private String email;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

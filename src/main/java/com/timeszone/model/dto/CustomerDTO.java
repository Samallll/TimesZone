package com.timeszone.model.dto;

import java.util.Set;

import com.timeszone.model.Address;
import com.timeszone.model.Role;

public class CustomerDTO {
	
	private Integer customerId;

	private String firstName;
	
	private String lastName;
	
	private String emailId;
	
	private String phoneNumber;
	
	private Set<Role> authorities;
	
	private Set<Address> addresses;
	
	private boolean isLocked=false;

	public CustomerDTO() {
		super();
	}

	public CustomerDTO(Integer customerId, String firstName, String lastName, String emailId, String phoneNumber,
			Set<Role> authorities, Set<Address> addresses, boolean isLocked) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.authorities = authorities;
		this.addresses = addresses;
		this.isLocked = isLocked;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Set<Role> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Role> authorities) {
		this.authorities = authorities;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	
}

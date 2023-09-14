package com.timeszone.model.dto;

public class LoginDTO {
	
	private String emailId;
	
	private String password;
	
	private String phoneNumber;
	
	private Integer otp;

	public LoginDTO() {
		super();
	}

	public LoginDTO(String emailId, String password,String phoneNumber,Integer otp) {
		super();
		this.emailId = emailId;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.otp = otp;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	@Override
	public String toString() {
		return "LoginDTO [emailId=" + emailId + ", password=" + password + ", phoneNumber=" + phoneNumber + ", otp="
				+ otp + "]";
	}
	
}

package com.timeszone.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Otp")
public class Otp {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="otpId")
	private Integer otp_id;
	
	@Column(name="phoneNumber",nullable=false,unique=true)
	private String phoneNumber;
	
	@Column(name="otp",nullable=true)
	private Integer otp;
	
	@Column(name="expirationTime",nullable=true)
	private LocalDateTime expirationTime;

	public Integer getOtp_id() {
		return otp_id;
	}

	public void setOtp_id(Integer otp_id) {
		this.otp_id = otp_id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public LocalDateTime getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(LocalDateTime expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Otp(String phoneNumber, Integer otp, LocalDateTime expirationTime) {
		super();
		this.phoneNumber = phoneNumber;
		this.otp = otp;
		this.expirationTime = expirationTime;
	}

	public Otp() {
		super();
	}
	
	
}

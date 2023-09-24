package com.timeszone.model.customer;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="Address")
public class Address {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="addressId",unique=true)
	private Integer addressId;
	
	@Column(name="addressLineOne",nullable=false)
	private String addressLineOne;
	
	@Column(name="addressLineTwo",nullable=false)
	private String addressLineTwo;
	
	@Column(name="city",nullable=false)
	private String city;
	
	@Column(name="state",nullable=false)
	private String state;
	
	@Column(name="pinCode",nullable=false)
	private Integer pinCode;
	
	@Column(name="isDefault",nullable=false)
	private boolean isDefault;
	
	@ManyToOne
    private Customer customer;
	
	public Address() {
		super();
	}

	public Address(Integer addressId, String addressLineOne, String addressLineTwo, String city, String state,
			Integer pinCode, boolean isDefault, Customer customer) {
		super();
		this.addressId = addressId;
		this.addressLineOne = addressLineOne;
		this.addressLineTwo = addressLineTwo;
		this.city = city;
		this.state = state;
		this.pinCode = pinCode;
		this.isDefault = isDefault;
		this.customer = customer;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getAddressLineOne() {
		return addressLineOne;
	}

	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}

	public String getAddressLineTwo() {
		return addressLineTwo;
	}

	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getPinCode() {
		return pinCode;
	}

	public void setPinCode(Integer pinCode) {
		this.pinCode = pinCode;
	}

	public boolean getIsDefault() {
		return isDefault();
	}
	
	public boolean isDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Address =" + addressId + ", addressLineOne=" + addressLineOne + ", addressLineTwo="
				+ addressLineTwo + ", city=" + city + ", state=" + state + ", pinCode=" + pinCode + ", isDefault="
				+ isDefault + ", customer=" + customer.getFirstName();
	}
}

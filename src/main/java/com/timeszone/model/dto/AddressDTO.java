package com.timeszone.model.dto;

public class AddressDTO {

	private Integer addressId;
	
	private String addressLineOne;
	
	private String addressLineTwo;
	
	private String city;
	
	private String state;
	
	private Integer pinCode;
	
	private boolean isDefault;
	
	private Integer customerId;
	
	private String contactName;
	
	private String contactNumber;

	public AddressDTO(String addressLineOne, String addressLineTwo, String city, String state, Integer pinCode,
			boolean isDefault, Integer customerId) {
		super();
		this.addressLineOne = addressLineOne;
		this.addressLineTwo = addressLineTwo;
		this.city = city;
		this.state = state;
		this.pinCode = pinCode;
		this.isDefault = isDefault;
		this.customerId = customerId;
	}

	
	
	public AddressDTO(String addressLineOne, String addressLineTwo, String city, String state, Integer pinCode,
			boolean isDefault, Integer customerId, String contactName, String contactNumber) {
		super();
		this.addressLineOne = addressLineOne;
		this.addressLineTwo = addressLineTwo;
		this.city = city;
		this.state = state;
		this.pinCode = pinCode;
		this.isDefault = isDefault;
		this.customerId = customerId;
		this.contactName = contactName;
		this.contactNumber = contactNumber;
	}



	public AddressDTO() {
		super();
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
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
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	
}

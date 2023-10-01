package com.timeszone.model.shared;

public class TransactionDetails {
	
	public String orderId;
	
	public String currency;
	
	public String amount;
	
	public String razorpaySecretId;
	
	public String razorpaySecretKey;

	public TransactionDetails(String orderId, String currency, String amount) {
		super();
		this.orderId = orderId;
		this.currency = currency;
		this.amount = amount;
	}

	public String getRazorpaySecretId() {
		return razorpaySecretId;
	}

	public void setRazorpaySecretId(String razorpaySecretId) {
		this.razorpaySecretId = razorpaySecretId;
	}

	public String getRazorpaySecretKey() {
		return razorpaySecretKey;
	}

	public void setRazorpaySecretKey(String razorpaySecretKey) {
		this.razorpaySecretKey = razorpaySecretKey;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
}

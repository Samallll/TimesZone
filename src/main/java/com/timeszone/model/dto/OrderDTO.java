package com.timeszone.model.dto;

import java.time.LocalDate;

public class OrderDTO {
	
	private Integer orderId;
	
	private LocalDate orderedDate;
	
	private Double orderAmount;
	
	private String paymentMethodName;
	
	private Integer orderedQuantity;
	
	private String orderStatus;
	
	private String[] orderStatusList = {"Shipped","Pending","Delivered","Cancelled","Refunded","Returned"};
	
	private Integer addressId;
	
	private Integer customerId;
	
	private Integer cartId;

	public OrderDTO() {
		super();
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public LocalDate getOrderedDate() {
		return orderedDate;
	}

	public void setOrderedDate(LocalDate orderedDate) {
		this.orderedDate = orderedDate;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}


	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}

	public Integer getOrderedQuantity() {
		return orderedQuantity;
	}

	public void setOrderedQuantity(Integer orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public String[] getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(String[] orderStatusList) {
		this.orderStatusList = orderStatusList;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	
}

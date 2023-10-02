package com.timeszone.model.dto;

import java.time.LocalDate;
import java.util.List;

import com.timeszone.model.shared.OrderItem;

public class OrderDTO {
	
	private Integer orderId;
	
	private LocalDate orderedDate;
	
	private Double orderAmount;
	
	private String paymentMethodName;
	
	private String orderStatus;
	
	private String[] orderStatusList = {"Shipped","Pending","Delivered","Cancelled","Refunded","Returned"};
	
	private Integer addressId;
	
	private Integer customerId;
	
	private List<OrderItem> orderItems;

	public OrderDTO() {
		super();
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


	public List<OrderItem> getOrderItems() {
		return orderItems;
	}


	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
}

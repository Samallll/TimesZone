package com.timeszone.model.shared;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.timeszone.model.customer.Address;
import com.timeszone.model.customer.Customer;

@Entity
public class PurchaseOrder {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer orderId;
	
	@OneToOne(mappedBy="order",cascade=CascadeType.ALL)
    private Address address;
	
	@OneToOne
    private Cart cart;
	
	@ManyToOne
    private Customer customer;
	
	@ManyToOne
    private PaymentMethod paymentMethod;
	
	private LocalDate orderedDate;
	
	private String orderStatus;
	
	private Integer orderedQuantity;
	
	private Double orderAmount;

	private String transcationId;
	
	public PurchaseOrder( Address address, Cart cart, Customer customer, PaymentMethod paymentMethod,
			LocalDate orderedDate, String orderStatus, Integer orderedQuantity, Double orderAmount) {
		super();
		this.address = address;
		this.cart = cart;
		this.customer = customer;
		this.paymentMethod = paymentMethod;
		this.orderedDate = orderedDate;
		this.orderStatus = orderStatus;
		this.orderedQuantity = orderedQuantity;
		this.orderAmount = orderAmount;
	}

	public PurchaseOrder(Address address, Cart cart, Customer customer, PaymentMethod paymentMethod, LocalDate orderedDate,
			String orderStatus, Integer orderedQuantity, Double orderAmount, String transcationId) {
		super();
		this.address = address;
		this.cart = cart;
		this.customer = customer;
		this.paymentMethod = paymentMethod;
		this.orderedDate = orderedDate;
		this.orderStatus = orderStatus;
		this.orderedQuantity = orderedQuantity;
		this.orderAmount = orderAmount;
		this.transcationId = transcationId;
	}

	public PurchaseOrder() {
		super();
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getTranscationId() {
		return transcationId;
	}

	public void setTranscationId(String transcationId) {
		this.transcationId = transcationId;
	}

	public LocalDate getOrderedDate() {
		return orderedDate;
	}

	public void setOrderedDate(LocalDate orderedDate) {
		this.orderedDate = orderedDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getOrderedQuantity() {
		return orderedQuantity;
	}

	public void setOrderedQuantity(Integer orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}
	
	
	
}

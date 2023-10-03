package com.timeszone.model.shared;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import com.timeszone.model.customer.Customer;

@Entity
public class Coupon {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer couponId;

	private String couponCode;
	
	private String description;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expiryDate;
	
	private Double percentage;
	
	private Double minimumPurchaseAmount;
	
	private boolean isActive=true;

	private Integer cartItemsCount;
	
	@OneToMany(mappedBy="coupon")
	private List<PurchaseOrder> ordersList;
	
	@ManyToMany(mappedBy = "coupons")
    private Set<Customer> customers = new HashSet<>();

	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public Double getMinimumPurchaseAmount() {
		return minimumPurchaseAmount;
	}

	public void setMinimumPurchaseAmount(Double minimumPurchaseAmount) {
		this.minimumPurchaseAmount = minimumPurchaseAmount;
	}
	
	public boolean getIsActive() {
		return this.isActive();
	}

	public boolean isActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getCartItemsCount() {
		return cartItemsCount;
	}

	public void setCartItemsCount(Integer cartItemsCount) {
		this.cartItemsCount = cartItemsCount;
	}

	public Coupon(String couponCode, String description, LocalDate expiryDate, Double percentage,
			Double minimumPurchaseAmount, boolean isActive, Integer cartItemsCount) {
		super();
		this.couponCode = couponCode;
		this.description = description;
		this.expiryDate = expiryDate;
		this.percentage = percentage;
		this.minimumPurchaseAmount = minimumPurchaseAmount;
		this.isActive = isActive;
		this.cartItemsCount = cartItemsCount;
	}

	public Coupon() {
		super();
	}

	public List<PurchaseOrder> getOrdersList() {
		return ordersList;
	}

	public void setOrdersList(List<PurchaseOrder> ordersList) {
		this.ordersList = ordersList;
	}
	
	
}

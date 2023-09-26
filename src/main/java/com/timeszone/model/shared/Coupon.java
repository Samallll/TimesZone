package com.timeszone.model.shared;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Coupon {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer couponId;

	private String couponCode;
	
	private String description;
	
	private LocalDateTime expiryDate;
	
	private Double percentage;
	
	private Double minimumPurchaseAmount;
	
	private boolean isActive;

	private Integer cartItemsCount;

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

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
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

	public boolean getIisActive() {
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

	public Coupon(String couponCode, String description, LocalDateTime expiryDate, Double percentage,
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
	
	
}

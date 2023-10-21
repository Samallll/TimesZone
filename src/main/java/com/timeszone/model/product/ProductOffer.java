package com.timeszone.model.product;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class ProductOffer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer productOfferId;
	
	private String productOfferCode;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expiryDate;
	
	private Double discountPercentage;
	
	private boolean isEnabled=true;
	
	private boolean isActive = false;
	
	@OneToMany(mappedBy = "productOffer",cascade={CascadeType.PERSIST, CascadeType.MERGE},fetch=FetchType.EAGER)
	private Set<Product> productList = new HashSet<>();

	public Integer getProductOfferId() {
		return productOfferId;
	}

	public void setProductOfferId(Integer productOfferId) {
		this.productOfferId = productOfferId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Set<Product> getProductList() {
		return productList;
	}

	public void setProductList(Set<Product> productList) {
		this.productList = productList;
	}

	public String getProductOfferCode() {
		return productOfferCode;
	}

	public void setProductOfferCode(String productOfferCode) {
		this.productOfferCode = productOfferCode;
	}

	public boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public ProductOffer() {
		super();
	}
}

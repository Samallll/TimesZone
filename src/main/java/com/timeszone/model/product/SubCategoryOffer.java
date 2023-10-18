package com.timeszone.model.product;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class SubCategoryOffer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer subCategoryOfferId;
	
	private String subCategoryOfferCode;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expiryDate;
	
	private boolean isEnabled=true;
	
	private boolean isActive=false;
	
	private Double discountPercentage;
	
	@OneToMany(mappedBy = "subCategoryOffer",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
	private Set<SubCategory> subCategories = new HashSet<>();

	public SubCategoryOffer() {
		super();
	}

	public Integer getSubCategoryOfferId() {
		return subCategoryOfferId;
	}

	public void setSubCategoryOfferId(Integer subCategoryOfferId) {
		this.subCategoryOfferId = subCategoryOfferId;
	}

	public String getSubCategoryOfferCode() {
		return subCategoryOfferCode;
	}

	public void setSubCategoryOfferCode(String subCategoryOfferCode) {
		this.subCategoryOfferCode = subCategoryOfferCode;
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

	public boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Set<SubCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Set<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}

}

package com.timeszone.model.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class OfferRequestDTO {
	
	private Integer offerid;
	
	private String offerCode;
	
	private Double percentage;
	
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expiryDate;
	
	private Boolean isEnabled=true;
	
	private Boolean isActive=false;
	
	private List<Integer> subItemListIds = new ArrayList<>();

	public OfferRequestDTO() {
		super();
	}

	public Integer getOfferid() {
		return offerid;
	}

	public void setOfferid(Integer offerid) {
		this.offerid = offerid;
	}

	public String getOfferCode() {
		return offerCode;
	}

	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public List<Integer> getSubItemListIds() {
		return subItemListIds;
	}

	public void setSubItemListIds(List<Integer> subItemListIds) {
		this.subItemListIds = subItemListIds;
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
	
	
}


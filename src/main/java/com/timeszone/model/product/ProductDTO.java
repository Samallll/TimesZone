package com.timeszone.model.product;

import java.time.LocalDate;


public class ProductDTO {		

	private Integer productId;

	private String productName;
	
	private String description;
	
	private Integer quantity;
	
	private Double caseSize;
	
	private Double price;
	
	private boolean isEnabled;
	
	private LocalDate dateAdded;

	public ProductDTO() {
		super();
	}

	public ProductDTO(Integer productId, String productName, String description, Integer quantity, Double caseSize,
			Double price, boolean isEnabled, LocalDate dateAdded) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.description = description;
		this.quantity = quantity;
		this.caseSize = caseSize;
		this.price = price;
		this.isEnabled = isEnabled;
		this.dateAdded = dateAdded;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getCaseSize() {
		return caseSize;
	}

	public void setCaseSize(Double caseSize) {
		this.caseSize = caseSize;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public boolean getIsEnabled() {
		return isEnabled();
	}

	private boolean isEnabled() {
		// TODO Auto-generated method stub
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public LocalDate getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded = dateAdded;
	}

}

package com.timeszone.model.product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="Product")
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="productId",unique=true)
	private Integer productId;
	
	@Column(name="productName",nullable=false)
	private String productName;
	
	@Column(name="description",nullable=false)
	private String description;
	
	@Column(name="quantity",nullable=false)
	private Integer quantity;
	
	@Column(name="caseSize",nullable=false)
	private Double caseSize;
	
	@Column(name="price",nullable=false)
	private Double price;
	
	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<ProductImage> productImages = new ArrayList<>();
	
	@Column(name="isEnabled",nullable=false)
	private boolean isEnabled=true;
	
	@Column(name="dateAdded",nullable=false)
	private LocalDate dateAdded;

	public Product() {
		super();
	}

	public Product(String productName, String description, Integer quantity, Double caseSize,
			Double price, LocalDate dateAdded, List<Category> categories,List<ProductImage> productImages) {
		super();
		this.productName = productName;
		this.description = description;
		this.quantity = quantity;
		this.caseSize = caseSize;
		this.price = price;
		this.dateAdded = dateAdded;
		this.categories = categories;
		this.productImages = productImages;
	}

	public Product(String productName, String description, Integer quantity, Double caseSize,
			Double price, LocalDate dateAdded) {
		// TODO Auto-generated constructor stub
		this.productName = productName;
		this.description = description;
		this.quantity = quantity;
		this.caseSize = caseSize;
		this.price = price;
		this.dateAdded = dateAdded;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public Double getCaseSize() {
		return caseSize;
	}

	public void setCaseSize(Double caseSize) {
		this.caseSize = caseSize;
	}

	public LocalDate getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDate dateAdded) {
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

	public Double getPrice() {
		return price;
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

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public void allCategories() {
		// TODO Auto-generated method stub
		System.out.println("Categories=");
		for(Category category:categories) {
			category.toString();
		}
	}
}

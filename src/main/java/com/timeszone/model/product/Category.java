package com.timeszone.model.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Category")
public class Category {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
	
	@Column(unique=true)
    private String categoryName;
    
    private String description;
    
    private Integer noOfSubCategories;
    
    @Column(name="isEnabled",nullable=false)
	private boolean isEnabled=true;
    
    @ManyToOne
    private Product product;
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<SubCategory> subcategories = new ArrayList<>();

	public Category(String categoryName, Product product, List<SubCategory> subcategories,String description) {
		super();
		this.categoryName = categoryName;
		this.product = product;
		this.subcategories = subcategories;
		this.description = description;
		noOfSubCategories=subcategories.size();
	}

	public Integer getNoOfSubCategories() {
		return noOfSubCategories;
	}

	public void setNoOfSubCategories(Integer noOfSubCategories) {
		this.noOfSubCategories = noOfSubCategories;
	}

	public Category() {
		super();
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<SubCategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<SubCategory> subcategories) {
		this.subcategories = subcategories;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean getIsEnabled() {
		return isEnabled();
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
}

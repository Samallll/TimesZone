package com.timeszone.model.product;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="categories")
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
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<SubCategory> subcategories;

    @ManyToMany
    private Set<Product> products;

	public Category() {
		super();
	}

	public Category(String categoryName, String description, Integer noOfSubCategories, boolean isEnabled,
			Set<SubCategory> subcategories, Set<Product> products) {
		super();
		this.categoryName = categoryName;
		this.description = description;
		this.noOfSubCategories = noOfSubCategories;
		this.isEnabled = isEnabled;
		this.subcategories = subcategories;
		this.products = products;
	}

	public Category(String categoryName, String description, Integer noOfSubCategories,
			Set<SubCategory> subcategories) {
		super();
		this.categoryName = categoryName;
		this.description = description;
		this.noOfSubCategories = noOfSubCategories;
		this.subcategories = subcategories;
	}

	public Category(String categoryName, String description, Integer noOfSubCategories) {
		super();
		this.categoryName = categoryName;
		this.description = description;
		this.noOfSubCategories = noOfSubCategories;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNoOfSubCategories() {
		return noOfSubCategories;
	}

	public void setNoOfSubCategories(Integer noOfSubCategories) {
		this.noOfSubCategories = noOfSubCategories;
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

	public Set<SubCategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(Set<SubCategory> subcategories) {
		this.subcategories = subcategories;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	
}

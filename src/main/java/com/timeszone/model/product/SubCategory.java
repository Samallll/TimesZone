package com.timeszone.model.product;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="subcategories")
public class SubCategory {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subCategoryId;
	
	@Column(unique=true)
    private String subCategoryName;
    
    @Column(name="isEnabled",nullable=false)
	private boolean isEnabled=true;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(mappedBy = "subcategories", cascade = CascadeType.ALL)
    private Set<Product> products;

	public SubCategory() {
		super();
	}

	public SubCategory(String subCategoryName, Category category, Set<Product> products) {
		super();
		this.subCategoryName = subCategoryName;
		this.category = category;
		this.products = products;
	}

	public SubCategory(String subCategoryName, Category category) {
		super();
		this.subCategoryName = subCategoryName;
		this.category = category;
	}

	public SubCategory(String subCategoryName) {
		super();
		this.subCategoryName = subCategoryName;
	}

	public Integer getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Integer subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	
   
}

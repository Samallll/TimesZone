package com.timeszone.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.timeszone.model.product.SubCategory;

public class CategoryRegistrationDTO {
	
    private Integer categoryId;	

    private String categoryName;
    
    private String description;
    
    private Integer noOfSubCategories;
    
    List<SubCategory> subcategories = new ArrayList<>();
    
	public CategoryRegistrationDTO() {
		super();
	}

	public CategoryRegistrationDTO(Integer categoryId, String categoryName, String description,
			Integer noOfSubCategories) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.description = description;
		this.noOfSubCategories = noOfSubCategories;
	}

	public CategoryRegistrationDTO(String categoryName, String description, Integer noOfSubCategories) {
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

	public List<SubCategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<SubCategory> subcategories) {
		this.subcategories = subcategories;
	}

    
}

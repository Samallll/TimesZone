package com.timeszone.model.dto;

public class CategoryRegistrationDTO {
	
    private Integer cateogryId;	

    private String categoryName;
    
    private String description;
    
    private Integer noOfSubCategories;
    
	public CategoryRegistrationDTO() {
		super();
	}

	public CategoryRegistrationDTO(Integer cateogryId, String categoryName, String description,
			Integer noOfSubCategories) {
		super();
		this.cateogryId = cateogryId;
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

	public Integer getCateogryId() {
		return cateogryId;
	}

	public void setCateogryId(Integer cateogryId) {
		this.cateogryId = cateogryId;
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

    
}

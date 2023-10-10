package com.timeszone.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.dto.CategoryRegistrationDTO;
import com.timeszone.model.product.Category;
import com.timeszone.model.product.Product;
import com.timeszone.model.product.SubCategory;
import com.timeszone.repository.CategoryRepository;
import com.timeszone.repository.SubCategoryRepository;

@Service
public class SubCategoryService {
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	List<SubCategory> getAllSubCategories(){
		
		return subCategoryRepository.findAll();
	}
	
	public void addSubCategory(CategoryRegistrationDTO cd) {
		
		Category existingCategory = categoryRepository.findById(cd.getCategoryId()).get();
        List<SubCategory> customerSubCategories = cd.getSubcategories();        
        List<SubCategory> existingSubCategories = new ArrayList<>(existingCategory.getSubcategories());
        
        Set<SubCategory> newlyAdded = new HashSet<>(categoryService.newSubCategories( existingSubCategories,customerSubCategories));
        for (SubCategory subcategory : newlyAdded) {
        	  existingCategory.getSubcategories().add(subcategory);
        	  subcategory.setCategory(existingCategory);
	      	  subCategoryRepository.save(subcategory);
        }
        
        categoryRepository.save(existingCategory);
	}
	
	public List<Product> getAllProductsBySubCategory(String subCategoryName){
		
		return subCategoryRepository.findAllBySubCategoryName(subCategoryName);
	}
	
	public SubCategory getSubCategoryBySubCategoryName(String subCategoryName) {
		
		return subCategoryRepository.findBySubCategoryName(subCategoryName);
	}
}

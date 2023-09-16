package com.timeszone.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.product.Category;
import com.timeszone.model.product.SubCategory;
import com.timeszone.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> getAllCategories() {
		
		return categoryRepository.findAll();
	}

	public void deleteCategory(Integer id) {
		
		categoryRepository.deleteById(id);
	}

	public void updateCategory(Integer modifyCategoryId, Category ec) {
		
		Category modifyCategory = categoryRepository.findById(modifyCategoryId).get();
		System.out.println(ec.getIsEnabled());
		modifyCategory.setCategoryName(ec.getCategoryName());
		modifyCategory.setDescription(ec.getDescription());
		modifyCategory.setNoOfSubCategories(ec.getNoOfSubCategories());
		modifyCategory.setEnabled(ec.getIsEnabled());
		System.out.println( categoryRepository.count());
		categoryRepository.save(modifyCategory);
	}
	
	
	public List<SubCategory> newSubCategories(List<SubCategory> existingSubCategories, List<SubCategory> customerSubCategories) {
	    Set<String> subCategoryNames = new HashSet<>();
	    List<SubCategory> difference = new ArrayList<>();
	    
	    for (SubCategory subCategory1 : customerSubCategories) {
	        boolean isUnique = true;
	        
	        for (SubCategory subCategory2 : existingSubCategories) {
	            if (subCategory1.getSubCategoryName().equalsIgnoreCase(subCategory2.getSubCategoryName())) {
	                isUnique = false;
	                break;
	            }
	        }
	        
	        if (isUnique && !subCategoryNames.contains(subCategory1.getSubCategoryName())) {
	            difference.add(subCategory1);
	            subCategoryNames.add(subCategory1.getSubCategoryName());
	        }
	    }
	    
	    return difference;
	}

	public void changeStatus(Integer id) {
		

		Category changeCategoryStatus = categoryRepository.findById(id).get();
		System.out.println("Before :" + changeCategoryStatus.getIsEnabled());
		if(changeCategoryStatus.getIsEnabled()) {
			changeCategoryStatus.setEnabled(false);
		}
		else {
			changeCategoryStatus.setEnabled(true);
		}
		categoryRepository.save(changeCategoryStatus);
	}

	
}

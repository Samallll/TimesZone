package com.timeszone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.product.Category;
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
	
	
}

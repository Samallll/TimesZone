package com.timeszone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.product.Product;
import com.timeszone.model.product.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory,Integer>{
	
	SubCategory findBySubCategoryName(String subCategoryName);
	List<Product> findAllBySubCategoryName(String subCategoryName);
	
}

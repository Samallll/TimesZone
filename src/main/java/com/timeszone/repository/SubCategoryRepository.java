package com.timeszone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.product.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory,Integer>{
	
	SubCategory findBySubCategoryName(String subCategoryName);
}

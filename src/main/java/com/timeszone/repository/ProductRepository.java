package com.timeszone.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.product.Product;
import com.timeszone.model.product.SubCategory;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
	
	Page<Product> findAllByProductNameContainingIgnoreCase(String name, Pageable pageable);
	Page<Product> findAllByProductNameContainingIgnoreCaseAndSubcategories_SubCategoryName(
	        String search, String subCategoryName, Pageable pageable
	    );
	Page<Product> findAllBySubcategories_SubCategoryName(String subCategoryName, Pageable pageable);
	Page<Product> findAll(Specification<Product> finalSpecification, Pageable pageable);
	List<Product> findAllByProductNameContainingIgnoreCase(String productName);
	Product findByProductName(String productName);
}

package com.timeszone.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
	
	Page<Product> findAllByProductNameContainingIgnoreCase(String name, Pageable pageable);
}

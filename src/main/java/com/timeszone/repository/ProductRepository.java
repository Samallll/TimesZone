package com.timeszone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

}

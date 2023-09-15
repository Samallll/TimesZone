package com.timeszone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.product.Category;
import com.timeszone.model.product.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Integer>{
	
	Category findByImageId(Integer iamgeId);
	Category findByImageName(String imageName);

}

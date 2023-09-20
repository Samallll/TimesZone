package com.timeszone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.product.Category;
import com.timeszone.model.product.Product;
import com.timeszone.model.product.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Integer>{
	
	ProductImage findByImageId(Integer iamgeId);
	ProductImage findByImageName(String imageName);
	ProductImage findByImage(byte[] image);
	ProductImage findByProduct(Product product);
}

package com.timeszone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.product.ProductImage;
import com.timeszone.repository.ProductImageRepository;

@Service
public class ProductImageService {
	
	@Autowired
	private ProductImageRepository productImageRepository;
	
	List<ProductImage> getAllProductImages(){
		
		return productImageRepository.findAll();
	}

}

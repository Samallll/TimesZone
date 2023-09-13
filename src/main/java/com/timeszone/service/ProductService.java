package com.timeszone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.product.Product;
import com.timeszone.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> getAllProducts(){
		
		return productRepository.findAll();
	}

	public void updateProduct(Integer id, String productName, Double caseSize, String description, boolean isEnabled,
			Double price, Integer quantity) {
		
		Product editProduct = productRepository.findById(id).get();
		editProduct.setCaseSize(caseSize);
		editProduct.setDescription(description);
		editProduct.setEnabled(isEnabled);
		editProduct.setPrice(price);
		editProduct.setQuantity(quantity);
		editProduct.setProductName(productName);
		productRepository.save(editProduct);
		
	}

	public void deleteProduct(Integer id) {
		
		productRepository.deleteById(id);
	}
}

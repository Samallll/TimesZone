package com.timeszone.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.product.Product;
import com.timeszone.model.product.ProductOffer;
import com.timeszone.repository.ProductOfferRepository;
import com.timeszone.repository.ProductRepository;

@Service
public class ProductOfferService {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductOfferRepository productOfferRepository;

	public boolean isValidOfferData(String offerCode, LocalDate offerStartDate, LocalDate offerExpiryDate, Double percentage,
			List<String> productNames) {
		if(offerCode == null || offerStartDate == null || offerExpiryDate == null || percentage == null || productNames == null) {
			return false;
		}
		if(offerStartDate.isBefore(offerExpiryDate) || offerStartDate.isBefore(LocalDate.now())) {
			return false;
		}
		return true;
	}
	
	public boolean isValidOfferCode(String offerCode) {
		return productOfferRepository.findByProductOfferCode(offerCode)==null?true:false;
	}

	public void createOffer(String offerCode, LocalDate offerStartDate, LocalDate offerExpiryDate, double percentage,
			List<String> productNames) {

		ProductOffer newOffer = new ProductOffer();
		newOffer.setProductOfferCode(offerCode);
		newOffer.setDiscountPercentage(percentage);
		newOffer.setExpiryDate(offerExpiryDate);
		newOffer.setStartDate(offerStartDate);
		Set<Product> productList = new HashSet<>();
		newOffer = productOfferRepository.save(newOffer);
		for (String productName : productNames) {
		    Product product = productService.getProductByName(productName);
		    product.setProductOffer(newOffer); 
		    productList.add(product);
		}
		newOffer.setProductList(productList);
		productOfferRepository.save(newOffer);
	}
	
	public List<ProductOffer> getAll(){
		return productOfferRepository.findAll();
	}
	
	public ProductOffer getProductOfferById(Integer offerId) {
		return productOfferRepository.findById(offerId).get();
	}

	public void modifyProductOffer(ProductOffer productOffer) {
		
		productOfferRepository.save(productOffer);
	}
	
	public List<ProductOffer> getAllByProductOfferEnabled(){
		return productOfferRepository.findAllByIsEnabledTrue();
	}
}

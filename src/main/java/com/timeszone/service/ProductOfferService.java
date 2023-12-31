package com.timeszone.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.product.Product;
import com.timeszone.model.product.ProductOffer;
import com.timeszone.repository.ProductOfferRepository;

@Service
public class ProductOfferService {
	
	@Autowired
	private ProductService productService;
	
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


	public List<ProductOffer> getAllOffersToApply() {
		
		return productOfferRepository.findByIsEnabledTrueAndStartDateEquals(LocalDate.now());
	}
	
	public List<Product> getProductsFromProductOffer(List<ProductOffer> productOfferList) {
		return productOfferList
		        .stream()
		        .flatMap(productOffer -> productOffer.getProductList().stream())
		        .collect(Collectors.toList());
	}


	public void applyOffers(List<ProductOffer> productOfferList) {
		
		for(ProductOffer productOffer:productOfferList) {
			productOffer.setIsActive(true);
			productOffer.getProductList().forEach(product -> {
				product.setProductOffer(productOffer);
				product.setDiscountedPrice(calculateDiscountedPrice(product,productOffer));
				productService.saveToTable(product);
			});
			productOfferRepository.save(productOffer);
		}
	}


	private Double calculateDiscountedPrice(Product product, ProductOffer productOffer) {
		Double price = (product.getPrice()*productOffer.getDiscountPercentage())/100;
		price = product.getPrice()-price;
		return price;
	}


	public List<ProductOffer> getAllActiveOffers() {

		return productOfferRepository.findAllByIsActiveTrue();
	}


	public ProductOffer saveToTable(ProductOffer productOffer) {
		return productOfferRepository.save(productOffer);
		
	}

	public List<ProductOffer> getAllByIsEnabled() {

		return productOfferRepository.findAllByIsEnabledTrue();
	}


	public List<ProductOffer> getAllOffersToRemove() {
		
		return productOfferRepository.findByIsEnabledTrueAndIsActiveTrueAndExpiryDateEquals(LocalDate.now());
	}


	public void removeAppliedOffers(List<ProductOffer> productOfferListForRemove) {
		
		for(ProductOffer offer:productOfferListForRemove) {
			offer.setIsActive(false);
			offer.setIsEnabled(false);
			offer.getProductList().forEach(product -> {
		        product.setProductOffer(null);
		        product.setDiscountedPrice(0.0);
		        productService.saveToTable(product);
		    });
			offer.getProductList().clear();
			productOfferRepository.save(offer);
		}
	}
}

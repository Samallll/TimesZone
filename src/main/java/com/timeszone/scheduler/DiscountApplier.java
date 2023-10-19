package com.timeszone.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.timeszone.model.product.Product;
import com.timeszone.model.product.ProductOffer;
import com.timeszone.model.product.SubCategory;
import com.timeszone.model.product.SubCategoryOffer;
import com.timeszone.service.ProductOfferService;
import com.timeszone.service.ProductService;
import com.timeszone.service.SubCategoryOfferService;

@Component
public class DiscountApplier {
	
	@Autowired
	private ProductOfferService productOfferService;
	
	@Autowired
	private SubCategoryOfferService categoryOfferService;
	
	@Autowired
	private ProductService productService;
	
	@Scheduled(cron = "0/2 0 0 * * ?", zone = "Asia/Kolkata")
	public void offerApplyAndRemovalChecking() {
		applyOffers();
		
		//Check for removing discount ------------------------------
		
		//productoffer && sub category offer = list of offers which are not enabled and the expiry date is today.
//		List<ProductOffer> productOfferListForRemove = productOfferService.getAllOffersToRemove();
//		List<SubCategoryOffer> subCategoryOfferListForRemove = categoryOfferService.getAllOffersToRemove();
		//check for opposite and other categories' offer's availability on the particular product and apply it.
//		List<SubCategoryOffer> appliedCategoryOffers = categoryOfferService.getAllAppliedOffers();
//		List<ProductOffer> appliedProductOffers = productOfferService.getAllAppliedOffers();
		//remove the discount
	}

	/**
	 * 
	 */
	public void applyOffers() {
		
		List<ProductOffer> productOfferList = productOfferService.getAllOffersToApply();
		List<SubCategoryOffer> subCategoryOfferList = categoryOfferService.getAllOffersToApply();
		List<ProductOffer> newProductOfferList = filterProductOffers(productOfferList,subCategoryOfferList);
		
		if(!newProductOfferList.isEmpty() && !subCategoryOfferList.isEmpty()) {
			productOfferService.applyOffers(newProductOfferList);
			categoryOfferService.applyOffers(subCategoryOfferList);
			return;
		}
		if(!newProductOfferList.isEmpty()) {
			productOfferService.applyOffers(productOfferList);
			return;
		}
		
		if(!subCategoryOfferList.isEmpty()) {
			List<ProductOffer> alreadyActiveProductOffers = productOfferService.getAllActiveOffers();
			removeCommonProductOffers(subCategoryOfferList,alreadyActiveProductOffers);
			categoryOfferService.applyOffers(subCategoryOfferList);
		}
	}

	private void removeCommonProductOffers(List<SubCategoryOffer> subCategoryOfferList,
			List<ProductOffer> alreadyActiveProductOffers) {
		
		List<Product> categoryOfferProducts = categoryOfferService.getProductsFromSubCategoryOffer(subCategoryOfferList);
		List<Product> productOfferProducts = productOfferService.getProductsFromProductOffer(alreadyActiveProductOffers);
		for(Product product:productOfferProducts) {
			if(categoryOfferProducts.contains(product)) {
				ProductOffer productOffer = productOfferService.getProductOfferById(product.getProductOffer().getProductOfferId());
				productOffer.getProductList().remove(product);
				productService.saveToTable(product);
				productOffer.setIsEnabled(false);
				productOffer.setIsActive(false);
				productOfferService.saveToTable(productOffer);
			}
		}
	}

	private List<ProductOffer> filterProductOffers(List<ProductOffer> productOfferList,
			List<SubCategoryOffer> subCategoryOfferList) {
		
		List<Product> categoryProducts = categoryOfferService.getProductsFromSubCategoryOffer(subCategoryOfferList);
		List<SubCategoryOffer> alreadyActiveCategoryOffers = categoryOfferService.getAllActiveOffers();
		categoryProducts.addAll(categoryOfferService.getProductsFromSubCategoryOffer(alreadyActiveCategoryOffers));
		productOfferList.removeIf(offer -> offer.getProductList()
												.stream()
												.anyMatch(categoryProducts::contains));
		
		return productOfferList;
	}

	

}

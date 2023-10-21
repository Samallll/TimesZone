package com.timeszone.scheduler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.timeszone.model.product.Product;
import com.timeszone.model.product.ProductOffer;
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
	
	@Scheduled(cron = "2 0 0 * * ?", zone = "Asia/Kolkata")
	public void offerApplyAndRemovalChecking() {
		applyOffers();
		removeAppliedOffers();
	}


	public void removeAppliedOffers() {
		
		List<ProductOffer> productOfferListForRemove = productOfferService.getAllOffersToRemove();
		List<SubCategoryOffer> subCategoryOfferListForRemove = categoryOfferService.getAllOffersToRemove();
		
		if(!productOfferListForRemove.isEmpty() && !subCategoryOfferListForRemove.isEmpty()) {
			productOfferService.removeAppliedOffers(productOfferListForRemove);
			categoryOfferService.removeAppliedOffers(subCategoryOfferListForRemove);
			return;
		}
		if(!productOfferListForRemove.isEmpty()) {
			productOfferService.removeAppliedOffers(productOfferListForRemove);
			return;
		}
		
		if(!subCategoryOfferListForRemove.isEmpty()) {
			categoryOfferService.removeAppliedOffers(subCategoryOfferListForRemove);
		}
	}


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
		
		 /*
		 * finding the common/already applied productoffers. disabling and removing the
		 * product offer references since category offers have higher priority.
		 */
		
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
		
		/*
		 * Removing the productOffers from the applying list if the products are found
		 * in the subcategory offer's product list. also checking with the already
		 * active category offer's product list and other product offer' list.
		 */
		
		List<Product> categoryProducts = categoryOfferService.getProductsFromSubCategoryOffer(subCategoryOfferList);
		
		List<SubCategoryOffer> alreadyActiveCategoryOffers = categoryOfferService.getAllActiveOffers();
		categoryProducts.addAll(categoryOfferService.getProductsFromSubCategoryOffer(alreadyActiveCategoryOffers));
		
		List<Product> allActiveProducts = productOfferService.getAllActiveOffers()
			    .stream()
			    .flatMap(productOffer -> productOffer.getProductList().stream())
			    .filter(product -> !categoryProducts.contains(product))
			    .collect(Collectors.toList());
		categoryProducts.addAll(allActiveProducts);
		
		productOfferList.removeIf(offer -> offer.getProductList()
												.stream()
												.anyMatch(categoryProducts::contains));
		return productOfferList;
	}

	

}

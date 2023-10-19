package com.timeszone.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.dto.OfferRequestDTO;
import com.timeszone.model.product.Product;
import com.timeszone.model.product.SubCategory;
import com.timeszone.model.product.SubCategoryOffer;
import com.timeszone.repository.SubCategoryOfferRepository;
import com.timeszone.repository.SubCategoryRepository;

@Service
public class SubCategoryOfferService {
	
	@Autowired
	private SubCategoryOfferRepository offerRepository;
	
	@Autowired
	private SubCategoryService subCategoryService;
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	@Autowired
	private ProductService productService;
	
	public List<SubCategoryOffer> getAllByIsEnabled(){
		return offerRepository.findAllByIsEnabledTrue();
	}

	public void createOffer(OfferRequestDTO offerRequest) {
		
		SubCategoryOffer newOffer = new SubCategoryOffer();
		newOffer = convertToSubCategoryOffer(offerRequest, newOffer);
		offerRepository.save(newOffer);
	}

	public SubCategoryOffer getById(Integer subCategoryOfferId) {
		return offerRepository.findById(subCategoryOfferId).get();
	}

	public void updateOffer(OfferRequestDTO offerRequest) {
		
		SubCategoryOffer newOffer = offerRepository.findById(offerRequest.getOfferid()).get();
		for(SubCategory subCategory:newOffer.getSubCategories()) {
			subCategory.setSubCategoryOffer(null);
			subCategoryService.saveToTable(subCategory);
		}
		newOffer.getSubCategories().clear();
		newOffer = offerRepository.save(newOffer);
		newOffer = convertToSubCategoryOffer(offerRequest, newOffer);
		offerRepository.save(newOffer);
	}

	private SubCategoryOffer convertToSubCategoryOffer(OfferRequestDTO offerRequest, SubCategoryOffer newOffer) {
		
		newOffer.setDiscountPercentage(offerRequest.getPercentage());
		newOffer.setIsActive(offerRequest.getIsActive());
		newOffer.setIsEnabled(offerRequest.getIsEnabled());
		newOffer.setExpiryDate(offerRequest.getExpiryDate());
		newOffer.setStartDate(offerRequest.getStartDate());
		newOffer.setSubCategoryOfferCode(offerRequest.getOfferCode());
		newOffer = offerRepository.save(newOffer);
		Set<SubCategory> subCategories = new HashSet<>();
		for(Integer subCategoryId: offerRequest.getSubItemListIds()) {
			SubCategory subCategory = subCategoryRepository.findById(subCategoryId).get();
			subCategory.setSubCategoryOffer(newOffer);
			subCategories.add(subCategory);
		}
		newOffer.setSubCategories(subCategories);
		return newOffer;
	}

	public SubCategoryOffer saveToTable(SubCategoryOffer offer) {
		return offerRepository.save(offer);
	}

	public List<SubCategoryOffer> getAllOffersToApply() {
		return offerRepository.findByIsEnabledTrueAndStartDateEquals(LocalDate.now().plusDays(1));
	}
	
	public List<Product> getProductsFromSubCategoryOffer(List<SubCategoryOffer> subCategoryOfferList) {
		return subCategoryOfferList.stream()
		        .flatMap(categoryOffer -> categoryOffer.getSubCategories().stream())
		        .flatMap(subCategory -> subCategory.getProducts().stream())
		        .collect(Collectors.toList());
	}

	public void applyOffers(List<SubCategoryOffer> subCategoryOfferList) {

		for(SubCategoryOffer offer:subCategoryOfferList) {
			offer.setIsActive(true);
			offer.getSubCategories().forEach(subCategory -> {
				subCategory.setSubCategoryOffer(offer);
				for(Product product:subCategory.getProducts()) {
					product.setDiscountedPrice(calculateDiscountedPrice(product,offer));
					productService.saveToTable(product);
				}
				subCategoryService.saveToTable(subCategory);
			});
			offerRepository.save(offer);
		}
	}

	private Double calculateDiscountedPrice(Product product, SubCategoryOffer offer) {
		Double price = (product.getPrice()*offer.getDiscountPercentage())/100;
		price = product.getPrice() - price;
		return price;
	}

	public List<SubCategoryOffer> getAllActiveOffers() {

		return offerRepository.findAllByIsActiveTrue();
	}

	public List<SubCategoryOffer> getAllOffersToRemove() {

		return offerRepository.findByIsEnabledTrueAndIsActiveTrueAndExpiryDateEquals(LocalDate.now().plusDays(2));
	}

	public void removeAppliedOffers(List<SubCategoryOffer> subCategoryOfferListForRemove) {

		for(SubCategoryOffer offer:subCategoryOfferListForRemove) {
			offer.setIsActive(false);
			offer.setIsEnabled(false);
			offer.getSubCategories().forEach(subCategory -> {
				subCategory.setSubCategoryOffer(null);
				for(Product product:subCategory.getProducts()) {
					product.setDiscountedPrice(0.0);
					productService.saveToTable(product);
				}
				subCategoryService.saveToTable(subCategory);
			});
			offer.getSubCategories().clear();
			offerRepository.save(offer);
		}
	}
	
}

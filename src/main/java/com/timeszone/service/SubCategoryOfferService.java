package com.timeszone.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.dto.OfferRequestDTO;
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
		newOffer = convertToSubCategoryOffer(offerRequest, newOffer);
		offerRepository.save(newOffer);
	}

	private SubCategoryOffer convertToSubCategoryOffer(OfferRequestDTO offerRequest, SubCategoryOffer newOffer) {
		
		newOffer.setDiscountPercentage(offerRequest.getPercentage());
		newOffer.setActive(offerRequest.getIsActive());
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

}

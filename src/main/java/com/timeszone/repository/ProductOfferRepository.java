package com.timeszone.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.product.ProductOffer;

@Repository
public interface ProductOfferRepository extends JpaRepository<ProductOffer,Integer>{
	
	ProductOffer findByProductOfferCode(String offerCode);
	List<ProductOffer> findAllByIsEnabledTrue();
	List<ProductOffer> findByIsEnabledTrueAndStartDateEquals(LocalDate date);
	List<ProductOffer> findAllByIsActiveTrue();
	List<ProductOffer> findByIsEnabledTrueAndIsActiveTrueAndExpiryDateEquals(LocalDate date);
}

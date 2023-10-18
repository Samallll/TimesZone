package com.timeszone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.product.SubCategoryOffer;

@Repository
public interface SubCategoryOfferRepository extends JpaRepository<SubCategoryOffer,Integer> {

	List<SubCategoryOffer> findAllByIsEnabledTrue();
	
}

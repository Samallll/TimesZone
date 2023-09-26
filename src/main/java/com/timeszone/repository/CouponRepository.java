package com.timeszone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.shared.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Integer>{
	
	Coupon findByCouponCode(String couponCode);
	List<Coupon> findAllByExpiryDate(String expiryDate);
	List<Coupon> findAllBycartItemsCount(Integer cartItemsCount);
	List<Coupon >findAllByIsActive(Boolean isActive);

}

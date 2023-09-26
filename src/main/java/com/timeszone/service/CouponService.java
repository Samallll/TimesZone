package com.timeszone.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.shared.Coupon;
import com.timeszone.repository.CouponRepository;

@Service
public class CouponService {
	
	@Autowired
	private CouponRepository couponRepository;
	
	public Coupon addCoupon(Coupon coupon) {
		
		couponRepository.save(coupon);
		return coupon;
	}
	
	public Coupon updateCoupon(Coupon coupon) {
		
//		write logic for editing the coupon
		couponRepository.save(coupon);
		return coupon;
	}
	
	public void deleteCoupon(Coupon coupon) {
		
		couponRepository.delete(coupon);
	}
	
	public void enableCoupon(Coupon coupon) {
		

	}
	
	public void disableCoupon(Coupon coupon) {
		

	}
	
	public List<Coupon> getAll(){
		
		return couponRepository.findAll();
	}
	
	public List<Coupon> getAllNonExpired(){
		
		LocalDateTime expirationTime;
    	LocalDateTime currentTime = LocalDateTime.now();
		List<Coupon> nonExpiredCoupons = new ArrayList<>();
		for(Coupon c:couponRepository.findAll()) {
			expirationTime = c.getExpiryDate();
			if(currentTime.isBefore(expirationTime)) {
				nonExpiredCoupons.add(c);
			}
		}
		return nonExpiredCoupons;
	}
	
	public List<Coupon> getAllIsActive(){
		
		List<Coupon> activeCoupons = new ArrayList<>();
		for(Coupon c:couponRepository.findAll()) {
			if(c.getIisActive()) {
				activeCoupons.add(c);
			}
		}
		return activeCoupons;
	}
	
	public List<Coupon> getAllBelowCartItemsCount(Integer count){
		
		List<Coupon> basedOnCount = new ArrayList<>();
		for(Coupon c:couponRepository.findAll()) {
			if(c.getCartItemsCount()<=count) {
				basedOnCount.add(c);
			}
		}
		return basedOnCount;
	}
	
	
	public List<Coupon> getAllBelowMinimumPurchaseAmount(Double amount){
		
		List<Coupon> basedOnAmount = new ArrayList<>();
		for(Coupon c:couponRepository.findAll()) {
			if(c.getMinimumPurchaseAmount()<=amount) {
				basedOnAmount.add(c);
			}
		}
		return basedOnAmount;
	}
}

package com.timeszone.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.shared.Coupon;
import com.timeszone.model.shared.PurchaseOrder;
import com.timeszone.repository.CouponRepository;

@Service
public class CouponService {
	
	@Autowired
	private CouponRepository couponRepository;
	
	public Coupon getCoupon(Integer id) {
		return couponRepository.findById(id).get();
	}
	
	public Coupon addCoupon(Coupon coupon) {
		
		couponRepository.save(coupon);
		return coupon;
	}
	
	public Coupon updateCoupon(Coupon coupon) {
		
//		write logic for editing the coupon
		Coupon editCoupon = couponRepository.findById(coupon.getCouponId()).get();
		editCoupon.setCartItemsCount(coupon.getCartItemsCount());
		editCoupon.setCouponCode(coupon.getCouponCode());
		editCoupon.setDescription(coupon.getDescription());
		editCoupon.setExpiryDate(coupon.getExpiryDate());
		editCoupon.setIsActive(coupon.getIsActive());
		editCoupon.setUsageCount(coupon.getUsageCount());
		editCoupon.setMinimumPurchaseAmount(coupon.getMinimumPurchaseAmount());
		editCoupon.setPercentage(coupon.getPercentage());
		couponRepository.save(editCoupon);
		return coupon;
	}
	
	public void deleteCoupon(Integer couponId) {
		
		couponRepository.deleteById(couponId);
	}
	
	public List<Coupon> getAll(){
		return couponRepository.findAll();
	}
	
	public List<Coupon> getAllNonExpired(){
		LocalDate expirationTime;
    	LocalDate currentTime = LocalDate.now();
		List<Coupon> nonExpiredCoupons = new ArrayList<>();
		for(Coupon c:couponRepository.findAll()) {
			expirationTime = c.getExpiryDate();
			if(currentTime.isBefore(expirationTime)) {
				nonExpiredCoupons.add(c);
			}
		}
		return nonExpiredCoupons;
	}
	
	public List<Coupon> getCouponsMatchingCriteria(double purchaseAmount) {
        LocalDate currentDate = LocalDate.now();
        return couponRepository.findAllByExpiryDateAfterAndUsageCountGreaterThanAndMinimumPurchaseAmountLessThanEqualAndIsActive(
            currentDate, 0, purchaseAmount,true
        );
	}
    
	
	public List<Coupon> getAllIsActive(){
		List<Coupon> activeCoupons = new ArrayList<>();
		for(Coupon c:couponRepository.findAll()) {
			if(c.getIsActive()) {
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
	
	public boolean containsCoupon(Customer customer,Coupon coupon) {
		for(Coupon c:customer.getCoupons()) {
			if(c.getCouponCode().equals(coupon.getCouponCode())) {
				return true;
			}
		}
		return false;
	}

	public boolean couoponApplied(Customer customer, Coupon coupon) {
		
		List<PurchaseOrder> orderList = coupon.getOrdersList();
		for(PurchaseOrder order: orderList) {
			if(order.getCustomer().getEmailId().equals(customer.getEmailId())) {
				return true;
			}
		}
		return false;
	}
	

}

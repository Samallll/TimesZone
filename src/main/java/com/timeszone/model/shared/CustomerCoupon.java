package com.timeszone.model.shared;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.timeszone.model.customer.Customer;

@Entity
@Table(name = "customer_coupon")
public class CustomerCoupon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerCouponId;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
    
    private boolean couponApplied;

	public Integer getCustomerCouponId() {
		return customerCouponId;
	}

	public void setCustomerCouponId(Integer customerCouponId) {
		this.customerCouponId = customerCouponId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public boolean isCouponApplied() {
		return couponApplied;
	}

	public void setCouponApplied(boolean couponApplied) {
		this.couponApplied = couponApplied;
	}

	public CustomerCoupon(Customer customer, Coupon coupon, boolean couponApplied) {
		super();
		this.customer = customer;
		this.coupon = coupon;
		this.couponApplied = couponApplied;
	}

	public CustomerCoupon(Customer customer, Coupon coupon) {
		super();
		this.customer = customer;
		this.coupon = coupon;
	}

	public CustomerCoupon() {
		super();
	}
    
    
}


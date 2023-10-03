package com.timeszone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.shared.CustomerCoupon;

@Repository
public interface CustomerCouponRepository extends JpaRepository<CustomerCoupon,Integer>{
	
	List<CustomerCoupon> findAllByCustomer(Customer customer);
}

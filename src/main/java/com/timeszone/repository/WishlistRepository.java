package com.timeszone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.shared.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Integer>{
	
	Wishlist findByCustomer(Customer customer);
}

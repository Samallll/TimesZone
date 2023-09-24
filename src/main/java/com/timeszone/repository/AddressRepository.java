package com.timeszone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.customer.Address;
import com.timeszone.model.customer.Customer;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer>{
	
	List<Address> findAllByCustomer(Customer customer);
}

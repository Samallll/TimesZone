package com.timeszone.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.customer.Customer;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
	
	Customer findByEmailId(String emailId);
	
	Customer findByPhoneNumber(String phoneNumber);
	
	Customer findByFirstName(String firstName);
	
	List<Customer> findAllByFirstName(String firstName);
	
	List<Customer> findAllByPhoneNumber(String phoneNumber);
	
	List<Customer> findAllByEmailId(String emailId);
}

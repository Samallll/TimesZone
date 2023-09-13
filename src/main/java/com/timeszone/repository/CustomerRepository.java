package com.timeszone.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.Customer;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
	
	Optional<Customer> findByEmailId(String emailId);
	
	Customer findByPhoneNumber(String phoneNumber);
}
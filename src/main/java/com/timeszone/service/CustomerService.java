package com.timeszone.service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.timeszone.model.Customer;
import com.timeszone.model.Role;
import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.repository.CustomerRepository;

@Service
public class CustomerService implements UserDetailsService{
	
	@Autowired
	public CustomerRepository customerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return customerRepository.findByEmailId(emailId).orElseThrow(()-> new UsernameNotFoundException("UserName not Found"));
	}
	
	public List<CustomerDTO> getAllUsers() {
		
		return customerRepository.findAll()
				.stream()
				.map(x -> convertIntoCustomerDTO(x))
				.collect(Collectors.toList());
	}
	
	
//	Locking methods --------------------------------------------------------------------
	public void lockUser(Integer id) {
		
		Customer lockCustomer = customerRepository.findById(id).get();
		lockCustomer.setLocked(false);
		customerRepository.save(lockCustomer);
	}
	public void unLockUser(Integer id) {
		
		Customer lockCustomer = customerRepository.findById(id).get();
		lockCustomer.setLocked(true);
		customerRepository.save(lockCustomer);
	}
	
//	Customer to CustomerDTO Converter --------------------------------------------------
	@SuppressWarnings("unchecked")
	public CustomerDTO convertIntoCustomerDTO(Customer customer) {
		
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCustomerId(customer.getCustomerId());
		customerDTO.setFirstName(customer.getFirstName());
		customerDTO.setLastName(customer.getLastName());
		customerDTO.setEmailId(customer.getEmailId());
		customerDTO.setPhoneNumber(customer.getPhoneNumber());
		customerDTO.setAuthorities((Set<Role>) customer.getAuthorities());
		customerDTO.setAddresses(customer.getAddresses());
		customerDTO.setLocked(customer.isLocked());
		return customerDTO;
	}

	

}

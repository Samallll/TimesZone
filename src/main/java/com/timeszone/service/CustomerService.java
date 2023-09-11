package com.timeszone.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

}

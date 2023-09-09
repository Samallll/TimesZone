package com.timeszone;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
	
	@Autowired
	public CustomerRepository customerRepository;
	
	@Autowired
	public RoleRepository roleRepository;
	
	@Autowired
	public PasswordEncoder encoder;
	
	public Customer register(RegistrationRequest request) {
		
		Role userRole;
		if(roleRepository.findByAuthority("USER").isPresent()) {
			userRole = roleRepository.findByAuthority("USER").get();
		}
		else {
			userRole = new Role("USER");
		}
		roleRepository.save(userRole);
		
		Set<Role> roles = new HashSet<>();
		roles.add(userRole);
		Customer newCustomer = new Customer(request.getFirstName(),request.getLastName(),request.getEmailId(),request.getPhoneNumber(),encoder.encode(request.getPassword()),roles);
		newCustomer.allAuthorities();
		return customerRepository.save(newCustomer);
	}
}

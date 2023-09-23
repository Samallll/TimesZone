package com.timeszone.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.timeszone.model.Customer;
import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.service.CustomerService;

@RequestMapping("/user")
@Controller
public class CustomerController {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerService customerService;
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@GetMapping("/profile")
	public String userProfile(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		
		Customer c = customerRepository.findByEmailId(userName);
		CustomerDTO customerData = customerService.convertIntoCustomerDTO(c);
		
		model.addAttribute("customerData", customerData);
		return "userProfile";
	}
	
	@GetMapping("/editCustomer")
	public String customerData(@RequestParam("id") Integer customerId) {
		
		return "editCustomer";
	}
	
}
package com.timeszone.service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.customer.Role;
import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.model.dto.RegistrationDTO;
import com.timeszone.repository.CustomerRepository;

/**
 * 
 */
@Service
public class CustomerService implements UserDetailsService{
	
	@Autowired
	public CustomerRepository customerRepository;
	
	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Customer customer = customerRepository.findByEmailId(emailId);
		
		if (customer != null) {
			return customer;
		}
		throw new UsernameNotFoundException("UserName Not Found");

	}
	
	public UserDetails loadUserForOtpLogin(String emailId) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
			Customer customer = customerRepository.findByEmailId(emailId);
			
			if (customer != null) {
				System.out.println("customer details from customer service:" + customer.getFirstName());
				return customer;
			}
			throw new UsernameNotFoundException("User not available");
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
		customerDTO.setWallet(customer.getWallet());
		customerDTO.setReferralCode(customer.getReferralCode());
		return customerDTO;
	}

	public boolean customerExists(RegistrationDTO customerData) {
		
		if(customerRepository.findByEmailId(customerData.getEmailId())!=null) {
			
			errorMsg="Email Id exists";
			return true;
		}
		else if(customerRepository.findByFirstName(customerData.getFirstName())!=null) {
			
			errorMsg="First Name exists";
			return true;
		}
		else if(customerRepository.findByPhoneNumber(customerData.getPhoneNumber())!=null) {
			
			errorMsg="Phone Number exists";
			return true;
		}
		else if(customerData.getReferralCode()!=null  && !isValidReferralCode(customerData.getReferralCode())) {
			errorMsg="Invalid Referral Code";
			return true;
		}
		else {
			errorMsg=null;
			return false;
		}
	}

	public Customer getCustomer(Integer customerId) {
		return customerRepository.findById(customerId).orElseThrow(() -> new UsernameNotFoundException("UserName Not Found with the given Id"));
	}

	public Customer getCustomerByEmailId(String emailId) {
		return customerRepository.findByEmailId(emailId);
	}
	
	public boolean isValidReferralCode(String referralCode) {
		Customer customer = customerRepository.findByReferralCode(referralCode);
		return customer!=null;
	}

	public boolean validateData(RegistrationDTO request) {

		String emailRegex = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$";

		if(request.getFirstName().isEmpty() || request.getFirstName() == null){
			errorMsg = "Please fill first name field";
			return false;
		}

		if(request.getLastName().isEmpty() || request.getLastName() == null){
			errorMsg = "Please fill last name field";
			return false;
		}

		if(request.getEmailId().isEmpty() || request.getEmailId() == null){
			errorMsg = "Email Id should not be empty";
			return false;
		}

		if(request.getPhoneNumber().isEmpty() || request.getPhoneNumber() == null){
			errorMsg = "Phone number should not be empty";
			return false;
		}

		if(request.getPassword().isEmpty() || request.getPassword() == null){
			errorMsg = "Password should not be empty";
			return false;
		}

		if (!request.getEmailId().matches(emailRegex)) {
			errorMsg = "Invalid Email Address";
			return false;
		}

		String phoneNumber = request.getPhoneNumber();
		if (phoneNumber.length() != 10) {
			errorMsg = "Phone Number should be 10 digits";
			return false;
		}

		if(!phoneNumber.matches("^\\d+$")){
			errorMsg = "Please enter a valid Phone number";
			return false;
		}

		String password = request.getPassword();
		if (password.length() < 8) {
			errorMsg = "Password must be at least 8 characters long";
			return false;
		}

		return true;
	}

}

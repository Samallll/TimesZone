package com.timeszone.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.Customer;
import com.timeszone.repository.CustomerRepository;

@Service
public class OtpService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
//	@Autowired
//	private TwilioSmsService twilioSmsService;
	
	private String errorMessage;

    public String getErrorMessage() {
		return errorMessage;
	}

    public void sendOtp(String phoneNumber) {
        
    	Customer customer = customerRepository.findByPhoneNumber(phoneNumber);
    	
        if (customer == null) {
            errorMessage = "Invalid phone number";
            System.out.println(errorMessage);
            return;
        }

    	// Create a random number generator
        Random random = new Random();
        int min = 100000; // Minimum 6-digit number
        int max = 999999; // Maximum 6-digit number
        int randomNumber = random.nextInt(max - min + 1) + min;

        // Set the expiration time for the OTP
        LocalDateTime expirationTime = LocalDateTime.now().plus(2, ChronoUnit.MINUTES);
        
//        SMS Service is not working as expected hence commenting the below lines.
//        String message = "Your Otp for TimeZone Login : " + randomNumber;
//        twilioSmsService.sendSms(phoneNumber, message);
        
        customer.setOtp(randomNumber);
        customer.setExpirationTime(expirationTime);
        customerRepository.save(customer);
        errorMessage = null;
        
    }
    
    public boolean verifyOtp(String phoneNumber, Integer otp) {
        // Check if the OTP is valid
    	// Get the current time
    	Customer verifyCustomer = customerRepository.findByPhoneNumber(phoneNumber);
    	System.out.println("Stored OTP: "+verifyCustomer.getOtp());
    	LocalDateTime expirationTime = verifyCustomer.getExpirationTime();
    	LocalDateTime currentTime = LocalDateTime.now();
    	int comparison = currentTime.compareTo(expirationTime);
    	

        // Compare the two times
        if (currentTime.isBefore(expirationTime) &&verifyCustomer.getOtp().equals(otp)) {
            System.out.println("Allowed Login...");
        	verifyCustomer.setExpirationTime(null);
        	verifyCustomer.setOtp(null);
        	customerRepository.save(verifyCustomer);
            return true;
        } 
        else if(comparison > 0) {
        	verifyCustomer.setExpirationTime(null);
        	verifyCustomer.setOtp(null);
        	System.out.println("Time expired....");
        	customerRepository.save(verifyCustomer);
        	return false;
        }
        else {
            System.out.println("Invalid OTP");
            return false;
        }
    }

}

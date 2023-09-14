package com.timeszone.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.Customer;
import com.timeszone.model.Otp;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.repository.OtpRepository;

@Service
public class OtpService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private OtpRepository otpRepository;
	
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
    	if(verifyCustomer==null) {
    		return false;
    	}
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
    
    
    
//    For user validation through registred phone number ----------------------------------------
    public void sendRegistrationOtp(String phoneNumber) {

    	// Create a random number generator
        Random random = new Random();
        int min = 100000; // Minimum 6-digit number
        int max = 999999; // Maximum 6-digit number
        int randomNumber = random.nextInt(max - min + 1) + min;

        // Set the expiration time for the OTP
        LocalDateTime expirationTime = LocalDateTime.now().plus(4, ChronoUnit.MINUTES);
        
//        SMS Service is not working as expected hence commenting the below lines.
//        String message = "Your Otp for TimeZone Login : " + randomNumber;
//        twilioSmsService.sendSms(phoneNumber, message);
        Otp newOtp = otpRepository.findByPhoneNumber(phoneNumber);
        if(newOtp==null) {
        	newOtp = new Otp(phoneNumber, randomNumber, expirationTime); 
            otpRepository.save(newOtp);
        }
        errorMessage = null;
        
    }
    
    public boolean validateRegistrationOtp(String phoneNumber, Integer otp) {
        // Check if the OTP is valid
    	// Get the current time

    	Otp validatedOtp = otpRepository.findByPhoneNumber(phoneNumber);

    	LocalDateTime expirationTime = validatedOtp.getExpirationTime();
    	LocalDateTime currentTime = LocalDateTime.now();
    	int comparison = currentTime.compareTo(expirationTime);
    	

        // Compare the two times
        if (currentTime.isBefore(expirationTime) &&validatedOtp.getOtp().equals(otp)) {
            System.out.println("Allowed Registration...");
            otpRepository.delete(validatedOtp);
            return true;
        } 
        else if(comparison > 0) {
        	System.out.println("Time expired....");
            otpRepository.delete(validatedOtp);
        	return false;
        }
        else {
            System.out.println("Invalid OTP");
            return false;
        }
    }

}

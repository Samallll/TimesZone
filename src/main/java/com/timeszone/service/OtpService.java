package com.timeszone.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.controller.MainController;
import com.timeszone.model.customer.Customer;
import com.timeszone.model.customer.Otp;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.repository.OtpRepository;

@Service
public class OtpService {
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private OtpRepository otpRepository;
	
	@Autowired
	private EmailSender emailSender;
	
	private String errorMessage;
	
	private String successMessage;

    public String getErrorMessage() {
		return errorMessage;
	}
    
    public String getSuccessMessage() {
		return successMessage;
	}

    public void sendOtp(String emailId) {
        
    	System.out.println(emailId);
    	Customer customer = customerRepository.findByEmailId(emailId);
    	
        if (customer == null) {
            errorMessage = "Invalid Email Address";
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
        
        String emailBody = "Your OTP is: " + randomNumber;
//        try {
//            emailSender.sendEmail(customer.getEmailId(), "OTP Verification", emailBody);
//        } catch (MessagingException e) {
//            logger.error("Failed to send email", e);
//        }
        
        customer.setOtp(randomNumber);
        customer.setExpirationTime(expirationTime);
        customerRepository.save(customer);
        errorMessage = null;
        
    }
    
    public boolean verifyOtp(String emailId, Integer otp) {
        // Check if the OTP is valid
    	// Get the current time
    	Customer verifyCustomer = customerRepository.findByEmailId(emailId);
    	if(verifyCustomer==null) {
    		errorMessage = "Invalid Email Address";
            System.out.println(errorMessage);
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
        	errorMessage = null;
        	customerRepository.save(verifyCustomer);
            return true;
        } 
        else if(comparison > 0) {
        	verifyCustomer.setExpirationTime(null);
        	verifyCustomer.setOtp(null);
        	errorMessage = "Time expired....";
        	System.out.println("Time expired....");
        	customerRepository.save(verifyCustomer);
        	return false;
        }
        else {
            System.out.println("Invalid OTP");
            errorMessage = "Invalid OTP";
            return false;
        }
    }
    
    
    
//    For user validation through registred phone number ----------------------------------------
    public void sendRegistrationOtp(String emailId) {
    	
    	
    	// Create a random number generator
        Random random = new Random();
        int min = 100000; // Minimum 6-digit number
        int max = 999999; // Maximum 6-digit number
        int randomNumber = random.nextInt(max - min + 1) + min;

        // Set the expiration time for the OTP
        LocalDateTime expirationTime = LocalDateTime.now().plus(4, ChronoUnit.MINUTES);
        
        String emailBody = "Your OTP is: " + randomNumber;
//        try {
//            emailSender.sendEmail(emailId, "OTP Verification", emailBody);
//        } catch (MessagingException e) {
//            logger.error("Failed to send email", e);
//        }
        
        
        Otp newOtp = otpRepository.findByEmailId(emailId);
        if(newOtp==null) {
        	newOtp = new Otp(emailId, randomNumber, expirationTime); 
        }
        newOtp.setOtp(randomNumber);
        newOtp.setExpirationTime(expirationTime);
        otpRepository.save(newOtp);
        errorMessage = null;
        
    }
    
    public boolean validateRegistrationOtp(String emailId, Integer otp) {
        // Check if the OTP is valid
    	// Get the current time

    	Otp validatedOtp = otpRepository.findByEmailId(emailId);

    	LocalDateTime expirationTime = validatedOtp.getExpirationTime();
    	LocalDateTime currentTime = LocalDateTime.now();
    	int comparison = currentTime.compareTo(expirationTime);
    	

        // Compare the two times
        if (currentTime.isBefore(expirationTime) &&validatedOtp.getOtp().equals(otp)) {
            System.out.println("Allowed Registration...");
            errorMessage = null;
            successMessage = "Account Created Successfully";
            otpRepository.delete(validatedOtp);
            return true;
        } 
        else if(comparison > 0) {
        	System.out.println("Time expired....");
        	errorMessage = "Time expired....";
        	successMessage = null;
            otpRepository.delete(validatedOtp);
        	return false;
        }
        else {
            System.out.println("Invalid OTP");
            errorMessage = "Invalid OTP";
            successMessage = null;
            return false;
        }
    }

}

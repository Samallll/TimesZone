package com.timeszone;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private TwilioSmsService twilioSmsService;
	
	private String errorMessage;

    public String getErrorMessage() {
		return errorMessage;
	}

	private static final int OTP_LENGTH = 6;

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
        String message = "Your Otp for TimeZone Login : " + randomNumber;
//        twilioSmsService.sendSms(phoneNumber, message);
        
        System.out.println(randomNumber);
        customer.setOtp(randomNumber);
        customer.setExpirationTime(expirationTime);
        customerRepository.save(customer);
        errorMessage = null;
        
    }
    
    public boolean verifyOtp(String phoneNumber, Integer otp) {
        // Check if the OTP is valid
    	// Get the current time
    	Customer verifyCustomer = customerRepository.findByPhoneNumber(phoneNumber);
    	LocalDateTime expirationTime = verifyCustomer.getExpirationTime();
    	LocalDateTime currentTime = LocalDateTime.now();
    	System.out.println(currentTime.toString());
    	System.out.println(expirationTime.toString());
    	System.out.println(currentTime.isEqual(expirationTime));
    	int comparison = currentTime.compareTo(expirationTime);

        // Compare the two times
        if (comparison < 0 &&verifyCustomer.getOtp()==otp) {
            System.out.println("The given time is before the expired time.");
        	verifyCustomer.setExpirationTime(null);
        	verifyCustomer.setOtp(null);
            return true;
        } 
        else if(comparison > 0) {
        	verifyCustomer.setExpirationTime(null);
        	verifyCustomer.setOtp(null);
        	System.out.println("Time expired....");
        	return false;
        }
        else {
        	System.out.println(currentTime.isBefore(expirationTime));
            System.out.println("Invalid OTP");
            return false;
        }
    }

}

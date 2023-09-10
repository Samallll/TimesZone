package com.timeszone;

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
        Date expirationTime = new Date(System.currentTimeMillis() + 120000); // 2 minutes
        
        String message = "Your Otp for TimeZone Login : " + randomNumber;
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
    	Date expirationTime = verifyCustomer.getExpirationTime();
        Date currentTime = new Date();

        // Compare the two times
        if (currentTime.before(expirationTime)&&verifyCustomer.getOtp()==otp) {
            System.out.println("The given time is before the expired time.");
        	verifyCustomer.setExpirationTime(null);
        	verifyCustomer.setOtp(null);
            return true;
        } else {
            System.out.println("Invalid OTP");
            return false;
        }
    }

}

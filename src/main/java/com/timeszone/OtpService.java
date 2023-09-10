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
            return;
        }
    	
    	// Create a random number generator
        Random random = new Random();
        int randomNumber = random.nextInt(10 ^ OTP_LENGTH);

        // Set the expiration time for the OTP
        Date expirationTime = new Date(System.currentTimeMillis() + 120000); // 2 minutes
        
        String message = "Your Otp for TimeZone Login : " + randomNumber;
        twilioSmsService.sendSms(phoneNumber, message);
        
        customer.setOtp(randomNumber);
        customer.setExpirationTime(expirationTime);
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

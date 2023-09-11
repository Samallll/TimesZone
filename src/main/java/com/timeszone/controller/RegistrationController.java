package com.timeszone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timeszone.model.dto.RegistrationDTO;
import com.timeszone.service.RegistrationService;

@RestController
@RequestMapping("/")
public class RegistrationController {
	
	@Autowired
	private RegistrationService registrationService;
	
	@PostMapping("/user_registration")
	public String register(@RequestBody RegistrationDTO request) {
		registrationService.register(request);
		return "Registered successfully";
	}
}

package com.timeszone;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	
	@GetMapping("/admin")
	public String adminHome() {
		return "In Admin Home , You have admin power";
	}
	
	@GetMapping("/user")
	public String userHome() {
		return "In User Home , You are normal user";
	}
	
	@GetMapping("/user_registration")
	public String userRegistration() {
		return "In user registration , please register";
	}
}

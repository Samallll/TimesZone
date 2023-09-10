package com.timeszone;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
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
	
//	For Login
	@GetMapping("/login")
	public String loginPage(Model model) {
//		To hold the data
		LoginDTO userLoginAccount = new LoginDTO();
		model.addAttribute("userLoginAccount", userLoginAccount);
		return "loginPage.html";
	}
}

package com.timeszone.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.timeszone.model.dto.LoginDTO;
import com.timeszone.service.OtpService;


@Controller
public class MainController {
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private OtpService otpService;
	
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
		logger.info("InSide Login Controller");
//		To hold the data
		LoginDTO userLoginAccount = new LoginDTO();
		model.addAttribute("userLoginAccount", userLoginAccount);
		return "loginPage.html";
	}
	
//	@PostMapping("/sendOtp")
//	public String sendOtp(@ModelAttribute("userLoginAccount") LoginDTO l, Model model) {
//		System.out.println("Invalid number");
//		otpService.sendOtp(l.getPhoneNumber());
//		
//		if(otpService.getErrorMessage()!=null) {
//			model.addAttribute("error", otpService.getErrorMessage());
//			System.out.println("Invalid number");
//			return "redirect:/login";
//		}
//		else {
//			model.addAttribute("validPhoneNumber", l.getPhoneNumber());
//			return "redirect:/otpLogin";
//		}
//	}
	
//	@PostMapping("/sendOtp")
//	public String sendOtp(@ModelAttribute("userLoginAccount") LoginDTO l,HttpSession session) {
//		System.out.println("In OTP Login");
//		logger.info("In OTP Login");
//		otpService.sendOtp(l.getPhoneNumber());
//		session.setAttribute("validPhoneNumber", l.getPhoneNumber());
//		return "redirect:/otpLogin";
//	}
//	
//	@GetMapping("/otpLogin")
//	public String otpLogin(Model model,HttpSession session) {
//		
//		LoginDTO otpBasedLoginAccount = new LoginDTO();
//		model.addAttribute("otpBasedLoginAccount", otpBasedLoginAccount);
//		return "otp.html";
//	}
//	
//	@PostMapping("/otpVerify")
//	public String otpVerification(@ModelAttribute("otpBasedLoginAccount") LoginDTO LoginAccount,HttpSession session) {
//		
////		LoginAccount contains only the otp entered by the user
//		
////		validPhoneNumber is the number entered by the user in the previous login page.
////		session.getAttribute("validPhoneNumber").toString()): contains the phoneNumber entered by the user.
//		
//		if(otpService.verifyOtp(session.getAttribute("validPhoneNumber").toString(),LoginAccount.getOtp())) {
//			return "redirect:/user";
//		}
//		else {
//			return "redirect:/otpLogin";
//		}		
//	}

}

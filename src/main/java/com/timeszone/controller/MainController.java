package com.timeszone.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.model.dto.LoginDTO;
import com.timeszone.model.dto.RegistrationDTO;
import com.timeszone.service.CustomerService;
import com.timeszone.service.OtpService;
import com.timeszone.service.RegistrationService;


@Controller
public class MainController {
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
//	@Autowired
//	private OtpService otpService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private RegistrationService registrationService;
	
	@GetMapping("/admin")
	public String adminHome() {
		return "adminHome.html";
	}
	
	@GetMapping("/user")
	public String userHome() {
		return "userHome.html";
	}
	
//	For user registration --------------------------------------------------------------------
	@GetMapping("/user_registration")
	public String userRegistration(Model model) {
		
		RegistrationDTO newUserData = new RegistrationDTO();
		model.addAttribute("newUserData", newUserData);
		return "userRegister.html";
	}
	
//	For Login --------------------------------------------------------------------------------
	@GetMapping("/login")
	public String loginPage(Model model) {
		logger.info("InSide Login Controller");
//		To hold the data
		LoginDTO userLoginAccount = new LoginDTO();
		model.addAttribute("userLoginAccount", userLoginAccount);
		return "login.html";
	}
	
	@PostMapping("/register_user")
	public String register(@ModelAttribute("newUserData") RegistrationDTO request) {
		logger.info("User Registeriing started");
		registrationService.register(request);
		logger.info("User Registration Completed Successfully");
		return "redirect:/login";
	}
	
	
//	For user management ---------------------------------------------------------------------
	@GetMapping("/user_management")
	public String userManagementPage(Model model) {
		logger.info("InSide User Management Controller");
		
//		To hold the data
		List<CustomerDTO> userList = customerService.getAllUsers();
		model.addAttribute("userList", userList);
		return "userManagement.html";
	}
	
//	Lock Management -------------------------------------------------------------------
	@GetMapping("/block/{id}")
	public String blockUser(@PathVariable Integer id) {
		
		logger.trace("InSide Locking Controller");
		customerService.lockUser(id);
		logger.info("Locked User");
		return "redirect:/user_management";
	}
	
	@GetMapping("/unBlock/{id}")
	public String unblockUser(@PathVariable Integer id) {
		
		logger.trace("InSide Unlocking Controller");
		System.out.println("Integer :" +id);
		customerService.unLockUser(id);
		logger.info("UnLocked User");
		return "redirect:/user_management";
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

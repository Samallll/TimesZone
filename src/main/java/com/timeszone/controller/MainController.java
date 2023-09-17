package com.timeszone.controller;

import java.time.LocalDate;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.timeszone.model.Customer;
import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.model.dto.LoginDTO;
import com.timeszone.model.dto.ProductDTO;
import com.timeszone.model.dto.RegistrationDTO;
import com.timeszone.model.product.Product;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.repository.ProductRepository;
import com.timeszone.service.CustomerService;
import com.timeszone.service.OtpService;
import com.timeszone.service.ProductService;
import com.timeszone.service.RegistrationService;


@Controller
public class MainController {
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping("/user")
	public String userHome() {
		return "userHome.html";
	}
	
//	For user registration --------------------------------------------------------------------
	@GetMapping("/otpVerification")
	public String otpVerification(Model model,HttpSession session) {
		
		LoginDTO otpBasedLoginAccount = new LoginDTO();
		model.addAttribute("otpBasedLoginAccount", otpBasedLoginAccount);
		return "userValidation.html";
	}
	
	@GetMapping("/user_registration")
	public String userRegistration(Model model) {
		
		RegistrationDTO newUserData = new RegistrationDTO();
		model.addAttribute("newUserData", newUserData);
		return "userRegister.html";
	}
	
	@PostMapping("/register_user")
	public String register(@ModelAttribute("newUserData") RegistrationDTO request,HttpSession session) {
		logger.info("User Registeriing started");
		Customer verifyCustomer = registrationService.register(request);
		otpService.sendRegistrationOtp(verifyCustomer.getPhoneNumber());
		session.setAttribute("validPhoneNumber", verifyCustomer.getPhoneNumber());
		session.setAttribute("verifyCustomer", verifyCustomer);
		return "redirect:/otpVerification";	
	}
	
	@PostMapping("/otpRegistrationValidation")
	public String otpRegistrationValidation(@ModelAttribute("otpBasedLoginAccount") LoginDTO LoginAccount,HttpSession session) {
		
//			LoginAccount contains only the otp entered by the user
		
//			validPhoneNumber is the number entered by the user in the previous login page.
//			session.getAttribute("validPhoneNumber").toString()): contains the phoneNumber entered by the user.
		String phoneNumber = session.getAttribute("validPhoneNumber").toString();
		boolean flag = otpService.validateRegistrationOtp(phoneNumber,LoginAccount.getOtp());
		System.out.println(flag);
		if(flag) {
			Customer verifyCustomer = (Customer) session.getAttribute("verifyCustomer");
			customerService.customerRepository.save(verifyCustomer);
			return "redirect:/login";
		}
		else {
			System.out.println("Inside OtpVerfication Failed Case");
			return "redirect:/otpVerification";
		}		
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
	
	@PostMapping("/sendOtp")
	public String sendOtp(@ModelAttribute("userLoginAccount") LoginDTO l,HttpSession session) {
		logger.debug("In OTP Login");
		otpService.sendOtp(l.getPhoneNumber());
		session.setAttribute("validPhoneNumber", l.getPhoneNumber());
		return "redirect:/otpLogin";
	}
	
	@GetMapping("/otpLogin")
	public String otpLogin(Model model,HttpSession session) {
		
		LoginDTO otpBasedLoginAccount = new LoginDTO();
		model.addAttribute("otpBasedLoginAccount", otpBasedLoginAccount);
		return "otp.html";
	}
//	
	@PostMapping("/otpVerify")
	public String otpVerification(@ModelAttribute("otpBasedLoginAccount") LoginDTO LoginAccount,HttpSession session) {
		
//		LoginAccount contains only the otp entered by the user
		
//		validPhoneNumber is the number entered by the user in the previous login page.
//		session.getAttribute("validPhoneNumber").toString()): contains the phoneNumber entered by the user.
		String phoneNumber = session.getAttribute("validPhoneNumber").toString();
		System.out.println(phoneNumber);
		boolean flag = otpService.verifyOtp(phoneNumber,LoginAccount.getOtp());
		System.out.println(flag);
		if(flag) {
			System.out.println("Inside Allowing mechanism");
			UserDetails userDetails = customerService.loadUserForOtpLogin(phoneNumber);
			System.out.println(userDetails.getAuthorities().size());
	        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        System.out.println(authentication.toString());
			return "redirect:/user";
		}
		else {
			System.out.println("Inside OtpVerfication Failed Case");
			return "redirect:/otpLogin";
		}		
	}

}

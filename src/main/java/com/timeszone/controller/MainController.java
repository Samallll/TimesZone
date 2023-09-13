package com.timeszone.controller;

import java.time.LocalDate;
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
import com.timeszone.model.product.Product;
import com.timeszone.model.product.ProductDTO;
import com.timeszone.repository.ProductRepository;
import com.timeszone.service.CustomerService;
import com.timeszone.service.OtpService;
import com.timeszone.service.ProductService;
import com.timeszone.service.RegistrationService;


@Controller
public class MainController {
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
//	@Autowired
//	private OtpService otpService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private ProductRepository productRepository;
	
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
	
	@PostMapping("/register_user")
	public String register(@ModelAttribute("newUserData") RegistrationDTO request) {
		logger.info("User Registeriing started");
		registrationService.register(request);
		logger.info("User Registration Completed Successfully");
		return "redirect:/login";
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
		customerService.unLockUser(id);
		logger.info("UnLocked User");
		return "redirect:/user_management";
	}
	
//	Product Management ---------------------------------------------------------------
	@GetMapping("/product_management")
	public String productManagementPage(Model model) {
		logger.info("InSide Product Management Controller");
//		To hold the data
		List<Product> productList = productService.getAllProducts();
		model.addAttribute("productList", productList);
		return "productManagement.html";
	}
	
	@GetMapping("/addProduct")
	public String addProductPage(Model model) {
		logger.trace("InSide Add Product Controller");
		
//		To hold the data
		Product newProduct = new Product();
		model.addAttribute("newProduct", newProduct);
		return "addProduct.html";
	}
	
	@PostMapping("/registerProduct")
	public String addProduct(@ModelAttribute("newProduct") Product p) {
		logger.trace("InSide Product Registering Controller");
		
//		To hold the data
		Product registerProduct = new Product(p.getProductId(), p.getProductName(), p.getDescription(), p.getQuantity(), p.getCaseSize(),
				p.getPrice(), LocalDate.now());
		productRepository.save(registerProduct);
		return "redirect:/product_management";
	}
	
	@GetMapping("/editProduct/{id}")
	public String editProductPage(@PathVariable Integer id,Model model) {
		logger.trace("InSide Edit Product Controller");
//		To hold the data
		Product editProduct = productRepository.findById(id).get();
		model.addAttribute("editProduct", editProduct);
		return "productManagement.html";
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

package com.timeszone.controller;

import java.io.IOException;
import java.security.MessageDigest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.dto.LoginDTO;
import com.timeszone.model.dto.ProductDTO;
import com.timeszone.model.dto.RegistrationDTO;
import com.timeszone.model.dto.RequestData;
import com.timeszone.model.product.Category;
import com.timeszone.model.product.Product;
import com.timeszone.model.product.ProductImage;
import com.timeszone.model.shared.Cart;
import com.timeszone.repository.CartRepository;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.repository.ProductImageRepository;
import com.timeszone.repository.ProductRepository;
import com.timeszone.service.CategoryService;
import com.timeszone.service.CustomerService;
import com.timeszone.service.OtpService;
import com.timeszone.service.PdfService;
import com.timeszone.service.ProductService;
import com.timeszone.service.PurchaseOrderService;
import com.timeszone.service.RegistrationService;
import com.timeszone.service.SubCategoryService;

@RequestMapping("/guest")
@Controller
public class MainController {
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private ProductImageRepository productImageRepository;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private PdfService pdfService;
	
	@Autowired
	private SubCategoryService subCategoryService;
	
	@Autowired
	private CategoryService categoryService;
	
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
	public String userRegistration(Model model,HttpSession session,
									@RequestParam(name="referralCode",defaultValue="null") String referralCode) {
		
		RegistrationDTO newUserData = new RegistrationDTO();
		if(referralCode!=null) {
			newUserData.setReferralCode(referralCode);
		}
		model.addAttribute("newUserData", newUserData);
		return "userRegister.html";
	}
	
	@PostMapping("/register_user")
	public String register(@ModelAttribute("newUserData") RegistrationDTO request,
						   HttpSession session) {
		logger.debug("User Registeriing started");
		
		if(customerService.customerExists(request)) {
			session.setAttribute("registerError", customerService.getErrorMsg());
			return "redirect:/guest/user_registration";
		} else if (!customerService.validateData(request)) {
			session.setAttribute("registerError", customerService.getErrorMsg());
			return "redirect:/guest/user_registration";
		}else {
			Customer verifyCustomer = registrationService.register(request);
			otpService.sendRegistrationOtp(verifyCustomer.getEmailId());
			session.setAttribute("validEmailId", verifyCustomer.getEmailId());
			session.setAttribute("verifyCustomer", verifyCustomer);		
			return "redirect:/guest/otpVerification";	
		}
	}
	
	@PostMapping("/otpRegistrationValidation")
	public String otpRegistrationValidation(@ModelAttribute("otpBasedLoginAccount") LoginDTO LoginAccount,HttpSession session) {
		
//			LoginAccount contains only the otp entered by the user
		
//			validEmailId is the email entered by the user in the previous login page.
//			session.getAttribute("validEmailId").toString()): contains the email entered by the user.
		String emailId = session.getAttribute("validEmailId").toString();
		boolean flag = otpService.validateRegistrationOtp(emailId,LoginAccount.getOtp());
		System.out.println(flag);
		if(flag) {
			Customer verifyCustomer = (Customer) session.getAttribute("verifyCustomer");
			
			Cart newCart = new Cart();
			cartRepository.save(newCart);
			
			verifyCustomer.setCart(newCart);
			if(verifyCustomer.getUsedCode()!=null && !verifyCustomer.getUsedCode().isEmpty()) {
				//if valid then add the amount to the referred person's account and this account
				Customer referredCustomer = customerRepository.findByReferralCode(verifyCustomer.getUsedCode());
				referredCustomer.setWallet(referredCustomer.getWallet()+1000.0);
				customerRepository.save(referredCustomer);
				verifyCustomer.setWallet(500.0);
			}
			verifyCustomer.setReferralCode(generateReferralCode(verifyCustomer.getEmailId()));
			customerService.customerRepository.save(verifyCustomer);
			
			newCart.setCustomer(verifyCustomer);
			cartRepository.save(newCart);
			
			session.setAttribute("registerSuccess", otpService.getSuccessMessage());
			session.removeAttribute("registerError");
			return "redirect:/guest/user_registration";
		}
		else {
			System.out.println("Inside OtpVerfication Failed Case");
			session.setAttribute("registerError", otpService.getErrorMessage());
			session.removeAttribute("registerSuccess");
			return "redirect:/otpVerification";
		}		
	}
	
    private String generateReferralCode(String emailId) {
    	
    	try {
    		MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] emailBytes = emailId.getBytes("UTF-8");
            byte[] hashBytes = md.digest(emailBytes);
            
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder code = new StringBuilder();
            
            for (byte b : hashBytes) {
                int index = (b & 0xFF) % characters.length();
                code.append(characters.charAt(index));
            } 
            return code.substring(0, 6);
    	}
    	catch(Exception e) {
    		System.out.println(e.getMessage());
    		return null;
    	}
	}

//	For Login --------------------------------------------------------------------------------
	@GetMapping("/login")
	public String loginPage(Model model,HttpSession session) {
		logger.debug("InSide Login Controller");
//		To hold the data
		LoginDTO userLoginAccount = new LoginDTO();
		model.addAttribute("userLoginAccount", userLoginAccount);
		return "login";
	}
	
	
//	Forgot Password -------------------------------------------------------
	
//	For rendering forgot password page rendering ------
	@GetMapping("/forgotPassword")
	public String forgotPasswordPage(HttpSession session) {
		
		return "forgotPassword";
	}
	
	@PostMapping("/sendOtpForPassword")
	public String sendOtpForPasswordChange(@RequestParam("emailId") String emailId,HttpSession session ) {
		
		logger.debug("ForgotPassword::sendOtpForPasswordChange");
		otpService.sendOtp(emailId);
		if(otpService.getErrorMessage()!=null) {
			session.setAttribute("passwordOtpFail", otpService.getErrorMessage());
			session.removeAttribute("error");
			System.out.println("Invalid Email Id");
			return "redirect:/guest/forgotPassword";
		}
		else {
			session.setAttribute("passwordChangeEmailId", emailId);
			session.removeAttribute("passwordOtpFail");
			session.removeAttribute("error");
			return "redirect:/guest/otpPassword";
		}
	}
	
	@PostMapping("/veriftyOtpPassword")
	public String otpVerificationForPasswordChange(@ModelAttribute("otpBasedLoginAccount") LoginDTO LoginAccount,HttpSession session) {
		
//		LoginAccount contains only the otp entered by the user
		
//		validPhoneNumber is the number entered by the user in the previous login page.
//		session.getAttribute("validPhoneNumber").toString()): contains the phoneNumber entered by the user.
		String emailId = session.getAttribute("passwordChangeEmailId").toString();
		System.out.println(emailId);
		boolean flag = otpService.verifyOtp(emailId,LoginAccount.getOtp());
		System.out.println(flag);
		if(flag) {
			System.out.println("Inside Allowing mechanism");
	        session.removeAttribute("passwordOtpFail");
			return "redirect:/guest/changePassword";
		}
		else {
			session.setAttribute("passwordOtpFail", otpService.getErrorMessage());
			return "redirect:/guest/otpPassword";
		}		
	}
	
//	For rendering otp entering page ---------------
	@GetMapping("/otpPassword")
	public String otpForPasswordChange(Model model,HttpSession session) {
		
		LoginDTO otpBasedLoginAccount = new LoginDTO();
		model.addAttribute("otpBasedLoginAccount", otpBasedLoginAccount);
		return "passwordChangeOtp.html";
	}
	
//	for rendering password change page ---------------
	@GetMapping("/changePassword")
	public String changePassword(Model model,HttpSession session) {
		
		LoginDTO passwordChange = new LoginDTO();
		model.addAttribute("passwordChange", passwordChange);
		return "changePassword";
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@ModelAttribute("passwordChange") LoginDTO editAccount,HttpSession session) {
		
		String emailId = session.getAttribute("passwordChangeEmailId").toString();
		if(!editAccount.getEmailId().equals(editAccount.getPassword())) {
			session.setAttribute("passwordChangeError", "Password doesn't match");
			session.removeAttribute("passwordChangeSuccess");
		}
		else {
			session.setAttribute("passwordChangeSuccess", "Password Changed Successfully");
			session.removeAttribute("passwordChangeError");
			Customer editCustomer = customerRepository.findByEmailId(emailId);
			editCustomer.setPassword(passwordEncoder.encode(editAccount.getPassword()));
			customerRepository.save(editCustomer);			
		}
		return "redirect:/guest/changePassword";
	}
	
	
//	Otp Based Login ------------------------------------------------------------------
	
	@PostMapping("/sendOtp")
	public String sendOtp(@ModelAttribute("userLoginAccount") LoginDTO l,HttpSession session) {
		logger.debug("In OTP Login");
		otpService.sendOtp(l.getEmailId());
		if(otpService.getErrorMessage()!=null) {
			session.setAttribute("otpFail", otpService.getErrorMessage());
			session.removeAttribute("error");
			System.out.println("Invalid Email Id");
			return "redirect:/login";
		}
		else {
			session.setAttribute("validEmailId", l.getEmailId());
			session.removeAttribute("otpFail");
			session.removeAttribute("error");
			return "redirect:/guest/otpLogin";
		}
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
		String emailId = session.getAttribute("validEmailId").toString();
		boolean flag = otpService.verifyOtp(emailId,LoginAccount.getOtp());
		if(flag) {
			UserDetails userDetails = customerService.loadUserForOtpLogin(emailId);
	        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        session.removeAttribute("otpSuccess");
	        session.removeAttribute("otpFail");
			return "redirect:/guest/user";
		}
		else {
			session.setAttribute("otpFail", otpService.getErrorMessage());
			session.setAttribute("otpSuccess", otpService.getSuccessMessage());
			return "redirect:/guest/otpLogin";
		}		
	}
	
//	Shopping side -------------------------------------------------------------------------------
	
	@GetMapping("/shop")
	public String shopPage(@RequestParam(name = "page", defaultValue = "0") int page,
						    @RequestParam(name = "size", defaultValue = "3") int size,
						    HttpSession session,
						    Model model) {

		List<Category> categories = categoryService.getAllCategories();
		RequestData searchRequest = new RequestData();
				
		Pageable pageable = PageRequest.of(page, size);
		Page<Product> productList; 
		String search = (String) session.getAttribute("search"); 
		List<String> selectedSubCategories =  (List<String>) session.getAttribute("selectedSubCategories");
		
		if(search!=null && selectedSubCategories !=null && !search.isEmpty() && !selectedSubCategories.isEmpty()) {
			Specification<Product> finalSpecification = productService.categoryFilterSpecificationCreation(search,
					selectedSubCategories);
	
		    productList = productRepository.findAll(finalSpecification, pageable);
		    model.addAttribute("subCategoryName", selectedSubCategories);
		    
		    searchRequest.setSearch(search);
		    searchRequest.setSelectedValues(selectedSubCategories);
			
		}else if(search!=null && !search.isEmpty()) {
			
			productList = productRepository.findAllByProductNameContainingIgnoreCase(search, pageable);
		    model.addAttribute("search", search);
		    searchRequest.setSearch(search);
		}else if(selectedSubCategories!=null && !selectedSubCategories.isEmpty()) {
			Specification<Product> finalSpecification = productService.categoryFilterSpecificationCreation(selectedSubCategories);
		    productList = productRepository.findAll(finalSpecification, pageable);
		    model.addAttribute("subCategoryName", selectedSubCategories);		    
		    searchRequest.setSelectedValues(selectedSubCategories);
		}else {
			productList = productRepository.findAll(pageable);
		}
		if(page!=0) {
			searchRequest.setPage(page);
		}
		
		model.addAttribute("searchRequest", searchRequest);
	    model.addAttribute("productList", productList);
	    model.addAttribute("categories", categories);
	    return "shop";
	}
	
	@PostMapping("/shop")
	public String shopByFilterAndSearch(@ModelAttribute("searchRequest") RequestData requestData,
								Model model,
								HttpSession session) {
		
		session.removeAttribute("selectedSubCategories");
	    session.removeAttribute("search");
		
		List<Category> categories = categoryService.getAllCategories();
		int page = requestData.getPage();
        int size = requestData.getSize();
        String search = requestData.getSearch();
        List<String> subCategories = requestData.getSelectedValues();        

        RequestData searchRequest = new RequestData();
		
		Pageable pageable = PageRequest.of(page, size);
		Page<Product> productList;
				
		if (search != null && !search.isEmpty() && subCategories != null && !subCategories.isEmpty()) {
			Specification<Product> finalSpecification = productService.categoryFilterSpecificationCreation(search,
					subCategories);
	
		    productList = productRepository.findAll(finalSpecification, pageable);
		    model.addAttribute("subCategoryName", subCategories);
		    
		    searchRequest.setSearch(search);
		    searchRequest.setSelectedValues(subCategories);
		    session.setAttribute("selectedSubCategories", subCategories);
		    session.setAttribute("search", search);
		    
		} else if (search != null && !search.isEmpty()) {
		    productList = productRepository.findAllByProductNameContainingIgnoreCase(search, pageable);
		    model.addAttribute("search", search);
		    
		    searchRequest.setSearch(search);
		    session.setAttribute("search", search);
		    
		} else if (subCategories != null && !subCategories.isEmpty()) {
			Specification<Product> finalSpecification = productService.categoryFilterSpecificationCreation(subCategories);
		    productList = productRepository.findAll(finalSpecification, pageable);
		    model.addAttribute("subCategoryName", subCategories);
		    
		    searchRequest.setSelectedValues(subCategories);
		    session.setAttribute("selectedSubCategories", subCategories);
		    
		} else {
		    productList = productRepository.findAll(pageable);
		}
		
		if(page!=0) {
			searchRequest.setPage(page);
		}
		
		model.addAttribute("searchRequest", searchRequest);
	    model.addAttribute("productList", productList);
	    model.addAttribute("categories", categories);
	    return "shop";
	}
	
//	Product details ------------------------------------------
		
	@GetMapping("/productDetails")
	public String showProductDetails(@RequestParam("id") Integer id,Model model) {
		
		Product p = productRepository.findById(id).get();
		ProductDTO showProduct = productService.convertToProduct(p);
		Integer cartItemQuantity = 1;
		model.addAttribute("product", showProduct);
		model.addAttribute("cartItemQuantity", cartItemQuantity);
		return "productDetails";
	}
	
//	for displaying the data -------------------
	@GetMapping("/showImage/{id}")
	public void showImagePage(@PathVariable("id") Integer id,HttpServletResponse response,ProductImage productImage) throws IOException {
		
		productImage = productImageRepository.findByImageId(id);
		response.setContentType("image/jpg");
		response.getOutputStream().write(productImage.getImage());
		response.getOutputStream().close();
	}
	
	


}

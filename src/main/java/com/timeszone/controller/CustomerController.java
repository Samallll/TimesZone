package com.timeszone.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.model.dto.LoginDTO;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.service.CustomerService;

@RequestMapping("/user")
@Controller
public class CustomerController {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@GetMapping("/profile")
	public String userProfile(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		
		Customer c = customerRepository.findByEmailId(userName);
		CustomerDTO customerData = customerService.convertIntoCustomerDTO(c);
		System.out.println(customerData.getFirstName());
		System.out.println(customerData.getLastName());
		model.addAttribute("customerData", customerData);
		return "userProfile";
	}
	
//	Edit User Data ---------------------------------------------------------------------------------------------------
	@GetMapping("/editCustomer")
	public String customerData(@RequestParam("id") Integer customerId,Model model,HttpSession session) {
		
		Customer c = customerRepository.findById(customerId).get();
		CustomerDTO customerData = customerService.convertIntoCustomerDTO(c);
		
		model.addAttribute("customerData", customerData);
		return "editCustomer";
	}
	
	@PostMapping("/updateCustomer")
	public String updateCustomer(@ModelAttribute("customerData") CustomerDTO editCustomer,HttpSession session) {
		
		Customer c = customerRepository.findById(editCustomer.getCustomerId()).get();
//		Checking whether email or phonenumber changed
		if(!(c.getEmailId().equals(editCustomer.getEmailId())&&c.getPhoneNumber().equals(editCustomer.getPhoneNumber()))) {

			if(customerRepository.findByEmailId(editCustomer.getEmailId())!=null && !(c.getEmailId().equals(editCustomer.getEmailId()))) {
				session.setAttribute("errorMsg", "Email ID exists");
				System.out.println("inside email id exits");
				return "redirect:/user/editCustomer?id="+ c.getCustomerId();
			}
			if(customerRepository.findByPhoneNumber(editCustomer.getPhoneNumber())!=null && !(c.getPhoneNumber().equals(editCustomer.getPhoneNumber()))) {
				session.setAttribute("errorMsg","Phone Number exists");
				System.out.println("inside phone number exits");
				return "redirect:/user/editCustomer?id="+ c.getCustomerId();
			}
			
			c.setEmailId(editCustomer.getEmailId());
			c.setPhoneNumber(editCustomer.getPhoneNumber());
			c.setFirstName(editCustomer.getFirstName());
			c.setLastName(editCustomer.getLastName());
			customerRepository.save(c);
			session.removeAttribute("errorMsg");
			return "redirect:/logout";
		}
		else {
			String firstName = editCustomer.getFirstName();
			String lastName = editCustomer.getLastName();
			c.setFirstName(firstName);
			c.setLastName(lastName);
			customerRepository.save(c);
			session.removeAttribute("errorMsg");
			return "redirect:/user/profile";
		}
		
	}
	
	
//	Edit Customer Password --------------------------------------------------------------------------------------------
	@GetMapping("/changePassword")
	public String changePassword(@RequestParam("id") Integer customerId,Model model,HttpSession session) {
		
		LoginDTO passwordChange = new LoginDTO();
//		Storing customerId in the otp variable to move it into the next step
		passwordChange.setOtp(customerId);
		model.addAttribute("passwordChange", passwordChange);
		
//		Clearing the messages created while changing the password
		session.removeAttribute("passwordChangeError");
		session.removeAttribute("passwordChangeSuccess");
		return "editCustomerPassword";
	}
	
	@PostMapping("/updatePassword")
	public String updatePassword(@ModelAttribute("passwordChange") LoginDTO passwordChange,HttpSession session) {
		
		logger.debug("ChangePassword::updatePassword");
		Integer customerId = passwordChange.getOtp();
		if(!passwordChange.getEmailId().equals(passwordChange.getPassword())) {
			session.setAttribute("passwordChangeError", "Password doesn't match");
			session.removeAttribute("passwordChangeSuccess");
		}
		else {
			session.setAttribute("passwordChangeSuccess", "Password Changed Successfully");
			session.removeAttribute("passwordChangeError");
			Customer editCustomer = customerRepository.findById(customerId).get();
			editCustomer.setPassword(passwordEncoder.encode(passwordChange.getPassword()));
			customerRepository.save(editCustomer);			
		}
		return "redirect:/user/changePassword?id="+customerId;
	}
	
	@GetMapping("/addAddress")
	public String addAddress() {
		return "addAddress";
	}
}
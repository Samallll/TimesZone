package com.timeszone.controller;

import java.security.Principal;
import java.util.List;

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

import com.timeszone.model.customer.Address;
import com.timeszone.model.customer.Customer;
import com.timeszone.model.dto.AddressDTO;
import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.model.dto.LoginDTO;
import com.timeszone.model.product.Product;
import com.timeszone.model.shared.Cart;
import com.timeszone.model.shared.CartItem;
import com.timeszone.repository.AddressRepository;
import com.timeszone.repository.CartItemRepository;
import com.timeszone.repository.CartRepository;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.repository.ProductRepository;
import com.timeszone.service.AddressService;
import com.timeszone.service.CartService;
import com.timeszone.service.CustomerService;

@RequestMapping("/user")
@Controller
public class CustomerController {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CartService cartService;
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@GetMapping("/profile")
	public String userProfile(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		
		Customer c = customerRepository.findByEmailId(userName);
		CustomerDTO customerData = customerService.convertIntoCustomerDTO(c);
		model.addAttribute("customerData", customerData);
		
		List<Address> addressList = addressService.getAllByCustomer(c);
		model.addAttribute("addressList",addressList );
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
	
	
//	Add new address -------------------------------------------------------------------------
	@GetMapping("/addAddress")
	public String addAddress(@RequestParam("id") Integer customerId,Model model) {
		
		AddressDTO newAddress = new AddressDTO();
		newAddress.setCustomerId(customerId);
		model.addAttribute("newAddress", newAddress);
		return "addAddress";
	}
	
	@PostMapping("/newAddress")
	public String newAddress(@ModelAttribute("newAddress") AddressDTO address) {
		
		addressService.newAddress(address.getCustomerId(),address);
		return "redirect:/user/profile";
	}
	
//	Edit Address -----------------------------------------------------------------------------------
	@GetMapping("/editAddress")
	public String editAddress(@RequestParam("id") Integer addressId, Model model) {
		
		Address address = addressRepository.findById(addressId).get();
		AddressDTO editAddress = addressService.convertIntoCustomerDTO(address);
		model.addAttribute("editAddress", editAddress);
		return "editAddress";
	}
	
	@PostMapping("/updateAddress")
	public String updateAddress(@ModelAttribute("editAddress") AddressDTO address) {
		
		addressService.updateAddress(address);
		return "redirect:/user/profile";
	}
	
//	Delete Address ----------------------------------------------------------------------------------
	@GetMapping("/deleteAddress")
	public String deleteAddress(@RequestParam("id") Integer addressId) {
		
		addressService.removeAddress(addressId);
		return "redirect:/user/profile";
	}
	
//	Cart Management =============================================================================================
	
	@GetMapping("/shoppingCart")
	public String shoppingCart(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = customerRepository.findByEmailId(authentication.getName());
		List<CartItem> cartItemList = cartService.getAll(customer.getCart());
		model.addAttribute("cartItemList", cartItemList);
		return "cart";
	}
	
//	adding product to the cart -----------------------------------------------------------
	@GetMapping("/addCart")
	public String addToCart(@RequestParam("id") Integer productId,@RequestParam("quantity") Integer productQuantity,Principal principal) {
		
		
		Product product = productRepository.findById(productId).get();
		CartItem cartItem = new CartItem(product,productQuantity);
//		Cart customerCart = cartService.addCartItem(cartItem,principal);
		
		String username = principal.getName();
		Customer customer = customerRepository.findByEmailId(username);
		
		Cart customerCart = customer.getCart();
		Integer existingCartItemId = cartService.contains(customerCart,cartItem);
		
		if(existingCartItemId!=null) {
			CartItem existingCartItem = cartItemRepository.findById(existingCartItemId).get();
			existingCartItem.setCartItemQuantity(productQuantity + existingCartItem.getCartItemQuantity());
			cartItemRepository.save(existingCartItem);
		}
		else {
			customerCart.getCartItems().add(cartItem);
			cartItem.setCart(customerCart);
			cartRepository.save(customerCart);
			customerRepository.save(customer);
			cartItem.setCart(customerCart);
			product.getCartItems().add(cartItem);
			productRepository.save(product);
		}
		return"redirect:/user/shoppingCart";		
	}
	
	@GetMapping("/deleteCart")
	public String deleteCart(@RequestParam("id") Integer cartItemId) {
		
		CartItem cartItem = cartItemRepository.findById(cartItemId).get(); 
		cartService.deleteCartItem(cartItem);
		return"redirect:/user/shoppingCart";
	}
	
}
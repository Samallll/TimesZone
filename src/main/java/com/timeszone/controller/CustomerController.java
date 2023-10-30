package com.timeszone.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import com.timeszone.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lowagie.text.DocumentException;
import com.razorpay.Order;
import com.timeszone.model.customer.Address;
import com.timeszone.model.customer.Customer;
import com.timeszone.model.dto.AddressDTO;
import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.model.dto.LoginDTO;
import com.timeszone.model.dto.ReturnReasonDTO;
import com.timeszone.model.product.Product;
import com.timeszone.model.shared.Cart;
import com.timeszone.model.shared.CartItem;
import com.timeszone.model.shared.Coupon;
import com.timeszone.model.shared.PaymentMethod;
import com.timeszone.model.shared.PurchaseOrder;
import com.timeszone.model.shared.ReturnReason;
import com.timeszone.model.shared.Wishlist;
import com.timeszone.repository.AddressRepository;
import com.timeszone.repository.CartItemRepository;
import com.timeszone.repository.CartRepository;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.repository.ProductRepository;

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
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private PaymentMethodService paymentMethodService; 
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private PdfService pdfService;
	
	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private EmailSender emailSender;

	
	Logger logger = LoggerFactory.getLogger(MainController.class);

	@GetMapping("/home")
	public String userHome() {
		return "userHome.html";
	}

	@GetMapping("/profile")
	public String userProfile(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		
		Customer c = customerRepository.findByEmailId(userName);
		CustomerDTO customerData = customerService.convertIntoCustomerDTO(c);
		model.addAttribute("customerData", customerData);
		
		List<Address> addressList = addressService.availableAddressByCustomer(c);
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
	public String shoppingCart(Model model,Principal principal,HttpSession session) {
		
		session.removeAttribute("addressId");
		Customer customer = customerRepository.findByEmailId(principal.getName());
		if(customer==null) {
			return "guest/login";
		}
		
		List<CartItem> cartItemList = cartService.getAll(customer.getCart());
		Cart customerCart = customer.getCart();
		List<Coupon> couponList = couponService.getCouponsMatchingCriteria(customerCart.getTotalPrice());
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("couponList", couponList);
		model.addAttribute("customerCart", customerCart);
		return "cart";
	}
	
//	adding product to the cart -----------------------------------------------------------
	@GetMapping("/addCart")
	public String addToCart(@RequestParam("id") Integer productId,
							@RequestParam(name="quantity",required=false,defaultValue="1") Integer productQuantity,
							Principal principal) {
		
		
		Product product = productRepository.findById(productId).get();
		CartItem cartItem = new CartItem(product,productQuantity);
		
		String username = principal.getName();
		Customer customer = customerRepository.findByEmailId(username);
		
		Cart customerCart = customer.getCart();
		Integer existingCartItemId = cartService.contains(customerCart,cartItem);
		
		addProductToCart(productQuantity, product, cartItem, customer, existingCartItemId);
		return"redirect:/user/shoppingCart";		
	}

	private void addProductToCart(Integer productQuantity, 
									Product product,
									CartItem cartItem, 
									Customer customer,
									Integer existingCartItemId) {
		
		Cart customerCart = customer.getCart();
		
		if(existingCartItemId!=null) {
			CartItem existingCartItem = cartItemRepository.findById(existingCartItemId).get();
			 if(existingCartItem.getCartItemQuantity()<existingCartItem.getProduct().getQuantity()) 
			 { 
				 existingCartItem.setCartItemQuantity(productQuantity + existingCartItem.getCartItemQuantity());
				 cartItemRepository.save(existingCartItem); 
			 }
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
	}
	
	@GetMapping("/deleteCart")
	public String deleteCart(@RequestParam("id") Integer cartItemId) {
		
		CartItem cartItem = cartItemRepository.findById(cartItemId).get(); 
		cartService.deleteCartItem(cartItem);
		return"redirect:/user/shoppingCart";
	}
	
//	CheckoutPage ----------------------------------------------
	
	@GetMapping("/checkout")
	public String checkoutPage(Model model,Principal principal,HttpSession session) {
		
		Customer customer = customerRepository.findByEmailId(principal.getName());
		List<Address> addressList = addressService.availableAddressByCustomer(customer);
		List<PaymentMethod> paymentMethodList = paymentMethodService.getAll();
		Double grandTotal;
		
		session.removeAttribute("paymentMethodId");
		Integer couponId = (Integer) session.getAttribute("couponApplied");
		grandTotal = calculateCouponAmount(model, customer, couponId);
		model.addAttribute("grandTotal", grandTotal);
		model.addAttribute("subTotal", customer.getCart().getTotalPrice());
		model.addAttribute("addressList", addressList);
		model.addAttribute("paymentMethodList", paymentMethodList);
		return "checkout";
	}

	private Double calculateCouponAmount(Model model, Customer customer, Integer couponId) {
		Double grandTotal;
		if(couponId!=null) {
			Coupon coupon = couponService.getCoupon(couponId);
			Double couponAmount = customer.getCart().getTotalPrice() * (coupon.getPercentage()/100);
			grandTotal = customer.getCart().getTotalPrice() - (customer.getCart().getTotalPrice() * (coupon.getPercentage()/100));
			model.addAttribute("couponAmount", couponAmount);
		}
		else {
			grandTotal = customer.getCart().getTotalPrice();
			model.addAttribute("couponAmount", 0);
		}
		return grandTotal;
	}
	
	@ResponseBody
	@PostMapping("/shippingAddress")
	public ResponseEntity<Map<String, Object>> addAddress(@RequestBody Map<String, Object> formData,Principal principal) {

	  Map<String, Object> responseMap = new HashMap<>();

	  String fullName = (String) formData.get("fullName");
	  String contactNumber = (String) formData.get("contactNumber");
	  String addressLineOne = (String) formData.get("address1");
	  String addressLineTwo = (String) formData.get("address2");
	  String city = (String) formData.get("city");
	  String state = (String) formData.get("state");
	  String pin = (String) formData.get("zip");
	  Integer zip = pin.isBlank()?null:Integer.parseInt(pin);

	  if (fullName == null || contactNumber == null || addressLineOne == null || addressLineTwo == null ||
	      city == null || state == null || zip == null) {
	    responseMap.put("message", "Please fill the fields.");
	    return new ResponseEntity<>(responseMap, HttpStatus.ACCEPTED);
	  }
	  Customer customer = customerRepository.findByEmailId(principal.getName());
	  
	  Address newAddress = new Address(fullName, contactNumber, addressLineOne, addressLineTwo, city, state, zip,customer);
	  addressService.addAddress(newAddress);

	  responseMap.put("message", "Address added successfully.");
	  return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
	}
	
//	Payment Initialization ------------------------------------------------------------------------
	@ResponseBody
	@PostMapping("/payment")
	public ResponseEntity<Map<String, Object>> proceedToCheckout(@RequestBody Map<String, Object> formData,HttpSession session,Principal principal) {

	  Map<String, Object> response = new HashMap<>();
	  
	  //payment method name
	  String methodId = (String) formData.get("selectedPaymentMethod");
	  String amount = (String) formData.get("grandTotal");
	  Double grandTotal = amount.isBlank()?null:Double.parseDouble(amount);
	  
	  if (methodId == null ) {
	    response.put("message", "Please select a payment method.");
	    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	  }
	  if (session.getAttribute("addressId") == null ) {
	    response.put("message", "Please select a shipping address.");
	    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	  }
	  String userName = principal.getName();
	  Customer customer = customerService.getCustomerByEmailId(userName);
	  session.setAttribute("paymentMethodId", methodId);
	  session.setAttribute("finalAmount", grandTotal);
	  
	  if(methodId.equals("Wallet")) {
		  if(purchaseOrderService.createOrderForWalletTransaction(grandTotal,customer,session,principal)) {
			  response.put("success", "Order has been placed successfully");
		  }
		  else {
			  response.put("message", "Insufficient Balance in Wallet");
		  }
		  return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	  }
	  if(methodId.equals("Cash On Delivery (COD)")) {
		  purchaseOrderService.createOrderForCodTransaction(grandTotal,customer,session,principal);
		  response.put("success", "Order has been placed successfully");
		  return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	  }
	  
	  Order order = purchaseOrderService.createTransaction(grandTotal);
	  
	  response.put("amount", grandTotal);
	  response.put("customer_name", customer.getFirstName());
	  response.put("email_id", customer.getEmailId());
	  response.put("phone_number", customer.getPhoneNumber());
	  response.put("order_id",order.get("id"));
	  response.put("secret_id",purchaseOrderService.getSecretId());
	  response.put("currency",purchaseOrderService.getCurrency());
	  return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
//	Payment Success -----------------------------------------------------------------------
	
	@ResponseBody
	@GetMapping("/paymentSuccess")
	public ResponseEntity<Map<String, Object>> paymentSuccess(@RequestParam(name = "razorPaymentId") String razorPaymentId,Principal principal,
	                                                         @RequestParam(name = "razorSignature") String razorSignature,HttpSession session){
		
		Map<String,Object> response = new HashMap<>();
		boolean transactionCreated = purchaseOrderService.completeTransaction(razorPaymentId,razorSignature);
		if(transactionCreated) {
			
			purchaseOrderService.createOrderAfterRazorPayment(razorPaymentId,razorSignature,session,principal);
			response.put("success", "transaction success");
		}
		else {
			response.put("message", "transactionFailed");
		}
		return ResponseEntity.ok(response);
	}
	
	
//	Order Management ================================================================================
	
	@GetMapping("/viewOrders")
	public String viewOrders(Model model,Principal principal) {
		
		String userName = principal.getName();
		Customer customer = customerRepository.findByEmailId(userName);
		List<PurchaseOrder> orderList = purchaseOrderService.getAllByCustomer(customer);
		if(orderList.isEmpty()) {
			return "redirect:/guest/shop";
		}
		model.addAttribute("orderList", orderList);
		return "viewOrders";
	}
	
	@GetMapping("/orderDetails")
	public String orderDetails(@RequestParam("id") Integer orderId,Model model) {
		
		PurchaseOrder order = purchaseOrderService.getOrder(orderId);
		model.addAttribute("orderItemList", order.getOrderItems());
		model.addAttribute("order", order);
		return "orderDetails";
	}
	
	@GetMapping("order/cancel")
	public ResponseEntity<Map<String,Object>> cancelOrder(@RequestParam("id") Integer orderId) {

		Map<String,Object> response = new HashMap<>();
		purchaseOrderService.cancelOrder(orderId);
		
		response.put("message", "Order Cancelled successfully");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("order/returnReason")
	public String returnOrder(@RequestParam("id") Integer orderId,Model model) {
		
		ReturnReasonDTO reason = new ReturnReasonDTO();
		model.addAttribute("reason", reason);
		model.addAttribute("orderId", orderId);
		return "returnReason";
	}
	
	@PostMapping("order/return/submitRequest")
	public String submitReturnRequest(@ModelAttribute("reason") ReturnReasonDTO reason) {
		
		ReturnReason newReason = new ReturnReason();
		newReason.setComment(reason.getComment());
		newReason.setReturnReason(reason.getReturnReason());
		PurchaseOrder order = purchaseOrderService.getOrder(reason.getOrderId());
		newReason.setOrder(order);
		purchaseOrderService.saveReason(newReason);
		order.setOrderStatus("Requested for Return");
		purchaseOrderService.updateOrder(order);
		return "redirect:/user/viewOrders";
	}
	
//	Download Invoice ----------------------------------
	@GetMapping("invoice/download")
    public ResponseEntity<byte[]> invoice(@RequestParam("id") Integer orderId) throws DocumentException, IOException {

		byte[] pdfBytes = pdfService.createPdf(orderId);
		// Create HTTP response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", UUID.randomUUID().toString()+"invoice.pdf");

        // Return the PDF as a byte array in the response
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
	}
    
//  WishList Management =========================================================================
    @GetMapping("/wishlist")
    public String wishlist(Principal principal,
    						HttpSession session,
    						Model model,
    						@RequestParam(name="id",required=false,defaultValue = "0") Integer productId) {
    	
    	session.removeAttribute("emptyWishlist");
    	String customerName = principal.getName();
    	Customer customer = customerService.getCustomerByEmailId(customerName);
    	Wishlist wishlist = customer.getWishlist();
    	if(wishlist==null) {
    		wishlist = new Wishlist();
    		wishlist.setCustomer(customer);
    		wishlistService.saveWishlist(wishlist);
    	}
    	if(productId!=0) {
    		wishlistService.addProduct(productId, wishlist);
    	}

    	model.addAttribute("wishlist", wishlist);
    	return "wishlist";
    }
    
    @GetMapping("wishlist/delete")
    public String wishlistProductDeletion(@RequestParam("id")Integer productId,Principal principal) {
    	
    	String customerName = principal.getName();
    	Customer customer = customerService.getCustomerByEmailId(customerName);
    	if(customer==null) {
    		throw new UsernameNotFoundException("Customer did not found");
    	}
    	wishlistService.deleteProduct(productId, customer.getWishlist());
    	return "redirect:/user/wishlist";
    }

	@PostMapping("/shareReferralCode")
	@ResponseBody
	public ResponseEntity<String> shareReferralCode(@RequestBody String email,Principal principal) throws MessagingException {

		String customerName = principal.getName();
		Customer customer = customerService.getCustomerByEmailId(customerName);
		String subject = "Hey, Join the Community: " + customer.getFirstName();
		String message = "Sign up to the community - timeszone.shop with the Referral Code :" +
				customer.getReferralCode() +" to get surprising offers!";
		emailSender.sendEmail(email,subject,message);
		return ResponseEntity.ok("Success");
	}

//	Ajax backend methods =============================================================================================
//	==================================================================================================================
	
//	Increment button for quantity page
	@GetMapping("/cart/incrementItem")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> incrementItemQuantity(@RequestParam Integer cartItemId,@RequestParam Integer productQuantity,HttpSession session) {
		
		Map<String, Object> responseMap = new HashMap<>();
		CartItem cartItem = cartItemRepository.findById(cartItemId).get();
		Product product = cartItem.getProduct();
		Integer newQuantity;
		session.removeAttribute("checkOutAmount");
		session.setAttribute("couponApplied", null);
		try {
			if(product!=null) {
				newQuantity = productQuantity +1;
				
				if(product.getQuantity()>=newQuantity) {
					
					Double productAmount;
					if(product.getDiscountedPrice()==0.0) {
						productAmount = newQuantity*product.getPrice();
					}
					else {
						productAmount = newQuantity*product.getDiscountedPrice();
					}
					Double finalAmount = productAmount;
					
					cartItem.setCartItemQuantity(newQuantity);
					cartItemRepository.save(cartItem);
					responseMap.put("newQuantity", newQuantity);
					responseMap.put("productAmount", productAmount);
					finalAmount = cartItem.getCart().getTotalPrice();
					responseMap.put("finalAmount", finalAmount);
					List<Coupon> couponList = couponService.getCouponsMatchingCriteria(finalAmount);
					if(couponList != null) {
						responseMap.put("additionalCoupons", couponList);
					}
					return ResponseEntity.ok(responseMap);
				}
				else {
					responseMap.put("error", "Out of stock");
					return ResponseEntity.ok(responseMap);
				}
				
			}
		}
		catch (Exception e) {
            responseMap.put("error", "Exception happened");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
		responseMap.put("error", "Internal server error.");
		return ResponseEntity.ok(responseMap);
	}
	
//	Decrement button for quantity page
	@GetMapping("/cart/decrementItem")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> decrementItemQuantity(@RequestParam Integer cartItemId,@RequestParam Integer productQuantity,HttpSession session) {

		Map<String, Object> responseMap = new HashMap<>();
		CartItem cartItem = cartItemRepository.findById(cartItemId).get();
		Product product = cartItem.getProduct();
		Integer newQuantity;
		session.removeAttribute("checkOutAmount");
		session.setAttribute("couponApplied", null);
		try {
			
			if(product!=null) {
				if(productQuantity>1) {
					newQuantity = productQuantity - 1;
					Double productAmount;
					if(product.getDiscountedPrice()==0.0) {
						productAmount = newQuantity*product.getPrice();
					}
					else {
						productAmount = newQuantity*product.getDiscountedPrice();
					}
					Double finalAmount = productAmount;
					
					cartItem.setCartItemQuantity(newQuantity);
					cartItemRepository.save(cartItem);
					responseMap.put("newQuantity", newQuantity);
					responseMap.put("productAmount", productAmount);
					finalAmount = cartItem.getCart().getTotalPrice();
					responseMap.put("finalAmount", finalAmount);
					List<Coupon> couponList = couponService.getCouponsMatchingCriteria(finalAmount);
					if(couponList != null) {
						responseMap.put("additionalCoupons", couponList);
					}
					return ResponseEntity.ok(responseMap);
				}
				
			}
		}
		catch (Exception e) {
            responseMap.put("error", "Exception happened");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
		responseMap.put("error", "Internal server error.");
		return ResponseEntity.ok(responseMap);
	}
	
	
//	Apply the coupon
	@GetMapping("/coupon/applyCoupon")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> applyCoupon(@RequestParam Integer couponId,HttpSession session,Principal principal) {

		Map<String, Object> responseMap = new HashMap<>();
		Coupon coupon = couponService.getCoupon(couponId);
		Customer customer = customerRepository.findByEmailId(principal.getName());
		session.removeAttribute("checkOutAmount");
		try {
			
			if(coupon!=null) {
				if(couponService.couoponApplied(customer,coupon)) {
					responseMap.put("error", "The selected coupon has already been applied and can only be used once.");
					return ResponseEntity.ok(responseMap);
				}
				Double cartPrice = customer.getCart().getTotalPrice();
				Double couponAmount = cartPrice * (coupon.getPercentage()/100);
				Double grandTotal = cartPrice - (cartPrice * (coupon.getPercentage()/100));
				session.setAttribute("checkOutAmount", grandTotal);
				session.setAttribute("couponApplied", null);
				responseMap.put("couponAmount", couponAmount);
				responseMap.put("grandTotal", grandTotal);
//				storing the coupon id in the session to proceed with checkout page
				session.setAttribute("couponApplied", couponId);
				return ResponseEntity.ok(responseMap);
			}
		}
		catch (Exception e) {
            responseMap.put("error", "Exception happened");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
		responseMap.put("error", "Internal server error.");
		return ResponseEntity.ok(responseMap);
	}
	
//	Apply the coupon
	@GetMapping("/selectAddress")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> addressSelection(@RequestParam Integer addressId,HttpSession session){
		
		Map<String,Object> responseMap = new HashMap<>();
		session.setAttribute("addressId", addressId);
		return ResponseEntity.ok(responseMap);
	}
}
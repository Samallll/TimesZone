package com.timeszone.service;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.timeszone.model.customer.Address;
import com.timeszone.model.customer.Customer;
import com.timeszone.model.dto.InvoiceDTO;
import com.timeszone.model.dto.OrderDTO;
import com.timeszone.model.product.Product;
import com.timeszone.model.shared.CartItem;
import com.timeszone.model.shared.Coupon;
import com.timeszone.model.shared.OrderItem;
import com.timeszone.model.shared.PaymentMethod;
import com.timeszone.model.shared.PurchaseOrder;
import com.timeszone.model.shared.ReturnReason;
import com.timeszone.repository.OrderItemRepository;
import com.timeszone.repository.PurchaseOrderRepository;
import com.timeszone.repository.ReturnReasonRepository;

@Service
public class PurchaseOrderService {
	
	@Value("${razorpay.keyId}")
    private String razorpayKeyId;

    @Value("${razorpay.keySecret}")
    private String razorpayKeySecret;
    
    @Value("${razorpay.currency}")
    private String currency;
	
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private ReturnReasonRepository reasonRepository;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private PaymentMethodService paymentMethodService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ProductService productService;
	
	@Value("${razorpay.keyId}")
	private String SECRET_ID; 
	
	@Value("${razorpay.keySecret}")
	private String SECRET_KEY;
	
	private String orderId;
	
	public PurchaseOrder getOrder(Integer orderId) {
		return purchaseOrderRepository.findById(orderId).orElseThrow(
	            () -> new RuntimeException("Optional is empty")
		        );
	}
	
//	orderList based on customer
	public List<PurchaseOrder> getAllByCustomer(Customer customer){
		return purchaseOrderRepository.findAllByCustomer(customer);
	}
	
//	list of all orders by payment method
	public List<PurchaseOrder> getAllByPaymentMethod(PaymentMethod paymentMethod){
		return purchaseOrderRepository.findAllByPaymentMethod(paymentMethod);
	}
	
//	list of all orders by order status
	public List<PurchaseOrder> getAllByOrderStatus(String orderStatus){
		return purchaseOrderRepository.findAllByOrderStatus(orderStatus);
	}
	
//	list of all orders before on a particular date
	public List<PurchaseOrder> getAllOrderBefore(LocalDate orderDate){
		List<PurchaseOrder> orderListByDate = new ArrayList<>();
		for(PurchaseOrder order:purchaseOrderRepository.findAll()) {
			if(order.getOrderedDate().isBefore(orderDate)) {
				orderListByDate.add(order);
			}
		}
		return orderListByDate;
	}
	
//	update order status of an order
	public void updateOrderStatus(Integer orderId,String orderStatus) {
		PurchaseOrder order = purchaseOrderRepository.findById(orderId).get();
		order.setOrderStatus(orderStatus);
		purchaseOrderRepository.save(order);
	}
	
//	update order status
	public void updateOrderAddress(Integer orderId,Address address) {
		PurchaseOrder order = purchaseOrderRepository.findById(orderId).get();
		order.setAddress(address);
		purchaseOrderRepository.save(order);
	}
	
//	create new order
	public void createOrder(PurchaseOrder order) {
		purchaseOrderRepository.save(order);
	}
	
//	delete an order by using orderId
	public void deleteOrderById(Integer orderId) {
		purchaseOrderRepository.deleteById(orderId);
	}
	
	public List<PurchaseOrder> getAllOrders(){
		return purchaseOrderRepository.findAll();
	}	
	
//	convert OrderDTO to purchaseOrder
	public PurchaseOrder convertToPurchaseOrder(OrderDTO order) {
		
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		Address address = addressService.getAddress(order.getAddressId());
		PaymentMethod paymentMethod = paymentMethodService.getPaymentMethod(order.getPaymentMethodName());
		Customer customer = customerService.getCustomer(order.getCustomerId());
		purchaseOrder.setAddress(address);
		purchaseOrder.setPaymentMethod(paymentMethod);
		purchaseOrder.setCustomer(customer);
		purchaseOrder.setOrderItems(order.getOrderItems());
		return purchaseOrder;
	}
	
//	convert PurchaseOrder to orderDTO
	public OrderDTO convertToOrderDTO(PurchaseOrder order) {
		
		OrderDTO orderDto = new OrderDTO();
		orderDto.setAddressId(order.getAddress().getAddressId());
		orderDto.setCustomerId(order.getCustomer().getCustomerId());
		orderDto.setOrderAmount(order.getOrderAmount());
		orderDto.setOrderedDate(order.getOrderedDate());
		orderDto.setOrderId(order.getOrderId());
		orderDto.setOrderStatus(order.getOrderStatus());
		orderDto.setOrderItems(order.getOrderItems());
		orderDto.setPaymentMethodName(order.getPaymentMethod().getPaymentMethodName());
		return orderDto;
	}
	
//	sending request to razorpay for order_id creation ------------------------------------------------
	public Order createTransaction(Double amount) {	
		try {
			  RazorpayClient client = new RazorpayClient(SECRET_ID,SECRET_KEY);
			  JSONObject orderRequest = new JSONObject();
			  orderRequest.put("amount", amount * 100); // amount in the smallest currency unit
			  orderRequest.put("currency", "INR");
			  orderRequest.put("receipt", generateRandomString(15));
			  
			  Order order = client.orders.create(orderRequest);
			  orderId = order.get("id");
			  return order;
			
			} catch (RazorpayException e) {
			  // Handle Exception
			  System.out.println(e.getMessage());
			}
		return null;
	}
	
//	random order id generation for this server ---------------------------------------------------------
	private String generateRandomString(int length) {
		
		final String PREFIX = "order_";
	    final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    final SecureRandom random = new SecureRandom();
		
        StringBuilder randomString = new StringBuilder(PREFIX);
        
        for (int i = PREFIX.length(); i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            randomString.append(randomChar);
        }
        
        return randomString.toString();
    }

//	signature verification ---------------------------------------------------------------------------
	public boolean completeTransaction(String razorPaymentId, String razorSignature) {	
		boolean verified = verifyTranscation(razorPaymentId,razorSignature);
		if(verified) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean verifyTranscation(String razorPaymentId, String razorSignature) {	
		try {
			String generatedSignature;
			
			generatedSignature = generateHmacSha256Signature(razorPaymentId,SECRET_KEY);
			if(generatedSignature.equals(razorSignature) ) {
				return true;
			}
		} catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException e) {
			
            e.printStackTrace();
        }
		return false;
	}
	
	private String generateHmacSha256Signature(String razorPaymentId, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException{
		
		Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac.init(secretKeySpec);
        byte[] bytes = hmac.doFinal((orderId + "|" + razorPaymentId).getBytes(StandardCharsets.UTF_8));
        return bytesToHex(bytes);
    }
	
	private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
	
	public String getSecretId() {
		return razorpayKeyId;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void createOrderItem(OrderItem orderItem) {
		orderItemRepository.save(orderItem);
	}
	
//	method to create an order once a payment is successfull  ---------------------------------------
	public void createOrderAfterRazorPayment(String razorPaymentId,String razorSignature,HttpSession session,Principal principal) {
		
		PurchaseOrder newOrder = new PurchaseOrder();
		newOrder.setTranscationId(razorPaymentId);
		createOrder(session,principal,newOrder);	
	}
	
	public List<PurchaseOrder> getOrdersForCustomer(Customer customer){
		return purchaseOrderRepository.findAllByCustomer(customer);
	}
	
//	Order creation based on the payment method ================================================
	
	public void createOrderForCodTransaction(Double grandTotal, Customer customer, HttpSession session,
			Principal principal) {
		
		PurchaseOrder newOrder = new PurchaseOrder();
		createOrder(session,principal,newOrder);
;	}
	
	//order creation for wallet transaction ----------------------------
	public boolean createOrderForWalletTransaction(Double grandTotal, Customer customer,HttpSession session,Principal principal) {
		
		if(customer.getWallet()<grandTotal) {
			return false;
		}
		PurchaseOrder newOrder = new PurchaseOrder();
		createOrder(session,principal,newOrder);
//		reducing the wallet amount
		customer.setWallet(customer.getWallet() - grandTotal);
		return true;
	}
	
//	for creating order for any type of paymentmethod -----------------------
	private void createOrder(HttpSession session,Principal principal,PurchaseOrder newOrder) {
		
		Integer couponId = (Integer) session.getAttribute("couponApplied");
		Integer addressId = (Integer) session.getAttribute("addressId");
		String paymentMethod = (String) session.getAttribute("paymentMethodId");
		Double finalAmount = (Double) session.getAttribute("finalAmount");
		int orderedQuantity = 0;
		
		String userName = principal.getName();
		Customer customer = customerService.getCustomerByEmailId(userName);
		newOrder.setCustomer(customer);
		
		Address address = addressService.getAddress(addressId);
		newOrder.setAddress(address);
		
		PaymentMethod getPaymentMethod = paymentMethodService.getPaymentMethod(paymentMethod);
		newOrder.setPaymentMethod(getPaymentMethod);
		
		newOrder.setOrderedDate(LocalDate.now());
		
		newOrder.setOrderStatus("Pending");
		
		newOrder.setOrderAmount(finalAmount);
		session.removeAttribute("finalAmount");
		
		purchaseOrderRepository.save(newOrder);
		
		if(couponId != null) {
//			update both order and coupon
			Coupon coupon = couponService.getCoupon(couponId);
			newOrder.setCoupon(coupon);
			coupon.getOrdersList().add(newOrder);
			coupon.setUsageCount(coupon.getUsageCount()-1);
			session.removeAttribute("couponApplied");
			couponService.addCoupon(coupon);
		}
		
		getPaymentMethod.getOrders().add(newOrder); 
		paymentMethodService.createPaymentMethod(getPaymentMethod);
		
		customer.getOrders().add(newOrder);
		customerService.customerRepository.save(customer);
		
//		convert cartitems into orderitems only after saving the purchase order
		Product product = new Product();
		List<CartItem> cartItemsList = new ArrayList<>(customer.getCart().getCartItems());
		
		for (CartItem cartItem: cartItemsList) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemCount(cartItem.getCartItemQuantity());
			
			product = cartItem.getProduct();
			//reducing the product quantity
			product.setQuantity(product.getQuantity()-cartItem.getCartItemQuantity());
			productService.modifyProduct(product);
			
			orderItem.setProduct(product);
			orderItem.setOrder(newOrder);
			createOrderItem(orderItem);
			
			//cart items deletion
			cartService.deleteCartItem(cartItem);
			newOrder.getOrderItems().add(orderItem);
			orderedQuantity = orderedQuantity + cartItem.getCartItemQuantity();
		}
		
		newOrder.setOrderedQuantity(orderedQuantity);
		
		purchaseOrderRepository.save(newOrder);	
	}
	
//	canceling a particular order ----------------------------------------
	public void cancelOrder(Integer orderId) {
		
		PurchaseOrder cancelOrder = this.getOrder(orderId);
		List<OrderItem> orderItems = cancelOrder.getOrderItems();
		Customer customer = cancelOrder.getCustomer();
		
		for(OrderItem orderItem:orderItems) {
			Product product = orderItem.getProduct();
			product.setQuantity(product.getQuantity()+orderItem.getOrderItemCount());
			productService.modifyProduct(product);
		}
		
		returnAmount(customer,cancelOrder.getOrderAmount(),cancelOrder.getPaymentMethod());
		cancelOrder.setOrderStatus("Cancelled");
		purchaseOrderRepository.save(cancelOrder);
		
	}
	
	private void returnAmount(Customer customer,Double returnAmount,PaymentMethod paymentMethod) {
		
		if(paymentMethod.getPaymentMethodName().equals("UPI") || paymentMethod.getPaymentMethodName().equals("Wallet")) {
			customer.setWallet(customer.getWallet()+returnAmount);
			customerService.customerRepository.save(customer);
		}
	}
	
//	canceling a particular order ----------------------------------------
	public void returnOrder(Integer orderId) {
		
		PurchaseOrder returnOrder = this.getOrder(orderId);
		List<OrderItem> orderItems = returnOrder.getOrderItems();
		Customer customer = returnOrder.getCustomer();
		
		for(OrderItem orderItem:orderItems) {
			Product product = orderItem.getProduct();
			product.setQuantity(product.getQuantity()+orderItem.getOrderItemCount());
			productService.modifyProduct(product);
		}
		
		returnAmount(customer,returnOrder.getOrderAmount(),returnOrder.getPaymentMethod());
		returnOrder.setOrderStatus("Refunded");
		if(returnOrder.getPaymentMethod().getPaymentMethodName().equals("Cash On Delivery (COD)")) {
			customer.setWallet(customer.getWallet()+returnOrder.getOrderAmount());
			customerService.customerRepository.save(customer);
		}
		purchaseOrderRepository.save(returnOrder);
		
	}
	
	public void saveReason(ReturnReason reason) {
		reasonRepository.save(reason);
	}
	
//	Invoice creation -----------------------------------
	public InvoiceDTO createInvoice(Integer orderId) {
		
		InvoiceDTO invoice = new InvoiceDTO();
		Double couponAmount = (double) 0;
		PurchaseOrder order = purchaseOrderRepository.findById(orderId).get();
		Address address = order.getAddress();
		Coupon coupon = order.getCoupon();
		Customer customer = order.getCustomer();
		
		invoice.setCustomerName(customer.getFirstName()+" "+customer.getLastName());
		invoice.setAddressLineOne(address.getAddressLineOne());
		invoice.setAddressLineOne(address.getAddressLineTwo());
		invoice.setCity(address.getCity());
		invoice.setZipCode(address.getPinCode().toString());
		invoice.setPhoneNumber(address.getContactNumber());
		
		LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String formattedDate = date.format(formatter);
		invoice.setDate(formattedDate);
		
		Random random = new Random();
        int min = 100000000;
        int max = 999999999; 
        int randomNumber = random.nextInt(max - min + 1) + min;
        String randomString = String.valueOf(randomNumber);
		invoice.setInvoiceNumber(randomString);
		invoice.setOrderItems(order.getOrderItems());
		invoice.setOrderedQuantity(order.getOrderedQuantity());
		
		
		if(coupon!=null) {
			couponAmount = (coupon.getPercentage()*order.getOrderAmount())/100;
		}
		invoice.setCouponAmount(couponAmount);
		invoice.setGrandTotal(order.getOrderAmount());
		invoice.setSubTotal(order.getOrderAmount()-couponAmount);
		return invoice;
	}

	public int getNumberOfOrdersForDate(LocalDate date) {
		
		return purchaseOrderRepository.findAllByOrderedDate(date).size();
	}
	
}

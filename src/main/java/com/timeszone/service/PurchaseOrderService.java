package com.timeszone.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.timeszone.model.customer.Address;
import com.timeszone.model.customer.Customer;
import com.timeszone.model.dto.OrderDTO;
import com.timeszone.model.shared.Cart;
import com.timeszone.model.shared.CartItem;
import com.timeszone.model.shared.Coupon;
import com.timeszone.model.shared.OrderItem;
import com.timeszone.model.shared.PaymentMethod;
import com.timeszone.model.shared.PurchaseOrder;
import com.timeszone.repository.OrderItemRepository;
import com.timeszone.repository.PurchaseOrderRepository;

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
	private AddressService addressService;
	
	@Autowired
	private PaymentMethodService paymentMethodService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private CartService cartService;
	
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
			
			System.out.println("generatedSignature: "+ generatedSignature);
			System.out.println("razorSignature: "+ razorSignature);
			System.out.println("orderId: "+orderId);
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
	public void createOrderAfterPayment(String razorPaymentId,String razorSignature,HttpSession session,Principal principal) {
		
		Integer couponId = (Integer) session.getAttribute("couponApplied");
		Integer addressId = (Integer) session.getAttribute("addressId");
		String paymentMethod = (String) session.getAttribute("paymentMethodId");
		Double finalAmount = (Double) session.getAttribute("finalAmount");
		PurchaseOrder newOrder = new PurchaseOrder();
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
		
		newOrder.setTranscationId(razorPaymentId);
		
		purchaseOrderRepository.save(newOrder);
		System.out.println("newOrder saved without saving coupon");
		
		if(couponId != null) {
//			update both order and coupon
			Coupon coupon = couponService.getCoupon(couponId);
			newOrder.setCoupon(coupon);
			coupon.getOrdersList().add(newOrder);
			session.removeAttribute("couponApplied");
			couponService.addCoupon(coupon);
		}
		
		getPaymentMethod.getOrders().add(newOrder);
		paymentMethodService.createPaymentMethod(getPaymentMethod);
		
		customer.getOrders().add(newOrder);
		customerService.customerRepository.save(customer);
		
//		convert cartitems into orderitems only after saving the purchase order
		OrderItem orderItem = new OrderItem();
		List<CartItem> cartItemsList = new ArrayList<>(customer.getCart().getCartItems());
		for (CartItem cartItem: cartItemsList) {
			orderItem.setOrderItemCount(cartItem.getCartItemQuantity());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(newOrder);
			createOrderItem(orderItem);
			//cart items deletion
			cartService.deleteCartItem(cartItem);
			orderedQuantity = orderedQuantity + cartItem.getCartItemQuantity();
		}
		
		newOrder.setOrderedQuantity(orderedQuantity);
		
		purchaseOrderRepository.save(newOrder);	
	}
	
	public List<PurchaseOrder> getOrdersForCustomer(Customer customer){
		return purchaseOrderRepository.findAllByCustomer(customer);
	}
}

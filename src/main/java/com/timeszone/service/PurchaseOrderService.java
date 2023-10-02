package com.timeszone.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import com.timeszone.model.shared.PaymentMethod;
import com.timeszone.model.shared.PurchaseOrder;
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
	private AddressService addressService;
	
	@Autowired
	private PaymentMethodService paymentMethodService;
	
	@Autowired
	private CustomerService customerService;
	
	@Value("${razorpay.keyId}")
	private String SECRET_ID; 
	
	@Value("${razorpay.keySecret}")
	private String SECRET_KEY;
	
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
	
	public Order createTransaction(Double amount) {
		
		try {
			  RazorpayClient client = new RazorpayClient(SECRET_ID,SECRET_KEY);
			  JSONObject orderRequest = new JSONObject();
			  orderRequest.put("amount", amount * 100); // amount in the smallest currency unit
			  orderRequest.put("currency", "INR");
			  orderRequest.put("receipt", generateRandomString(15));
			  
			  Order order = client.orders.create(orderRequest);
			  return order;
			
			} catch (RazorpayException e) {
			  // Handle Exception
			  System.out.println(e.getMessage());
			}
		return null;
	}
	
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
	
}

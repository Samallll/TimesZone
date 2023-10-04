package com.timeszone.model.shared;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.timeszone.model.customer.Address;
import com.timeszone.model.customer.Customer;

@Entity
public class PurchaseOrder {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer orderId;
	
	@OneToOne
    private Address address;
	
	@OneToMany(mappedBy="order",cascade=CascadeType.ALL)
    private List<OrderItem> orderItems= new ArrayList<>();
	
	@ManyToOne
    private Customer customer;
	
	@ManyToOne
    private PaymentMethod paymentMethod;
	
	private LocalDate orderedDate;
	
	private String orderStatus;
	
	private Double orderAmount;

	private String transcationId;
	
	private Integer orderedQuantity;
	
	@ManyToOne
	private Coupon coupon;
	
	public PurchaseOrder( Address address, Customer customer, PaymentMethod paymentMethod,
			LocalDate orderedDate, String orderStatus,Double orderAmount) {
		super();
		this.address = address;
		this.customer = customer;
		this.paymentMethod = paymentMethod;
		this.orderedDate = orderedDate;
		this.orderStatus = orderStatus;
		this.orderAmount = orderAmount;
	}

	public PurchaseOrder(Address address, Customer customer, PaymentMethod paymentMethod, LocalDate orderedDate,
			String orderStatus, Double orderAmount, String transcationId) {
		super();
		this.address = address;
		this.customer = customer;
		this.paymentMethod = paymentMethod;
		this.orderedDate = orderedDate;
		this.orderStatus = orderStatus;
		this.orderAmount = orderAmount;
		this.transcationId = transcationId;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public PurchaseOrder() {
		super();
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}


	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getTranscationId() {
		return transcationId;
	}

	public void setTranscationId(String transcationId) {
		this.transcationId = transcationId;
	}

	public LocalDate getOrderedDate() {
		return orderedDate;
	}

	public void setOrderedDate(LocalDate orderedDate) {
		this.orderedDate = orderedDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getTotalOrderedQuantity() {
		
		Integer count=0;
		for(OrderItem orderItem:this.orderItems) {
			count = orderItem.getOrderItemCount()+count;
		}
		return count;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public Integer getOrderedQuantity() {
		return orderedQuantity;
	}

	public void setOrderedQuantity(Integer orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}
	
	public double getActualAmount() {
        double totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getProduct().getPrice() * orderItem.getOrderItemCount();
        }
        return totalPrice;
    }
	
}

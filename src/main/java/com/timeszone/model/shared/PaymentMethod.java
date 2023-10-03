package com.timeszone.model.shared;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class PaymentMethod {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer paymentMethodId;
	
	private boolean isEnabled=true;
	
	@Column(unique=true,nullable=true)
	private String paymentMethodName;
	
	@OneToMany(mappedBy="paymentMethod")
	private List<PurchaseOrder> orders;

	public Integer getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(Integer paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public boolean  getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}


	public PaymentMethod() {
		super();
	}

	public PaymentMethod(String paymentMethodName) {
		super();
		this.paymentMethodName = paymentMethodName;
	}

	public List<PurchaseOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<PurchaseOrder> orders) {
		this.orders = orders;
	}
	
	
}

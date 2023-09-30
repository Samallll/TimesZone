package com.timeszone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.shared.PaymentMethod;
import com.timeszone.model.shared.PurchaseOrder;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod,Integer>{
	
	PaymentMethod findByPaymentMethodName(String paymentMethodName);
	List<PaymentMethod> findAllByIsEnabled(boolean isEnabled);
	List<PurchaseOrder> findAllByPaymentMethodName(String paymentMethodName);
}

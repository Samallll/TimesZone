package com.timeszone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.shared.PaymentMethod;
import com.timeszone.model.shared.PurchaseOrder;
import com.timeszone.repository.PaymentMethodRepository;

@Service
public class PaymentMethodService {
	
	@Autowired
	public PaymentMethodRepository paymentMethodRepository;
	
	public PaymentMethod getPaymentMethod(String paymentMethodName) {
		return paymentMethodRepository.findByPaymentMethodName(paymentMethodName);
	}
	
	public PaymentMethod getPaymentMethod(Integer paymentMethodId) {
		return paymentMethodRepository.findById(paymentMethodId).get();
	}
	
	public PaymentMethod createPaymentMethod(PaymentMethod paymentMethod) {
		return paymentMethodRepository.save(paymentMethod);
	}
	
	public void editPaymentMethod(Integer paymentMethodId,PaymentMethod editMethod) {
		
		PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).get();
		paymentMethod.setEnabled(editMethod.getIsEnabled());
		paymentMethod.setPaymentMethodName(editMethod.getPaymentMethodName());
		paymentMethodRepository.save(paymentMethod);
	}
	
	public void deletePaymentMethod(Integer paymentMethodId) {
		paymentMethodRepository.deleteById(paymentMethodId);
	} 
	
	public List<PurchaseOrder> getAllOrdersByPaymentMethod(String paymentMethodName){
		return paymentMethodRepository.findAllByPaymentMethodName(paymentMethodName);
	}
	
	public List<PaymentMethod> getAll(){
		return paymentMethodRepository.findAll();
	}
	
	public boolean contains(String methodName) {
		
		for (PaymentMethod p:paymentMethodRepository.findAll()) {

			if(p.getPaymentMethodName().equals(methodName)) {
				return true;
			}
		}
		return false;
	}
}

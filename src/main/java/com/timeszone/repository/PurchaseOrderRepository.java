package com.timeszone.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.shared.PaymentMethod;
import com.timeszone.model.shared.PurchaseOrder;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Integer>{
	
	List<PurchaseOrder> findAllByCustomer(Customer customer);
	List<PurchaseOrder> findAllByPaymentMethod(PaymentMethod paymentMethod);
	List<PurchaseOrder> findAllByOrderStatus(String orderStatus);
	List<PurchaseOrder> findAllByOrderedDate(LocalDate date);
	Page<PurchaseOrder> findByOrderStatusNotIn(List<String> statusList, Pageable pageable);
	Page<PurchaseOrder> findAllByOrderStatus(Pageable pageableReturnApproved, String orderStatus);
}

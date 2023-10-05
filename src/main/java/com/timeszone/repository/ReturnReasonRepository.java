package com.timeszone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.shared.PurchaseOrder;
import com.timeszone.model.shared.ReturnReason;

@Repository
public interface ReturnReasonRepository extends JpaRepository<ReturnReason,Integer>{
	
	ReturnReason findByOrder(PurchaseOrder order);
}

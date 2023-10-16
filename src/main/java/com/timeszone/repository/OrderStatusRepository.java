package com.timeszone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.shared.OrderStatus;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus,Integer> {
	
	OrderStatus findByOrderStatusName(String statusName);
	List<OrderStatus> findAllByOrderStatusName(String statusName);
}

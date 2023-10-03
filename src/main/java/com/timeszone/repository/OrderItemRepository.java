package com.timeszone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.shared.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Integer>{

}

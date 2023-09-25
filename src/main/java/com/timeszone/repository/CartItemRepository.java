package com.timeszone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.shared.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer>{

}

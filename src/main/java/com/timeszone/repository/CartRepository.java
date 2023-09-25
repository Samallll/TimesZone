package com.timeszone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.shared.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer>{

}

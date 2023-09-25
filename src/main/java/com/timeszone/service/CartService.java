package com.timeszone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.customer.CartItem;
import com.timeszone.repository.CartItemRepository;
import com.timeszone.repository.CartRepository;

@Service
public class CartService {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	public void addCartItem(CartItem cartItem) {
		
	}
	
	public void deleteCartItem(CartItem cartItem) {
		
	}
	
	public void updateCartItem(CartItem cartItem) {
		
	}

}

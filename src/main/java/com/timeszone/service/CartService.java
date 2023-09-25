package com.timeszone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.shared.Cart;
import com.timeszone.model.shared.CartItem;
import com.timeszone.repository.CartItemRepository;
import com.timeszone.repository.CartRepository;
import com.timeszone.repository.CustomerRepository;

@Service
public class CartService {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public Cart addCartItem(CartItem cartItem) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Customer customer = customerRepository.findByEmailId(username);
		
		Cart customerCart = customer.getCart();
		customerCart.getCartItems().add(cartItem);
		cartRepository.save(customerCart);
		customerRepository.save(customer);
		
		return customerCart;
		
	}
	
	public void deleteCartItem(CartItem cartItem) {
		
	}
	
	public void updateCartItem(CartItem cartItem) {
		
	}

}

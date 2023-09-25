package com.timeszone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.product.Product;
import com.timeszone.model.shared.Cart;
import com.timeszone.model.shared.CartItem;
import com.timeszone.repository.CartItemRepository;
import com.timeszone.repository.CartRepository;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.repository.ProductRepository;

@Service
public class CartService {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	public Cart addCartItem(CartItem cartItem) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Customer customer = customerRepository.findByEmailId(username);
		
		Cart customerCart = customer.getCart();
		customerCart.getCartItems().add(cartItem);
		cartItem.setCart(customerCart);
		cartRepository.save(customerCart);
		customerRepository.save(customer);
		 
		return customerCart;
		
	}
	
	public void deleteCartItem(CartItem cartItem) {
		
		Product product = cartItem.getProduct();
		Cart cart = cartItem.getCart();
		
		product.getCartItems().remove(cartItem);
		cart.getCartItems().remove(cartItem);
		
		productRepository.save(product);
		cartRepository.save(cart);
		
		cartItemRepository.delete(cartItem);
	}
	
	public void updateCartItem(CartItem cartItem) {
		
	}
	
	public List<CartItem> getAll(Cart cart){
		
		List<CartItem> cartItemList = cartItemRepository.findAllByCart(cart);
		return cartItemList;
	}

}

package com.timeszone.service;

import java.security.Principal;
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

	public Integer contains(Cart customerCart, CartItem cartItem) {
		
		Integer cartItemId = null;
		for(CartItem ci:customerCart.getCartItems()) {
			if(ci.getProduct().getProductName().equals(cartItem.getProduct().getProductName())) {
				cartItemId = ci.getCartItemId();
				break;
			}
		}
	
		return cartItemId;
	}

}

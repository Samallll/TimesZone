package com.timeszone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.product.Product;
import com.timeszone.model.shared.Wishlist;
import com.timeszone.repository.WishlistRepository;

@Service
public class WishlistService {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private WishlistRepository wishlistRepository;
	
	public void addProduct(Integer productId,Wishlist wishlist) {
		Product product = productService.getProduct(productId);
		product.getWishlist().add(wishlist);
		productService.saveProduct(product);
		wishlist.getProducts().add(product);
		wishlistRepository.save(wishlist);
	}
	
	public void deleteProduct(Integer productId,Wishlist wishlist) {
		Product product = productService.getProduct(productId);
		product.getWishlist().remove(wishlist);
		productService.saveProduct(product);
		wishlist.getProducts().remove(product);
		wishlistRepository.save(wishlist);
	}
	
	public Wishlist getWishlistByCustomer(Customer customer) {
		
		return wishlistRepository.findByCustomer(customer);
	}
	
	public void saveWishlist(Wishlist wishlist) {
		wishlistRepository.save(wishlist);
	}
}

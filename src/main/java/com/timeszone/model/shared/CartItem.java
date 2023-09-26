package com.timeszone.model.shared;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.timeszone.model.product.Product;

@Entity
public class CartItem {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",unique=true)
    private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

	private Integer cartItemQuantity;

	public CartItem(Integer cartItemId, Product product, Cart cart, Integer cartItemQuantity) {
		super();
		this.cartItemId = cartItemId;
		this.product = product;
		this.cart = cart;
		this.cartItemQuantity = cartItemQuantity;
	}

	public CartItem(Product product, Integer cartItemQuantity) {
		super();
		this.product = product;
		this.cartItemQuantity = cartItemQuantity;
	}

	public CartItem() {
		super();
	}

	public Integer getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(Integer cartItemId) {
		this.cartItemId = cartItemId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Integer getCartItemQuantity() {
		return cartItemQuantity;
	}

	public void setCartItemQuantity(Integer cartItemQuantity) {
		this.cartItemQuantity = cartItemQuantity;
	}
	
	
	
}

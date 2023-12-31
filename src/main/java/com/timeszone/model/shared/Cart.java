package com.timeszone.model.shared;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.timeszone.model.customer.Customer;


@Entity
public class Cart {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private Set<CartItem> cartItems = new HashSet<>();
    
    private Double finalAmount;

    public Cart() {
    }

    public Cart(Customer customer) {
        this.customer = customer;
        this.cartItems = new HashSet<>();
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(Double finalAmount) {
		this.finalAmount = finalAmount;
	}
	
	public double getTotalPrice() {
        Double totalPrice = 0.0;
        Double price = 0.0;
        for (CartItem cartItem : cartItems) {
        	price = cartItem.getProduct().getDiscountedPrice()==0.0?cartItem.getProduct().getPrice():cartItem.getProduct().getDiscountedPrice();
            totalPrice = totalPrice + (price * cartItem.getCartItemQuantity());
        }
        return totalPrice;
    }
}

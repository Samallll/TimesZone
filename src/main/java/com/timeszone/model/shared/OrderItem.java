package com.timeszone.model.shared;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.timeszone.model.product.Product;

@Entity
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer orderItemId;
	
	private Integer orderItemCount;
	
	@ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

	
	@ManyToOne
    @JoinColumn(name = "order_id")
    private PurchaseOrder order;

	public Integer getOrderItemCount() {
		return orderItemCount;
	}

	public void setOrderItemCount(Integer orderItemCount) {
		this.orderItemCount = orderItemCount;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


	public PurchaseOrder getOrder() {
		return order;
	}

	public void setOrder(PurchaseOrder order) {
		this.order = order;
	}

	public OrderItem(Integer orderItemCount, Product product, String strapColor, PurchaseOrder order) {
		super();
		this.orderItemCount = orderItemCount;
		this.product = product;
		this.order = order;
	}

	public OrderItem() {
		super();
	}

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

}

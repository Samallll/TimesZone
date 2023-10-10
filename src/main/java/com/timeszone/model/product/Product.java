package com.timeszone.model.product;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.timeszone.model.shared.CartItem;
import com.timeszone.model.shared.OrderItem;
import com.timeszone.model.shared.Wishlist;


@Entity
@Table(name="products")
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer productId;
	
	@Column(name="productName",nullable=false)
	private String productName;
	
	@Column(name="description",nullable=false)
	private String description;
	
	@Column(name="caseSize",nullable=false)
	private Double caseSize;
	
	@Column(name="price",nullable=false)
	private Double price;
	
	@ManyToMany(mappedBy = "products", cascade = CascadeType.MERGE)
    private Set<Category> categories;
	
	@OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;
	
	@ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "product_subcategories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id"))
    private Set<SubCategory> subcategories = new HashSet<>();

	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<ProductImage> productImages;
	
	@Column(name="isEnabled",nullable=false)
	private boolean isEnabled=true;
	
	@Column(name="dateAdded",nullable=false)
	private LocalDate dateAdded;

	@OneToMany(mappedBy = "product")
    private Set<CartItem> cartItems = new HashSet<>();
	
	@Column(name="quantity",nullable=false)
	private Integer quantity;
	
	@ManyToMany(mappedBy = "products")
    private Set<Wishlist> wishlist = new HashSet<>();
	
	public Product() {
		super();
	}

	public Product(String productName, String description,Integer quantity, Double caseSize, Double price,
			Set<Category> categories, Set<SubCategory> subcategories, List<ProductImage> productImages,
			LocalDate dateAdded) {
		super();
		this.productName = productName;
		this.quantity = quantity;
		this.description = description;
		this.caseSize = caseSize;
		this.price = price;
		this.categories = categories;
		this.subcategories = subcategories;
		this.productImages = productImages;
		this.dateAdded = dateAdded;
	}

	public Product(Integer productId, String productName, String description, Double caseSize,
			Double price, Set<Category> categories, Set<SubCategory> subcategories, List<ProductImage> productImages,
			boolean isEnabled, LocalDate dateAdded) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.description = description;
		this.caseSize = caseSize;
		this.price = price;
		this.categories = categories;
		this.subcategories = subcategories;
		this.productImages = productImages;
		this.isEnabled = isEnabled;
		this.dateAdded = dateAdded;
	}

	public Set<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getCaseSize() {
		return caseSize;
	}

	public void setCaseSize(Double caseSize) {
		this.caseSize = caseSize;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<SubCategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(Set<SubCategory> subcategories) {
		this.subcategories = subcategories;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public boolean getIsEnabled() {
		return isEnabled();
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public LocalDate getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Set<Wishlist> getWishlist() {
		return wishlist;
	}

	public void setWishlist(Set<Wishlist> wishlist) {
		this.wishlist = wishlist;
	}
	
	
}

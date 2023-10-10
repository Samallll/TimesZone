package com.timeszone.model.customer;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.timeszone.model.shared.Cart;
import com.timeszone.model.shared.Coupon;
import com.timeszone.model.shared.PurchaseOrder;
import com.timeszone.model.shared.Wishlist;

@Entity
@Table(name="Customer")
public class Customer implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="customerId")
	private Integer customerId;

	@Column(name="firstName",nullable=false)
	private String firstName;
	
	@Column(name="lastName",nullable=false)
	private String lastName;
	
	@Column(name="emailId",nullable=false,unique=true)
	private String emailId;
	
	@Column(name="phoneNumber",nullable=false,unique=true)
	private String phoneNumber;
	
	@Column(name="password",nullable=false)
	private String password;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinColumn(name = "roleId")
	private Set<Role> authorities;
	
	@OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Address> addresses;
	
	@Column(name="isLocked")
	private boolean isLocked=false;
	
	@Column(name="otp",nullable=true)
	private Integer otp;
	
	@Column(name="expirationTime",nullable=true)
	private LocalDateTime expirationTime;
	
	@OneToOne(mappedBy = "customer")
    private Cart cart;
	
	private Double wallet = 0.0;
	
	@OneToOne(mappedBy = "customer",cascade=CascadeType.ALL)
    private Wishlist wishlist = new Wishlist();
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "customer_coupon",
               joinColumns = @JoinColumn(name = "customer_id"),
               inverseJoinColumns = @JoinColumn(name = "coupon_id"))
    private Set<Coupon> coupons = new HashSet<>();
	
	@OneToMany(mappedBy="customer",cascade=CascadeType.REMOVE)
	private List<PurchaseOrder> orders;
	
	public Customer() {
		super();
		this.cart = new Cart(this);
	}
	
	public Customer(String firstName, String lastName, String emailId, String phoneNumber,
			String password, Set<Role> authorities, Cart cart) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.authorities = authorities;
		this.cart = cart;
	}

	public Customer(Integer customerId, String firstName, String lastName, String emailId, String phoneNumber,
			String password, Set<Role> authorities, List<Address> addresses) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.authorities = authorities;
		this.addresses = addresses;
	}

	public Customer(String firstName, String lastName, String emailId, String phoneNumber,
			String password, Set<Role> authorities) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}
	
	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.getEmailId();
	}
	
	public String getEmailId() {
		// TODO Auto-generated method stub
		return this.emailId;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public Integer getCustomerId() {
		return this.customerId;
	}
	
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Set<Role> authorities) {
		this.authorities = authorities;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public LocalDateTime getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(LocalDateTime expirationTime) {
		this.expirationTime = expirationTime;
	}


	public List<PurchaseOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<PurchaseOrder> orders) {
		this.orders = orders;
	}

	public List<Address> getAddresses() {
		// TODO Auto-generated method stub
		return this.addresses;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !isLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void allAuthorities() {
		// TODO Auto-generated method stub
		System.out.println("Authorities=");
		for(Role role:authorities) {
			System.out.println(role.getAuthority());
		}
	}
	
	public void allAddresses() {
		// TODO Auto-generated method stub
		System.out.println("Addresses=");
		for(Address address:addresses) {
			address.toString();
		}
	}

	public Set<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	public Double getWallet() {
		return wallet;
	}

	public void setWallet(Double wallet) {
		this.wallet = wallet;
	}

	public Wishlist getWishlist() {
		return wishlist;
	}

	public void setWishlist(Wishlist wishlist) {
		this.wishlist = wishlist;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", emailId=" + emailId + ", phoneNumber=" + phoneNumber + ", password=" + password + ", authorities="
				+ authorities  + ", isLocked=" + isLocked + ", otp=" + otp
				+ ", expirationTime=" + expirationTime + "]";
	}
}

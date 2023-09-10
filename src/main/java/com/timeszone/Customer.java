package com.timeszone;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name = "addressId")
	private Set<Address> addresses;
	
	@Column(name="isLocked")
	private boolean isLocked=false;
	
	@Column(name="otp",nullable=true)
	private Integer otp;
	
	@Column(name="expirationTime",nullable=true)
	private Date expirationTime;
	
	public Customer() {
		super();
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

	public Customer(Integer customerId, String firstName, String lastName, String emailId, String phoneNumber,
			String password, Set<Role> authorities, Set<Address> addresses) {
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
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.firstName;
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

	public void setAddresses(Set<Address> addresses) {
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

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Set<Address> getAddresses() {
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

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", emailId=" + emailId + ", phoneNumber=" + phoneNumber + ", password=" + password + ", authorities="
				+ authorities + ", addresses=" + addresses + ", isLocked=" + isLocked + ", otp=" + otp
				+ ", expirationTime=" + expirationTime + "]";
	}
}

package com.timeszone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.customer.Address;
import com.timeszone.model.customer.Customer;
import com.timeszone.model.dto.AddressDTO;
import com.timeszone.repository.AddressRepository;
import com.timeszone.repository.CustomerRepository;

@Service
public class AddressService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	public void newAddress(Integer customerId, AddressDTO address) {
		
		Customer customer = customerRepository.findById(customerId).get();
		
		Address newAddress = new Address();
		newAddress.setAddressLineOne(address.getAddressLineOne());
		newAddress.setAddressLineTwo(address.getAddressLineTwo());
		newAddress.setCity(address.getCity());
		newAddress.setState(address.getState());
		newAddress.setIsDefault(address.getIsDefault());
		newAddress.setPinCode(address.getPinCode());
		newAddress.setCustomer(customer);
		addressRepository.save(newAddress);
		customer.getAddresses().add(newAddress);
		customerRepository.save(customer);
	}

	public List<Address> getAllByCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return addressRepository.findAllByCustomer(customer);
	}

	public AddressDTO convertIntoCustomerDTO(Address address) {
		
		AddressDTO newAddress = new AddressDTO();
		newAddress.setAddressId(address.getAddressId());
		newAddress.setAddressLineOne(address.getAddressLineOne());
		newAddress.setCity(address.getCity());
		newAddress.setAddressLineTwo(address.getAddressLineTwo());
		newAddress.setPinCode(address.getPinCode());
		newAddress.setIsDefault(address.getIsDefault());
		newAddress.setCustomerId(address.getCustomer().getCustomerId());
		newAddress.setState(address.getState());
		return newAddress;
	}

	public void updateAddress(AddressDTO address) {
		
		Address editAddress = addressRepository.findById(address.getAddressId()).get();
		editAddress.setAddressLineOne(address.getAddressLineOne());
		editAddress.setCity(address.getCity());
		editAddress.setAddressLineTwo(address.getAddressLineTwo());
		editAddress.setPinCode(address.getPinCode());
		editAddress.setIsDefault(address.getIsDefault());
		editAddress.setState(address.getState());
		addressRepository.save(editAddress);
		
	}

	public void removeAddress(Integer addressId) {
		
		Address deleteAddress = addressRepository.findById(addressId).get();
		Customer customer = deleteAddress.getCustomer();
		customer.getAddresses().remove(deleteAddress);
		customerRepository.save(customer);
		addressRepository.delete(deleteAddress);
	}

}

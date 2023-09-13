package com.timeszone.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.model.product.Product;
import com.timeszone.repository.ProductRepository;
import com.timeszone.service.CustomerService;
import com.timeszone.service.ProductService;

@RequestMapping("/admin")
@Controller
public class AdminController {
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	
	
	@GetMapping("/")
	public String adminHome() {
		return "adminHome.html";
	}
	
//	For User management ---------------------------------------------------------------------
	@GetMapping("/user_management")
	public String userManagementPage(Model model) {
		logger.info("InSide User Management Controller");
//		To hold the list of users
		List<CustomerDTO> userList = customerService.getAllUsers();
		model.addAttribute("userList", userList);
		return "userManagement.html";
	}
	
	
//	Lock Management -------------------------------------------------------------------------
	@GetMapping("/block/{id}")
	public String blockUser(@PathVariable Integer id) {
		logger.trace("InSide Locking Controller");
		customerService.lockUser(id);
		logger.info("Locked User");
		return "redirect:/admin/user_management";
	}
	
	@GetMapping("/unBlock/{id}")
	public String unblockUser(@PathVariable Integer id) {
		logger.trace("InSide Unlocking Controller");
		customerService.unLockUser(id);
		logger.info("UnLocked User");
		return "redirect:/admin/user_management";
	}
	
	
//	Product Management ----------------------------------------------------------------------
	@GetMapping("/product_management")
	public String productManagementPage(Model model) {
		logger.trace("InSide Product Management Controller");
//		To hold the data
		List<Product> productList = productService.getAllProducts();
		model.addAttribute("productList", productList);
		return "productManagement.html";
	}
	
//	Add Product -------------
	@GetMapping("/addProduct")
	public String addProductPage(Model model) {
		logger.trace("InSide Add Product Controller");	
//		To hold the data
		Product newProduct = new Product();
		model.addAttribute("newProduct", newProduct);
		return "addProduct.html";
	}
	
	@PostMapping("/registerProduct")
	public String addProduct(@ModelAttribute("newProduct") Product p) {
		logger.trace("InSide Product Registering Controller");
		Product registerProduct = new Product(p.getProductId(), p.getProductName(), p.getDescription(), p.getQuantity(), p.getCaseSize(),
				p.getPrice(), LocalDate.now());
		productRepository.save(registerProduct);
		return "redirect:/admin/product_management";
	}
	
//	Edit Product -------------
	@GetMapping("/editProduct/{id}")
	public String editProductPage(@PathVariable Integer id,Model model) {
		logger.trace("InSide Edit Product Controller");
		Product editProduct = productRepository.findById(id).get();
		model.addAttribute("editProduct", editProduct);
		return "editProductTest.html";
	}
	
	@PostMapping("/{id}")
	public String editProduct(@PathVariable Integer id,@ModelAttribute("editProduct") Product ep) {
		logger.trace("InSide Product Editing Controller");
		productService.updateProduct(id,ep.getProductName(),ep.getCaseSize(),ep.getDescription(),ep.getIsEnabled(),ep.getPrice(),ep.getQuantity());
		return "redirect:/admin/product_management";
	}
	
//	Delete Product -------------
	@GetMapping("/deleteProduct/{id}")
	public String deletrProduct(@PathVariable Integer id) {
		logger.trace("InSide Delete Product Controller");
		productService.deleteProduct(id);
		return "redirect:/admin/product_management";
	}

	
	
	
	
	
}

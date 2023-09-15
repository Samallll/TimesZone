package com.timeszone.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.timeszone.model.dto.CategoryRegistrationDTO;
import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.model.product.Category;
import com.timeszone.model.product.Product;
import com.timeszone.model.product.SubCategory;
import com.timeszone.repository.CategoryRepository;
import com.timeszone.repository.ProductRepository;
import com.timeszone.service.CategoryService;
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
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
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
		Product registerProduct = new Product( p.getProductName(), p.getDescription(), p.getQuantity(), p.getCaseSize(),
				p.getPrice(), LocalDate.now());
		productRepository.save(registerProduct);
		return "redirect:/admin/product_management";
	}
	
//	Edit Product -------------
	@GetMapping("/editProduct/{id}")
	public String editProductPage(@PathVariable Integer id,Model model) {
		logger.info("InSide Edit Product Controller");
		Product editProduct = productRepository.findById(id).get();
		model.addAttribute("newProduct", editProduct);
		return "editProduct.html";
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

	
//	Category Management ----------------------------------------------------------------------
	
	@GetMapping("/category_management")
	public String categoryManagementPage(Model model) {
		logger.trace("InSide Category Management Controller");
//		To hold the data
		List<Category> categoryList = categoryService.getAllCategories();
		model.addAttribute("categoryList", categoryList);
		return "categoryManagement.html";
	}
	
//	Add Product -------------
	@GetMapping("/addCategory")
	public String addCategoryPage(Model model) {
		logger.info("InSide Add Category Controller");	
//		To hold the data
		CategoryRegistrationDTO newCategory = new CategoryRegistrationDTO();
		model.addAttribute("newCategory", newCategory);
		return "addCategory.html";
	}
	
	@PostMapping("/registerCategory")
	public String addCategory(@ModelAttribute("newCategory") CategoryRegistrationDTO c) {
		logger.trace("InSide Category Registering Controller");
		
		Category registerCategory = new Category();
		registerCategory.setCategoryName(c.getCategoryName());
		registerCategory.setDescription(c.getDescription());
		registerCategory.setNoOfSubCategories(c.getNoOfSubCategories());
		categoryRepository.save(registerCategory);
		return "redirect:/admin/category_management";
	}
	
	
	@GetMapping("/addSubCategory/{id}")
	public String addSubCategoryPage(@PathVariable Integer id,Model model) {
		logger.info("InSide Add Category Controller");	
//		To hold the data
		Category newCategory = categoryRepository.findById(id).get();
		model.addAttribute("newCategory", newCategory);
		return "addSubCategory.html";
	}
	
	@PostMapping("/registerSubCategory")
	public String addSubCategory(@ModelAttribute("newCategory") Category c) {
		logger.trace("InSide Sub Category Registering Controller");
		
		Category existingCategory = categoryRepository.findById(c.getCategoryId()).get();
        List<SubCategory> subcategories = c.getSubcategories();

     // Set the category property on each subcategory
        for (SubCategory subcategory : subcategories) {
          subcategory.setCategory(existingCategory);
        }
        
        existingCategory.setSubcategories(subcategories);
        
        categoryRepository.save(existingCategory);
        
		return "redirect:/admin/category_management";
	}
	
////	Edit Product -------------
//	@GetMapping("/editCategory/{id}")
//	public String editCategoryPage(@PathVariable Integer id,Model model) {
//		logger.info("InSide Edit Category Controller");
//		Category editCategory = categoryRepository.findById(id).get();
//		model.addAttribute("newCategory", editCategory);
//		return "editCategory.html";
//	}
	
//	@PostMapping("/{id}")
//	public String editCategory(@PathVariable Integer id,@ModelAttribute("editCategory") Category ec) {
//		logger.trace("InSide Category Editing Controller");
//		categoryService.updateProduct(id,ec.getProductName(),ec.getCaseSize(),ec.getDescription(),ec.getIsEnabled(),ec.getPrice(),ec.getQuantity());
//		return "redirect:/admin/category_management";
//	}
	
//	Delete Product -------------
//	@GetMapping("/deleteProduct/{id}")
//	public String CategoryProduct(@PathVariable Integer id) {
//		logger.trace("InSide Delete Category Controller");
//		categoryService.deleteCategory(id);
//		return "redirect:/admin/category_management";
//	}

	
	
	
}

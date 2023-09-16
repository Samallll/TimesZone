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

import com.timeszone.model.Customer;
import com.timeszone.model.dto.CategoryRegistrationDTO;
import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.model.dto.SearchCustomerDTO;
import com.timeszone.model.product.Category;
import com.timeszone.model.product.Product;
import com.timeszone.model.product.SubCategory;
import com.timeszone.repository.CategoryRepository;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.repository.ProductRepository;
import com.timeszone.repository.SubCategoryRepository;
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
	private CustomerRepository customerRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
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
		SearchCustomerDTO searchKey = new SearchCustomerDTO();
		model.addAttribute("userList", userList);
		model.addAttribute("searchKey", searchKey);
		return "userManagement.html";
	}
	
//	User Search ------------------
	@PostMapping("/admin/searchUser")
	public String searchUser(@ModelAttribute("searchKey") SearchCustomerDTO c,Model model) {
			
		logger.debug("Inside Searching Controller");
		System.out.println("Inside search controller");
		List<Customer> customerList = customerRepository.findAllByFirstName(c.getFirstName());
//		List<Customer> customerListByPhoneNumber = customerRepository.findAllByPhoneNumber(c.getFirstName());
//		List<Customer> customerListByEmailId = customerRepository.findAllByEmailId(c.getFirstName());
		
//		customerList.addAll(customerListByEmailId);
//		customerList.addAll(customerListByPhoneNumber);
		if(customerList.isEmpty()) {
			return "noResult.html";
		}
		else {
			logger.trace("User data found");
			model.addAttribute("customerList", customerList);
			return "searchResultCustomer.html";
		}
	}
	
//	Lock Management for User -------------------------------------------------------------------------
	@GetMapping("/block/{id}")
	public String blockUser(@PathVariable Integer id) {
		logger.trace("InSide Locking Controller");
		customerService.lockUser(id);
		logger.info("Locked User");
		return "redirect:/admin/user_management";
	}
	
	@GetMapping("/unBlock/{id}")
	public String unblockUser(@PathVariable Integer id) {
		logger.debug("InSide Unlocking Controller");
		customerService.unLockUser(id);
		logger.debug("UnLocked User");
		return "redirect:/admin/user_management";
	}
	
	
//	Product Management ----------------------------------------------------------------------
	@GetMapping("/product_management")
	public String productManagementPage(Model model) {
		logger.debug("InSide Product Management Controller");
//		To hold the data
		List<Product> productList = productService.getAllProducts();
		model.addAttribute("productList", productList);
		return "productManagement.html";
	}
	
//	Add Product -------------
	@GetMapping("/addProduct")
	public String addProductPage(Model model) {
		logger.debug("InSide Add Product Controller");	
//		To hold the data
		Product newProduct = new Product();
		model.addAttribute("newProduct", newProduct);
		return "addProduct.html";
	}
	
	@PostMapping("/registerProduct")
	public String addProduct(@ModelAttribute("newProduct") Product p) {
		logger.debug("InSide Product Registering Controller");
		Product registerProduct = new Product( p.getProductName(), p.getDescription(), p.getQuantity(), p.getCaseSize(),
				p.getPrice(), LocalDate.now());
		productRepository.save(registerProduct);
		return "redirect:/admin/product_management";
	}
	
//	Edit Product -------------
	@GetMapping("/editProduct/{id}")
	public String editProductPage(@PathVariable Integer id,Model model) {
		logger.debug("InSide Edit Product Controller");
		Product editProduct = productRepository.findById(id).get();
		model.addAttribute("newProduct", editProduct);
		return "editProduct.html";
	}
	
	@PostMapping("/{id}")
	public String editProduct(@PathVariable Integer id,@ModelAttribute("editProduct") Product ep) {
		logger.debug("InSide Product Editing Controller");
		productService.updateProduct(id,ep.getProductName(),ep.getCaseSize(),ep.getDescription(),ep.getIsEnabled(),ep.getPrice(),ep.getQuantity());
		return "redirect:/admin/product_management";
	}
	
//	Delete Product -------------
	@GetMapping("/deleteProduct/{id}")
	public String deletrProduct(@PathVariable Integer id) {
		logger.debug("InSide Delete Product Controller");
		productService.deleteProduct(id);
		return "redirect:/admin/product_management";
	}

	
//	Category Management ----------------------------------------------------------------------
	
	@GetMapping("/category_management")
	public String categoryManagementPage(Model model) {
		logger.debug("InSide Category Management Controller");
//		To hold the data
		List<Category> categoryList = categoryService.getAllCategories();
		model.addAttribute("categoryList", categoryList);
		return "categoryManagement.html";
	}
	
//	Add Category ---------------------------------
	@GetMapping("/addCategory")
	public String addCategoryPage(Model model) {
		logger.debug("InSide Add Category Controller");	
//		To hold the data
		CategoryRegistrationDTO newCategory = new CategoryRegistrationDTO();
		model.addAttribute("newCategory", newCategory);
		return "addCategory.html";
	}
	
	@PostMapping("/registerCategory")
	public String addCategory(@ModelAttribute("newCategory") CategoryRegistrationDTO c) {
		logger.debug("InSide Category Registering Controller");
		
		Category registerCategory = new Category();
		registerCategory.setCategoryName(c.getCategoryName());
		registerCategory.setDescription(c.getDescription());
		registerCategory.setNoOfSubCategories(c.getNoOfSubCategories());
		categoryRepository.save(registerCategory);
		return "redirect:/admin/category_management";
	}
	
//	Add Sub Category -------------
	@GetMapping("/addSubCategory/{id}")
	public String addSubCategoryPage(@PathVariable Integer id,Model model) {
		logger.debug("InSide Add Category Controller");	
//		To hold the data
		Category newCategory = categoryRepository.findById(id).get();
		model.addAttribute("newCategory", newCategory);
		return "addSubCategory.html";
	}
	
	@PostMapping("/registerSubCategory")
	public String addSubCategory(@ModelAttribute("newCategory") Category c) {
		logger.debug("InSide Sub Category Registering Controller");
		
		Category existingCategory = categoryRepository.findById(c.getCategoryId()).get();
        List<SubCategory> customerSubCategories = c.getSubcategories();
        
        List<SubCategory> existingSubCategories = existingCategory.getSubcategories();
        
        List<SubCategory> newlyAdded = categoryService.newSubCategories( existingSubCategories,customerSubCategories);
        for (SubCategory subcategory : newlyAdded) {
	      	  subcategory.setCategory(existingCategory);
	      	  subCategoryRepository.save(subcategory);
          } 
        
		return "redirect:/admin/category_management";
	}
	
//	Edit Category ---------------------------------
	@GetMapping("/editCategory/{id}")
	public String editCategoryPage(@PathVariable Integer id,Model model) {
		logger.debug("InSide Edit Category Page Loading Controller");
		Category editCategory = categoryRepository.findById(id).get();
		model.addAttribute("editCategory", editCategory);
		return "editCategory.html";
	}
	
	@PostMapping("/modifyCategory/{id}")
	public String editCategory(@PathVariable Integer id,@ModelAttribute("editCategory") Category ec) {
		logger.debug("InSide Category Editing Controller");
		categoryService.updateCategory(id,ec);
		return "redirect:/admin/category_management";
	}
	
//	Delete Category -------------------------------
	@GetMapping("/deleteCategory/{id}")
	public String CategoryProduct(@PathVariable Integer id) {
		logger.debug("InSide Delete Category Controller");
		categoryService.deleteCategory(id);
		return "redirect:/admin/category_management";
	}

//	Delete SubCategory -------------
	@GetMapping("/deleteSubCategory/{id}")
	public String editSubCategoryPage(@PathVariable Integer id,Model model) {
		logger.debug("InSide Delete Sub Category Page Loading Controller");
		Category deleteChildCategory = categoryRepository.findById(id).get();
		model.addAttribute("deleteChildCategory", deleteChildCategory);
		return "deleteSubCategory.html";
	}
	
	@GetMapping("/removeSubCategory/{id}")
	public String editSubCategory(@PathVariable Integer id) {
		logger.debug("InSide Sub Category deleting Controller");
		subCategoryRepository.deleteById(id);
		return "redirect:/admin/category_management";
	}
	
//	Locking Category ---------------------------------------------
	@GetMapping("/lockCategory/{id}")
	public String lockingCategory(@PathVariable Integer id) {
		logger.debug("InSide Locking Category Controller");	
		categoryService.changeStatus(id);
		return "redirect:/admin/category_management";
	}
	
//	UnLocking Category ---------------------------------------------
	@GetMapping("/unlockCategory/{id}")
	public String unLockingCategory(@PathVariable Integer id) {
		logger.debug("InSide Unlocking Category Controller");	
		categoryService.changeStatus(id);
		return "redirect:/admin/category_management";
	}
}

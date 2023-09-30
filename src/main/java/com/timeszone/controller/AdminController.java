package com.timeszone.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.dto.CategoryRegistrationDTO;
import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.model.dto.ProductDTO;
import com.timeszone.model.dto.RegistrationDTO;
import com.timeszone.model.product.Category;
import com.timeszone.model.product.Product;
import com.timeszone.model.product.ProductImage;
import com.timeszone.model.product.SubCategory;
import com.timeszone.model.shared.Coupon;
import com.timeszone.model.shared.PaymentMethod;
import com.timeszone.repository.CategoryRepository;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.repository.ProductImageRepository;
import com.timeszone.repository.ProductRepository;
import com.timeszone.repository.SubCategoryRepository;
import com.timeszone.service.CategoryService;
import com.timeszone.service.CouponService;
import com.timeszone.service.CustomerService;
import com.timeszone.service.PaymentMethodService;
import com.timeszone.service.ProductImageService;
import com.timeszone.service.ProductService;
import com.timeszone.service.SubCategoryService;

@RequestMapping("/admin")
@Controller
public class AdminController {
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private SubCategoryService subCategoryService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductImageService productImageService;
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private PaymentMethodService paymentMethodService;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	@Autowired
	private ProductImageRepository productImageRepository;
	
	@GetMapping("/")
	public String adminHome() {
		return "adminHome.html";
	}
	
//	For User management ---------------------------------------------------------------------
	@GetMapping("/user_management")
	public String userManagementPage(Model model) {
		logger.debug("InSide User Management Controller");
//		To hold the list of users
		List<CustomerDTO> userList = customerService.getAllUsers();
		CustomerDTO searchKey = new CustomerDTO();
		model.addAttribute("userList", userList);
		model.addAttribute("searchKey", searchKey);
		return "userManagement.html";
	}
	
////	User Search ------------------
	@PostMapping("/admin/searchUser")
	public String search(@PathVariable CustomerDTO c,Model model) {
			
		logger.debug("Inside Searching Controller");
		System.out.println("Inside search controller");
		List<Customer> customerList = customerRepository.findAllByFirstName(c.getFirstName());
		List<Customer> customerListByPhoneNumber = customerRepository.findAllByPhoneNumber(c.getFirstName());
		List<Customer> customerListByEmailId = customerRepository.findAllByEmailId(c.getFirstName());
		
		customerList.addAll(customerListByEmailId);
		customerList.addAll(customerListByPhoneNumber);
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
		logger.debug("InSide Locking Controller");
		customerService.lockUser(id);
		logger.debug("Locked User");
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
		ProductDTO newProduct = new ProductDTO();
		List<Category> categories= categoryRepository.findAll();
		model.addAttribute("newProduct", newProduct);
		model.addAttribute("categories", categories);	
		
		return "addProduct";
	}
	
	@PostMapping("/registerProduct")
	public String addProduct(@ModelAttribute("newProduct") ProductDTO p) {
		logger.debug("InSide Product Registering Controller");
		
		Product newProduct = new Product();
		Category c;
		SubCategory sc;
		Set<Category> categoryList = new HashSet<>();
		newProduct.setProductName(p.getProductName());
		newProduct.setCaseSize(p.getCaseSize());
		newProduct.setDateAdded(LocalDate.now());
		newProduct.setDescription(p.getDescription());
		newProduct.setPrice(p.getPrice());
		newProduct.setQuantity(p.getQuantity());
		for(String s:p.getSelectedSubCategories()) {
			sc = subCategoryRepository.findBySubCategoryName(s);
			c = sc.getCategory();
			c.getProducts().add(newProduct);
			categoryList.add(c);
			newProduct.getSubcategories().add(sc);
		}
		newProduct.setCategories(categoryList);
		productRepository.save(newProduct);

		return "redirect:/admin/product_management";
	}
	
//	Edit Product -------------
	@GetMapping("/editProduct/{id}")
	public String editProductPage(@PathVariable Integer id,Model model) {
		logger.debug("InSide Edit Product Controller");
		
		Product existingProduct = productRepository.findById(id).get();
		ProductDTO editProduct = productService.convertToProduct(existingProduct);
		List<Category> categoryList= categoryRepository.findAll();
		
		model.addAttribute("editProduct", editProduct);
		model.addAttribute("categoryList", categoryList);
		
		return "editProduct";
	}
	
	@PostMapping("/modifyProduct")
	public String editProduct(@ModelAttribute("editProduct") ProductDTO ep) {
		logger.debug("InSide Product Editing Controller");
		

		System.out.println(ep.getDescription());
		System.out.println(ep.getProductId());
		System.out.println(ep.getProductName());
		System.out.println(ep.getCaseSize());
		System.out.println(ep.getQuantity());
		System.out.println(ep.getPrice());
		System.out.println(ep.getIsEnabled());
		for(String s:ep.getSelectedSubCategories()) {
			System.out.println("Selected SubCategory"+s);
		}
		productService.updateProduct(ep);
		
		return "redirect:/admin/product_management";
	}
	
//	Delete Product -------------
	@GetMapping("/deleteProduct/{id}")
	public String deletrProduct(@PathVariable Integer id) {
		logger.debug("InSide Delete Product Controller");
		productService.deleteProduct(id);
		return "redirect:/admin/product_management";
	}
	
	
	@GetMapping("/addProductImage/{id}")
	public String addProductImagePage(@PathVariable("id") Integer prodcutId,Model model) {
		
		Product p = productRepository.findById(prodcutId).get();
		List<ProductImage> imageList = p.getProductImages();
		model.addAttribute("imageList", imageList);
		model.addAttribute("product", p);
		return "addProductImage";
	}
	
	@PostMapping("/upload")
	public RedirectView uploadProductImage(@RequestParam("file") MultipartFile file,@ModelAttribute("product") Product p,Model model) throws IOException {
		
		ProductImage newImage = new ProductImage();
		Product imageProduct = productRepository.findById(p.getProductId()).get();		
		String fileName = file.getOriginalFilename();
		if(!fileName.isEmpty()) {
			newImage.setImageName(fileName);
			newImage.setImage(file.getBytes());
			newImage.setSize(file.getSize());
			newImage.setProduct(imageProduct);
			productImageService.create(newImage);
			productRepository.save(imageProduct);
		}
		
		// Redirect to another endpoint with a path variable
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/admin/addProductImage/{id}"); 
        redirectView.addStaticAttribute("id", p.getProductId());
        model.addAttribute("success", "File Uploaded Successfully");
        return redirectView;
	}
	

	
	@GetMapping("/deleteProductImage/{id}")
	public String deleteProductImage(@PathVariable("id") Integer productImageId) {
		
		ProductImage pi = productImageRepository.findByImageId(productImageId);
		Product p = pi.getProduct();
		p.getProductImages().remove(pi);
		productRepository.save(p);
		productImageRepository.deleteById(productImageId);
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
		Category c = categoryRepository.findById(id).get();
		CategoryRegistrationDTO newCategory = categoryService.convertToCategoryDTO(c);
		model.addAttribute("newCategory", newCategory);
		return "addSubCategory.html";
	}
	
	@PostMapping("/registerSubCategory")
	public String addSubCategory(@ModelAttribute("newCategory") CategoryRegistrationDTO cd) {
		logger.debug("InSide Sub Category Registering Controller");
		
		subCategoryService.addSubCategory(cd);
		
		logger.debug("Exiting from  Sub Category Registering Controller");
        
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
	
//	Coupon Management =============================================================================================================================
	
//	viewing coupon management --------------------------------------------------------------
	@GetMapping("/couponManagement")
	public String couponManagement(Model model) {
		
		List<Coupon> couponList = couponService.getAll();
		model.addAttribute("couponList", couponList);
		return "couponManagement.html";
	}
	
//	Adding coupon -------------------------------------------------------------------------
	@GetMapping("/addCoupon")
	public String addCouponPage(Model model) {
		logger.debug("InSide Add Coupon Controller");	
		Coupon newCoupon = new Coupon();
		model.addAttribute("newCoupon", newCoupon);
		return "addCoupon";
	}
	
	@PostMapping("/addingCoupon")
	public String addCoupon(@ModelAttribute("newCoupon") Coupon newCoupon) {
		logger.debug("Controller::Coupon registering");	
		couponService.addCoupon(newCoupon);
		return "redirect:/admin/couponManagement";
	}
	
//	Deleting Coupon ------------------------------------------------------------------------
	@GetMapping("/deleteCoupon/{id}")
	public String deleteCoupon(@PathVariable("id") Integer couponId) {
		
		couponService.deleteCoupon(couponId);
		return "redirect:/admin/couponManagement";
	}
	
//	Edit coupon ------------------------------------------------------------------------------
	@GetMapping("/editCoupon/{id}")
	public String editCouponPage(@PathVariable("id") Integer couponId,Model model) {
		logger.debug("Controller::Coupon edit page");
		Coupon editCoupon = couponService.getCoupon(couponId);
		model.addAttribute("editCoupon", editCoupon);
		return "editCoupon";
	}
	
	@PostMapping("/modifyCoupon")
	public String modificationCoupon(@ModelAttribute("editCoupon") Coupon editCoupon) {
		logger.debug("Controller::Coupon updating");
		couponService.updateCoupon(editCoupon);
		return "redirect:/admin/couponManagement";
	}
	
//	PaymentMethod ===============================================================================================
	
//	adding payment method -------------------------------------------------------------------
	@GetMapping("/paymentMethods")
	public String paymentMethods(Model model) {
		
		List<PaymentMethod> paymentMethodsList = paymentMethodService.getAll();
		PaymentMethod paymentMethod = new PaymentMethod();
		model.addAttribute("paymentMethodsList", paymentMethodsList);
		model.addAttribute("paymentMethod", paymentMethod);
		return "paymentMethods";
	}
	
//	add paymentMethod -----------------------------------------------------------------------
	@PostMapping("/addPaymentMethod")
	public ResponseEntity<Map<String, Object>> addPaymentMethod(@RequestParam("data") String methodName) {

	  // Validate the payment method
	  Map<String, Object> responseMap = new HashMap<>();
	  if (methodName.isEmpty()) {
		  responseMap.put("error", "Payment method name cannot be empty.");
	      return ResponseEntity.ok(responseMap);
	  }
	  if(paymentMethodService.contains(methodName)) {
		  responseMap.put("error", "Payment method exists");
	      return ResponseEntity.ok(responseMap);
	  }
	  
	  PaymentMethod newMethod = new PaymentMethod(methodName);
	  // Save the payment method to the database
	  paymentMethodService.createPaymentMethod(newMethod);
	  
	  responseMap.put("success", "Payment Method Added Successfully");

	  // Return a success response
	  return ResponseEntity.ok(responseMap);
	}
	
//	edit payment --------------------------------------------------------------------------------
	@GetMapping("/editPaymentMethod")
	public String editPaymentMethod(@RequestParam("id") Integer id,Model model) {
		PaymentMethod paymentMethod = paymentMethodService.getPaymentMethod(id);
		model.addAttribute("paymentMethod", paymentMethod);
		return "editPaymentMethod";
	}
	
	@PostMapping("/modifyPaymentMethod")
	public String modifyMethod(@ModelAttribute("paymentMethod") PaymentMethod paymentMethod) {
		
		paymentMethodService.editPaymentMethod(paymentMethod.getPaymentMethodId(), paymentMethod);
		return "redirect:/admin/paymentMethods";
	}
	
//	delete payment ------------------------------------------------------------------------------
	@GetMapping("/deletePaymentMethod")
	public String deleteMethod(@RequestParam("id") Integer paymentMethodId) {
		paymentMethodService.deletePaymentMethod(paymentMethodId);
		return "redirect:/admin/paymentMethods";
	}
}

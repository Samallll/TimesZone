package com.timeszone.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.io.OutputStream;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import com.timeszone.model.customer.Customer;
import com.timeszone.model.dto.CategoryRegistrationDTO;
import com.timeszone.model.dto.CustomerDTO;
import com.timeszone.model.dto.OfferRequestDTO;
import com.timeszone.model.dto.ProductDTO;
import com.timeszone.model.dto.RegistrationDTO;
import com.timeszone.model.product.Category;
import com.timeszone.model.product.Product;
import com.timeszone.model.product.ProductImage;
import com.timeszone.model.product.ProductOffer;
import com.timeszone.model.product.SubCategory;
import com.timeszone.model.product.SubCategoryOffer;
import com.timeszone.model.shared.Coupon;
import com.timeszone.model.shared.PaymentMethod;
import com.timeszone.model.shared.PurchaseOrder;
import com.timeszone.model.shared.ReturnReason;
import com.timeszone.repository.CategoryRepository;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.repository.OrderStatusRepository;
import com.timeszone.repository.ProductImageRepository;
import com.timeszone.repository.ProductOfferRepository;
import com.timeszone.repository.ProductRepository;
import com.timeszone.repository.ReturnReasonRepository;
import com.timeszone.repository.SubCategoryRepository;
import com.timeszone.service.CategoryService;
import com.timeszone.service.CouponService;
import com.timeszone.service.CustomerService;
import com.timeszone.service.PaymentMethodService;
import com.timeszone.service.ProductImageService;
import com.timeszone.service.ProductOfferService;
import com.timeszone.service.ProductService;
import com.timeszone.service.PurchaseOrderService;
import com.timeszone.service.ReportGeneratorService;
import com.timeszone.service.SubCategoryOfferService;
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
	private OrderStatusRepository orderStatusRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductImageService productImageService;
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private PaymentMethodService paymentMethodService;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	@Autowired
	private ProductImageRepository productImageRepository;
	
	@Autowired
	private ReturnReasonRepository returnReasonRepository;
	
	@Autowired
	private ReportGeneratorService reportGeneratorService;
	
	@Autowired
	private ProductOfferService productOfferService;
	
	@Autowired
	private ProductOfferRepository productOfferRepository;
	
	@Autowired
	private SubCategoryOfferService subCategoryOfferService;
	
	@GetMapping("/")
	public String adminHome(Model model){
		
		Long noOfUsers = customerRepository.count();
		Map<String,Integer> orderStatusMapData;
		Map<String,Integer> orderByDateMapdata;
		int noOfOrders = purchaseOrderService.getAllOrders().size();
		orderStatusMapData = reportGeneratorService.generateOrderCountForChart();
		orderByDateMapdata = reportGeneratorService.generateOrderPlacedForWeek();
		model.addAttribute("orderStatusMapData", orderStatusMapData);
		model.addAttribute("orderByDateMapdata", orderByDateMapdata);
		model.addAttribute("noOfUsers", noOfUsers);
		model.addAttribute("noOfOrders", noOfOrders);
		model.addAttribute("noOfProducts", productRepository.count());
		model.addAttribute("totalRevenue", reportGeneratorService.revenueCalculator());
		return "adminHome.html";
	}
	
//	User management =====================================================================================
	
//	User Management page rendering ----------------------------------------------
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
	
	
//	Order Management ============================================================================================
	
//	Order table rendering -------------------------------------------------------------------------
	@GetMapping("/orderManagement")
	public String orderManagement(Model model,
								@RequestParam(name="pageOrder",defaultValue="0") int pageOrder,
								@RequestParam(name="pageReturnRequest",defaultValue="0") int pageReturnRequest
								) {
		
		Pageable pageable = PageRequest.of(pageOrder, 15);
		Pageable pageableReturnRequest = PageRequest.of(pageReturnRequest, 5);

		Page<PurchaseOrder> orderListByReturnRequest = purchaseOrderService.getAllByOrderStatusForPagination(pageableReturnRequest,"Requested for Return");
		model.addAttribute("orderListByReturnRequest", orderListByReturnRequest);
		
		List<PurchaseOrder> orderListByApprovedReturnRequest = purchaseOrderService.getAllByOrderStatus("Return Request Approved");
		model.addAttribute("orderListByApprovedReturnRequest", orderListByApprovedReturnRequest);
		
		List<String> statusList = new ArrayList<>();
		statusList.add("Requested for Return");
		statusList.add("Return Request Approved");
		Page<PurchaseOrder> orderList = purchaseOrderService.getAllOrdersByPageByRemovingMulipleOrderStatus(pageable,statusList);		
		model.addAttribute("orderList", orderList);
		model.addAttribute("orderStatusList", orderStatusRepository.findAll());
		return "orderManagement";
	}
	
//	change order status --------------------------------------------------------------
	@GetMapping("/changeOrderStatus")
	public ResponseEntity<Map<String,Object>> changeOrderStatus(@RequestParam("selectedValue") String orderStatus,
								@RequestParam("orderId") Integer orderId){
		
		PurchaseOrder order = purchaseOrderService.getOrder(orderId);
		System.out.println(orderStatus);
		order.setOrderStatus(orderStatus);
		purchaseOrderService.updateOrder(order);
		Map<String,Object> response = new HashMap<>();
		response.put("success", "completed");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/orderDetails")
	public String showOrderDetails(@RequestParam("id") Integer orderId,Model model) {
		
		PurchaseOrder order = purchaseOrderService.getOrder(orderId);
		ReturnReason reason = returnReasonRepository.findByOrder(order);
		if(reason == null) {
			return "redirect:/admin/orderManagement";
		}
		model.addAttribute("order", order);
		model.addAttribute("reason", reason);
		model.addAttribute("orderItemList",order.getOrderItems());
		return "orderDetailsAdmin";
	}
	
//	return order admin approval ----------------------------------------------------------
	@GetMapping("order/return/request")
	public String returnRequestCheck(@RequestParam("id") Integer approval,@RequestParam("orderId") Integer orderId) {
		
		if(approval==1) {
			purchaseOrderService.updateOrderStatus(orderId, "Return Request Approved");
		}
		if(approval==0) {
			purchaseOrderService.updateOrderStatus(orderId, "Return Request Declined");
		}
		return "redirect:/admin/orderManagement";
	}
	
	
	@GetMapping("order/initiateRefund")
	public String initiateRefund(@RequestParam("id") Integer orderId) {
		
		purchaseOrderService.returnOrder(orderId);
		return "redirect:/admin/orderManagement";
	}
	
//	Download report ---------------------------------------------------------------------------
	@GetMapping("/dashboard/generateReport")
	public ResponseEntity<byte[]> generateReport(
	        HttpServletResponse response,
	        @RequestParam String selectedOption,
	        @RequestParam String dateFrom,
	        @RequestParam String dateTo) throws IOException {

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate startDate = LocalDate.parse(dateFrom, formatter);
	    LocalDate endDate = LocalDate.parse(dateTo, formatter);

	    // Generate the CSV content
	    StringBuilder csvContent;
	    
	    if(selectedOption.equalsIgnoreCase("Orders")) {
	    	csvContent = reportGeneratorService.generateOrderReport(startDate, endDate);
	    }
	    else if(selectedOption.equalsIgnoreCase("Users")) {
	    	csvContent = reportGeneratorService.generateUserReport();
	    }
	    else {
	    	csvContent = reportGeneratorService.generateProductReport();
	    }
	    // Convert the CSV content to bytes
	    byte[] csvBytes = csvContent.toString().getBytes();

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    String uniqueFileName = "order_" + UUID.randomUUID().toString() + ".csv";
	    headers.setContentDispositionFormData("attachment", uniqueFileName);

	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(csvBytes);
	}
	
//	Offer Management ======================================================================================
	
	@GetMapping("/offerManagement")
	public String offerManagement(Model model,HttpSession session) {
		if(session.getAttribute("error")!= null) {
			session.removeAttribute("error");
		}
		List<ProductOffer> productOfferList = productOfferService.getAll();
		model.addAttribute("productOfferList", productOfferList);
		List<SubCategoryOffer> subCategoryOfferList = subCategoryOfferService.getAllByIsEnabled();
		model.addAttribute("subCategoryOfferList", subCategoryOfferList);
		return "offerManagement";
	}
	
	@GetMapping("/offerManagement/addProductOffer")
	public String addProductOffer(HttpSession session) {
		if(session.getAttribute("selectedProducts")!= null) {
			session.removeAttribute("selectedProducts");
		}
		return "addProductOffer";
	}

//  Search the product
	@GetMapping("/productOffer/searchProduct")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> getProducts(@RequestParam("search") String searchData){
		
		Map<String,Object> response = new HashMap<>();
		List<Product> productList = productService.getProductsByName(searchData);
		String[] productNames = new String[productList.size()];
		int i=0;
		for(Product product: productList) {
			productNames[i]=product.getProductName();
			i++;
		}
		response.put("productList", productNames);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/productOffer/selectProducts")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> selectProducts(@RequestParam("productName") String productName,HttpSession session){
		
		List<Product> products = productService.getProductsByName(productName);
		Map<String,Object> response = new HashMap<>();
		int i=0;
		Product product = products.get(0);
		@SuppressWarnings("unchecked")
		Set<Product> selectedProducts = (Set<Product>) session.getAttribute("selectedProducts");
		
		if(selectedProducts == null) {
			selectedProducts = new HashSet<>();;
		}
		
		selectedProducts.add(product);
		session.setAttribute("selectedProducts", selectedProducts);

		String[] productNames = new String[selectedProducts.size()];
		for(Product p:selectedProducts) {
			productNames[i]=p.getProductName();
			i++;
		}
		
		response.put("selectedProducts", productNames);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/offerManagement/submitProductOffer")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> submitProductOffer(@RequestBody Map<String,Object> formData){
		
		Map<String,Object> response = new HashMap<>();
		
		String offerCode = (String) formData.get("offerCode");
		String startDate = (String) formData.get("offerStartDate");
		String expiryDate = (String) formData.get("offerExpiryDate");
		String percentage = (String) formData.get("percentage");
		List<String>  productNames=  (List<String>) formData.get("productNameList");
		String message = "Offer Created Succesfullly";;
		if(startDate == null || expiryDate == null || startDate.isEmpty() || expiryDate.isEmpty()) {
			message = "Please enter valid dates";
			if(offerCode == null || percentage == null || productNames == null) {
				message = "Please enter fill valid data";
			}
			response.put("message", message);
			return ResponseEntity.ok(response);
		}

		LocalDate offerStartDate = LocalDate.parse(startDate);
		LocalDate offerExpiryDate = LocalDate.parse(expiryDate);
		if((offerStartDate.isBefore(offerExpiryDate) || offerStartDate.isBefore(LocalDate.now()))&&
				!offerStartDate.isBefore(offerExpiryDate)) {
			message = "Please enter valid date range";
		}
		else {
			productOfferService.createOffer(offerCode,offerStartDate,offerExpiryDate,Double.parseDouble(percentage),productNames);
		}
		response.put("message", message);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/offerManagement/productOfferEdit")
	public String editProductOffer(@RequestParam("id") Integer productOfferId,Model model) {
		ProductOffer offer = productOfferService.getProductOfferById(productOfferId);
		model.addAttribute("productOffer", offer);
		return "editProductOffer";
	}
	
	@PostMapping("/offerManagement/modifyProductOffer")
	public String updateProductOffer(@ModelAttribute("offer") ProductOffer offer) {
		
		ProductOffer productOffer = productOfferRepository.findById(offer.getProductOfferId()).get();
		productOffer.setDiscountPercentage(offer.getDiscountPercentage());
		productOffer.setExpiryDate(offer.getExpiryDate());
		productOffer.setProductOfferCode(offer.getProductOfferCode());
		productOffer.setStartDate(offer.getStartDate());
		productOfferService.modifyProductOffer(productOffer);
		return "redirect:/admin/offerManagement";
	}
	
	@GetMapping("/offerManagement/productOfferDelete")
	public String deleteProductOffer(@RequestParam("id") Integer productOfferId) {
		
		ProductOffer offer = productOfferRepository.findById(productOfferId).get();
		offer.setIsEnabled(false);
		for(Product product:offer.getProductList()) {
			product.setProductOffer(null);
			productRepository.save(product);
		}
		offer.getProductList().clear();
		productOfferRepository.save(offer);
		return "redirect:/admin/offerManagement";
	}
	
//	Product Offer Completed ---------------------------------------------------------------------------
//	Category Offer ------------------------------------------------------------------------------------
	
	@GetMapping("/offerManagement/addSubCategoryOffer")
	public String SubCategoryOffer(Model model) {
		
		List<Category> categoryList = categoryService.getAllCategories();
		model.addAttribute("categoryList", categoryList);
		OfferRequestDTO offerRequest = new OfferRequestDTO();
		model.addAttribute("offerRequest", offerRequest);
		return "addSubCategoryOffer";
	}
	
	@PostMapping("/offerManagement/addSubCategoryOffer")
	public String addSubCategoryOffer(@ModelAttribute("offerRequest") OfferRequestDTO offerRequest,HttpSession session) {
		
		LocalDate offerStartDate = offerRequest.getStartDate();
		LocalDate offerExpiryDate = offerRequest.getExpiryDate();
		if((offerStartDate.isBefore(offerExpiryDate) || offerStartDate.isBefore(LocalDate.now()))&&
				!offerStartDate.isBefore(offerExpiryDate)) {
			session.setAttribute("error", "Please select a valid date range");
			return "redirect:/admin/offerManagement/addSubCategoryOffer";
		}
		if(offerRequest.getSubItemListIds()==null || offerRequest.getSubItemListIds().isEmpty()) {
			session.setAttribute("error", "Please select subcategories");
			return "redirect:/admin/offerManagement/addSubCategoryOffer";
		}
		subCategoryOfferService.createOffer(offerRequest);
		return "redirect:/admin/offerManagement";
	}
	
	@GetMapping("/offerManagement/subCategoryOfferEdit")
	public String editSubCategoryOffer(@RequestParam("id") Integer subCategoryOfferId, Model model) {
		
		SubCategoryOffer offer = subCategoryOfferService.getById(subCategoryOfferId);
		model.addAttribute("offer", offer);
		OfferRequestDTO offerRequest = new OfferRequestDTO();
		model.addAttribute("offerRequest", offerRequest);
		List<Category> categoryList = categoryService.getAllCategories();
		model.addAttribute("categoryList", categoryList);
		return "editSubCategoryOffer";
	}
	
	@PostMapping("/offerManagement/editSubCategoryOffer")
	public String editSubCategoryOffer(@ModelAttribute("offerRequest") OfferRequestDTO offerRequest,HttpSession session) {
		
		LocalDate offerStartDate = offerRequest.getStartDate();
		LocalDate offerExpiryDate = offerRequest.getExpiryDate();
		if((offerStartDate.isBefore(offerExpiryDate) || offerStartDate.isBefore(LocalDate.now()))&&
				!offerStartDate.isBefore(offerExpiryDate)) {
			session.setAttribute("error", "Please select a valid date range");
			return "redirect:/admin/offerManagement/subCategoryOfferEdit";
		}
		if(offerRequest.getSubItemListIds()==null || offerRequest.getSubItemListIds().isEmpty()) {
			session.setAttribute("error", "Please select subcategories");
			return "redirect:/admin/offerManagement/subCategoryOfferEdit";
		}
		subCategoryOfferService.updateOffer(offerRequest);
		return "redirect:/admin/offerManagement";
	}
	
	@GetMapping("/offerManagement/subCategoryOfferDelete")
	public String disableSubCategoryOffer(@RequestParam("id") Integer offerId) {
		
		SubCategoryOffer offer = subCategoryOfferService.getById(offerId);
		offer.setIsEnabled(false);
		for(SubCategory subCategory:offer.getSubCategories()) {
			subCategory.setSubCategoryOffer(null);
			subCategoryService.saveToTable(subCategory);
		}
		offer.getSubCategories().clear();
		subCategoryOfferService.saveToTable(offer);
		return "redirect:/admin/offerManagement";
	}
}

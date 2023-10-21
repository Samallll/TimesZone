package com.timeszone.service;

import java.util.Date;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.product.Product;
import com.timeszone.model.shared.PurchaseOrder;

@Service
public class ReportGeneratorService {
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private TemplateEngine templateEngine;

	public Map<String, Integer> generateOrderCountForChart() {
		
		Map<String,Integer> orderDataByStatus = new HashMap<>();
		int size=0;
		String[] orderStatus = {"Pending","Shipped","Delivered","Cancelled","Requested for Return","Return Request Approved",
								"Return Request Declined","Refunded"};
		for(String status:orderStatus) {
			size = purchaseOrderService.getAllByOrderStatus(status).size();
			orderDataByStatus.put(status, size);
		}
		return orderDataByStatus;
	}
	
	public Map<String,Integer> generateOrderPlacedForDaily(){
        
        Calendar calendar = Calendar.getInstance();

        Date endDate = calendar.getTime();
        
        // Calculate the start date as five days ago (exclusive)
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date startDate = calendar.getTime();
		
        Map<String, Integer> orderCounts = new LinkedHashMap<>();
        
        while (!startDate.after(endDate)) {
            
        	Instant instant = startDate.toInstant();
            // Convert Instant to LocalDate
            LocalDate lastDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            int orderCount = purchaseOrderService.getNumberOfOrdersForDate(lastDate);
            orderCounts.put(lastDate.getDayOfWeek().toString(), orderCount);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            startDate = calendar.getTime();
        }
        
        return orderCounts;
	}
	
	public Map<String,Integer> generateOrderPlacedForMonthly(){
		
		Map<String, Integer> orderCounts = new LinkedHashMap<>();
		int year = LocalDate.now().getYear();
		int orderCount = 0;
		
		for(Month month:Month.values()) {
			LocalDate startDayOfMonth = LocalDate.of(year, month, 1);
			LocalDate endDayOfMonth = startDayOfMonth.withDayOfMonth(startDayOfMonth.lengthOfMonth());
			
			orderCount = purchaseOrderService.getAllPurchaseOrdersByDateRange(startDayOfMonth, endDayOfMonth).size();
			
			String monthName = month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
			
			orderCounts.put(monthName,orderCount);
		}
        return orderCounts;
	}
	
	public Map<String, Integer> generateOrderPlacedForYearly() {
		
		Map<String, Integer> orderCounts = new LinkedHashMap<>();
		
		int currentYear = LocalDate.now().getYear();
		for(int year = currentYear;year>=currentYear-4;year--) {
			int orderCount = 0;
			for(Month month:Month.values()) {
				
				LocalDate startDayOfMonth = LocalDate.of(year, month, 1);
				LocalDate endDayOfMonth = startDayOfMonth.withDayOfMonth(startDayOfMonth.lengthOfMonth());
				
				orderCount = orderCount + purchaseOrderService.getAllPurchaseOrdersByDateRange(startDayOfMonth, endDayOfMonth).size();
			}
			String yearString = String.valueOf(year);
			
			orderCounts.put(yearString, orderCount);
		}
        return orderCounts;
	}
	
	public Double revenueCalculator() {
		
		Double amount = (double) 0;
		List<PurchaseOrder> orderList = purchaseOrderService.getAllOrders();
		for(PurchaseOrder order:orderList) {
			amount = amount + order.getOrderAmount();
		}
		return Double.parseDouble(String.format("%.2f", amount / 100000.0));	
	}

	public StringBuilder generateCSVOrderReport(LocalDate dateFrom, LocalDate dateTo) {
		
		List<PurchaseOrder> orderList = purchaseOrderService.getAllOrders();
		orderList.stream()
                .filter(order -> isDateBetween(order.getOrderedDate(), dateFrom, dateTo))
                .collect(Collectors.toList());
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Order ID,Customer Name, Order Amount,Quantity,Status,Ordered Date,Payment Method\n");

        for (PurchaseOrder order : orderList) {
            csvContent.append(String.format("%s,%s,%.2f,%d,%s,%s,%s\n",
                    order.getOrderId().toString(),
                    order.getCustomer().getFirstName().toString(),
                    order.getOrderAmount(),
                    order.getOrderedQuantity(),
                    order.getOrderStatus(),
                    order.getOrderedDate(),
                    order.getPaymentMethod().getPaymentMethodName()));
        }
        return csvContent;
	}

	private boolean isDateBetween(LocalDate dateToCheck, LocalDate startDate, LocalDate endDate) {
        return !dateToCheck.isBefore(startDate) && !dateToCheck.isAfter(endDate);
    }

	public StringBuilder generateUserReport() {

		List<Customer> customerList = customerService.customerRepository.findAll();

        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Customer ID,Customer Name, Email Id,Phone Number,Wallet Amount\n");

        for (Customer customer : customerList) {
            csvContent.append(String.format("%s,%s,%s,%s,%d\n",
            		customer.getCustomerId().toString(),
            		customer.getFirstName().toString()+' '+customer.getLastName().toString(),
            		customer.getEmailId(),
            		customer.getPhoneNumber(),
            		customer.getWallet().toString()));
        }
        return csvContent;
	}

	public StringBuilder generateProductReport() {

		List<Product> productList = productService.getAllProducts();
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Product ID,Product Name, Description, Quantity, Price, Case Size \n");
        
        for (Product product : productList) {
            csvContent.append(String.format("%s,%s,%s,%s,%d,%d\n",
            		product.getProductId().toString(),
            		product.getProductName().toString(),
            		product.getDescription(),
            		product.getQuantity().toString(),
            		product.getPrice().toString(),
            		product.getCaseSize().toString()));
        }
        return csvContent;
	}
	
	public byte[] generatePdfOrderReport(LocalDate dateFrom, LocalDate dateTo) throws Exception {
		
		List<PurchaseOrder> orderList = purchaseOrderService.getAllPurchaseOrdersByDateRange(dateFrom, dateTo);
	
		Context context = new Context();
		context.setVariable("orderList", orderList);
		
		String htmlContent = templateEngine.process("purchaseOrderPdf", context);
		
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(htmlContent);
		renderer.layout();
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		renderer.createPDF(os);
		os.close();
		
		return os.toByteArray();
	}
	
}

package com.timeszone.service;

import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.shared.PurchaseOrder;

@Service
public class ReportGeneratorService {
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;

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
	
	public Map<String,Integer> generateOrderPlacedForWeek(){
        
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
	
	public Double revenueCalculator() {
		
		Double amount = (double) 0;
		List<PurchaseOrder> orderList = purchaseOrderService.getAllOrders();
		for(PurchaseOrder order:orderList) {
			amount = amount + order.getOrderAmount();
		}
		return Double.parseDouble(String.format("%.2f", amount / 100000.0));	
	}
	
}

package com.timeszone.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DiscountApplier {
	
	@Scheduled(cron = "0 08 21 * * ?", zone = "Asia/Kolkata")
	public void applyProductOffer() {
		System.out.println("hello world");
	}

}

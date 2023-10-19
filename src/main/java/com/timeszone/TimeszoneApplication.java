package com.timeszone;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.timeszone.model.customer.Customer;
import com.timeszone.model.customer.Role;
import com.timeszone.model.product.Product;
import com.timeszone.model.product.ProductOffer;
import com.timeszone.model.shared.OrderStatus;
import com.timeszone.model.shared.PurchaseOrder;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.repository.OrderStatusRepository;
import com.timeszone.repository.ProductOfferRepository;
import com.timeszone.repository.ProductRepository;
import com.timeszone.repository.PurchaseOrderRepository;
import com.timeszone.repository.RoleRepository;
import com.timeszone.repository.SubCategoryRepository;
import com.timeszone.scheduler.DiscountApplier;

@SpringBootApplication
@EnableScheduling
public class TimeszoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeszoneApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(CustomerRepository customerRepository,
			RoleRepository roleRepository,
			PasswordEncoder encoder,
			ProductOfferRepository repo,
			DiscountApplier applier
			) {
		
		return args -> {
			
			applier.offerApplyAndRemovalChecking();
					
//			If admin role is present in the table then no need to create a separate admin role. For ddl-auto:update , it is used.
			if(roleRepository.findByAuthority("ADMIN").isPresent()) return;
			
//			Creating a admin role when the application executes if there is no admins.
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			Role userRole = roleRepository.save(new Role("USER"));
			
			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);
			roles.add(userRole);
			
			Customer admin = new Customer("root","root","root@example.com","+917907208032",encoder.encode("root"),roles);
			customerRepository.save(admin);
			
			
			};
		}

	
}

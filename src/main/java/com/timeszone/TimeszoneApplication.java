package com.timeszone;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.timeszone.model.Customer;
import com.timeszone.model.Role;
import com.timeszone.model.product.Category;
import com.timeszone.model.product.Product;
import com.timeszone.model.product.SubCategory;
import com.timeszone.repository.CategoryRepository;
import com.timeszone.repository.CustomerRepository;
import com.timeszone.repository.ProductRepository;
import com.timeszone.repository.RoleRepository;
import com.timeszone.repository.SubCategoryRepository;

@SpringBootApplication
public class TimeszoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeszoneApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(CustomerRepository customerRepository,
			RoleRepository roleRepository,
			PasswordEncoder encoder,
			ProductRepository productRepository,
			CategoryRepository categoryRepository,
			SubCategoryRepository subCategoryRepository
			) {
		
		return args -> {
			
//			Adding product
			
//			Product product1 = new Product(1, "Rolex Submariner", "Stainless steel diver's watch", 10, 40.0, 7950.0, true, LocalDate.of(2023, 9, 12));
//			Product product2 = new Product(2, "Omega Speedmaster", "Automatic chronograph watch", 15, 42.0, 5900.0, true, LocalDate.of(2023, 9, 13));
//			Product product3 = new Product(3, "Patek Philippe Nautilus", "Stainless steel luxury watch", 20, 40.0, 23500.0, true, LocalDate.of(2023, 9, 14));
//			Product product4 = new Product(4, "Cartier Tank", "Stainless steel dress watch", 30, 29.0, 6500.0, true, LocalDate.of(2023, 9, 15));
//			Product product5 = new Product(5, "TAG Heuer Monaco", "Automatic chronograph watch", 25, 39.0, 5400.0, true, LocalDate.of(2023, 9, 16));
//
//			productRepository.save(product1);
//			productRepository.save(product2);
//			productRepository.save(product3);
//			productRepository.save(product4);
//			productRepository.save(product5);
			
			
			
////			User Creation : For testing purpose implemented
//			Set<Role> roles1 = new HashSet<>();
//			roles1.add(roleRepository.findByAuthority("USER").get());
//			
//			Customer admin1 = new Customer("sam","sam","sam@example.com","2222222222",encoder.encode("sam"),roles1);
//			customerRepository.save(admin1);
			
//			Set<SubCategory> subCategories = new HashSet<>();
//			SubCategory subCategory1 = new SubCategory("blue standard");
//			SubCategory subCategory2 = new SubCategory("blue class");
//			SubCategory subCategory3 = new SubCategory("blue premium");
//			subCategories.add(subCategory1);
//			subCategories.add(subCategory2);
//			subCategories.add(subCategory3);
//			
//			Category manualAddCat = new Category("Blue", "Based on Blue color", 3);
//			subCategory1.setCategory(manualAddCat);
//			subCategory2.setCategory(manualAddCat);
//			subCategory3.setCategory(manualAddCat);
//			
//			categoryRepository.save(manualAddCat);
//			
//			subCategoryRepository.save(subCategory1);
//			subCategoryRepository.save(subCategory2);
//			subCategoryRepository.save(subCategory3);
//			
//			manualAddCat.setSubcategories(subCategories);
//			
//			categoryRepository.save(manualAddCat);
			
//			Category c = categoryRepository.findById(2).get()	;		
//			c.setCategoryName("Brand Color Red");
//			
//			categoryRepository.save(c);
			
			
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

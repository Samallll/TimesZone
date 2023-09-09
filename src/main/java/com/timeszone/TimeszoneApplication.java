package com.timeszone;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TimeszoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeszoneApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(CustomerRepository customerRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
		
		return args -> {
			
//			If admin role is present in the table then no need to create a separate admin role. For ddl-auto:update , it is used.
			if(roleRepository.findByAuthority("ADMIN").isPresent()) return;
			
//			Creating a admin role when the application executes if there is no admins.
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			Role userRole;
			if(roleRepository.findByAuthority("USER").isPresent()) {
				userRole = roleRepository.findByAuthority("USER").get();
			}
			else {
				userRole = new Role("USER");
			}
			
			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);
			roles.add(userRole);
			
			Customer admin = new Customer("root","root","root@example.com","2222222222",encoder.encode("root"),roles);
			admin.allAuthorities();
			customerRepository.save(admin);
			
			};
		}

	
}

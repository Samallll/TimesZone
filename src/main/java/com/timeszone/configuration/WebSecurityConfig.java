package com.timeszone.configuration;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.timeszone.service.CustomerService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CustomerService customerService;
	
	
	public WebSecurityConfig(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http	
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/user_registration").permitAll()
				.antMatchers("/user").hasAuthority("USER")
				.antMatchers("/admin").hasAuthority("ADMIN")
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login").permitAll()
				.loginProcessingUrl("/login").permitAll()
				.successHandler((req, resp, authentication) -> {
					
					Set<String> roleSet= AuthorityUtils.authorityListToSet(authentication.getAuthorities());
				    
					if(roleSet.contains("ADMIN"))
				    {
				    	resp.sendRedirect("/admin");
				    }
				    else 
				    {
				    	resp.sendRedirect("/user");
				    }
					
			});
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.authenticationProvider(daoAuthenticationProvider());
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(customerService);
		return provider;
	}
}

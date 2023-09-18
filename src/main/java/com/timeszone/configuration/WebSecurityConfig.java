package com.timeszone.configuration;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	
	
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
				.antMatchers("/user_registration","/otpLogin","/sendOtp","/otpVerify","/register_user","/otpVerification","/otpRegistrationValidation","/assets/**","/user").permitAll()
				.antMatchers("/user/**").hasAuthority("USER")
				.antMatchers("/admin/**").hasAuthority("ADMIN")
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login").permitAll()
				.loginProcessingUrl("/login").permitAll()
				.failureHandler(customAuthenticationFailureHandler)
				.successHandler((req, resp, authentication) -> {
					
					Set<String> roleSet= AuthorityUtils.authorityListToSet(authentication.getAuthorities());
				    
					if(roleSet.contains("ADMIN"))
				    {
				    	resp.sendRedirect("/admin/");
				    }
				    else 
				    {
				    	resp.sendRedirect("/user");
				    }
					
			}).and()
				.logout()
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutSuccessUrl("/login");
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

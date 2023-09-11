package com.timeszone.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
	    // Get the error message
	    String errorMessage = exception.getMessage();
	
	    // Set the error message in the session
	    request.getSession().setAttribute("error", errorMessage);
	    
	    System.out.println(errorMessage);
	
	    // Redirect to the login page
	    response.sendRedirect("/login");
	}
}
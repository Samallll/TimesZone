//package com.timeszone.configuration;
//
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.ModelAndView;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//	
//	@ExceptionHandler(Exception.class)
//	public ModelAndView handleException(Exception ex) {
//		System.out.println("inside exception handler");
//		ModelAndView modelAndView = new ModelAndView("redirect:/error");
//		return modelAndView;
//	}
//
//}

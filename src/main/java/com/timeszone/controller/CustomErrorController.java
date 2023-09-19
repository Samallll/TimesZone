//package com.timeszone.controller;
//
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.context.request.WebRequest;
//
//public class CustomErrorController implements ErrorController{
//
//	@RequestMapping("/error")
//	public String handleError(WebRequest webRequest, Model model) {
//		
//		HttpStatus status = HttpStatus.resolve((Integer) webRequest.getAttribute("javax.servlet.error.status_code", WebRequest.SCOPE_REQUEST));
//		
//		System.out.println(status.name().toString());
//		System.out.println(status.value());
//		
//		if(status != null) {
//			if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
//                return "error404.html";
//                
//            } else if (status == HttpStatus.FORBIDDEN) {
//                return "error404.html";
//                
//            } else if (status == HttpStatus.NOT_FOUND) {
//                return "error404.html";
//                
//            } else if (status == HttpStatus.UNAUTHORIZED) {
//                return "error404.html";
//                
//            }
//		}
//		return "error404.html";
//	}
//}

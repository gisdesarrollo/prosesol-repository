package com.prosesol.springboot.app.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionController implements ErrorController{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		if(status != null){
			Integer statusCode = Integer.valueOf(status.toString());
			
			if(statusCode == HttpStatus.FORBIDDEN.value()) {
				return "/error/error_403";
			}
			if(statusCode == HttpStatus.NOT_FOUND.value()) {
				return "/error/error_404";
			}
			if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return "/error/error_500";
			}
			
		}
		
		return "error";
	}
	
	@Override
	public String getErrorPath() {
		return "/error";
	}
	
}

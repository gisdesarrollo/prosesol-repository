package com.prosesol.springboot.app.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomUserExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request){
		
		String errorMessageDescription = ex.getLocalizedMessage();
		
		if(errorMessageDescription == null)errorMessageDescription = ex.toString();

		return new ResponseEntity<>(errorMessageDescription, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@ExceptionHandler(value = {NumberFormatException.class})
	public ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex, WebRequest request){
		
		String errorMessageDescription = ex.getLocalizedMessage();
		
		if(errorMessageDescription == null)errorMessageDescription = ex.toString();

		return new ResponseEntity<>(errorMessageDescription, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@ExceptionHandler(value = {CustomUserException.class})
	public ResponseEntity<Object> handleCustomUserException(CustomUserException ex, WebRequest request){
		
		String errorMessageDescription = ex.getLocalizedMessage();
		
		if(errorMessageDescription == null)errorMessageDescription = ex.toString();

		return new ResponseEntity<>(errorMessageDescription, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
}

package com.prosesol.springboot.app.exception;

public class CustomUserException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public CustomUserException(String message) {
		super(message);
	}
	
}

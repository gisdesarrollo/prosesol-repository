package com.prosesol.springboot.app.exception;

public class CustomUserException extends Exception{

	private static final long serialVersionUID = 1L;

	public CustomUserException() {
		super();
	}
	
	public CustomUserException(String message) {
		super(message);
	}
	
}

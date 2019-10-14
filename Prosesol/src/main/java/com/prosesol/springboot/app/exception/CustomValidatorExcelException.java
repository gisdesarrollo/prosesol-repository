package com.prosesol.springboot.app.exception;

public class CustomValidatorExcelException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private String message;

    public CustomValidatorExcelException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

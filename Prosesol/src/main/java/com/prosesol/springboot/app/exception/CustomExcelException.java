package com.prosesol.springboot.app.exception;

public class CustomExcelException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;

    public CustomExcelException(String message){
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

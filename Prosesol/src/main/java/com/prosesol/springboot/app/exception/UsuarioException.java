package com.prosesol.springboot.app.exception;

/**
 * @author Luis Enrique Morales Soriano
 */
public class UsuarioException extends RuntimeException{

    private String message;
    private int code;

    public UsuarioException(String message){
        super(message);
    }

    public UsuarioException(int code, String message){
        super();
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

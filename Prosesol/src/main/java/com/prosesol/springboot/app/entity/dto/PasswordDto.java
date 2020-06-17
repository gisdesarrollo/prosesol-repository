package com.prosesol.springboot.app.entity.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @author Luis Enrique Morales Soriano
 */
public class PasswordDto {

    @Email
    @NotEmpty(message = "Por favor ingrese su correo")
    private String email;

    @NotEmpty(message = "Ingrese la contraseña nueva")
    private String password;

    @NotEmpty(message = "Repita la contraseña ingresado anteriormente")
    private String repeatPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}

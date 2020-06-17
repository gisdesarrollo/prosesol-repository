package com.prosesol.springboot.app.controller;

import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.entity.dto.PasswordDto;
import com.prosesol.springboot.app.exception.UsuarioException;
import com.prosesol.springboot.app.service.IUsuarioService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

/**
 * @author Luis Enrique Morales Soriano
 */

@Controller
@RequestMapping("/restaurar")
public class PasswordController {

    protected static final Log LOG = LogFactory.getLog(PasswordController.class);

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute("passwordDto")
    public PasswordDto passwordDto(){
        return new PasswordDto();
    }

    @GetMapping
    public String diplayForgotPassword(){
        return "restaurar";
    }

    @PostMapping
    public String restorePassword(@ModelAttribute("passwordDto") @Valid PasswordDto passwordDto,
                                  BindingResult result, RedirectAttributes redirect){

        String passwordEncoded = null;

        if(result.hasErrors()){
            return "restaurar";
        }

        try{
            Usuario usuario = getUsuarioByEmailUsuario(passwordDto.getEmail());
            if(usuario != null){
                if(!passwordDto.getPassword().equals(passwordDto.getRepeatPassword())){
                    redirect.addFlashAttribute("error",
                            "Las contraseñas no coinciden");
                    return "redirect:/restaurar";
                }else if(passwordDto.getPassword().length() < 7){
                    redirect.addFlashAttribute("error",
                            "Su contraseña debe de contener 7 caracteres");
                    return "redirect:/restaurar";
                }else{
                    for(int x = 0; x < 2; x++){
                        passwordEncoded = passwordEncoder.encode(passwordDto.getPassword());
                    }

                    usuario.setPassword(passwordEncoded);
                    usuarioService.save(usuario);
                }
            }
        }catch (UsuarioException uExc){
            redirect.addFlashAttribute("error", uExc.getMessage());
            LOG.error(uExc.getMessage());
            return "redirect:/restaurar";
        }catch (Exception e){
            redirect.addFlashAttribute("error", "Hubo algún en el proceso");
            LOG.error(e.getMessage());
            return "redirect:/restaurar";
        }

        redirect.addFlashAttribute("success",
                "Su cotraseña ha sido cambiada con éxito");
        return "redirect:/login";
    }

    private Usuario getUsuarioByEmailUsuario(String email)throws UsuarioException{

        Usuario usuario = usuarioService.findUsuarioByEmail(email);

        if(usuario != null){
            return usuario;
        }else{
            throw new UsuarioException(1, "Usuario no encontrado");
        }
    }

}

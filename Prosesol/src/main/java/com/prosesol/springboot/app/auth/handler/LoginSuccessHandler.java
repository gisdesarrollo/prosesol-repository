package com.prosesol.springboot.app.auth.handler;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	private static boolean IS_ADMIN = false;
	private static boolean IS_USUARIO = false;
	private static boolean IS_ASISTENCIA = false;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		SessionFlashMapManager flashManager = new SessionFlashMapManager();
		
		String url = determineTargetUrl(authentication);
		
		FlashMap flashMap = new FlashMap();
		
		flashMap.put("success", authentication.getName() + " Ha iniciado sesión con éxito");
		flashManager.saveOutputFlashMap(flashMap, request, response);
		if(authentication != null) {
			logger.info("El usuario " + authentication.getName() + " ha iniciado sesión con éxito");
		}
		
		redirectStrategy.sendRedirect(request, response, url);
		
//		super.onAuthenticationSuccess(request, response, authentication);
	}

	private String determineTargetUrl(Authentication authentication) {

		
		Collection<? extends GrantedAuthority>authorities = authentication.getAuthorities();
		authorities.forEach(authority ->{
			if(authority.getAuthority().equals("ROLE_ADMINISTRADOR")) {
				IS_ADMIN = true;			
			}else if(authority.getAuthority().equals("ROLE_USUARIO")) {
				IS_USUARIO = true;
			}else if(authority.getAuthority().equals("ROLE_ASISTENCIA")) {
				IS_ASISTENCIA = true;
			}
		});
		
		if(IS_ADMIN || IS_USUARIO) {
			return "/home";
		}else if(IS_ASISTENCIA) {
			return "/homeAsistencia";
		}else {
			throw new IllegalStateException();
		}
		
	}

	
	
}

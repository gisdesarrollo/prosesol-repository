package com.prosesol.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.prosesol.springboot.app.auth.handler.LoginSuccessHandler;
import com.prosesol.springboot.app.service.JpaUserDetailService;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private LoginSuccessHandler successHandler;
		
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JpaUserDetailService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/jquery/**", "/webfonts/**", "/login").permitAll()
//			.antMatchers("/ver/**").hasAnyRole("USER")
//			.antMatchers("/crear/**").hasAnyRole("ADMIN")
			.anyRequest().authenticated()
			.and()
				.formLogin()
					.successHandler(successHandler)
					.loginPage("/login")
			.permitAll()
			.and()
			.logout().permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/error_403");
		
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder)throws Exception{
		
		builder.userDetailsService(userDetailsService)
			   .passwordEncoder(passwordEncoder);

	}
	
}

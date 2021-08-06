package com.example.userservice.security;

import javax.servlet.Filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final Environment env;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 권한
		http.csrf().disable();
		// http.authorizeRequests().antMatchers("/users/**").permitAll();
		http.authorizeRequests().antMatchers("/**")
			.hasIpAddress("172.30.64.236")
			.and()
			.addFilter(getAuthenticationFilter());

		// h2-console 창이 정상 작동하지 않는 현상 방지
		http.headers().frameOptions().disable();
	}

	private Filter getAuthenticationFilter() throws Exception {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, env);

		return authenticationFilter;
	}

	// 인증 관련
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 로그인을 처리해줌. 사용자 데이터를 조회
		// select pwd from users where email=?
		// db_pwd(encrypted) == input_pwd(encrypted)
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}
}

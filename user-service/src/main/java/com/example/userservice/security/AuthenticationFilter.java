package com.example.userservice.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final UserService userService;
	private final Environment env;

	public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
		super(authenticationManager);
		this.userService = userService;
		this.env = env;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		try {
			RequestLogin credits = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

			// 인증 처리를 해줌
			return getAuthenticationManager()
				.authenticate(
					new UsernamePasswordAuthenticationToken(
						credits.getEmail(),
						credits.getPassword(),
						new ArrayList<>()
					)
				);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		// 실제 로그인 성공시 정확하게 어떤 처리를 해줄 것인지. 토큰을 만들거나, 반환값 등을 처리하는 작업을 여기에 넣음
		String username = ((User)authResult.getPrincipal()).getUsername();
		UserDto userDetails = userService.getUserDetailsByEmail(username);

		String token = Jwts.builder()
			.setSubject(userDetails.getUserId())
			.setExpiration(
				new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
			.signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
			.compact();

		response.addHeader("token", token);
		response.addHeader("userId", userDetails.getUserId());
		// super.successfulAuthentication(request, response, chain, authResult);
	}
}

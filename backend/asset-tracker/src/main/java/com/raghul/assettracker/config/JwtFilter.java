package com.raghul.assettracker.config;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.raghul.assettracker.service.JwtUserService;
import com.raghul.assettracker.util.JwtUtils;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Resource
	JwtUtils jwtUtils;
	
	@Resource
	JwtUserService jwtUserService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String requestHeader = request.getHeader("Authorization");
		System.out.println(requestHeader);
		String username = null;
		String jwtToken = null;
		if(requestHeader != null && requestHeader.startsWith("Bearer ") ) {
			jwtToken = requestHeader.substring(7);
			try {
				
				username = jwtUtils.getUsernameFromToken(jwtToken);

				
			}catch(Exception e) {
				
			}
		}else {
			System.out.println("Jwt token does not begins with Bearer");
		}
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.jwtUserService.loadUserByUsername(username);
			if(jwtUtils.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

			}

		}
		filterChain.doFilter(request, response);

	}
	
	

}

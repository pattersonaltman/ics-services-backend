package com.cognixia.jump.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.util.JwtUtil;

// filters in spring are used to filter through requests/responses
// perform some check for security before a request is completed or a response is sent

// this filter will intercept every request coming in and examine the header for tokens

// will label this as a component so spring can recognize it and be able to autowire some of its properties
// and call it to filter requests before sending any responses
@Component
public class JwtRequestFilter extends OncePerRequestFilter { // abstract class that makes sure an action performed once when filter is called

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	// check the header of the request and make sure the jwt is valid in order to access the API its requesting
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// this is the header that contains our jwt info from the request
		final String authorizationHeader = request.getHeader("Authorization");
		
		String jwt = null;
		String username = null;
		
		// if header was null, no jwt was sent
		// then if there was a jwt, it would be formatted like "Bearer <token>"
		if( authorizationHeader != null && authorizationHeader.startsWith("Bearer ") ) {
			
			// token is there, grab the actual token string, remove the "Bearer " part of the string
			jwt = authorizationHeader.substring(7);
			
			// grab the user associated with this token
			// if token is not valid, will return a null
			username = jwtUtil.extractUsername(jwt);
			
		}
		
		// security context -> stores info on the currently authenticated users (users who have generated a jwt
		//					   and are using it to access APIs in this application)
		
		// if we found the user and not already in the security context...
		if( username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
			
			// ...load in their details
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			
			// check if token is valid based on the given user and as long as token is not expired
			if( jwtUtil.validateToken(jwt, userDetails) ) {
				
				// code below is what is usually done by default by spring security
				// but b/c we have our own token-based authentication, we have to place our tokens in security context
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request) );
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
						
		}
		
		// since there may be more filters that will need to access the request, we finish up and pass along
		// the request to the next filter in the chain
		filterChain.doFilter(request, response);
	}

}














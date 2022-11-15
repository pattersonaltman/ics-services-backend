package com.cognixia.jump.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cognixia.jump.filter.JwtRequestFilter;

@Configuration
public class SecurityConfiguration {
	
	private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
            // other public endpoints of your API may be appended to this array
    };

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
	// Authentication
	@Bean
	protected UserDetailsService userDetailsService() {
		
		return userDetailsService;
	}
	
	
	// Authorization
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		

//        http.
//                // ... here goes your custom security configuration
//                authorizeRequests().
//                antMatchers(AUTH_WHITELIST).permitAll().  // whitelist Swagger UI resources
//                // ... here goes your custom security configuration
//                antMatchers("/**").authenticated();  // require authentication for any endpoint that's not whitelisted
		
		
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/api/user").permitAll()
			.antMatchers(HttpMethod.PUT, "/api/user/update").access("hasRole('ADMIN')")
			.antMatchers(HttpMethod.GET, "/api/user").access("hasRole('ADMIN')")
			.antMatchers(HttpMethod.POST, "/api/game").access("hasRole('ADMIN')")
			.antMatchers(HttpMethod.PUT, "/api/game").access("hasRole('ADMIN')")
			.antMatchers(HttpMethod.DELETE, "/api/game").access("hasRole('ADMIN')")
			.antMatchers(HttpMethod.GET, "/api/purchase").access("hasRole('ADMIN')")
			.antMatchers(HttpMethod.GET, "/api/purchase/game").access("hasRole('ADMIN')")
			.antMatchers("/authenticate").permitAll() // anyone can create a JWT w/o needing to have a JWT first
			.antMatchers(AUTH_WHITELIST).permitAll()
			.anyRequest().authenticated() // all APIs, you have to have a user account
			.and()
			.sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS );
			 // tell spring security to NOT create sessions
		
		// request will go through many filters, but typically the FIRST filter it checks is the one for username & password
		// however, we will set it up, that our JWT filter gets checked first, or else the authentication will fail, since spring security
		// won't know where to find the username & password
		
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class );
		
		
		return http.build();
	}
	
	// manage password encoding
	@Bean
	protected PasswordEncoder encoder() {
		
		// plain text encoder -> encode/encrypt the password
		//return NoOpPasswordEncoder.getInstance();
		
		// encrypt the password with the bcrypt algorithm
		return new BCryptPasswordEncoder();
		
	}
	
	// load in the encoder & user details service that are needed for security to do authentication & authorization
	@Bean
	protected DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); 
		
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder( encoder() );
		
		return authProvider;
	}
	
	
	// can autowire and access the authentication manager (manages authentication (login) of our project)
	@Bean
	protected AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	
	
	
}
















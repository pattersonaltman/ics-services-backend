package com.cognixia.jump.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CORS -> policy or set of rules about which clients can consume APIs from this service

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
	
	// define the rules of which APIs can be consumed and by who
	@Override
	public void addCorsMappings(CorsRegistry registry)  {
		
		registry.addMapping("/**") // which paths are open to be consumed
				.allowedMethods( "GET", "POST", "PUT", "DELETE", "PATCH" ); // this request types also available
		
	}

}

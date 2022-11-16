package com.cognixia.jump.exception;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

// before the controller gives a response, gets some advice
// on how to construct that response here (exceptions)
@ControllerAdvice
public class GlobalExceptionHandler {

	// which exception to look for
	@ExceptionHandler(ResourceNotFoundException.class) 
	public ResponseEntity<?> resourceNotFound(ResourceNotFoundException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		
		return ResponseEntity.status(404).body(errorDetails);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> methodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {

		StringBuilder errors = new StringBuilder("");
		for(FieldError fe : ex.getBindingResult().getFieldErrors()) {
			errors.append( "[" + fe.getField() + " : " + fe.getDefaultMessage() + "]; " );
		}
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), errors.toString(), request.getDescription(false) );

		return ResponseEntity.status(400).body(errorDetails);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> usernameNotFound(UsernameNotFoundException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		
		return ResponseEntity.status(404).body(errorDetails);
		
	}
	

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> badCredentials(BadCredentialsException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		
		return ResponseEntity.status(404).body(errorDetails);
		
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> exception(Exception ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		
		return ResponseEntity.status(400).body(errorDetails);
		
	}
}

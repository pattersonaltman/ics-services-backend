package com.cognixia.jump.exception;

public class ResourceAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	
	
	
	public ResourceAlreadyExistsException(String msg) {
		super(msg);
	}
	
	
	
	
	
	public ResourceAlreadyExistsException(String resource, Long id) {
		super("[" + resource + "]" + " with id = [" + id + "] already exists");
	}
	
	
	
	
	
}

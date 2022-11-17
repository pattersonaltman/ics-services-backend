package com.cognixia.jump.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	UserRepository repo;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	UserService service;

	
	@GetMapping()
	public List<User> getUsers() {
		return repo.findAll();
	}
	
	@Operation(summary = "Create A User", 
			   description = "Allow a user to sign up and create a user object"
			   		+ "based on the request body. The request body and user"
			   		+ "object should each have username, password, email,"
			   		+ "phone number, first name, last name, and role. "
			   		+ "The user object will also contain a unique ID as well as "
			   		+ "an enabled value set to true by default.")
	
	@GetMapping("/id")
	public ResponseEntity<?> getUserById(@RequestParam Long user_id) throws ResourceNotFoundException {
		
		Optional<User> found = repo.findById(user_id);
		
		if(found.isPresent())
		{
			return ResponseEntity.status(200).body(found);
		}
		
		throw new ResourceNotFoundException("User", user_id);
	}
	
	@PostMapping()
	public ResponseEntity<?> createUser(@RequestBody User user) {
		
		user.setId(null);
		
		// each password for a new user gets encoded
		user.setPassword( encoder.encode( user.getPassword() ) );
		
		User created = repo.save(user);
		
		return ResponseEntity.status(201).body(created);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@Valid @RequestBody User user) throws Exception {
		
		user.setPassword( encoder.encode( user.getPassword() ) );
		User updated = service.updateUser(user);
		
		return ResponseEntity.status(200).body(updated);
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUserById(@RequestParam(name="user_id") Long id) throws ResourceNotFoundException {
		
		User deleted = service.deleteUserById(id);
		
		return ResponseEntity.status(200).body(deleted);
		
	}
	
	
}









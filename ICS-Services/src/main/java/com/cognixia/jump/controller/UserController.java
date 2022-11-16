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









package com.cognixia.jump.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.ResourceAlreadyExistsException;
import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.model.Service;
import com.cognixia.jump.repository.ServiceRepository;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

	
	
	@Autowired
	ServiceRepository repo;
	
	
	
	
	//Get all services
	@GetMapping()
	public List<Service> getAllServices() {
		return repo.findAll();
	}
	
	
	
	@GetMapping("/id")
	public ResponseEntity<?> getServiceById(@RequestParam(name = "serv_id") Long serv_id) throws ResourceNotFoundException{
		
		Optional<Service> found = repo.findById(serv_id);
		
		if(found.isPresent())
		{
			return ResponseEntity.status(200).body(found);
		}
		
		throw new ResourceNotFoundException("Service", serv_id);
	}
	
	
	
	@PostMapping()
	public ResponseEntity<?> createService(@RequestBody Service service) throws ResourceAlreadyExistsException {
		
		if(repo.existsById(service.getServ_id()))
		{
			throw new ResourceAlreadyExistsException("Service", service.getServ_id());
		}
		
		service.setServ_id(null);
		
		Service created = repo.save(service);
		
		return ResponseEntity.status(201).body(created);
	}
	
	
	
	
	@PutMapping("/update")
	public ResponseEntity<?> updateServiceById(@RequestBody Service service) throws ResourceNotFoundException {
		
		if(repo.existsById(service.getServ_id()))
		{
			Service updated = repo.save(service);
			return ResponseEntity.status(200).body(updated);
		}
		
		throw new ResourceNotFoundException("Service", service.getServ_id());
	}
	
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteServiceById(@RequestParam Long serv_id) throws ResourceNotFoundException {
		
		Optional<Service> deleted = repo.findById(serv_id);
		
		if(deleted.isPresent())
		{
			repo.deleteById(serv_id);
			return ResponseEntity.status(200).body(deleted);
		}
		
		throw new ResourceNotFoundException("Service", serv_id);
	}
	
	
	
}























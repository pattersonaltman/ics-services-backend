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
import com.cognixia.jump.model.Purchase;
import com.cognixia.jump.repository.PurchaseRepository;
import com.cognixia.jump.service.PurchaseService;

@RestController
@RequestMapping("api/purchases")
public class PurchaseController {

	@Autowired
	PurchaseRepository repo;
	
	@Autowired
	PurchaseService service;
	
	//Get all orders
	@GetMapping()
	List<Purchase> getAllPurchases() {
		return repo.findAll();
	}	
	
	//Get order by id
	@GetMapping("/id")
	public ResponseEntity<?> getPurchaseById(@RequestParam(name = "pur_id") Long pur_id) throws ResourceNotFoundException {
		
		Optional<Purchase> found = repo.findById(pur_id);
		
		if(found.isPresent())
		{
			return ResponseEntity.status(200).body(found);
		}
		
		throw new ResourceNotFoundException("Purchase", pur_id);
	}
	
	//Create purchase
	@PostMapping()
	public ResponseEntity<?> createPurchase(@RequestParam(name="serv_id") Long serv_id) throws ResourceNotFoundException {
		
		Purchase completed = service.createPurchase(serv_id);
		
		return ResponseEntity.status(201).body(completed);
		
	}
	
	//Delete an Order
	@DeleteMapping("/delete")
	public ResponseEntity<?> deletePurchaseById(@RequestParam Long pur_id) throws ResourceNotFoundException {
		
		Optional<Purchase> opt = repo.findById(pur_id);
		
		if(opt.isPresent())
		{
			Purchase deleted = opt.get();
			
			repo.deleteById(pur_id);
			
			return ResponseEntity.status(200).body(deleted);
		}
		
		throw new ResourceNotFoundException("Purchase", pur_id);
	}
	
	
	//Delete all Orders
	@DeleteMapping("/delete/all")
	public ResponseEntity<?> deleteAllPurchases() throws ResourceNotFoundException {
		
		if(repo.count() > 0)
		{
			long count = repo.count();
			repo.deleteAll();
			return ResponseEntity.status(200).body("Deleted [" + count + "] purchases");
		}
		
		throw new ResourceNotFoundException("No existing purchases");
	}

}
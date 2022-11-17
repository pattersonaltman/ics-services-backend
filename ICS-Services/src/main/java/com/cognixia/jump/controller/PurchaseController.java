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

@RestController
@RequestMapping("api/purchases")
public class PurchaseController {

	@Autowired
	PurchaseRepository repo;
	
	//Get all orders
	@GetMapping()
	List<Purchase> getAllPurchases() {
		return repo.findAll();
	}	
	
	//Get order by id
	@GetMapping("/id")
	public ResponseEntity<?> getOrderById(@RequestParam(name = "pur_id") Long pur_id) throws ResourceNotFoundException {
		
		Optional<Purchase> found = repo.findById(pur_id);
		
		if(found.isPresent())
		{
			return ResponseEntity.status(200).body(found);
		}
		
		throw new ResourceNotFoundException("Purchase", pur_id);
	}
	
	//Create an order
//	@PostMapping()
//	public ResponseEntity<?> createOrder() {
//		
//		Purchase completed = service.purchaseGameIdAndQty(game_id, 1);
//		
//		return ResponseEntity.status(201).body(completed);
//		
//	}
	
	
	//Update an order
//	@PutMapping()
//	
//		Optional<OrderItem> found = repo.findById(order_id);
//		
//		return null;	// finish
//		
//		
//	}
	
	
	//Delete an Order
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteOrderById(@RequestParam Long pur_id) throws ResourceNotFoundException {
		
		Optional<Purchase> opt = repo.findById(pur_id);
		
		if(opt.isPresent())
		{
			Purchase deleted = opt.get();
			
			repo.deleteById(pur_id);
			
			return ResponseEntity.status(200).body(deleted);
		}
		
		throw new ResourceNotFoundException("Purchase", pur_id);
	}

}
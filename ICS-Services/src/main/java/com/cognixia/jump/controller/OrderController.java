package com.cognixia.jump.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.cognixia.jump.model.OrderItem;
import com.cognixia.jump.model.Purchase;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.OrderRepository;
import com.cognixia.jump.repository.PurchaseRepository;
import com.cognixia.jump.repository.ServiceRepository;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.PurchaseService;

@RestController
@RequestMapping("api/orders")
public class OrderController {

	@Autowired
	OrderRepository repo;
	
	@Autowired
	PurchaseRepository purchaseRepo;
	
	@Autowired
	PurchaseService purchaseService;
	
	@Autowired
	ServiceRepository servRepo;
	
	//Get all orders
	@GetMapping()
	List<OrderItem> getAllOrders() {
		return repo.findAll();
	}	
	
	//Get order by id
	@GetMapping("/id")
	public ResponseEntity<?> getOrderById(@RequestParam(name = "order_id") Long order_id) throws ResourceNotFoundException {
		
		Optional<OrderItem> found = repo.findById(order_id);
		
		if(found.isPresent())
		{
			return ResponseEntity.status(200).body(found);
		}
		
		throw new ResourceNotFoundException("Order", order_id);
	}
	
	@PostMapping("/quote")
	public ResponseEntity<?> getOrderQuote(@RequestBody Long[] ids) throws ResourceNotFoundException {
		
		double discount = ids.length * 0.1;
		if(ids.length > 5)
		{
			discount = 0.5;
		}
		
		OrderItem quote = new OrderItem(0L, null, ids.length, discount, 0, new HashSet<Purchase>());
		
		for (int i=0; i < ids.length; i++ ) {
			Purchase purchase = purchaseService.createPurchaseQuote(ids[i]);
			quote.getPurchases().add(purchase);
			double price = servRepo.findById(ids[i]).get().getPrice();
			quote.setTotal(quote.getTotal() + price);
		}
		
		return ResponseEntity.status(200).body(quote);
	}
	
	@PostMapping("/checkout")
	public ResponseEntity<?> getOrderCheckout(@RequestBody Long[] ids) throws ResourceNotFoundException {
		
		User user = null;
		
		for (int i=0; i < ids.length; i++ ) {
			Purchase purchase = purchaseService.createPurchase(ids[i]);
			user = purchase.getUser();
		}
		
		Optional<OrderItem> opt = repo.findByUser(user);
		
		if(opt.isPresent())
		{
			OrderItem checkout = opt.get();
			return ResponseEntity.status(201).body(checkout);
		}
		
		throw new ResourceNotFoundException("No user found: Checkout could not be completed");
	}
	
	//Delete an Order
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteOrderById(@RequestParam Long order_id) throws ResourceNotFoundException {
		
		Optional<OrderItem> opt = repo.findById(order_id);
		
		if(opt.isPresent())
		{
			OrderItem deleted = opt.get();
			
			repo.deleteById(order_id);
			
			return ResponseEntity.status(200).body(deleted);
		}
		
		throw new ResourceNotFoundException("Order", order_id);
	}

	
}































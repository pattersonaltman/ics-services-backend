package com.cognixia.jump.service;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.model.OrderItem;
import com.cognixia.jump.model.Purchase;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.OrderRepository;
import com.cognixia.jump.repository.UserRepository;

@Service
public class OrderService {

	
	@Autowired
	OrderRepository repo;
	
	
	@Autowired
	UserRepository userRepo;
	
	
	
	
	//Create an order
	//No mapping needed [?]
	public ResponseEntity<?> createOrder(String username) throws ResourceNotFoundException {

		Optional<User> opt = userRepo.findByUsername(username);
		
		if(opt.isPresent())
		{
			User user = opt.get();
			OrderItem newOrder = new OrderItem(null, user, 0, 0, 0, new HashSet<Purchase>());
			repo.save(newOrder);
			return ResponseEntity.status(201).body(newOrder);
		}
		
		throw new ResourceNotFoundException("User", username);
	}
	
	
	//Update an order
	//No mapping needed [?]
	public ResponseEntity<?> updateOrderById(Long order_id, int qty, double discount, double total, Purchase purchase) throws ResourceNotFoundException {
		
		Optional<OrderItem> opt = repo.findById(order_id);
		
		if(opt.isPresent())
		{
			OrderItem update = opt.get();
			update.setQty(qty);
			update.setDiscount(discount);
			update.setTotal(total);
			update.getPurchases().add(purchase);
			update = repo.save(update);
			
			return ResponseEntity.status(200).body(update);
		}
		
		throw new ResourceNotFoundException("Order", order_id);
	}
	
	
	
	
}

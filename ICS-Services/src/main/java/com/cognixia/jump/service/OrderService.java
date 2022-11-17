package com.cognixia.jump.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.model.OrderItem;
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
	public ResponseEntity<?> createOrder() throws ResourceNotFoundException {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Optional<User> opt = userRepo.findByUsername(username);
		
		if(opt.isPresent())
		{
			User user = opt.get();
			OrderItem newOrder = new OrderItem(0L, user, 0, 0, 0);
			repo.save(newOrder);
			return ResponseEntity.status(201).body(newOrder);
		}
		
		throw new ResourceNotFoundException("User", username);
	}
	
	
	//Update an order
	//No mapping needed [?]
	public ResponseEntity<?> updateOrderById(Long order_id, int qty, double discount, double total) throws ResourceNotFoundException {
		
		Optional<OrderItem> opt = repo.findById(order_id);
		
		if(opt.isPresent())
		{
			OrderItem update = opt.get();
			update.setQty(qty);
			update.setDiscount(discount);
			update.setTotal(total);
			update = repo.save(update);
			
			return ResponseEntity.status(200).body(update);
		}
		
		throw new ResourceNotFoundException("Order", order_id);
	}
	
	
	
	
}

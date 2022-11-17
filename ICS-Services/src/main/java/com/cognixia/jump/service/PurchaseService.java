package com.cognixia.jump.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.model.OrderItem;
import com.cognixia.jump.model.Purchase;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.OrderRepository;
import com.cognixia.jump.repository.PurchaseRepository;
import com.cognixia.jump.repository.UserRepository;

@Service
public class PurchaseService {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PurchaseRepository repo;
	
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	OrderService orderService;

	public Purchase createPurchase(Long serv_id) throws ResourceNotFoundException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails)principal).getUsername();
		Optional<User> user = userRepo.findByUsername(username);
		if (user.isPresent()) {
			Optional<OrderItem> orderItem = orderRepo.findByUser(user);
			if (!orderItem.isPresent()) {
				orderService.createOrder(username);
				Optional<OrderItem> orderItem = orderRepo.findById(user.get().getId());
			}
		
			
		} else {
			throw new BadCredentialsException(username);
		}
		
	}

}

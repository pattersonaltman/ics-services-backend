package com.cognixia.jump.service;

import java.util.ArrayList;
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

import com.cognixia.jump.model.Services;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.OrderRepository;
import com.cognixia.jump.repository.PurchaseRepository;
import com.cognixia.jump.repository.ServiceRepository;
import com.cognixia.jump.repository.UserRepository;

@Service
public class PurchaseService {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PurchaseRepository repo;
	
	@Autowired
	ServiceRepository servRepo;
	
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	OrderService orderService;

	public Purchase createPurchase(Long serv_id) throws ResourceNotFoundException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails)principal).getUsername();
		Optional<User> user = userRepo.findByUsername(username);
		if (user.isPresent()) {
			User currUser = user.get();
			Optional<OrderItem> orderItem = orderRepo.findByUser(currUser);
			if (!orderItem.isPresent()) {
				orderService.createOrder(username);
				orderItem = orderRepo.findByUser(currUser);
			}
			OrderItem currOrder = orderItem.get();
			Optional<Services> service = servRepo.findById(serv_id);
			if (service.isPresent()) {
				Services currService = service.get();
				
//				List<Purchase> purchases = repo.findAll();
//				if(!purchases.isEmpty()) {
//					for (int i=0; i < purchases.size() ;i++) {
//						if (purchases.get(i).getService().getType() == currService.getType()) {
//							if (purchases.get(i).getUser() == currUser) {
//								repo.delete(purchases.get(i));
//							}
//						}
//					}
//				}
				
				Purchase currPurchase = new Purchase(null, currUser, currService, currOrder);
				repo.save(currPurchase);
				int newQty = currOrder.getQty()+1;
				double newDiscount = 0.1*newQty;
				if (newDiscount > 0.5) {
					newDiscount = 0.5;
				}
				double newTotal = currOrder.getTotal() + currService.getPrice();
				orderService.updateOrderById(currOrder.getOrder_id(), newQty, newDiscount, newTotal, currPurchase);
				return currPurchase;
			
			} else {
				throw new ResourceNotFoundException("service", serv_id);
			}
		} else {
			throw new BadCredentialsException(username);
		}
	}
	
	public Purchase createPurchaseQuote(Long serv_id) throws ResourceNotFoundException {
		
		Optional<Services> service = servRepo.findById(serv_id);
		if (service.isPresent()) {
			Services currService = service.get();
			Purchase purchase = new Purchase(0L, null, currService, null);
			return purchase;
		}
		else {
			throw new ResourceNotFoundException("Service", serv_id);
		}
	}
	
	
}

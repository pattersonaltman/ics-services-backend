package com.cognixia.jump.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.cognixia.jump.model.Services;
import com.cognixia.jump.model.Services.Type;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.OrderRepository;
import com.cognixia.jump.repository.PurchaseRepository;
import com.cognixia.jump.repository.ServiceRepository;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.OrderService;
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
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	OrderService orderService;
	
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
		
		OrderItem quote = new OrderItem(0L, null, 0, 0, 0, new HashSet<Purchase>());
		
		/* Parse @ids by Type and add into respective ArrayList */
		List<Services> cable = new ArrayList<Services>();
		List<Services> internet = new ArrayList<Services>();
		Set<Services> streaming = new HashSet<Services>();		// STREAMING as a Set -> allow multiple, but only 1 of each
		
		for(int i = 0; i < ids.length; i++)
		{
			Optional<Services> opt = servRepo.findById(ids[i]);
			if(opt.isPresent())
			{
				Services service = opt.get();
				
				if(service.getType() == Type.CABLE)
				{
					cable.add(service);
				}
				if(service.getType() == Type.INTERNET)
				{
					internet.add(service);
				}
				if(service.getType() == Type.STREAMING)
				{
					streaming.add(service);
				}
			}
		}
		
		Purchase purchase;
		double price;
		double discount = 0;
		
		/* Delete all CABLE elements in List except for last element; add last element as purchase choice and calculate total */
		int cableSize = cable.size() - 1;
		for(int i = 0; i < cableSize; i++)
		{
			cable.remove(0);
		}
		purchase = purchaseService.createPurchaseQuote(cable.get(0).getServ_id());
		quote.getPurchases().add(purchase);
		price = cable.get(0).getPrice();
		quote.setTotal(quote.getTotal() + price);
		for(int i = 0; i < cable.size(); i++)
		{
			quote.setQty(quote.getQty() + 1);
			discount += 1;
		}
		
		
		
		
		/* Delete all INTERNET elements in List except for last element; add last element as purchase choice and calculate total */
		int internetSize = internet.size() - 1;
		for(int i = 0; i < internetSize; i++)
		{
			internet.remove(0);
		}
		purchase = purchaseService.createPurchaseQuote(internet.get(0).getServ_id());
		quote.getPurchases().add(purchase);
		price = internet.get(0).getPrice();
		quote.setTotal(quote.getTotal() + price);
		for(int i = 0; i < internet.size(); i++)
		{
			quote.setQty(quote.getQty() + 1);
			discount += 1;
		}
		
		/* Add all STREAMING elements */
		for(Services serv : streaming)
		{
			purchase = purchaseService.createPurchaseQuote(serv.getServ_id());
			quote.getPurchases().add(purchase);
			price = serv.getPrice();
			quote.setTotal(quote.getTotal() + price);
			quote.setQty(quote.getQty() + 1);
			discount++;
		}
		
		discount *= 0.1;
		quote.setDiscount(discount);
		
		return ResponseEntity.status(200).body(quote);
	}
	
	
	
	@PostMapping("/checkout2")
	public ResponseEntity<?> getOrderCheckout2(@RequestBody Long[] ids) throws ResourceNotFoundException {
		
		OrderItem checkout = new OrderItem(null, null, 0, 0, 0, null);
		repo.save(checkout);
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails)principal).getUsername();
		Optional<User> user = userRepo.findByUsername(username);
		if (user.isPresent()) {
			User currUser = user.get();
			Optional<OrderItem> orderItem = repo.findByUser(currUser);
			if (!orderItem.isPresent()) {
				checkout = new OrderItem(null, currUser, ids.length, 0, 0, new HashSet<Purchase>());
			}
			else
			{
				Optional<OrderItem> opt = repo.findByUser(currUser);
				if(opt.isPresent())
				{
					checkout = opt.get();
				}
			}
			
		
//			OrderItem currOrder = orderItem.get();
//			Optional<Services> service = servRepo.findById(serv_id);
		
		
		
			/* Parse @ids by Type and add into respective ArrayList */
			List<Services> cable = new ArrayList<Services>();
			List<Services> internet = new ArrayList<Services>();
			Set<Services> streaming = new HashSet<Services>();		// STREAMING as a Set -> allow multiple, but only 1 of each
			
			for(int i = 0; i < ids.length; i++)
			{
				Optional<Services> opt = servRepo.findById(ids[i]);
				if(opt.isPresent())
				{
					Services service = opt.get();
					
					if(service.getType() == Type.CABLE)
					{
						cable.add(service);
					}
					if(service.getType() == Type.INTERNET)
					{
						internet.add(service);
					}
					if(service.getType() == Type.STREAMING)
					{
						streaming.add(service);
					}
				}
			}
			
			Purchase purchase;
			double price;
			double discount = 0;
			
			/* Delete all CABLE elements in List except for last element; add last element as purchase choice and calculate total */
			int cableSize = cable.size() - 1;
			for(int i = 0; i < cableSize; i++)
			{
				cable.remove(0);
			}
			repo.save(checkout);
			purchase = purchaseService.createPurchaseCheckout(cable.get(0).getServ_id(), currUser, checkout);
			checkout.getPurchases().add(purchase);
			price = cable.get(0).getPrice();
			checkout.setTotal(checkout.getTotal() + price);
			for(int i = 0; i < cable.size(); i++)
			{
				checkout.setQty(checkout.getQty() + 1);
				discount += 1;
			}
			
			
			/* Delete all INTERNET elements in List except for last element; add last element as purchase choice and calculate total */
			int internetSize = internet.size() - 1;
			for(int i = 0; i < internetSize; i++)
			{
				internet.remove(0);
			}
			repo.save(checkout);
			purchase = purchaseService.createPurchaseCheckout(internet.get(0).getServ_id(), currUser, checkout);
			checkout.getPurchases().add(purchase);
			price = internet.get(0).getPrice();
			checkout.setTotal(checkout.getTotal() + price);
			for(int i = 0; i < cable.size(); i++)
			{
				checkout.setQty(checkout.getQty() + 1);
				discount += 1;
			}
			
			/* Add all STREAMING elements */
			for(Services serv : streaming)
			{
				repo.save(checkout);
				purchase = purchaseService.createPurchaseCheckout(serv.getServ_id(), currUser, checkout);
				checkout.getPurchases().add(purchase);
				price = serv.getPrice();
				checkout.setTotal(checkout.getTotal() + price);
				checkout.setQty(checkout.getQty() + 1);
				discount++;
			}
			
			discount *= 0.1;
			checkout.setDiscount(discount);
			
			checkout.setQty((int) (discount * 10));
			
			repo.save(checkout);
			
			return ResponseEntity.status(200).body(checkout);
		}
		else {
			throw new ResourceNotFoundException("User not found");
		}
		
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































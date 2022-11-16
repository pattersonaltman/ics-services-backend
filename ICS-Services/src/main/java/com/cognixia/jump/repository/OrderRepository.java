package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.OrderItem;

@Repository
public interface OrderRepository extends JpaRepository<OrderItem, Long> {

	
	
	
	
}

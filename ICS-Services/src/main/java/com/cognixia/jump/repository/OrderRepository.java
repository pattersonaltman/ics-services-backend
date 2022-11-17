package com.cognixia.jump.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.OrderItem;
import com.cognixia.jump.model.User;

@Repository
public interface OrderRepository extends JpaRepository<OrderItem, Long> {

	public Optional<OrderItem> findByUser(User user);
	
}

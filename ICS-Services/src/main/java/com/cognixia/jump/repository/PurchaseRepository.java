package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognixia.jump.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}

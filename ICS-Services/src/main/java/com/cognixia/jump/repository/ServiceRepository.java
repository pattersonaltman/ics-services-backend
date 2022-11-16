package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {


	
}

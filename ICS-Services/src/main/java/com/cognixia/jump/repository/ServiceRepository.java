package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Services;
import com.cognixia.jump.model.Services.Type;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {


	
	public List<Services> findAllByType(Type t); 
	
	
	
}

package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Services;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {


	
}

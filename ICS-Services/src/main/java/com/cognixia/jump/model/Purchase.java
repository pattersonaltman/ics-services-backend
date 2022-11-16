package com.cognixia.jump.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Purchase implements Serializable {

	private static final long serialVersionUID = 1L;



	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "purchase_id")
	Long purchase_id;
	
	@JsonIgnoreProperties
	Service service;
	
	@JsonIgnoreProperties
	User user;
	
//	@JsonIgnoreProperties
//	Order order;
	
	
	
	
	
	
}

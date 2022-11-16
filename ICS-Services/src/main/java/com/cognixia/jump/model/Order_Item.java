package com.cognixia.jump.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class Order_Item implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	
	
	//fields
	
	Long id;
	
	User user;
	
	private int qty;
		
	private double discount;
	
	private double total;
	
	
	
	
	
	
	
	

}

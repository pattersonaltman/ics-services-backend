package com.cognixia.jump.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Purchase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "pur_id")
	Long id;
	
	@JsonIgnoreProperties("purchases")
	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;
	
	@JsonIgnoreProperties("purchases")
	@ManyToOne
	@JoinColumn(name = "serv_id")
	Service service;
	
	@JsonIgnoreProperties("purchases")
	@ManyToOne
	@JoinColumn(name = "order_id")
	OrderItem orderItem;
	
}

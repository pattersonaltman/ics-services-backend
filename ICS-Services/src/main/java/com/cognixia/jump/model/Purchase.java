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
	Services service;
	
	@JsonIgnoreProperties("purchases")
	@ManyToOne
	@JoinColumn(name = "order_id")
	OrderItem orderItem;


	public Purchase() {
		
	}



	public Purchase(Long id, User user, Services service, OrderItem orderItem) {
		super();
		this.id = id;
		this.user = user;
		this.service = service;
		this.orderItem = orderItem;
	}




	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Services getService() {
		return service;
	}


	public void setService(Services service) {
		this.service = service;
	}


	public OrderItem getOrderItem() {
		return orderItem;
	}


	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}


	@Override
	public String toString() {
		return "Purchase [id=" + id + ", user=" + user + ", service=" + service + ", orderItem=" + orderItem + "]";
	}
	

	
}

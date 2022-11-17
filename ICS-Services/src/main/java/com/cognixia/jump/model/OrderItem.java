package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class OrderItem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//fields
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "order_id")
	Long order_id;
	
	@JsonIgnoreProperties
	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;
	
	@Column(nullable = false)
	private int qty;
		
	@Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
	private double discount;
	
	@Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
	private double total;
	
	
	//One To Many with Purchases
	@JsonIgnoreProperties({"orderItem","user"})
	@OneToMany(mappedBy = "orderItem", fetch = FetchType.EAGER)
	private Set<Purchase> purchases = new HashSet<>();
	
	
	public OrderItem() {
		
	}
	

	public OrderItem(Long order_id, User user, int qty, double discount, double total) {
		super();
		this.order_id = order_id;
		this.user = user;
		this.qty = qty;
		this.discount = discount;
		this.total = total;
	}





	public Long getOrder_id() {
		return order_id;
	}





	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}





	public User getUser() {
		return user;
	}





	public void setUser(User user) {
		this.user = user;
	}





	public int getQty() {
		return qty;
	}





	public void setQty(int qty) {
		this.qty = qty;
	}





	public double getDiscount() {
		return discount;
	}





	public void setDiscount(double discount) {
		this.discount = discount;
	}





	public double getTotal() {
		return total;
	}





	public void setTotal(double total) {
		this.total = total;
	}





	@Override
	public String toString() {
		return "OrderItem [order_id=" + order_id + ", user=" + user + ", qty=" + qty + ", discount=" + discount
				+ ", total=" + total + "]";
	}
	
	
	
	
	
	
	
	
	

}

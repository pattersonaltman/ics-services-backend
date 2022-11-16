package com.cognixia.jump.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Service implements Serializable {

	
	
	private static final long serialVersionUID = 1L;

	
	
	
	
	public static enum Type {
		
		INTERNET1("This is internet 1"), INTERNET2("This is internet 2"), INTERNET3("This is internet 3"), 
		CABLE1("This is cable 1"), CABLE2("This is cable 2"), CABLE3("This is cable 3"),
		NETFLIX("This is Netflix"), HULU("This is Hulu"), HBO("This is HBO");
		
		private String descrip;
		
		private Type() {}
		
		private Type(String descrip) {
			this.descrip = descrip;
		}
	}
	
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long serv_id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Type type;
	
	@Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
	private double price;

	
	
	
	
	public Service() {
		
	}
	
	
	public Service(Long serv_id, String name, Type type, double price) {
		super();
		this.serv_id = serv_id;
		this.name = name;
		this.type = type;
		this.price = price;
	}


	public Long getServ_id() {
		return serv_id;
	}


	public void setServ_id(Long serv_id) {
		this.serv_id = serv_id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Type getType() {
		return type;
	}


	public void setType(Type type) {
		this.type = type;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	@Override
	public String toString() {
		return "Service [serv_id=" + serv_id + ", name=" + name + ", type=" + type + ", price=" + price + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
























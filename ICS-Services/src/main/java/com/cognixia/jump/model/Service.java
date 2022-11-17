package com.cognixia.jump.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class Service implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static enum Type {
		CABLE, INTERNET, STREAMING;
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long serv_id;
	
	@Column(nullable = false)
	@Schema(description = "Model for service information", example = "Netflix", required = true)
	private String name;
	
	@Column(nullable = false)
	private Type type;
	
	@Column(nullable = false)
	private String description;
	


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

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	@Override
	public String toString() {
		return "Service [serv_id=" + serv_id + ", name=" + name + ", type=" + type + ", description=" + description
				+ ", price=" + price + "]";
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
}
























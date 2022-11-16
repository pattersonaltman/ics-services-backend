package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.cognixia.jump.model.User.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum Role {
		ROLE_USER, ROLE_ADMIN
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // incrementation will use auto_increment
	@Column(name = "user_id")
	private Long id;
	
	@Size(min = 1, max = 100)
	@Column(nullable = false, unique = true, length = 25)
	private String username;

	@Column(nullable = false, length = 255)
	private String password;
	
	@Column(name="first_name", nullable = false)
	private String firstName;
	
	@Column(name="last_name", nullable = false)
	private String lastName;
	
	@Column(nullable = false, unique = true)
	@Email(message = "Not a valid email format.")
	private String email;
	
	@Column(name="phone", nullable = false)
	private String phone;
	
	@Column(nullable = false)
	@Valid
	@Temporal(TemporalType.DATE)
	private Date dob;
	
	// will store the role as a string in the db
	@Column(updatable = false, nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(columnDefinition = "boolean default true")
	private boolean enabled;
	
	//One To Many with Purchases
	@JsonIgnoreProperties("user")
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private Set<Purchase> purchases = new HashSet<>();
	
	//One To Many with Purchases
//	@JsonIgnoreProperties("user")
//	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
//	private Set<OrderItem> orderItems = new HashSet<>();
	
	public User() {
		this.id = 1L;
		this.username = "N/A";
		this.password = "N/A";
		this.firstName = "N/A";
		this.lastName = "N/A";
		this.email = "N/A";
		this.phone = "N/A";
		this.dob = new Date();
		this.role = null;
		this.enabled = false;
		this.purchases = null;
	}
	
	public User(Long id, @Size(min = 1, max = 100) String username, String password, String firstName, String lastName,
			@Email(message = "Not a valid email format.") String email, String phone, @Valid Date dob, Role role,
			boolean enabled, Set<Purchase> purchases) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.dob = dob;
		this.role = role;
		this.enabled = enabled;
		this.purchases = purchases;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public @Valid Date getDob() {
		return dob;
	}

	public void setDob(@Valid Date dob) {
		this.dob = dob;
	}

	public Set<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(Set<Purchase> purchases) {
		this.purchases = purchases;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", phone=" + phone + ", dob=" + dob + ", role=" + role
				+ ", enabled=" + enabled + ", purchases=" + purchases + "]";
	}

}

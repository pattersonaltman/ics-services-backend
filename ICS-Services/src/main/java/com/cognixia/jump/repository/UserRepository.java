package com.cognixia.jump.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { // JpaRepository< table, datatype id / primary key>
	
	// custom query for finding users by username
	// important for security, security will only know how to find a user by the username
	// Optional -> possiblity that no user exists with this username, so we represent that with an optional (could be null)
	public Optional<User> findByUsername(String username);

}

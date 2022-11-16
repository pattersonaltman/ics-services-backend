package com.cognixia.jump.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.cognixia.jump.model.User;
import com.cognixia.jump.model.User.Role;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.service.UserService;
import com.cognixia.jump.util.JwtUtil;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

	@MockBean
	UserService service;
	
	@MockBean
	UserRepository repo;
	
	@MockBean
	MyUserDetailsService muds;
	
	@MockBean
	JwtUtil jwtu;
	
	@MockBean
	PasswordEncoder pe;
	
	@InjectMocks
	UserController controller;
	
	@Autowired
	MockMvc mockMvc;
	
	@WithMockUser(username="admin")
	@Test
	void testGetUsers() throws Exception {
		String uri = "/api/user";
		
		List<User> users = new ArrayList<User>();
		
		users.add( new User(1L, "user1", "pass", "user", "name", "user@name.com",
				"1234567890", new Date(), Role.ROLE_USER, true, null));
		users.add( new User(2L, "user2", "pass", "user2", "name", "user2@name.com",
				"1234567891", new Date(), Role.ROLE_USER, true, null));
		
		when( repo.findAll() ).thenReturn( users );
		
		mockMvc.perform( get(uri) )
		.andDo( print() )
		.andExpect( status().isOk() )
		.andExpect( jsonPath("$.length()").value( users.size() ) ) // check size of resulting array
		.andExpect( jsonPath("$[0].id").value( users.get(0).getId() ) ) // checks if first student has id = 1
		.andExpect( jsonPath("$[1].username").value( users.get(1).getUsername() ) ) // checks second student has lastName = platypus
		.andExpect( jsonPath("$[0].phone").value( users.get(0).getPhone()) )
		;

		// verify can check how many times a method is called during a test
		verify( repo, times(1) ).findAll(); // check this method is only called once
		
		// make sure no more methods from repo are being called
		verifyNoMoreInteractions( repo );
	}
//
//	@Test
//	void testCreateUser() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testUpdateUser() {
//		fail("Not yet implemented");
//	}

}


package com.raghul.assettracker.controller;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raghul.assettracker.dto.AuthenticateRequestDTO;
import com.raghul.assettracker.dto.UserDTO;
import com.raghul.assettracker.service.UserService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
@Api( tags = "User Management")
public class UserController {
	
	@Resource
	UserService userService;
	
	
	/**
	 * Method to register the user 
	 * @param userDTO
	 * @return
	 */
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){
		return ResponseEntity.ok().body(userService.createUser(userDTO));
	}
	
	/**
	 * Method to authenticate the user
	 * @param authenticateRequestDTO
	 * @return token  {AuthenticationResponse}
	 */
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticateUser(@RequestBody AuthenticateRequestDTO authenticateRequestDTO){
		return ResponseEntity.ok().body(userService.authenticateUser(authenticateRequestDTO));
	}

}

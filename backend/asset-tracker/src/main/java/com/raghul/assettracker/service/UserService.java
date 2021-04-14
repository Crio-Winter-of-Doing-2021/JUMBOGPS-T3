package com.raghul.assettracker.service;

import com.raghul.assettracker.dto.AuthenticateRequestDTO;
import com.raghul.assettracker.dto.AuthenticateResponseDTO;
import com.raghul.assettracker.dto.UserDTO;

public interface UserService {
	
	/**
	 * Method to create user
	 * @param userDTO
	 * @return userId
	 */
	 UserDTO createUser(UserDTO userDTO);
	 
	 /**
	  * Method to authenticate user
	  * @param authenticateRequestDTO
	  * @return AuthenticateResponseDTO
	  */
	 AuthenticateResponseDTO authenticateUser(AuthenticateRequestDTO authenticateRequestDTO);
	 
	 void logout();
	
}

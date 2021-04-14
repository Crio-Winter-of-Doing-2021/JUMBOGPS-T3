package com.raghul.assettracker.service;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.raghul.assettracker.dto.AuthenticateRequestDTO;
import com.raghul.assettracker.dto.AuthenticateResponseDTO;
import com.raghul.assettracker.dto.UserDTO;
import com.raghul.assettracker.manager.UserManager;
import com.raghul.assettracker.model.UserModel;
import com.raghul.assettracker.translator.UserTranslator;
import com.raghul.assettracker.util.CommonConstants;
import com.raghul.assettracker.util.JwtUtils;

@Component
public class UserServiceImpl implements UserService {
	
	@Resource
	JwtUtils jwtUtils;
	
	@Resource
	UserManager userManager;
	
	@Resource
	UserTranslator userTranslator;
	
	@Resource
	private AuthenticationManager authenticationManager;
	
	
	@Resource
	private JwtUserService jwtUserService;

	/**
	 *Method to create the user
	 *@param UserDTO userDTO
	 *@return UserDTO 
	 */
	@Override
	public UserDTO createUser(UserDTO userDTO) {
		UserModel user = userTranslator.translateToUser(userDTO);
		UUID userId = userManager.save(user);
		 userDTO = new UserDTO();
		 userDTO.setUserId(userId.toString());
		 return userDTO;
	}

	
	@Override
	public void logout() {
		
	}


	/**
	 * Method to authenticate user
	 * @param authenticateRequestDTO {AuthenticateRequestDTO}
	 * @return response of the token {AuthenticateResponseDTO}
	 */
	@Override
	public AuthenticateResponseDTO authenticateUser(AuthenticateRequestDTO authenticateRequestDTO) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticateRequestDTO.getUserName(), authenticateRequestDTO.getPassword()));
		UserDetails userDetails = jwtUserService.loadUserByUsername(authenticateRequestDTO.getUserName());
		String token = jwtUtils.generateToken(userDetails);
		AuthenticateResponseDTO response = new AuthenticateResponseDTO();
		response.setJsonToken(token);
        response.setExpiresIn(CommonConstants.JWT_TOKEN_VALIDITY);		
		return response;
	}

}

package com.raghul.assettracker.manager;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.raghul.assettracker.model.UserModel;
import com.raghul.assettracker.repository.UserRepository;

@Service
public class UserManager {

	@Resource
	private UserRepository userRepository;
	
	
	/**
	 * Method to find user by Id
	 * @param userId
	 * @return User details
	 */
	public UserModel findUserByUserId(UUID userId) {
		return userRepository.findByUserId(userId);
	}

	/**
	 * Method to find user by username
	 * @param userName
	 * @return User Details
	 */
	public UserModel findUserByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	/**
	 * Method to save the user 
	 * @param user
	 * @return unique id of the user
	 */
	public UUID save(UserModel user) {
		return userRepository.save(user).getUserId();
	}
}

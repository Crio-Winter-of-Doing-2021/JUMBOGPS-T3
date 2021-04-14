package com.raghul.assettracker.translator;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.raghul.assettracker.dto.UserDTO;
import com.raghul.assettracker.manager.ReferenceManager;
import com.raghul.assettracker.model.UserModel;

@Component
public class UserTranslator {
	
	@Resource
	private ReferenceManager referenceManager;
	
	public UserModel translateToUser(UserDTO userDTO) {
		UserModel user = new UserModel();
		user.setUserName(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		user.setEmailId(userDTO.getEmailId());
        user.setMobileNo(userDTO.getMobileNo());
        user.setUserId(UUID.randomUUID());
        user.setCreatedBy(user.getUserId().toString());
        
		return user;
	}

}

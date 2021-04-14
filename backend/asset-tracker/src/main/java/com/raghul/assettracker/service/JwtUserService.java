package com.raghul.assettracker.service;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.raghul.assettracker.manager.UserManager;
import com.raghul.assettracker.model.UserModel;

@Component
public class JwtUserService implements UserDetailsService {
	
	@Resource
	private UserManager userManager;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	 UserModel user = 	userManager.findUserByUserName(username);
	 if(user!= null) {
		 return new User(user.getUserName(),user.getPassword(),new ArrayList<>());
	 }
		throw new UsernameNotFoundException("No user found");
	}

}

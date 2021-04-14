package com.raghul.assettracker.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raghul.assettracker.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel,UUID>{

	UserModel findByUserId(UUID userId);

	UserModel findByUserName(String userName);

}

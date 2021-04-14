package com.raghul.assettracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raghul.assettracker.model.NotificationModel;

public interface NotificationModelRepository extends JpaRepository<NotificationModel,UUID>{

	List<NotificationModel> findByAssetId(UUID assetId);

}

package com.raghul.assettracker.service;

import java.util.List;
import java.util.UUID;

import com.raghul.assettracker.dto.NotificationDTO;

public interface NotificationService {
	
	/**
	 * Method to save notification
	 * @param notification
	 */
	void saveNotification(NotificationDTO notification);
	
	/**
	 * Method to get all the notifications
	 * @return list of notifications
	 */
	List<NotificationDTO> getAllNotification();
	
	/**
	 * Method to get notification for the asset
	 * @param assetId
	 * @return notifications for the asset
	 */
	
	List<NotificationDTO> getNotificationForAsset(UUID assetId);

}

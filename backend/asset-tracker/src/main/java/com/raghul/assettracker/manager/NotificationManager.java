package com.raghul.assettracker.manager;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.raghul.assettracker.model.NotificationModel;
import com.raghul.assettracker.repository.NotificationModelRepository;

@Component
public class NotificationManager {
	
	@Resource
	NotificationModelRepository notificationModelRepository;

	/**
	 * Method to save the notification
	 * @param notif notification obj
	 */
	public void saveNotification(NotificationModel notif) {
		notificationModelRepository.save(notif);
	}
	
	
	/**
	 * Method to get all the notifications
	 * @return list of notifications
	 */
	public List<NotificationModel> getAllNotification(){
		return notificationModelRepository.findAll();
	}
	
	/**
	 * Method to get all the notifications by asset id
	 * @param assetId
	 * @return list of notificationModel
	 */
	public List<NotificationModel> getAllNotificationByAssetId(UUID assetId){
		return notificationModelRepository.findByAssetId(assetId);
	}
}

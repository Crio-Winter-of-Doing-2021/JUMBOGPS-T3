package com.raghul.assettracker.service;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.raghul.assettracker.dto.NotificationDTO;
import com.raghul.assettracker.manager.NotificationManager;
import com.raghul.assettracker.model.NotificationModel;
import com.raghul.assettracker.translator.NotificationTranslator;

@Component
public class NotificationServiceImpl implements NotificationService {
	
	@Resource
	private NotificationTranslator notificationTranslator;
	
	@Resource
	private NotificationManager notificationManager;
	

	@Override
	public void saveNotification(NotificationDTO notification) {
		NotificationModel model = notificationTranslator.translateToNotificationModel(notification);
		System.out.println("value is working");
		notificationManager.saveNotification(model);
		
	}

	@Override
	public List<NotificationDTO> getAllNotification() {
		List<NotificationModel> notifications = notificationManager.getAllNotification();
		return notificationTranslator.translateToNotificationDTOs(notifications);
	}

	@Override
	public List<NotificationDTO> getNotificationForAsset(UUID assetId) {
		List<NotificationModel> notifications = notificationManager.getAllNotificationByAssetId(assetId);
		return notificationTranslator.translateToNotificationDTOs(notifications);
	}
	

}

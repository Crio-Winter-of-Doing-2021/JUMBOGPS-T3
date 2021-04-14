package com.raghul.assettracker.translator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.raghul.assettracker.dto.NotificationDTO;
import com.raghul.assettracker.manager.AssetManager;
import com.raghul.assettracker.manager.ReferenceManager;
import com.raghul.assettracker.model.Asset;
import com.raghul.assettracker.model.NotificationModel;
import com.raghul.assettracker.model.RefTerm;
import com.raghul.assettracker.util.CommonConstants;

@Component
public class NotificationTranslator {
	
	@Resource
	private ReferenceManager referenceManager;
	
	@Resource
	private AssetManager assetManager;
	
	@Resource
	private AssetTranslator assetTranslator;

	public NotificationModel translateToNotificationModel(NotificationDTO notification) {
		RefTerm refTerm = referenceManager.findRefTermByValue(CommonConstants.SYSTEM_USER);
		NotificationModel notiModel = new NotificationModel();
		notiModel.setAssetId(UUID.fromString(notification.getAssetId()));
		notiModel.setMessage(notification.getMessage());
		notiModel.setNotificationId(UUID.randomUUID());
		notiModel.setCreatedBy(refTerm.getRefTermId().toString());
		return notiModel;
	}
	
	public NotificationDTO translateToNotificationDTO(NotificationModel notification) {
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setAssetId(notification.getAssetId().toString());
		notificationDTO.setMessage(notification.getMessage());
		notificationDTO.setNotificationId(notification.getNotificationId().toString());
		Asset asset = assetManager.findByAssetId(notification.getAssetId());
		notificationDTO.setAsset(assetTranslator.translateToAssetDTO(asset));
		return notificationDTO;
	}
	
	public List<NotificationDTO> translateToNotificationDTOs(List<NotificationModel> notifications){
		if(notifications != null && !notifications.isEmpty()) {
			List<NotificationDTO> notificationDTOs = new ArrayList<>();
			notifications.forEach(
					notification->{
						NotificationDTO notif  = translateToNotificationDTO(notification);
						notificationDTOs.add(notif);
					}
					);
			return notificationDTOs;
		}
		return Collections.emptyList();
	}
}

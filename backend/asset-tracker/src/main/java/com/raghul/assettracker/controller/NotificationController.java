package com.raghul.assettracker.controller;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raghul.assettracker.dto.NotificationDTO;
import com.raghul.assettracker.service.NotificationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/notification")
@Api( tags = "Notifications")
@CrossOrigin
public class NotificationController {
	
	@Resource
	NotificationService notificationService;
	
	@GetMapping("")
	public ResponseEntity<?> getAllNotifications(){
		return ResponseEntity.ok(notificationService.getAllNotification());
	}
	
	@GetMapping("{assetId}")
	public ResponseEntity<?> getAssetNotifications(@PathVariable("assetId") UUID assetId ){
		return ResponseEntity.ok(notificationService.getNotificationForAsset(assetId));
	}
	
	@PostMapping("")
	public ResponseEntity<?> saveNotification(@RequestBody NotificationDTO notificationDTO){
		notificationService.saveNotification(notificationDTO);
		return ResponseEntity.ok().build();
	}

}

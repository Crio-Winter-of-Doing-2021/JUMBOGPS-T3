package com.raghul.assettracker.service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.raghul.assettracker.dto.AssetDTO;
import com.raghul.assettracker.dto.AssetGeofenceDTO;
import com.raghul.assettracker.dto.AssetRouteDTO;
import com.raghul.assettracker.dto.CoordinateDTO;
import com.raghul.assettracker.dto.GeofenceDTO;
import com.raghul.assettracker.dto.NotificationDTO;
import com.raghul.assettracker.exception.AssetLocationNotFoundException;
import com.raghul.assettracker.manager.AssetManager;
import com.raghul.assettracker.manager.LocationManager;
import com.raghul.assettracker.model.Asset;
import com.raghul.assettracker.model.AssetGeofenceModel;
import com.raghul.assettracker.model.AssetLocation;
import com.raghul.assettracker.model.AssetRouteModel;
import com.raghul.assettracker.model.AssetRoutePathModel;
import com.raghul.assettracker.translator.LocationTranslator;

@Component
public class LocationServiceImpl implements LocationService {
	
	@Resource
	LocationManager locationManager;
	
	@Resource
	LocationTranslator locationTranslator;
	
	@Resource
	AssetManager assetManager;
	
	
	@Resource
	NotificationService notificationService;

	@Override
	public List<AssetDTO> getAssets(Integer fromIndex,Integer maxSize,List<UUID> assetTypes,String assetName,Timestamp startTime,Timestamp endTime) {
		boolean isQuery = false;
		if((Objects.nonNull(assetTypes) && !assetTypes.isEmpty()) || assetName!=null || (startTime!=null && endTime!=null)) {
			isQuery = true;
		}
		assetName = assetName == null ? "" : assetName.trim();
		List<String> assetIds = Collections.emptyList();
		List<Asset> assets = locationManager.getAsset(assetTypes,assetName);
		if(assets != null && !assets.isEmpty()) {
			assetIds = assets.stream().map(Asset::getAssetId).
					map(item -> item.toString()).
					collect(Collectors.toList());
		}
		if(isQuery && assets.isEmpty()) {
			return Collections.emptyList();
		}
		List<AssetLocation> locations = locationManager.getAssetLocation(assetIds,fromIndex,maxSize,startTime,endTime);
		if(Objects.nonNull(locations) && !locations.isEmpty()) {
			return locationTranslator.translateToAssetDTOs(locations);
		}
		
		return Collections.emptyList();
	}
	
	
	@Override
	public AssetDTO getAssetTimeline(UUID assetId) throws AssetLocationNotFoundException {
			Asset asset = assetManager.findByAssetId(assetId);
			AssetLocation assetLocation  = locationManager.findOneByAssetId(assetId);
			List<AssetLocation> locations = locationManager.findLocationByCreatedOn(assetId.toString(),assetLocation);
			return locationTranslator.translateToAssetDTO(locations,asset);
	}


	@Override
	public void saveAssetGeofence(GeofenceDTO assetGeofenceDTO) {
		
		AssetGeofenceModel assetGeofenceModel = locationTranslator.translateToAssetGeofenceModel(assetGeofenceDTO);
	   locationManager.saveAssetGeofence(assetGeofenceModel);
	   
		
	}


	@Override
	public AssetGeofenceDTO getAssetGeofence(UUID assetId) throws AssetLocationNotFoundException {
		AssetGeofenceModel assetGeofence = locationManager.getAssetGeofenceByAssetId(assetId);
	  AssetLocation assetLocation  = locationManager.findOneByAssetId(assetId);
	  Asset asset = assetManager.findByAssetId(assetId);
	   
	  AssetGeofenceDTO assetGeofenceDTO  = locationTranslator.translateToAssetGeofenceDTO(assetLocation,asset,assetGeofence);
	   if(assetGeofenceDTO!=null && assetGeofenceDTO.getIsWithinFence() != null && assetGeofenceDTO.getIsWithinFence() == false) {
			String message = asset.getAssetName()+" is not within geofence";
			assetGeofenceDTO.setMessage(message);
		   ExecutorService exe =  Executors.newSingleThreadScheduledExecutor();
		  exe.execute(()->{
			  NotificationDTO notification = new NotificationDTO();
			  notification.setAssetId(assetId.toString());
			  notification.setMessage(message);
			  notificationService.saveNotification(notification);
		  });
		  
	   }
		return assetGeofenceDTO;
	}


	@Override
	public List<GeofenceDTO> getAssetsGeofence(List<UUID> assetIds) {
		List<AssetGeofenceModel> assetsGeofence = locationManager.getAssetsGeofence(assetIds);
		return locationTranslator.translateToAssetGeofenceDTOs(assetsGeofence);
	}


	@Override
	public void saveAssetRoute(AssetRouteDTO assetRouteDTO) {
		AssetRouteModel routeModel = locationTranslator.translateToAssetRouteModel(assetRouteDTO);
		UUID routeModelId = locationManager.saveAssetRoute(routeModel);
		routeModel.setAssetRouteId(routeModelId);
		List<AssetRoutePathModel> routePaths = locationTranslator.translateToAssetRoutePath(routeModel, assetRouteDTO);
		locationManager.saveAssetRoutePath(routePaths);
		
	}


	@Override
	public AssetRouteDTO getAssetRoute(UUID assetId) {
		AssetRouteModel routeModel = locationManager.findByAssetId(assetId);
		System.out.println("sa"+routeModel);
		List<AssetRoutePathModel> routes = locationManager.findByAssetRoute(routeModel.getAssetRouteId());
		System.out.println("rote"+routes.size());
		return locationTranslator.translateToRouteDTO(routeModel, routes);
		
	}


	@Override
	public AssetRouteDTO checkAssetRoute(UUID assetId,UUID assetRoute, CoordinateDTO coordinate) {
           List<AssetRoutePathModel> routes = locationManager.findByAssetRoute(assetRoute);	
          AssetRouteDTO assetRouteDTO =  locationTranslator.translateToAssetRouteDTO(routes, coordinate);
          Asset asset = assetManager.findByAssetId(assetId);
          if(assetRouteDTO.getIsWithinRoute() != null && assetRouteDTO.getIsWithinRoute() == false) {
        	  String message = asset.getAssetName()+" is away from the route";
        	  assetRouteDTO.setMessage(message);
        	  ExecutorService exe =  Executors.newSingleThreadScheduledExecutor();
              exe.execute(()->{
    			  NotificationDTO notification = new NotificationDTO();
                 notification.setAssetId(assetId.toString());
                 notification.setMessage(message);
                 notificationService.saveNotification(notification);
              });
          }
		   
           return assetRouteDTO;
	}
	
	

}

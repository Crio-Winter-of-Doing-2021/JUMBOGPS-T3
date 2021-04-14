package com.raghul.assettracker.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.raghul.assettracker.dto.AssetDTO;
import com.raghul.assettracker.dto.AssetGeofenceDTO;
import com.raghul.assettracker.dto.AssetRouteDTO;
import com.raghul.assettracker.dto.CoordinateDTO;
import com.raghul.assettracker.dto.GeofenceDTO;
import com.raghul.assettracker.exception.AssetLocationNotFoundException;

public interface LocationService {
	
	/**
	 * Method to get the assets
	 * @param fromIndex
	 * @param maxSize
	 * @param assetTypes
	 * @param assetId
	 * @param startTime
	 * @param endTime
	 * @return assets 
	 */
	List<AssetDTO> getAssets(Integer fromIndex,Integer maxSize,List<UUID> assetTypes,String assetId,Timestamp startTime,Timestamp endTime);
	
	/**
	 * Method to get asset timeline
	 * @param assetId
	 * @return
	 * @throws AssetLocationNotFoundException
	 */
	AssetDTO getAssetTimeline(UUID assetId) throws AssetLocationNotFoundException;
	
	/**
	 * Method to save asset geofence
	 * @param assetGeofenceDTO
	 */
	void saveAssetGeofence(GeofenceDTO assetGeofenceDTO) ;
	
	/**
	 * Method to get asset geofence
	 * @param assetId
	 * @return AssetGeofenceDTO
	 * @throws AssetLocationNotFoundException
	 */
	AssetGeofenceDTO getAssetGeofence(UUID assetId) throws AssetLocationNotFoundException;
	
	
	/**
	 * Method to get the asset geofence
	 * @param assetIds
	 * @return assets geofence
	 */
	List<GeofenceDTO> getAssetsGeofence(List<UUID> assetIds);
	
	/**
	 * Method to save asset route
	 * @param assetRouteDTO
	 */
    void saveAssetRoute(AssetRouteDTO assetRouteDTO);
    
    /**
     * Method to get asset route by id
     * @param assetId
     * @return AssetRoute
     */
    AssetRouteDTO getAssetRoute(UUID assetId);
    
    /**
     * Method to check the asset route
     * @param assetId
     * @param assetRoute
     * @param coordinate
     * @return AssetRouteDTO
     */
    AssetRouteDTO checkAssetRoute(UUID assetId,UUID assetRoute,CoordinateDTO coordinate);
}

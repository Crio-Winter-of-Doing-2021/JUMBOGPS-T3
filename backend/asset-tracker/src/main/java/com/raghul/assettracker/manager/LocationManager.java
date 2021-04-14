package com.raghul.assettracker.manager;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import com.raghul.assettracker.exception.AssetLocationNotFoundException;
import com.raghul.assettracker.exception.DataException;
import com.raghul.assettracker.model.Asset;
import com.raghul.assettracker.model.AssetGeofenceModel;
import com.raghul.assettracker.model.AssetLocation;
import com.raghul.assettracker.model.AssetRouteModel;
import com.raghul.assettracker.model.AssetRoutePathModel;
import com.raghul.assettracker.repository.AssetGeofenceRepository;
import com.raghul.assettracker.repository.AssetLocationRepository;
import com.raghul.assettracker.repository.AssetRepository;
import com.raghul.assettracker.repository.AssetRouteModelRepository;
import com.raghul.assettracker.repository.AssetRoutePathRepository;

@Component
public class LocationManager {
	
	@Resource
	EntityManager entityManager;
	
	
	@Resource
	AssetRepository assetRepository;
	
	@Resource
	AssetLocationRepository assetLocationRepository;
	
	@Resource
	AssetGeofenceRepository assetGeofenceRepository;
	
	
	@Resource
	AssetRouteModelRepository assetRouteModelRepository;
	
	
	@Resource
	AssetRoutePathRepository assetRoutePathRepository;
	
	/**
	 * Method to get the asset
	 * @param assetType
	 * @param assetName
	 * @return list of assets
	 */
	public List<Asset> getAsset(List<UUID> assetType,String assetName){
		if(Objects.isNull(assetType) || assetType.isEmpty()) {
			return assetRepository.findByAssetNameIgnoreCaseContaining(assetName);
		}
		return assetRepository.findByAssetTypeInAndAssetNameIgnoreCaseContaining(assetType, assetName);
	}
	
	
	@SuppressWarnings("unchecked")
	/**
	 * Method to get the asset location
	 * @param assetIds
	 * @param fromSize
	 * @param maxSize
	 * @param startTime
	 * @param endTime
	 * @return list of asset locations for the asset
	 */
	public List<AssetLocation> getAssetLocation(List<String> assetIds,Integer fromSize,Integer maxSize,Timestamp startTime,Timestamp endTime){
		
		StringBuilder sql = new StringBuilder("select * from (select *,ROW_NUMBER() over(partition by asset_id order by created_on desc ) as row_num from asset_location where is_active = true ");
		if(assetIds != null && !assetIds.isEmpty()) {
			sql.append("and asset_id in (:assetIds)");
		}
		
		if(startTime != null && endTime != null) {
			sql .append(" and (created_on between :startTime and :endTime )");
		}
		
		sql.append(") as loc_val where loc_val.row_num = 1");
		Query query  = entityManager.createNativeQuery(sql.toString(),AssetLocation.class);
		if(assetIds != null && !assetIds.isEmpty()) {
			query.setParameter("assetIds", assetIds);
		}
		
		if(startTime != null && endTime!= null) {
			query.setParameter("startTime", startTime);
			query.setParameter("endTime", endTime);
		}
		
		List<AssetLocation> locations = query.setFirstResult(fromSize*maxSize).setMaxResults(maxSize).getResultList();
		locations.forEach(
				location -> {
					System.out.println(location.getCreatedOn().toString());
				}
				);
		return locations;
	}
	
	/**
	 * Method to get the asset location by id
	 * @param assetId
	 * @return AssetLocation
	 * @throws AssetLocationNotFoundException
	 */
	public AssetLocation findOneByAssetId(UUID assetId) throws AssetLocationNotFoundException {
		try {
			return assetLocationRepository.findByAssetId(assetId).get(0);
		}catch(Exception e) {
			throw new AssetLocationNotFoundException("No asset location found for the asset");
		}
	}
	
	/**
	 * Method to find the location  assetId
	 * @param assetId
	 * @param assetLocation
	 * @return
	 */
	public List<AssetLocation> findLocationByCreatedOn(String assetId,AssetLocation assetLocation){
		return assetLocationRepository.findByCreatedOnAndAssetId(assetId.toString(),assetLocation.getCreatedOn());
	}
	
	public AssetLocation saveAssetLocation(AssetLocation assetLocation) throws DataException {
		try {
			return assetLocationRepository.save(assetLocation);
		}catch(Exception ex) {
			throw new DataException("Data exception occurs while saving the data");
		}
	}
	
	/**
	 * Method to delete the asset location
	 * @param assetId
	 */
	public void deleteAssetLocation(UUID assetId) {
		List<AssetLocation> locations = assetLocationRepository.findByAssetId(assetId);
		if(locations != null && !locations.isEmpty()) {
			assetLocationRepository.deleteInBatch(locations);
		}
	}
	
	/**
	 * Method to save the geofence
	 * @param assetGeofence
	 */
	public void saveAssetGeofence(AssetGeofenceModel assetGeofence) {
		assetGeofenceRepository.save(assetGeofence);
	}
	
	/**
	 * Method to get the geofence by assetId
	 * @param assetId
	 * @return AssetGeofenceModel
	 */
	public AssetGeofenceModel getAssetGeofenceByAssetId(UUID assetId) {
		return assetGeofenceRepository.findByAssetId(assetId);
	}
	
	/**
	 * Method to geth the asset geofence based on the assetIds
	 * @param assetIds
	 * @return list of asset geofence
	 */
	public List<AssetGeofenceModel> getAssetsGeofence(List<UUID> assetIds){
		return assetGeofenceRepository.findByAssetIdIn(assetIds);
	}
	
	/**
	 * Method to save the asset route
	 * @param assetRoute
	 * @return uuid of the asset
	 */
	public UUID saveAssetRoute(AssetRouteModel assetRoute) {
		return assetRouteModelRepository.save(assetRoute).getAssetRouteId();
		
	}

	/**
	 * Method to save the asset route paths
	 * @param assetRoutePaths
	 */
	public void saveAssetRoutePath(List<AssetRoutePathModel> assetRoutePaths) {
		assetRoutePathRepository.saveAll(assetRoutePaths);
	}
	
	/**
	 * Method to find the asset by id
	 * @param assetId
	 * @return AssetRouteModel
	 */
	public AssetRouteModel findByAssetId(UUID assetId) {
		return assetRouteModelRepository.findByAssetId(assetId);
	}

	/**
	 * Method to find the asset route
	 * @param routeId
	 * @return list of asset route paths
	 */
	public List<AssetRoutePathModel> findByAssetRoute(UUID routeId){
		return assetRoutePathRepository.findByAssetRouteIdOrderBySortOrderAsc(routeId);
	}
}

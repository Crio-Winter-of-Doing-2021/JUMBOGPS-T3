package com.raghul.assettracker.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raghul.assettracker.dto.AssetRouteDTO;
import com.raghul.assettracker.dto.CoordinateDTO;
import com.raghul.assettracker.dto.GeofenceDTO;
import com.raghul.assettracker.service.LocationService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v1/gps")
@CrossOrigin
@Api( tags = "Location Management")
public class GpsController {
	
	@Resource
	private LocationService locationService;
	
	/**
	 * Method to get the assets
	 * @param fromIndex
	 * @param maxSize
	 * @param assetTypes
	 * @param assetId
	 * @param startTime
	 * @param endTime
	 * @return asset details
	 */
	@GetMapping("/assets")
	public ResponseEntity<?> getAssets(@RequestParam(value="from",defaultValue="0",required = false) Integer fromIndex,
			@RequestParam(value = "size",defaultValue = "100" ,required = false) Integer maxSize,
			@RequestParam(value = "assetTypes",required = false) List<UUID> assetTypes,
			@RequestParam(value = "assetId" ,required = false) String assetId,
			@RequestParam(value = "startTime",required = false) Timestamp startTime,
			@RequestParam(value = "endTime" ,required = false) Timestamp endTime){
		return ResponseEntity.ok().body(locationService.getAssets(fromIndex,maxSize,assetTypes,assetId,startTime,endTime));
	}
	
	/**
	 * Method to get the asset timeline
	 * @param assetId
	 * @return timeline of the asset
	 */
	@GetMapping("/{assetId}/timeline")
	public ResponseEntity<?> getAssetTimeline(@PathVariable("assetId") String assetId) {
		try {
			return ResponseEntity.ok().body(locationService.getAssetTimeline(UUID.fromString(assetId)));
			
		}catch(Exception e) {
			return ResponseEntity.noContent().build();
		}
	}
	
	/**
	 * Method to save the geofence of the asset
	 * @param assetId
	 * @param assetGeofenceDTO
	 * @return
	 */
	@PostMapping("/{assetId}/geofence")
	public ResponseEntity<?> saveAssetGeofence(@PathVariable("assetId") String assetId,@RequestBody GeofenceDTO assetGeofenceDTO){
		assetGeofenceDTO.setAssetId(assetId);
		locationService.saveAssetGeofence(assetGeofenceDTO);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Method to get the geofence for the asset
	 * @param assetId
	 * @return geofence of the asset
	 */
	@GetMapping("/{assetId}/geofence")
		public ResponseEntity<?> getGeofence(@PathVariable("assetId") UUID assetId ){
			try {
				return ResponseEntity.ok(locationService.getAssetGeofence(assetId));
			} catch (Exception e) {
				return ResponseEntity.noContent().build();
			}
		}
	
	/**
	 * Method to get the geofence for all the asset Ids
	 * @param assetIds
	 * @return
	 */
	@GetMapping("/assets/geofence")
	public ResponseEntity<?> getAssetsGeofence(@RequestParam("assetIds")List<UUID> assetIds){
		return ResponseEntity.ok(locationService.getAssetsGeofence(assetIds));
	}
	
	/**
	 * Method to save asset route
	 * @param assetId
	 * @param assetRouteDTO
	 * @return
	 */
	@PostMapping("{assetId}/asset-route")
	public ResponseEntity<?> saveAssetRoute(@PathVariable("assetId") UUID assetId,@RequestBody AssetRouteDTO assetRouteDTO){
		try {
			locationService.saveAssetRoute(assetRouteDTO);
			return ResponseEntity.ok().build();
		}catch(Exception ex) {
			ex.getLocalizedMessage();
			return ResponseEntity.noContent().build();
		}
	}
	
	/**
	 * Method to get asset route for asset id
	 * @param assetId
	 * @return
	 */
	@GetMapping("{assetId}/asset-route")
	public ResponseEntity<?> getAssetRoute(@PathVariable("assetId") UUID  assetId ){
		try {
			return ResponseEntity.ok(locationService.getAssetRoute(assetId));
		}catch(Exception ex) {
			return ResponseEntity.noContent().build();
		}
	}
	
	/**
	 * Method to verify the asset georoute
	 * @param assetId
	 * @param assetRouteId
	 * @param coordinate
	 * @return true if within and false if it is not within 
	 * Currently this implementation done in android app 
	 */ 
	@PostMapping("{assetId}/asset-route/{assetRouteId}/verify")
	public ResponseEntity<?> checkAssetRoute(@PathVariable("assetId")UUID assetId,@PathVariable("assetRouteId") UUID assetRouteId,@RequestBody CoordinateDTO coordinate){
		try {
			return ResponseEntity.ok().body(
					locationService.checkAssetRoute(assetId,assetRouteId, coordinate)
					);
		}
		catch(Exception ex) {
			return ResponseEntity.noContent().build();
		}
		
	}
	
	
	
	

}

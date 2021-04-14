package com.raghul.assettracker.controller;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raghul.assettracker.dto.AssetDTO;
import com.raghul.assettracker.dto.LocationAssetDTO;
import com.raghul.assettracker.exception.DataException;
import com.raghul.assettracker.service.AssetService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v1/assets")
@CrossOrigin
@Api( tags = "Assets Management")
public class AssetController {
	
	@Resource
	private AssetService assetService;
	
	/**
	 * Method to get all the assets 
	 * @param from
	 * @param size
	 * @return list of assets List<Asset>
	 */
	@GetMapping("")
	public ResponseEntity<?> getAllAssets(
			@RequestParam(name="from" ,defaultValue="0" ,required = false) Integer from
			,@RequestParam(name="size",defaultValue="100",required = false) Integer size){
		return ResponseEntity.ok().body(assetService.getAllAssets(from,size));
	}
	
	
	/**
	 * Method to register new asset
	 * @param assetDTO
	 * @return asset Id 
	 */
	@PostMapping("/register")
	public ResponseEntity<?> createAsset(@RequestBody AssetDTO assetDTO){
		if(Objects.nonNull(assetDTO)) {
			try {
				return ResponseEntity.ok().body(assetService.createAsset(assetDTO));
			} catch (DataException e) {
				return ResponseEntity.noContent().build();
			}
		}
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * Method to get the asset location
	 * @param locationAssetDTO 
	 * @return HttpStatus.OK
	 */
	@PostMapping("/updates")
	public ResponseEntity<?> getUpdatedAssetLocation(@RequestBody LocationAssetDTO locationAssetDTO){
		try {
			assetService.createAssetLocationInformation(locationAssetDTO);
			return ResponseEntity.ok().build();
		} catch (DataException e) {
			return ResponseEntity.noContent().build();
		}
	}
	
	/**
	 * Method to delete the asset
	 * @param assetId
	 * @return HttpStatus.OK
	 */
	@DeleteMapping("/delete/{assetId}")
	public ResponseEntity<?> deleteAsset(@PathVariable("assetId") UUID assetId ){
		assetService.deleteAsset(assetId);
		return ResponseEntity.ok().build();
	}
	

}

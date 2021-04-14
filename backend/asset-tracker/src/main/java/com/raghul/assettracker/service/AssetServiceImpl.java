package com.raghul.assettracker.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.raghul.assettracker.dto.AssetDTO;
import com.raghul.assettracker.dto.LocationAssetDTO;
import com.raghul.assettracker.exception.DataException;
import com.raghul.assettracker.manager.AssetManager;
import com.raghul.assettracker.manager.LocationManager;
import com.raghul.assettracker.model.Asset;
import com.raghul.assettracker.model.AssetLocation;
import com.raghul.assettracker.translator.AssetTranslator;
import com.raghul.assettracker.translator.LocationTranslator;

@Service
public class AssetServiceImpl implements AssetService{
	
	
	
	@Resource
	private AssetTranslator assetTranslator;
	
	@Resource
	private LocationTranslator locationTranslator;
	
	@Resource
	private AssetManager assetManager;
	
	@Resource
	private LocationManager locationManager;
	
	/**
	 * @author raghul
	 * Method to create asset 
	 * @param AssetDTO assetDTO
	 * @return assetId 
	 * @throws DataException 
	 */
	@Override
	public AssetDTO createAsset(AssetDTO assetDTO) throws DataException {
		Asset asset = assetTranslator.translateToAsset(assetDTO);
		UUID assetId = assetManager.saveAsset(asset).getAssetId();
		assetDTO = new AssetDTO();
		assetDTO.setId(assetId.toString());
		return assetDTO;
		
	}

	/**
	 * @author raghul
	 * Method to craete asset location 
	 * @param Location 
	 * @throws DataException
	 */
	@Override
	public void createAssetLocationInformation(LocationAssetDTO locationAssetDTO) throws DataException {
		if(locationAssetDTO.getLat() == null || locationAssetDTO.getLon() == null
				|| locationAssetDTO.getAssetId() == null) {
			return ;
		}
	AssetLocation assetLocation = locationTranslator.translateToAssetLocation(locationAssetDTO);
	locationManager.saveAssetLocation(assetLocation);
		
	}
	/**@author raghul
	 * Method to get all assets 
	 * @param from  Integer
	 * @param size Integer
	 * @return list of assets List<AssetDTO>
	 * 
	 */
	@Override
	public List<AssetDTO> getAllAssets(Integer from,Integer size){
		List<Asset> assets = assetManager.findAllAssets(from, size);
		if(assets!=null && !assets.isEmpty()) {
			return assetTranslator.translateToAssetDTOs(assets);
		}
		return Collections.emptyList();
				
	}
	
	/**
	 * @author raghul
	 * Method to delete asset
	 * @param assetId UUID 
	 */

	@Override
	public void deleteAsset(UUID assetId) {
		locationManager.deleteAssetLocation(assetId);
		assetManager.deleteAsset(assetId);
		
	}
	
	
}

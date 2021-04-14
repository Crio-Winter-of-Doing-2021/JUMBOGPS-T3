package com.raghul.assettracker.service;

import java.util.List;
import java.util.UUID;

import com.raghul.assettracker.dto.AssetDTO;
import com.raghul.assettracker.dto.LocationAssetDTO;
import com.raghul.assettracker.exception.DataException;

public interface AssetService {
	
	/**
	 * 
	 * 
	 * @param assetDTO
	 * @return
	 * @throws DataException
	 */
	AssetDTO createAsset(AssetDTO assetDTO) throws DataException;
	
	void createAssetLocationInformation(LocationAssetDTO locationAssetDTO) throws DataException;
	
	
	List<AssetDTO> getAllAssets(Integer from,Integer size);
	
	void deleteAsset(UUID assetId);
	

}

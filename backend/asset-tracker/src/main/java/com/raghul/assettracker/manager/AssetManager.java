package com.raghul.assettracker.manager;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.raghul.assettracker.exception.DataException;
import com.raghul.assettracker.model.Asset;
import com.raghul.assettracker.repository.AssetRepository;

@Component
public class AssetManager {

	@Resource 
	private AssetRepository assetRepository;
	
	/**
	 * @author raghul
	 * Method to find asset by id
	 * @param assetId
	 * @return Asset
	 */
	
	public Asset findByAssetId(UUID assetId) {
		return assetRepository.findByAssetId(assetId);
	}
	
	/**
	 * @author raghul
	 * Method to save asset
	 * @param asset
	 * @return Asset
	 * @throws DataException
	 */
	public Asset saveAsset(Asset asset) throws DataException {
		try {
			return assetRepository.save(asset);
		}catch(Exception ex) {
			throw new DataException("Exception while saving data"+ex.getMessage());
		}
	}
	
	/**
	 * @author raghul
	 * Method to find all the assets
	 * @param from
	 * @param size
	 * @return List<Asset> list of assets
	 */
	public List<Asset> findAllAssets(Integer from,Integer size){
		Pageable page = PageRequest.of(from, size);
		return assetRepository.findAll(page).getContent();
		
	}
	
	/**
	 * @author raghul
	 * Method to delete asset by id
	 * @param assetId
	 */
	public void deleteAsset(UUID assetId) {
		Asset asset = assetRepository.findByAssetId(assetId);
		if(asset != null) {
			assetRepository.delete(asset);
		}
		
	}
	
	
}

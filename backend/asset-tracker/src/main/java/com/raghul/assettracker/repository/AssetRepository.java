package com.raghul.assettracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raghul.assettracker.model.Asset;

public interface AssetRepository extends JpaRepository<Asset, UUID>{
	
	
	Asset findByAssetId(UUID assetId);
	
	List<Asset> findByAssetNameIgnoreCaseContaining(String assetName);
	
	List<Asset> findByAssetTypeInAndAssetNameIgnoreCaseContaining(List<UUID> assetType,String assetName);

}

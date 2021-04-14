package com.raghul.assettracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raghul.assettracker.model.AssetGeofenceModel;

public interface AssetGeofenceRepository extends JpaRepository<AssetGeofenceModel, UUID>{

	AssetGeofenceModel findByAssetId(UUID assetId);

	List<AssetGeofenceModel> findByAssetIdIn(List<UUID> assetIds);

}

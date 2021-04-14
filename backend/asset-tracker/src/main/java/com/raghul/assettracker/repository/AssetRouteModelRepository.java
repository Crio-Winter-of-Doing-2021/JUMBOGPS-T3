package com.raghul.assettracker.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raghul.assettracker.model.AssetRouteModel;

public interface AssetRouteModelRepository extends JpaRepository<AssetRouteModel, UUID>{

	AssetRouteModel findByAssetId(UUID assetId);

}

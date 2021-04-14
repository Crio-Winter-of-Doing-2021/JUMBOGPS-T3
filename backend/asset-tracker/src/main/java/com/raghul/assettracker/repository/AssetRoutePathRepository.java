package com.raghul.assettracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raghul.assettracker.model.AssetRoutePathModel;

public interface AssetRoutePathRepository extends JpaRepository<AssetRoutePathModel,UUID>{

	List<AssetRoutePathModel> findByAssetRouteId(UUID routeId);

	List<AssetRoutePathModel> findByAssetRouteIdOrderBySortOrderAsc(UUID routeId);

}

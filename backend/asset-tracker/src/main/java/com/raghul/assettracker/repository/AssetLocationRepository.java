package com.raghul.assettracker.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raghul.assettracker.model.AssetLocation;

public interface AssetLocationRepository extends JpaRepository<AssetLocation,UUID>{
	

	@Query(value = "select *,ROW_NUMBER() over(partition by asset_id order by created_on desc ) from asset_location" , nativeQuery = true)
	List<AssetLocation> findAll();
	
	
	@Query("select al from AssetLocation al where al.asset.assetId = :assetId order by createdOn desc")
	List<AssetLocation> findByAssetId(UUID assetId);
	
	
	@Query(value = "select * from asset_location where asset_id = :assetId and created_on >= DATE_SUB(:createdOn,INTERVAL 24 HOUR) order by created_on desc ", nativeQuery = true)
	List<AssetLocation> findByCreatedOnAndAssetId(String assetId,Timestamp createdOn);

	@Query("select al from AssetLocation al where al.asset.assetId = :assetId order by createdOn desc")
	AssetLocation findOneByAssetId(UUID assetId);
	
	
}

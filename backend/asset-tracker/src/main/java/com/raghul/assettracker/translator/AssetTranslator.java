package com.raghul.assettracker.translator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.raghul.assettracker.dto.AssetDTO;
import com.raghul.assettracker.model.Asset;
import com.raghul.assettracker.model.RefTerm;
import com.raghul.assettracker.repository.RefTermRepository;
import com.raghul.assettracker.util.CommonConstants;

@Component
public class AssetTranslator {
	
	@Resource 
	private RefTermRepository refTermRepository;
	
	
	public Asset translateToAsset(AssetDTO assetDTO) {
		RefTerm systemUser = refTermRepository.findByRefTermValue(CommonConstants.SYSTEM_USER);
		Asset asset = new Asset();
		asset.setAssetId(UUID.randomUUID());
		asset.setAssetName(assetDTO.getAssetName());
		asset.setCreatedOn(new Timestamp(Calendar.getInstance().getTime().getTime()));
		asset.setAssetType(UUID.fromString(assetDTO.getAssetTypeId()));
		asset.setCreatedBy(systemUser.getRefTermId().toString());
		return asset;
	}
	
	public AssetDTO translateToAssetDTO(Asset asset) {
		AssetDTO assetDTO = new AssetDTO();
		assetDTO.setId(asset.getAssetId().toString());
		assetDTO.setAssetName(asset.getAssetName());
		assetDTO.setAssetType(refTermRepository.getOne(asset.getAssetType()).getRefTermValue());
		assetDTO.setCreatedOn(asset.getCreatedOn().toString());
		return assetDTO;
		
	}
	
	public List<AssetDTO> translateToAssetDTOs(List<Asset> assets){
		List<AssetDTO> assetDTOs = new ArrayList<>();
		assets.forEach(
				asset->{
					assetDTOs.add(translateToAssetDTO(asset));
				});
		
		return assetDTOs;
		
	}

}

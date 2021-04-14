package com.raghul.assettracker.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.raghul.assettracker.dto.AssetDTO;
import com.raghul.assettracker.dto.LocationAssetDTO;
import com.raghul.assettracker.exception.DataException;
import com.raghul.assettracker.manager.ReferenceManager;
import com.raghul.assettracker.model.Asset;
import com.raghul.assettracker.model.RefTerm;
import com.raghul.assettracker.repository.AssetLocationRepository;
import com.raghul.assettracker.repository.AssetRepository;

@Component
public class BatchServiceImpl implements BatchService{
	
	@Resource
	private ResourceLoader resourceLoader;
	
	@Resource
	private AssetRepository assetRepository;
	
	@Resource
	private AssetService assetService;
	
	@Resource
	private AssetLocationRepository assetLocationRepository;
	
	@Resource 
	private ReferenceManager referenceManager;

	@Override
	public void insertRecords() {
		InputStream file;
		try {
			String baseTruckName = "truck-";
			String baseSalesName="salesperson-";
			Integer assetCount = 5;
			assetLocationRepository.deleteAll();
			assetRepository.deleteAll();
			file = resourceLoader.getResource("classpath:sample_records_asset_tracker.xlsx").getInputStream();
			Workbook workbook = WorkbookFactory.create(file);
			Sheet sheet = workbook.getSheet("AssetLocation");
			createAssets(workbook.getSheet("Asset"));
			List<Asset> assets = assetRepository.findAll();
			System.out.println("raghul"+assets.size());
			int start = 1;
			int end = 40;
			
			for(Asset asset : assets) {
				for(int i = start;i<=end;i++) {
					System.out.println(asset.getAssetName());
					   LocationAssetDTO locationAssetDTO = new LocationAssetDTO();
					   locationAssetDTO.setAssetId(asset.getAssetId().toString());
					   locationAssetDTO.setLat(sheet.getRow(i).getCell(0).getNumericCellValue());
					   locationAssetDTO.setLon(sheet.getRow(i).getCell(1).getNumericCellValue());
					   System.out.println(locationAssetDTO);
					   try {
						assetService.createAssetLocationInformation(locationAssetDTO);
					} catch (DataException e) {
						e.printStackTrace();
					}
					  
					}
				 start = end+200;
				   end = start+40;
			}
			
			
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DataException e1) {
			e1.printStackTrace();
		}
	}
	
	public void createAssets(Sheet sheet) throws DataException{
		List<RefTerm> values = referenceManager.getRefTerms("assetType");
		Map<String,UUID> mapValues = values.stream().collect(Collectors.toMap(RefTerm::getRefTermValue,RefTerm::getRefTermId));
		for(int i = 1;i<sheet.getPhysicalNumberOfRows();i++) {
			Row row = sheet.getRow(i);
			String assetName = row.getCell(0).getStringCellValue().trim();
			String assetType = row.getCell(1).getStringCellValue().trim();
			AssetDTO assetDTO = new AssetDTO();
			assetDTO.setAssetName(assetName);
			assetDTO.setAssetTypeId(mapValues.get(assetType).toString());
			assetService.createAsset(assetDTO);
		}
	}

}

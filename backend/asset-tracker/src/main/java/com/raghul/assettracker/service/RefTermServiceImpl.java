package com.raghul.assettracker.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.raghul.assettracker.dto.RefTermDTO;
import com.raghul.assettracker.manager.ReferenceManager;
import com.raghul.assettracker.model.RefTerm;
import com.raghul.assettracker.translator.RefTermTranslator;
import com.raghul.assettracker.util.CommonConstants;

@Service
public class RefTermServiceImpl implements RefTermService{
	
	@Resource
	private RefTermTranslator refTermTranslator;
	
	@Resource
	private ReferenceManager referenceManager;
	
	/**
	 * Method to get RefTerm values
	 * @param key String
	 * @return List<RefTermDTO> list of refTerms 
	 */
	
	public List<RefTermDTO> getRefTermsValue(String key){
		if(key.equalsIgnoreCase(CommonConstants.ASSET_TYPE)) {
			List<RefTerm> refTerms =referenceManager.getRefTerms(CommonConstants.ASSET_TYPE);
			return refTermTranslator.translateToRefTermDTO(refTerms);
			
		}
		return Collections.emptyList();
	}

	
	
}

package com.raghul.assettracker.service;

import java.util.List;

import com.raghul.assettracker.dto.RefTermDTO;

public interface RefTermService {

	
	/**
	 * Method to get the refterm value by key
	 * @param key
	 * @return list of refterms
	 */
	List<RefTermDTO> getRefTermsValue(String key);
}

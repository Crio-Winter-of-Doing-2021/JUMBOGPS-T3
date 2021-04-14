package com.raghul.assettracker.manager;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.raghul.assettracker.model.RefSetModel;
import com.raghul.assettracker.model.RefTerm;
import com.raghul.assettracker.model.SetRefTermModel;
import com.raghul.assettracker.repository.RefSetRepository;
import com.raghul.assettracker.repository.RefTermRepository;
import com.raghul.assettracker.repository.SetRefTermRepository;

@Component
public class ReferenceManager {
	@Resource
	private RefTermRepository refTermRepository;
	
	@Resource
	private RefSetRepository refSetRepository;
	
	@Resource
	private SetRefTermRepository setRefTermRepository;
	
	
	
	/**
	 * Method to get the refterms 
	 * @param refSet
	 * @return list of refterms
	 */
	public List<RefTerm> getRefTerms(String refSet){
		
		RefSetModel refSetValue = refSetRepository.findByRefSetKey(refSet);
		List<UUID> refTermValues = setRefTermRepository.findByRefSetId(refSetValue.getRefSetId()).stream().map(SetRefTermModel::getRefTermId).
				collect(Collectors.toList());
		return refTermRepository.findByRefTermIdIn(refTermValues);
	}
	
	
	/**
	 * Method to get the ref set
	 * @param refSetKey
	 * @return the key value pairs of refterm and id
	 */
	public Map<UUID, RefTerm> getRefTermValue(String refSetKey){
		RefSetModel refSetValue = refSetRepository.findByRefSetKey(refSetKey);
		List<UUID> refTermValues = setRefTermRepository.findByRefSetId(refSetValue.getRefSetId()).stream().map(SetRefTermModel::getRefTermId).
				collect(Collectors.toList());
		return refTermRepository.findByRefTermIdIn(refTermValues).stream().collect(Collectors.toMap(RefTerm::getRefTermId, r->r));
		
	}
	
	/**
	 * Method to find ref term by value
	 * @param value
	 * @return RefTerm of the value
	 */
	public RefTerm findRefTermByValue(String value) {
		return refTermRepository.findByRefTermValue(value);
	}
	
	

}

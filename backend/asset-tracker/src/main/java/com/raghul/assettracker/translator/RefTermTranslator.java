package com.raghul.assettracker.translator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.raghul.assettracker.dto.RefTermDTO;
import com.raghul.assettracker.model.RefTerm;

@Component
public class RefTermTranslator {
	
	public List<RefTermDTO> translateToRefTermDTO(List<RefTerm> refTerms){
		if(Objects.nonNull(refTerms) && !refTerms.isEmpty()) {
			List<RefTermDTO> values = new ArrayList<>();
			refTerms.forEach(
					refTerm->{
						values.add(translateToRefTerm(refTerm));
					}
					);
			
			return values;
		}
		return Collections.emptyList();
		
	}
	
	public RefTermDTO translateToRefTerm(RefTerm refTerm) {
		RefTermDTO refTermDTO = new RefTermDTO();
		refTermDTO.setName(refTerm.getRefTermValue());
		refTermDTO.setId(refTerm.getRefTermId().toString());
		return refTermDTO;
	}

}

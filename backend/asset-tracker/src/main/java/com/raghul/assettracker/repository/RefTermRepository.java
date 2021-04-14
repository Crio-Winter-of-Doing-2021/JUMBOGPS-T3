package com.raghul.assettracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raghul.assettracker.model.RefTerm;

public interface RefTermRepository extends JpaRepository<RefTerm, UUID>{
	
	List<RefTerm> findByRefTermValueIn(List<String> refTermvalue);
	
	RefTerm findByRefTermValue(String value);
	
	List<RefTerm> findByRefTermIdIn(List<UUID> refTermIds);

}

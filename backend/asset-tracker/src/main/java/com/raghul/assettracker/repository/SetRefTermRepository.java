package com.raghul.assettracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raghul.assettracker.model.SetRefTermModel;

public interface SetRefTermRepository extends JpaRepository<SetRefTermModel,UUID>{

	List<SetRefTermModel> findByRefSetId(UUID refSetId);

}
